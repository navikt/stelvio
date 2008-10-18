package no.nav.maven.moose.utility;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

public class EarFileRetriever {

	private Map mooseIdToEarNameMathcer;

	public Map getMooseIdToEarNameMathcer() {
		return mooseIdToEarNameMathcer;
	}

	public void setMooseIdToEarNameMathcer(Map mooseIdToEarNameMathcer) {
		this.mooseIdToEarNameMathcer = mooseIdToEarNameMathcer;
	}
	
	public File getEarDescriptorConfig(String mooseID, String version, String earLocation){
		if(getMooseIdToEarNameMathcer() != null){
			System.out.println("MAP: " + getMooseIdToEarNameMathcer());
			for (Iterator it = mooseIdToEarNameMathcer.entrySet().iterator(); it.hasNext() ; ) {
				Map.Entry entry = (Map.Entry)it.next();
				
				if(mooseID.startsWith((String)entry.getKey())){
					return new File(earLocation + "/" + (String)entry.getValue() + version+".ear");
				}
			}
		}
		return null;
	}
	
}
