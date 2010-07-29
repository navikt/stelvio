package no.nav.maven.plugin.websphere.plugin.mojo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import no.nav.maven.plugin.websphere.plugin.utils.Archiver;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * @author test@example.com
 * 
 *         Goal that modifies the specified business process modules to not delete the finished process instances when complete.
 * 
 * @goal modify-bproc
 * @requiresDependencyResolution
 */
public class ModifyBPROCModules extends WebsphereUpdaterMojo {

	/**
	 * @component roleHint="zip"
	 * @required
	 * @readonly
	 */
	private UnArchiver unArchiver;

	ArrayList<String> modules = new ArrayList<String>();

	public ModifyBPROCModules() {

		// Specifies which modules to modify
		modules.add("nav-bproc-pen-ppen002");
		modules.add("nav-bproc-pen-ppen003");
		modules.add("nav-bproc-pen-ppen008");
		modules.add("nav-bproc-pen-ppen015");
	}

	public final void applyToWebSphere(final Commandline commandLine) throws MojoExecutionException, MojoFailureException {

		File earsDir = new File(deployableArtifactsHome);
		File stagingArea = new File(deployableArtifactsHome + "/staging");

		// create staging area, throws exception if unable to
		if (!stagingArea.mkdirs()) {
			throw new MojoFailureException("[ERROR] Unable to create staging area: " + stagingArea);
		}

		String[] ears = earsDir.list();

		String earFilePath, jarFilePath;
		File extractedEARFolder = null;

		// for each module defined
		for (String module : modules) {

			getLog().info("[INFO] Modifying " + module + " ...");

			for (int i = 0; i < ears.length; i++) {
				if (ears[i].contains(module)) {
					String earName = ears[i];
					earFilePath = deployableArtifactsHome + "/" + earName;
					File ear = new File(earFilePath);
					Archiver.extractArchive(ear, stagingArea, unArchiver);

					extractedEARFolder = new File(stagingArea + "/" + earName.replace(".ear", ""));
					String[] filesInsideEAR = extractedEARFolder.list();

					for (int j = 0; j < filesInsideEAR.length; j++) {
						if (earName.contains(filesInsideEAR[j].replace(".jar", "")) && !filesInsideEAR[j].endsWith("EJB.jar")) {
							String jarName = filesInsideEAR[j];
							jarFilePath = extractedEARFolder + "/" + jarName;
							File jar = new File(jarFilePath);
							Archiver.extractArchive(jar, extractedEARFolder, unArchiver);
							File bpelFileDir = new File(extractedEARFolder + "/" + jarName.replace(".jar", "") + "/no/nav/bpel");
							String[] bpelFileDirFiles = bpelFileDir.list();
							File bpelFile = null;
							for (int k = 0; k < bpelFileDirFiles.length; k++) {
								if (bpelFileDirFiles[k].endsWith(".bpel")) {
									if (module.equals("nav-bproc-pen-ppen008")) {
										if (bpelFileDirFiles[k].equals("OverforeOmsorgspoengBPEL.bpel")) {
											bpelFile = new File(bpelFileDir + "/" + bpelFileDirFiles[k]);
										} else {
											continue;
										}
									} else {
										bpelFile = new File(bpelFileDir + "/" + bpelFileDirFiles[k]);
									}
								}
							}
							modifyBPELFile(bpelFile);
							File modifiedJarFolder = new File(extractedEARFolder + "/" + jarName.replace(".jar", ""));
							Archiver.createArchive(modifiedJarFolder, extractedEARFolder, "jar");
						}
					}
				}
			}
			Archiver.createArchive(extractedEARFolder, earsDir, "ear");
			getLog().info("[INFO] Successfully modified " + module + "!");
		}
		stagingArea.delete();
	}

	private void modifyBPELFile(File bpel) throws MojoFailureException {

		try {

			BufferedReader br = new BufferedReader(new FileReader(bpel));
			StringBuffer sb = new StringBuffer();

			String text = null;
			while ((text = br.readLine()) != null) {
				if (text.contains("wpc:autonomy=\"peer\"")) {
					text = text.replace("wpc:autonomy=\"peer\"", "wpc:autonomy=\"peer\" wpc:autoDelete=\"no\"");
				}
				sb.append(text).append(System.getProperty("line.separator"));
			}

			br.close();
			BufferedWriter bw = new BufferedWriter(new FileWriter(bpel));
			bw.write(sb.toString());
			bw.close();

		} catch (IOException e) {
			throw new MojoFailureException("[ERROR] Error during modification of .bpel file " + e);
		}
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Modify BPROC modules to not delete finished business processes";
	}
}