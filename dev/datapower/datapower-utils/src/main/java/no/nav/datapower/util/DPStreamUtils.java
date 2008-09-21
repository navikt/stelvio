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
	
	public static void pump(InputStream in, OutputStream out) throws IOException {
		byte[] buf = new byte[10240];
		int len;
		while((len=in.read(buf))>0){
			out.write(buf,0,len);
		}
	}

	public static void pumpAndClose(InputStream in, OutputStream out) throws IOException {
		pump(in,out);
		in.close();
		out.close();
	}
}
