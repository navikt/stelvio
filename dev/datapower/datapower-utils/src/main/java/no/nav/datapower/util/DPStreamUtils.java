package no.nav.datapower.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class DPStreamUtils {

	public static String getInputStreamAsString(InputStream input, boolean closeStream) throws IOException {
		StringBuffer str = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input,"UTF-8"));
		String line = null;
		while((line = reader.readLine()) != null){
			str.append(line + System.getProperty("line.separator"));
		}
		if (closeStream)
			input.close();
		return str.toString();
	}
	
	public static void writeStringToOutputStream(String data, OutputStream output, boolean closeStream) throws IOException {
		output.write(data.getBytes());
		output.flush();
		if (closeStream)
			output.close();
	}
}
