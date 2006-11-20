package no.stelvio.common.error;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static org.junit.Assert.fail;
import org.junit.Test;

import no.stelvio.common.error.support.ExceptionToCopyHolder;

/**
 * Unit test for alle exception checking that the correct constructors exist.
 *
 * @todo make this into something that should be in all modules and run when maven runs tests; should be a CheckStyle
 * or PMD module.
 */
//public class AllExceptionsTest extends DynamicTestSuite {
public class AllExceptionsTest {
    @Test
    public void allExceptionHaveCorrectConstructors() throws IOException, ClassNotFoundException, NoSuchMethodException {
        Enumeration<URL> resources = getClass().getClassLoader().getResources("");
        List<Class<Exception>> filelist = new ArrayList<Class<Exception>>();

        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            File topDir = new File(url.getFile());
            findAllClassesRecursive(topDir, filelist, topDir);
        }

        List<Class<Exception>> classes = new ArrayList<Class<Exception>>();

        for (Class<Exception> clazz : filelist) {
            Constructor<Exception> constructor;

            try {
                constructor = clazz.getDeclaredConstructor(ExceptionToCopyHolder.class);

                if (!Modifier.isProtected(constructor.getModifiers())) {
                    classes.add(clazz);
                }
            } catch (NoSuchMethodException e) {
                classes.add(clazz);
            }
        }

        if (!classes.isEmpty()) {
            fail(classes + " does not declare correct copy constructor");
        }
    }

    private void findAllClassesRecursive(File directory, List<Class<Exception>> filelist, File start) throws ClassNotFoundException {
        File[] files = directory.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".class") || pathname.isDirectory();
            }
        });

        for (File file : files) {
            if (file.isDirectory()) {
                findAllClassesRecursive(file, filelist, start);
            } else {
                Class<?> exception = makeException(file, start);

                if (null != exception) {
                    filelist.add((Class<Exception>) exception);
                }
            }
        }
    }

    private Class<?> makeException(File file, File start) throws ClassNotFoundException {
        String filename = file.getAbsolutePath();
        String classAsPath = filename.substring(start.getAbsolutePath().length() + 1);
        String className = classAsPath.replace(File.separatorChar, '.').replace(".class", "");
        Class<?> exceptionClass = Class.forName(className, false, Thread.currentThread().getContextClassLoader());

        if (!Modifier.isAbstract(exceptionClass.getModifiers())) {
            if (UnrecoverableException.class.isAssignableFrom(exceptionClass) ||
                RecoverableException.class.isAssignableFrom(exceptionClass)) {
                return exceptionClass;
            }
        }

        return null;
    }
}
