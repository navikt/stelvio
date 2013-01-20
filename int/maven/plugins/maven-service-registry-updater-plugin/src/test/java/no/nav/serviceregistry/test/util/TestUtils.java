package no.nav.serviceregistry.test.util;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public abstract class TestUtils {
	public static List<String> fileToLines(String filename) {
		List<String> lines = new LinkedList<String>();
		String line = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
			while ((line = in.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}

	public static String fileToString(String filename) {
		StringBuilder lines = new StringBuilder();
		String line = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
			while ((line = in.readLine()) != null) {
				lines.append(line+"\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines.toString();
	}

	public static void assertNoFileDiff(String expected, String actual){
		String expectedStringRepresentasion = fileToString(expected);
		String actualStringRepresentasion = fileToString(actual);
		assertEquals(expectedStringRepresentasion, actualStringRepresentasion);
	}
	
	public static String getResource(String resource){
		return TestUtils.class.getResource(resource).getFile();
	}

}
