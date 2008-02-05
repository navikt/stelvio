package no.nav.maven.plugins.datapower.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtils {

	public static String getInputStreamAsString(InputStream input, boolean closeStream) throws IOException {
		StringBuffer str = new StringBuffer();
		byte[] bytes = new byte[4096];
		while ((input.read(bytes) != -1)) {
			str.append(new String(bytes));
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
