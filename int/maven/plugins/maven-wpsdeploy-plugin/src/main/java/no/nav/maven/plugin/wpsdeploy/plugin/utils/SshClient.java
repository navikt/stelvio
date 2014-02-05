package no.nav.maven.plugin.wpsdeploy.plugin.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import no.nav.maven.plugin.wpsdeploy.plugin.exceptions.NonZeroSshExitCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

public class SshClient {
	private Connection conn;
	private Session session;
	private final static Logger log = LoggerFactory.getLogger(SshClient.class);

	public SshClient(String host, String username, String password) {
		try {
			this.conn = new Connection(host);
			this.conn.connect();
			this.session = conn.openSession();
			this.session.requestDumbPTY();
			boolean authenticated = conn.authenticateWithPassword(username, password);
			if (!authenticated)
				throw new RuntimeException("User " + username + " is not authenticated on " + host);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	/**
	 * @param sshCommand
	 * @return Standard out from ssh command if command was successful
	 */
	public String execute(String sshCommand) {
		log.info("Executing: " + sshCommand.replace("-password \\S+", "-password ******"));

		try {
			session.execCommand(sshCommand);

			String output = readInputStream(session.getStdout());
			String errOutput = readInputStream(session.getStderr());

			int exitCode = session.getExitStatus();

			if(exitCode != 0){
				NonZeroSshExitCode nonZeroSshExitCode = new NonZeroSshExitCode("The command returned a non zero exit code(" + exitCode + ")" +
						"\n and had this error output: " + errOutput);
				nonZeroSshExitCode.setExitCode(exitCode);
				throw nonZeroSshExitCode;
			}

			return output.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (session != null) session.close();
		}
	}

	public void close() {
		this.conn.close();
	}

	private String readInputStream(InputStream inputStream) {
		StringBuffer output = new StringBuffer();

		Scanner scanner = new Scanner(inputStream);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			output.append(line);
			if (scanner.hasNextLine()) {
				output.append("\n");
				log.debug(line);
			}
		}
		scanner.close();
		return output.toString();
	}
}
