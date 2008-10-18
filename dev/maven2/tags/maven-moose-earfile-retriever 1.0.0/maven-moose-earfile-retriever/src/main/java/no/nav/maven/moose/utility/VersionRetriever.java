package no.nav.maven.moose.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;



public class VersionRetriever {
	
	/**
	 * Searches through the mapping file, and returns the correct Maven version
	 * id based on the parameter bid (Moose's build id).
	 * 
	 * @param bid
	 *            the BuildId from Moose
	 * @param mapFile
	 *            The path of the version-mapping file
	 * @return A String representing the maven version Id for the build
	 * @throws IOException
	 */
	public String getVersionId(String bid, File mapFile) throws IOException 
	{
		String version = null;
		if (!mapFile.exists()) 
		{
			throw new IOException("The Version Map File '" + mapFile
					+ "' can not be found");
		}
		BufferedReader reader = new BufferedReader(new FileReader(mapFile));
		String line;
		while ((line = reader.readLine()) != null) 
		{
			if (line.startsWith(bid)) 
			{
				version = line.substring(bid.length() + 1);
			}
		}
		reader.close();
		System.out.println("Version" + version );
		return version;
	}

}
