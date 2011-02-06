package no.stelvio.maven.plugin.rsa.transform;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

/**
 * Generate WSDL interface from UML model
 * 
 * @author test@example.com
 * 
 * @goal uml2wsdl
 */
public class Uml2WsdlMojo extends AbstractMojo {

	/**
	 * @parameter default-value="${project.build.directory}/projects"
	 */
	private File projectDirectory;
	
	/**
	 * @parameter default-value="${project.build.directory}/workspace"
	 */
	private File workspace;
	
	/**
	 * @parameter default-value="${project.build.outputDirectory}"
	 */
	private File outputDirectory;
	
	/**
	 * @parameter expression="${rsa.command}"
	 * @required
	 */
	private String rsaCommand;
	
	/**
	 * @parameter default-value="/input/UML2WSDL.tc"
	 */
	private String transformationConfiguration;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		// Create Eclipse .project file in output directory
		createEclipseProject();
		
		// Execute transformation
		try {
			executeTransformation();
		} catch (CommandLineException e) {
			throw new MojoExecutionException("An error occured during invocation of IBM Rational Software Architect", e);
		}
	}

	private void createEclipseProject() throws MojoExecutionException {
		outputDirectory.mkdirs();
		File targetFile = new File(outputDirectory, ".project");
		InputStream in = this.getClass().getResourceAsStream("/project");
		try {
			FileOutputStream out = new FileOutputStream(targetFile);
			byte buf[] = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
		} catch (IOException e) {
			throw new MojoExecutionException("An error occured while writing to " + targetFile, e);
		}
	}

	private void executeTransformation() throws CommandLineException, MojoExecutionException {
		Commandline commandline = new Commandline(rsaCommand);
		
		commandline.createArg().setLine("-application stelvio.rsa.uml2wsdl.headless.uml2wsdl");
		commandline.createArg().setLine("-data " + workspace);
		commandline.createArg().setLine("-tc " + transformationConfiguration);
		commandline.createArg().setLine("-projectDir " + outputDirectory);
		for (File project : Arrays.asList(projectDirectory.listFiles())) {
			commandline.createArg().setLine("-projectDir " + project.getAbsolutePath());
		}
		
		StreamConsumer systemOut = new StreamConsumer() {
			public void consumeLine(String line) {
				getLog().info(line);
			}
		};
		StreamConsumer systemErr = new StreamConsumer() {
			public void consumeLine(String line) {
				getLog().error(line);
			}
		};
		ErrorCheckingStreamConsumer errorChecker = new ErrorCheckingStreamConsumer();
		getLog().info("Executing " + commandline);		
		int ret = CommandLineUtils.executeCommandLine(commandline, new StreamConsumerChain(systemOut).add(errorChecker),
				new StreamConsumerChain(systemErr).add(errorChecker));
		if (ret != 0 || errorChecker.isError()) {
			throw new MojoExecutionException("Errors executing RSA transformation. Please consult previous output for details.");

		}
	}

	private static class StreamConsumerChain implements StreamConsumer {
		private final Collection<StreamConsumer> chain = new ArrayList<StreamConsumer>();

		public StreamConsumerChain() {
		}

		public StreamConsumerChain(StreamConsumer streamConsumer) {
			add(streamConsumer);
		}

		public StreamConsumerChain add(StreamConsumer streamConsumer) {
			chain.add(streamConsumer);
			return this;
		}

		public void consumeLine(String line) {
			for (StreamConsumer streamConsumer : chain) {
				streamConsumer.consumeLine(line);
			}
		}
	}

	private static class ErrorCheckingStreamConsumer implements StreamConsumer {
		private boolean error;

		public void consumeLine(String line) {
			if (line.contains("[error]")) {
				error = true;
			}
		}

		public boolean isError() {
			return error;
		}
	}
}
