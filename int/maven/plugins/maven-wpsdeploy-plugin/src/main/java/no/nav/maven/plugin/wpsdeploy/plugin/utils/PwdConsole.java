package no.nav.maven.plugin.wpsdeploy.plugin.utils;

import java.io.*;

public class PwdConsole { 
	public static String getPassword() {
		ConsoleEraser consoleEraser = new ConsoleEraser();
		BufferedReader stdin = new BufferedReader(new
				InputStreamReader(System.in));
		consoleEraser.start();                       
		String pass;
		try {
			pass = stdin.readLine();
		} catch (IOException e) {
			throw new RuntimeException("An error occured during password reading", e);
		}
		consoleEraser.halt();
		System.out.print("\b");
		return pass;
	}
 }

class ConsoleEraser extends Thread {
	private boolean running = true;
	
	public void run() {
		while (running) {
			System.out.print("\b ");
		}
	}

	 public synchronized void halt() {
	  running = false;
	 }
}

