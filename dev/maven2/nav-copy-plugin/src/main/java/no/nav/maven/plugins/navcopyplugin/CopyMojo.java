/**
 * 
 */
package no.nav.maven.plugins.navcopyplugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;

/**
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 * @goal copy
 */
public class CopyMojo extends AbstractCopyMojo {
	private static final String EMPTY_STRING_ARRAY[] = new String[0];
    
	/**
	 * @parameter alias="directory"
	 * @required
	 */
	private File sourceDirectory;
	
	/**
	 * @parameter alias="excludes"
	 */
    private String sourceExcludes;

    /**
	 * @parameter alias="includes" default-value="**"
	 */
    private String sourceIncludes;
    
    /**
     * @parameter default-value="true"
     */
    private boolean recursive;
    
    public void execute() throws MojoExecutionException, MojoFailureException {
        if(sourceDirectory.exists() && sourceDirectory.isDirectory()) {            
            try {
//            	if (recursive) {
//            		FileUtils.copyDirectoryStructure(sourceDirectory, new File(targetDirectory));
//            	}
//            	else {
	                String filenames[] = getFiles(sourceDirectory);
	                for (String filename : filenames) {
	                	File sourceFile = new File(sourceDirectory, filename);
	                    File targetFile = new File(targetDirectory, filename);
	                    FileUtils.copyFile(sourceFile, targetFile);
	                    getLog().info("Copying "+sourceFile.getName()+" to "+targetFile);
	                }
//            	}

            }
            catch(IOException e){
                getLog().error(e);
            }
        }
        else {
            getLog().warn("Source-directory "+sourceDirectory+" does not exist. No files will be copied.");
        }
    }

    protected String[] getExcludes() {
        List<String> excludeList = new ArrayList<String>(FileUtils.getDefaultExcludesAsList());
        if(sourceExcludes != null && !"".equals(sourceExcludes)) {
            excludeList.addAll(Arrays.asList(StringUtils.split(sourceExcludes, ",")));
        }
        
        return (String[])excludeList.toArray(EMPTY_STRING_ARRAY);
    }
    
    protected String[] getIncludes() {
        if (sourceIncludes != null) {
        	return StringUtils.split(sourceIncludes, ",");
        }
        
        return EMPTY_STRING_ARRAY;
    }

    private String[] getFiles(File sourceDir) {
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(sourceDir);
        scanner.addDefaultExcludes();
        scanner.setIncludes(getIncludes());
        if (getIncludes() == null || getIncludes().length <= 0) {
        	scanner.setExcludes(getExcludes());
        }
        scanner.scan();
        return scanner.getIncludedFiles();
    }
}