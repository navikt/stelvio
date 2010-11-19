package no.nav.maven.plugin.confluence.mojo;

import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;
import org.codehaus.swizzle.confluence.Confluence;

/**
 * An abstract class inherited by all mojo's in the confluence plugin. It contains a common interactivity prompter common 
 * for all mojos. It also extracts the bus-deploy version and the busconfiguration version from the deploy. These are used by several 
 * children. Surprisingly the execute() method also logs in to confluence and exposes the confluence object to children.
 * 
 * @author test@example.com
 */
@SuppressWarnings("unchecked")
public abstract class ConfluenceMojo extends AbstractMojo {
	
	/** @component */
	protected Prompter prompter;
	
	/**
	 * 
	 * @parameter expression="${confluenceusername}" default-value="deployer"
	 * @required
	 */
	protected String userName;

	/**
	 * 
	 * @parameter expression="${confluenceusername}" default-value="deployer"
	 * @required
	 */
	protected String password;

	/**
	 * 
	 * @parameter expression="${endpoint}"
	 *            default-value="http://confluence.adeo.no"
	 * @required
	 */
	protected String endPoint;

	/**
	 * @parameter expression="${environment}"
	 * @required
	 */
	protected String environment;

	/**
	 * @parameter expression="${project.dependencyArtifacts}"
	 * @required
	 */
	protected Set<Artifact> dependencyArtifacts;

	/**
	 * @parameter expression="${project.artifacts}"
	 * @required
	 */
	protected Set<Artifact> artifacts;
	
	/**
	 * @parameter expression="${interactiveMode}" default-value="false"
	 * @required
	 */
	protected Boolean interactiveMode;

	protected Confluence confluence;
	
	protected String deployString = "";
	protected String configurationString = "";
	
	public void execute(){

		if (interactiveMode == true) {
			String answer = null;
			try {
				answer = prompter.prompt("Do you want to perform step \"" + getGoalPrettyPrint() + "\" (y/n/a)? ", "n");
			} catch (PrompterException e) {
				System.err.println("An error occured during prompt input. Skipping this step...");
				return;
			}
			if ("n".equalsIgnoreCase(answer)) {
				getLog().info("Skipping step: " + getGoalPrettyPrint());
				return;
			}
			if ("a".equalsIgnoreCase(answer)) {
				getLog().info("Aborting " + getGoalPrettyPrint());
				System.exit(0);
			}

		}
		
		for (Artifact a : dependencyArtifacts) {
			if (a.getArtifactId().equals("esb-modules")) {
				deployString = a.getVersion();
			}
			if (a.getArtifactId().equals("busconfiguration")) {
				configurationString = a.getVersion();
			}
		}
		
		try {
			confluence = new Confluence(endPoint);
			confluence.login(userName, password);
			doExecute();
			confluence.logout();
		} catch (Exception e) {
			getLog().error(e.getMessage());			
			try {
				if(abortProcess() == true) {
					System.err.println("An error occured during step \"" + getGoalPrettyPrint() + "\"");
				} else {
					return;
				}
			} catch (PrompterException e1) {
				System.err.println("An error occured during prompt input. Skipping this step...");
			}
		}	
	}

	private final boolean abortProcess() throws PrompterException {
		if(interactiveMode == true) {
			String answer=null;
			answer = prompter.prompt("An error occured during step \"" + getGoalPrettyPrint() + "\". Do you want to abort the deploy process (y/n)? ", "n");
			if("y".equalsIgnoreCase(answer)) {
				return true;
			} else {
				return  false;
			}
		} else {
			return true;
		}
	}
	
	protected abstract String getGoalPrettyPrint();
	protected abstract void doExecute() throws MojoExecutionException, MojoFailureException;
};