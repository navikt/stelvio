package no.nav.maven.plugin.websphere.plugin.mojo;

import java.io.File;
import java.io.FilenameFilter;

public class Testing {

	public static void main(String[] args) {

		File targetFolder = new File("C:/deploy-pom/target/EARFilesToDeploy");
		FilenameFilter fnFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if (name.endsWith(".ear"))
					return true;
				return false;
			}
		};

		String[] deployedModules = targetFolder.list(fnFilter);

		for (int i = 0; i < deployedModules.length; i++) {

			String module = deployedModules[i].replace(".ear", "");
			System.out.println(module);
		}
	}

}
