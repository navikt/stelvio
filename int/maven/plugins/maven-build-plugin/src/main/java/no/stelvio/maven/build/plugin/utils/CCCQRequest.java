package no.stelvio.maven.build.plugin.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Class that runs different additional CC/CQ requests
 * 
 * @author test@example.com
 */
public class CCCQRequest {

	private static final String TMP_FILE = "cleartool_output.txt";

	/**
	 * This method runs deliver -preview command, logs output to a tmp file and then parses it to extract NAV00xxxxxx IDs
	 * 
	 * @param buildStream
	 *            - project name to search in the right folder
	 * @return list of activities' IDs
	 */
	public static List<String> getActivitiesToDeliver(String ccProjectDir, String buildStream, String activities) {
		String workingDir = ccProjectDir + buildStream;
		String subcommand = "deliver -preview > " + TMP_FILE;
		List<String> result = new ArrayList<String>(0);
		try {
			// run command and write output to a file
			CleartoolCommandLine.runClearToolCommand(workingDir, subcommand);
			// parse the file and get activities' IDs
			File tmpFile = new File(workingDir +"\\"+ TMP_FILE);
			BufferedReader br = new BufferedReader(new FileReader(tmpFile));
			String s = br.readLine();
			while (s != null) {
				if (s.contains("activity:"))
					result.add(s.substring(s.indexOf(":") + 1, s.indexOf(":") + 12).trim());
				s = br.readLine();
			}
			if (result.size() == 0) return null;
			// check to see if we decided to include not all activities
			if (!activities.equalsIgnoreCase("all")){
				String [] activityList = activities.split(",");
				List<String> res = new ArrayList<String>();
				for (String act : activityList){
					if (!result.contains(act)) throw new MojoFailureException("Detected an activity that has nothing to do with this build");
					res.add(act);
				}
				return res;
			}
		} catch (MojoFailureException e) {
			System.out
					.println("[WARNING] Unable to run \"cleartool deliver -preview\". All activities will be included in delivery.");
		} catch (FileNotFoundException e) {
			System.out.println("[WARNING] Tmpfile not found. All activities will be included in delivery.");
		} catch (IOException e) {
			System.out.println("[WARNING] Tmpfile readning error. All activities will be included in delivery.");
		}

		return result;
	}

	/**
	 * This method checks out the given file
	 * 
	 * @param f
	 *            - file to check out
	 * @return 0 if everything is OK
	 * @throws MojoFailureException
	 */
	public static int checkOutFile(File f) throws MojoFailureException {
		String subcommand = "checkout -nc -force" + f.getName();
		return CleartoolCommandLine.runClearToolCommand(f.getParent(), subcommand);
	}

	/**
	 * This method checks in the given file
	 * 
	 * @param f
	 *            - file to check in
	 * @return 0 if everything is OK
	 * @throws MojoFailureException
	 */
	public static int checkInFile(File f) throws MojoFailureException {
		String subcommand = "checkin -nc " + f.getName();
		return CleartoolCommandLine.runClearToolCommand(f.getParent(), subcommand);
	}

	private static final String TASK_SCRIPT = "create_task.pl";

	/**
	 * This method creates new activity in CQ
	 * 
	 * @param workDir
	 *            - working directory
	 * @return activity id
	 * @throws MojoExecutionException 
	 * @throws MojoFailureException
	 */
	public static String createActivity(String workDir, String headline, String description) throws MojoFailureException {
		try {
			// script creates activity  and writes it's id to tmp file
			String command = "cqperl " + TASK_SCRIPT + " " + headline + " " + description;
			Commandline cmd = new Commandline();
			cmd.setWorkingDirectory(workDir);
			Commandline.Argument arg = new Commandline.Argument();
			arg.setLine(command);
			cmd.addArg(arg);
			CommandLineUtil.executeCommand(cmd);
			
			// read id from tmp file
			File tmpFile = new File(workDir + TMP_FILE);
			BufferedReader br = new BufferedReader(new FileReader(tmpFile));
			return br.readLine();
		} catch (IOException e) {
			throw new MojoFailureException("Unable obtain task ID for this build. Try running setactivity manually");
		}
	}

	/**
	 * This method sets a given activity to the view
	 * 
	 * @param buildStreamLocation
	 *            - path to the stream
	 * @param activity
	 *            - activity id
	 * @return 0 if everything is OK
	 * @throws MojoFailureException 
	 */
	public static int setActivity(String buildStreamLocation, String activity) throws MojoFailureException {
		String subcommand = "setact " + activity;
		String workingDir = buildStreamLocation;
		return CleartoolCommandLine.runClearToolCommand(workingDir, subcommand);
	}
}
