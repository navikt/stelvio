package no.nav.maven.plugin.wpsdeploy.plugin.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import no.nav.maven.plugin.wpsdeploy.plugin.exceptions.NonZeroSshExitCode;
import no.nav.maven.plugin.wpsdeploy.plugin.models.SshUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

public class SshClient {
	private Connection conn;
	private Session session;
	private final static Logger log = LoggerFactory.getLogger(SshClient.class);

	public SshClient(SshUser sshUser) {
		try {
			conn = new Connection(sshUser.getHostname());
			conn.connect();
			boolean authenticated = conn.authenticateWithPassword(sshUser.getUsername(), sshUser.getPassword());
			if (!authenticated){
				throw new RuntimeException("User " + sshUser.getUsername() + " is not authenticated on " + sshUser.getHostname());
			}
			session = conn.openSession();
			session.requestDumbPTY();
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

			waitForSshCommandToFinish(session);
			int exitCode = session.getExitStatus();

			if(exitCode != 0){
				NonZeroSshExitCode nonZeroSshExitCode = new NonZeroSshExitCode("The command returned a non zero exit code(" + exitCode + ")" +
						"\n and had this error output: " + errOutput);
				nonZeroSshExitCode.setExitCode(exitCode);
				throw nonZeroSshExitCode;
			}

			return output.toString();
		} catch (IOException e) {
			throw new RuntimeException("The SshClient failed to execute the ssh command!", e);
		} catch (InterruptedException e) {
			throw new RuntimeException("The SshClient failed to wait for the ssh command to finish!", e);
		} finally {
			if (session != null) session.close();
		}
	}

	private String readInputStream(InputStream inputStream) {
		StringBuffer output = new StringBuffer();

		Scanner scanner = new Scanner(inputStream);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			output.append(line);
			if (scanner.hasNextLine()) {
				output.append("\n");
			}
			log.info("SSH: " + line);
		}
		scanner.close();
		return output.toString();
	}

	private void waitForSshCommandToFinish(Session session) throws InterruptedException {
		Thread.sleep(20);
		while (session.getExitStatus() == null){
			int furtherWaitTime = 1000;
			log.info("Waiting " + furtherWaitTime + " milliseconds for the SSH channel to properly close...");
			Thread.sleep(furtherWaitTime);
		}
	}
}
