package no.stelvio.maven.build.plugin.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.MojoFailureException;

/**
 * Class that runs different additional CC/CQ requests
 * 
 * @author test@example.com
 */
public class CCCQRequest {
	
	private static final String TMP_FILE = "D:\\Temp\\cleartool_output.txt";
	
	/**
	 * This method runs deliver -preview command, logs output to a tmp file
	 * and then parses it to extract NAV00xxxxxx IDs
	 * @param project - project name to search in the right folder
	 * @return list of activities' IDs
	 */
	public static List<String> getActivitiesToDeliver(String project){
		String workingDir = "D:/cc/"+project+"_Dev";
		String subcommand = "deliver -preview > " + TMP_FILE;
		List<String> result = new ArrayList<String>();
		try {
			// run command and write output to a file
			CleartoolCommandLine.runClearToolCommand(workingDir, subcommand);
			// parse the file and get activities' IDs
			File tmpFile = new File(TMP_FILE);
			BufferedReader br = new BufferedReader(new FileReader(tmpFile));
			String s = br.readLine();
			while (s != null){
				if (s.contains("activity:"))
					result.add(s.substring(s.indexOf(":")+1, s.indexOf(":")+12).trim());
				s = br.readLine();
			}
		} catch (MojoFailureException e) {
			System.out.println("[WARNING] Unable to run \"cleartool deliver -preview\". All activities will be included in delivery.");
		} catch (FileNotFoundException e) {
			System.out.println("[WARNING] Tmpfile not found. All activities will be included in delivery.");
		} catch (IOException e) {
			System.out.println("[WARNING] Tmpfile readning error. All activities will be included in delivery.");
		}
		
		return result;
	}

}
