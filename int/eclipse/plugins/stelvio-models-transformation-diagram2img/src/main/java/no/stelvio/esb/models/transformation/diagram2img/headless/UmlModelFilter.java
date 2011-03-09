package no.stelvio.esb.models.transformation.diagram2img.headless;

import java.io.File;
import java.io.FilenameFilter;

public class UmlModelFilter implements FilenameFilter 
{
	public boolean accept(File dir, String name) 
	{
		return name.endsWith(".emx");
	}

}
