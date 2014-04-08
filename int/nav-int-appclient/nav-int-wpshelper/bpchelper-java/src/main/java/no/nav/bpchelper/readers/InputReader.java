package no.nav.bpchelper.readers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class InputReader {

	public InputReader() {
	}

	public ArrayList<String> getContent(File inputFile) {

		String inputString = null;
		BufferedReader reader;
		ArrayList<String> content = new ArrayList<String>();

		try {
			reader = new BufferedReader(new FileReader(inputFile));
			try {
				while ((inputString = reader.readLine()) != null) {
					content.add(inputString);
				}
			} finally {
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return content;
	}
}
