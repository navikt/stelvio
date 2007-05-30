/**
 * 
 */
package no.nav.maven.plugins.navcopyplugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.FileUtils;

/**
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 * @goal copy-file
 */
public class CopyFileMojo extends AbstractCopyMojo {
	/**
	 * @parameter
	 */
	private List<SourceFile> sourceFiles;

    public void execute() throws MojoExecutionException, MojoFailureException {
        if(sourceFiles != null && sourceFiles.size() > 0)
            try {
            	for (SourceFile sourceFile : sourceFiles) {
            		if(sourceFile.getPath().exists() && sourceFile.getPath().isFile()) {
                        File targetDirectory = new File(sourceFile.getTargetDirectory());
                        File targetFile = new File(targetDirectory, sourceFile.getPath().getName());
                        FileUtils.copyFile(sourceFile.getPath(), targetFile);
                        getLog().info((new StringBuilder()).append("Copying ").append(sourceFile.getPath().getName()).append(" to ").append(targetFile).toString());
                    }
                    else {
                        getLog().warn("The file "+sourceFile.getPath()+" does not exists or is a directory. The file will not be copied.");
                    }
            	}
            }
            catch(IOException e) {
                getLog().error(e);
            }
        else {
            getLog().warn("No files specified. Nothing will be copied.");
        }
    }
}