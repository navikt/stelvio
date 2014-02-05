package no.nav.maven.plugin.wpsdeploy.plugin.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class SshClient {
	private Connection conn;
	private final static Logger log = LoggerFactory.getLogger(SshClient.class);

	public SshClient(String host, String user, String password) {
		try {
			conn = new Connection(host);
			conn.connect();
			boolean authenticated = conn.authenticateWithPassword(user, password);
			if (!authenticated)
				throw new RuntimeException("User " + user + " is not authenticated on " + host);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void close() {
		conn.close();
	}

	/**
	 * @param sshCommand
	 * @return Standard out from ssh command if command was successful
	 */
	public String execute(String sshCommand) {
		return execute(sshCommand, true, true, false, null);
	}

	/**
	 *
	 * @param sshCommand
	 * @return
	 */
	public String executeIgnoreReturnCode(String sshCommand) {
		return execute(sshCommand, true, false, true, null);
	}

	/**
	 *
	 * @param sshCommand
	 * @param printCommand
	 * @param verbose
	 * @return
	 */
	public String execute(String sshCommand, boolean printCommand, boolean verbose) {
		return execute(sshCommand, printCommand, verbose, false, null);
	}

	/**
	 *
	 * @param sshCommand
	 * @param ignoreReturnCode
	 *            Will not throw Exception if return code > 0 Useful when running command that you want to recover from without
	 *            throwing exception
	 * @return
	 */
	public String execute(String sshCommand, boolean ignoreReturnCode) {
		return execute(sshCommand, true, true, ignoreReturnCode, null);
	}

	/**
	 * @param sshCommand
	 * @param printCommand
	 * @param verbose
	 * @return Standard out from ssh command if command was successful
	 */
	// TODO Consider returning error code when ignore is set?
	public String execute(String sshCommand, boolean printCommand, boolean verbose, boolean ignoreReturnCode, Set<String> passwordsToMask) {
		if (printCommand) {
			if (passwordsToMask != null) {
				String maskedCommand = sshCommand;
				for (String passwordToMask : passwordsToMask) {
					maskedCommand = maskedCommand.replace(passwordToMask, "*****");
				}
				log.debug("Executing: " + maskedCommand);
			} else {
				log.debug("Executing: " + sshCommand);
			}

		}
		StringBuffer sysout = new StringBuffer();
		Session session = null;
		Scanner stdOutScanner = null;
		Scanner stdErrScanner = null;
		try {
			session = conn.openSession();
			session.requestDumbPTY();
			session.execCommand(sshCommand);
			InputStream stdout = new StreamGobbler(session.getStdout());
			stdOutScanner = new Scanner(stdout);
			while (stdOutScanner.hasNextLine()) {
				String line = stdOutScanner.nextLine();
				sysout.append(line);
				if (stdOutScanner.hasNextLine()) {
					sysout.append("\n");
				}
				if (verbose) {
					System.out.println(line);
				}
			}

			InputStream stderr = new StreamGobbler(session.getStderr());
			StringBuffer sysErr = new StringBuffer();

			stdErrScanner = new Scanner(stderr);
			while (stdErrScanner.hasNextLine()) {
				String line = stdErrScanner.nextLine();
				sysErr.append(line);
				if (stdErrScanner.hasNextLine()) {
					sysErr.append("\n");
				}
				System.err.println(line);
			}

			// Will only throw exception when we are not ignoring the return code
			if (!ignoreReturnCode) {
				if (session.getExitStatus() != null && 0 != session.getExitStatus()) {
					throw new RuntimeException("Error executing " + sshCommand + " ExitCode " + session.getExitStatus() + " \n System.out: " + sysout.toString() + " \n System.err: " + sysErr.toString());
				}
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (stdErrScanner != null) {
				stdErrScanner.close();
			}
			if (stdOutScanner != null) {
				stdOutScanner.close();
			}
			if (session != null) {
				session.close();
			}
		}
		return sysout.toString();
	}

	/**
	 * Puts localFile to the remoteTargetDirectory
	 *
	 * @param localFile
	 * @param remoteTargetDirectory
	 */
	public void scpPut(String localFile, String remoteTargetDirectory) {
		SCPClient scpClient = new SCPClient(conn);
		try {
			scpClient.put(localFile, remoteTargetDirectory);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void scpPut(byte[] data, String remoteFileName, String remoteTargetDirectory){
		SCPClient scpClient = new SCPClient(conn);
		try {
			scpClient.put(data, remoteFileName, remoteTargetDirectory);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets remoteFile and downloads to targetDirectory
	 *
	 * @param remoteFile
	 * @param targetDirectory
	 */
	public void scpGet(String remoteFile, String targetDirectory) {
		SCPClient scpClient = new SCPClient(conn);
		try {
			scpClient.get(remoteFile, targetDirectory);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public BufferedReader getReaderFromFile(String remoteFilePath) {
		SCPClient scpClient = new SCPClient(conn);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			scpClient.get(remoteFilePath, outputStream);
			return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(outputStream.toByteArray())));
		} catch (IOException e) {
			throw new RuntimeException("Unable to read content of file " + remoteFilePath, e);
		}
	}
}
