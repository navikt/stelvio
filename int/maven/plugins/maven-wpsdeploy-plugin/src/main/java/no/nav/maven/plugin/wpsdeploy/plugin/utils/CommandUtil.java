package no.nav.maven.plugin.wpsdeploy.plugin.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandUtil {


	public static void runCommand(String cmd) {
		Runtime run = Runtime.getRuntime();
		Process pr;
		try {
			pr = run.exec(cmd);
			pr.waitFor();
			BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line = "";
			while ((line=buf.readLine())!=null) {
				System.out.println(line);
			}
		} catch (InterruptedException e) {
			throw new ExceptionConverter(e);
		} catch (IOException e1) {
			throw new ExceptionConverter(e1);
		}
	}
}
