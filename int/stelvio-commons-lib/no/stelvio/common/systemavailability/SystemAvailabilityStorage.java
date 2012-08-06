/*
 * Created on Mar 27, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package no.stelvio.common.systemavailability;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import no.stelvio.common.util.IOUtils;

import com.ibm.websphere.bo.BOXMLDocument;
import com.ibm.websphere.bo.BOXMLSerializer;
import com.ibm.websphere.sca.ServiceManager;
import commonj.sdo.DataObject;

/**
 * @author utvikler
 * 
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
public class SystemAvailabilityStorage {
	String saDirName = null;
	private com.ibm.websphere.bo.BOFactory boFactory = null;
	BOXMLSerializer xmlSerializerService = null;
	private static Map<String, AvailabilityCacheEntry> availabilityCache = new HashMap<String, AvailabilityCacheEntry>();

	public SystemAvailabilityStorage() {

		ServiceManager serviceManager = new com.ibm.websphere.sca.ServiceManager();
		xmlSerializerService = (BOXMLSerializer) serviceManager.locateService("com/ibm/websphere/bo/BOXMLSerializer");
		boFactory = (com.ibm.websphere.bo.BOFactory) serviceManager.locateService("com/ibm/websphere/bo/BOFactory");

		saDirName = System.getProperty("stelvio.systemavailability.directory");
		if (saDirName == null) {
			saDirName = System.getProperty("user.dir") + "/StelvioSystemAvailabilityFramework";
		}
		File saDir = new File(saDirName);
		if (!saDir.exists())
			saDir.mkdirs();
		// System.out.println("Setting working directory to "+saDirName );

	}

	public String getSystemAvailabilityDirectory() {
		return saDirName;
	}

	public void close() {
		// For potential DB support.
	}

	public List listAvailabilityRecordSystemNames() {
		ArrayList<String> ret = new ArrayList<String>();
		File dir = new File(saDirName);
		File[] sysList = dir.listFiles();
		for (int i = 0; i < sysList.length; i++) {
			if (sysList[i].isFile())
				ret.add(sysList[i].getName().substring(0, sysList[i].getName().length() - 4)); // Deducts
			// .xml-suffix
		}
		return ret;
	}

	public void storeAvailabilityRecord(AvailabilityRecord record) {
		if (record == null || "".equals(record.systemName)) {
			return;
		}
		
		FileOutputStream fos = null;
		try {
			DataObject obj = boFactory.create("http://stelvio-commons-lib/no/stelvio/common/systemavailability",
					"SystemAvailabilityRecord");
			obj.setString("SystemName", record.systemName);
			obj.setInt("MaxSimultaneousInvocations", record.maxSimultaneousInvocations);

			List<DataObject> operationList = new ArrayList<DataObject>();
			int i;
			if (record.operations == null)
				record.operations = new ArrayList();
			Iterator opIt = record.operations.iterator();
			while (opIt.hasNext()) {
				OperationAvailabilityRecord opRec = (OperationAvailabilityRecord) opIt.next();

				DataObject operationObj = boFactory.create("http://stelvio-commons-lib/no/stelvio/common/systemavailability",
						"OperationAvailabilityRecord");
				operationObj.setString("OperationName", opRec.operationName);
				operationObj.setBoolean("Unavailable", opRec.unAvailable);
				operationObj.setString("Reason", opRec.unavailableReason);
				operationObj.setBoolean("Stubbed", opRec.stubbed);
				operationObj.setBoolean("RecordStubData", opRec.recordStubData);
				operationList.add(operationObj);
			}
			obj.setList("Operations", operationList);
			File f = new File(saDirName, record.systemName + ".xml"); // May
			// not
			// handle
			// special
			// characters
			
			fos = new FileOutputStream(f);
			xmlSerializerService.writeDataObject(obj, "http://no/stelvio/systemavailability/", "SystemAvailabilityRecord", fos);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			IOUtils.closeQuietly(fos);
		}
	}

	@SuppressWarnings("unchecked")
	public AvailabilityRecord getAvailabilityRecord(String systemName) {
		if (systemName == null) {
			return null;
		}
		FileInputStream fis = null;
		try {
			File f = new File(saDirName, systemName + ".xml");
			if (!f.exists())
				return null;
			synchronized (availabilityCache) {
				AvailabilityCacheEntry cachedElement = availabilityCache.get(systemName);
				if (cachedElement != null) {
					if (cachedElement.lastModified == f.lastModified()) {
						return cachedElement.availabilityRecord;
					}
				}
			}

			fis = new FileInputStream(f);
			BOXMLDocument doc = xmlSerializerService.readXMLDocument(fis);
			DataObject obj = doc.getDataObject();
			AvailabilityRecord rec = new AvailabilityRecord();
			rec.systemName = obj.getString("SystemName");
			rec.maxSimultaneousInvocations = obj.getInt("MaxSimultaneousInvocations");
			rec.operations = new ArrayList();
			List storedOperationList = obj.getList("Operations");
			Iterator i = storedOperationList.iterator();
			while (i.hasNext()) {
				DataObject storedOperation = (DataObject) i.next();
				OperationAvailabilityRecord opRec = new OperationAvailabilityRecord();
				opRec.operationName = storedOperation.getString("OperationName");
				opRec.unAvailable = storedOperation.getBoolean("Unavailable");
				opRec.unavailableReason = storedOperation.getString("Reason");
				opRec.stubbed = storedOperation.getBoolean("Stubbed");
				opRec.recordStubData = storedOperation.getBoolean("RecordStubData");
				rec.operations.add(opRec);
			}
			
			AvailabilityCacheEntry newEntry = new AvailabilityCacheEntry(rec, f.lastModified());
			synchronized (availabilityCache) {
				availabilityCache.put(systemName, newEntry);
			}
			return rec;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally{
			IOUtils.closeQuietly(fis);
		}
	}

	public OperationAvailabilityRecord calculateOperationAvailability(String systemName, String operationName) {
		AvailabilityRecord sRec = findSystemRecord(systemName);
		if (sRec == null) {
			return null;
		}
		OperationAvailabilityRecord opRec = sRec.findOrCreateOperation(operationName);
		OperationAvailabilityRecord allOpRec = sRec.findOrCreateOperation("ALL");
		OperationAvailabilityRecord resultRec = new OperationAvailabilityRecord();
		resultRec.operationName = operationName;
		if (allOpRec.unAvailable) {
			resultRec.unAvailable = allOpRec.unAvailable;
			resultRec.unavailableReason = allOpRec.unavailableReason;
		} else {
			resultRec.unAvailable = opRec.unAvailable;
			resultRec.unavailableReason = opRec.unavailableReason;
		}
		resultRec.stubbed = allOpRec.stubbed || opRec.stubbed;
		resultRec.recordStubData = allOpRec.recordStubData || opRec.recordStubData;
		return resultRec;
	}

	public AvailabilityRecord findOrCreateSystemRecord(String systemName) {
		AvailabilityRecord rec = this.getAvailabilityRecord(systemName);
		if (rec == null) {
			rec = new AvailabilityRecord();
			rec.systemName = systemName;
			rec.operations = new ArrayList();
			rec.findOrCreateOperation("ALL");
			storeAvailabilityRecord(rec);
		}
		return rec;
	}

	public AvailabilityRecord findSystemRecord(String systemName) {
		AvailabilityRecord rec = this.getAvailabilityRecord(systemName);
		return rec;
	}

	public void deleteAvailabilityRecord(String systemName) {
		File f = new File(saDirName, systemName + ".xml");
		if (!f.exists())
			return;
		f.delete();
		return;
	}

	private class AvailabilityCacheEntry {
		public AvailabilityCacheEntry(AvailabilityRecord rec, long l) {
			this.availabilityRecord = rec;
			this.lastModified = l;
		}

		public long lastModified;
		public AvailabilityRecord availabilityRecord;
	}

	/**
	 * Turns on stubbing for all SystemNames. 
	 */
	public void turnOnAllStubs() {
		turnOnOffStubbingRecording(true, false);
	}
	
	/**
	 * Turns off stubbing for all SystemNames.
	 */
	public void turnOffAllStubs() {
		turnOnOffStubbingRecording(false, null);
	}
	
	/**
	 * Turns on recording for all SystemNames.
	 */
	public void turnOnAllRecords() {
		turnOnOffStubbingRecording(false, true);
	}
	
	/**
	 * Turns off recording for all SystemNames.
	 */
	public void turnOffAllRecords() {
		turnOnOffStubbingRecording(null, false);
	}
	
	/**
	 * Changes the operation "ALL" to stub or record for all SystemNames.
	 * 
	 * Turns stubbing on/off if the boolean parameter "stubbing" is true/false.
	 * If the boolean parameter "stubbing" is null, stubbing is not changed.
	 * 
	 * Turns recording on/off if the boolean parameter "recording" is true/false.
	 * If the boolean parameter "recording" is null, recording is not changed.
	 * 
	 * Recording will be set to off if setting stubbing on, and opposite.
	 * 
	 * @param stubbing
	 * @param recording
	 */
	private void turnOnOffStubbingRecording(Boolean stubbing, Boolean recording) {
		List tjenester = listAvailabilityRecordSystemNames();
		// Alle tjenestene
		for (int i = 0; i < tjenester.size(); i++) {
			String systemName = (String) tjenester.get(i);
			AvailabilityRecord record = getAvailabilityRecord(systemName);
			// Alle operasjonene
			for (int x = 0; x < record.operations.size(); x++) {
				OperationAvailabilityRecord operation = (OperationAvailabilityRecord) record.operations.get(x);
				if (operation != null) {
					// Kun "ALL"-operasjonen blir satt til true ved true
					if ("ALL".equals(operation.operationName) && null != recording) {
						operation.recordStubData = recording.booleanValue();
					}
					if ("ALL".equals(operation.operationName) && null != stubbing) {
						operation.stubbed = stubbing.booleanValue();
					}
					// Alle operasjoner blir satt til false ved false
					if (null != recording && recording.booleanValue() == false) {
						operation.recordStubData = false;
					}
					if (null != stubbing && stubbing.booleanValue() == false) {
						operation.stubbed = false;
					}
				}
			}
			// Lagre til XML
			storeAvailabilityRecord(record);
		}
	}
	
	/**
	 * Returns a list of java.io.File with installed nav-modules on the wps-server (name begins at "nav-").
	 * 
	 * @return
	 */
	public List<File> listInstalledNAVModules() {
		
		List<File> liste = new ArrayList<File>();
		
		// Finne absolutt-path til denne klassen:
		String ressursnavn = SystemAvailabilityStorage.class.getName().replace('.', '/') + ".class";
		ClassLoader cl = SystemAvailabilityStorage.class.getClassLoader();
		if (cl == null){
			cl = ClassLoader.getSystemClassLoader();
		}
		URL url = cl.getResource(ressursnavn);
		String urlString = url.toString();
		File fil = null;
		
		// Gjør om URL til File
		if (urlString.startsWith("jar:file:")) {
			try {
				urlString = urlString.substring("jar:".length(), urlString.lastIndexOf("!"));
				URI uri = new URI(urlString);
				fil = new File(uri);
			}
			catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		else if (urlString.startsWith("wsjar:file:")) {
			try {
				urlString = urlString.substring("wsjar:".length(), urlString.lastIndexOf("!"));
				URI uri = new URI(urlString);
				fil = new File(uri);
			}
			catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		else if (urlString.startsWith("file:")) {
			try {
				urlString = urlString.substring(0, urlString.length() - ressursnavn.length());
				URI uri = new URI(urlString);
				fil = new File(uri);
			}
			catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		
		
		// Hvis fil ikke er null, så kan vi bla oss tilbake til directory hvor alle applikasjonene er installert.
		if (fil != null && fil.exists()) {
			String test = "";
			// 1. Finne StelvioSystemAvailabilityWebEAR.ear-mappen
			while (fil != null && !fil.getName().equals("installedApps")) {
				test = fil.getName();
				fil = fil.getParentFile();
			}
			// 2. Bla oss tilbake til directory
			if (fil != null) {
				File[] installedApps = fil.listFiles();
				File directory = null;
				for (File file : installedApps) {
					if (test.equals(file.getName())) {
						directory = file;
						break;
					}
				}
				if (directory != null) {
					int length = directory.listFiles().length;
					String app = "", s = "";
					
					for (int x = 0; x < length; x++) {
						fil = directory.listFiles()[x];
						app = fil.getName();
						if (app.indexOf("App.ear") != -1) {
							app = app.substring(0, app.indexOf("App.ear"));
							// Går gjennom alle mappene/filene som ligger i installedApps
							// Hvis det finnes en fil der som slutter på App.ear, så legges den til i lista.
							for (int y = 0; y < fil.list().length; y++) {
								s = fil.list()[y];
								if (s.startsWith(app)) {
									liste.add(fil);
									break;
								}
							}
						}
					}
				} else {
					System.err.println("\"directory\" var null!");
				}
			}
		}
		// Hvis fil er null
		else {
			System.err.println("\"fil\" var null!");
		}
		
		return liste;
	}
	
	/**
	 * Reads all SystemNames from a nav-module.
	 * Returns a String[] with all the SystemNames that have been added. 
	 * 
	 * @param navModule
	 * @return
	 */
	public String[] readSystemNamesFromNAVModule(File navModule) {
		StringBuffer buffer = new StringBuffer();
		
		// Lister opp filene som nav-modulen inneholder
		File[] filer = navModule.listFiles();
		for (int j = 0; j < filer.length; j++) {
			// Denne modulens JAR-fil åpnes for å finne komponenter med "Avail"
			if (filer[j].getName().equals(getNAVModuleName(navModule) + ".jar")) {
				java.util.List jarList = readJarForSystemNames(filer[j]);

				// For hver JarEntry (disse inneholder "Avail" og er .component)
				for (int x = 0; x < jarList.size(); x++) {
					JarEntry entry = (JarEntry)jarList.get(x);
					
					// Kutter stringen der "Avail" begynner, og det som er foran, er SystemName
					String name = entry.getName();
					name = name.substring(0, name.indexOf("Avail"));
					
					buffer.append(name + ":");
				}
				break;
			}
		}
		String kladd = buffer.toString();
		
		if (!"".equals(kladd)) {
			kladd = kladd.substring(0, kladd.length()-1);
			return kladd.split(":");
		}
		
		return null;
	}
	
	/**
	 * Reads a jar-file and returns a list of java.util.jar.JarEntry, so we can read SystemNames from it. 
	 * 
	 * @param jarFile
	 * @return
	 */
	private List<JarEntry> readJarForSystemNames(File jarFile) {
		// returliste
		List<JarEntry> liste = new ArrayList<JarEntry>();
		try {
			// Lese input-fila som en jar-fil.
			JarFile jar = new JarFile(jarFile);
			Enumeration<JarEntry> entries = jar.entries();
			// Gå gjennom alle entries som jarfila har (mapper og filer)
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				String name = entry.getName();
				// Alle filer som slutter på .component og inneholder "Avail", er SystemName-komponenter
				if (name.endsWith(".component") && name.contains("Avail")) {
					liste.add(entry);
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return liste;
	}
	
	/**
	 * The nav-module file is f.ex. named "nav-ent-pen-vedtakApp.ear".
	 * This method returns "nav-ent-pen-vedtak", or "" (blank String) if the input File is null.
	 * 
	 * @param module
	 * @return
	 */
	public String getNAVModuleName(File module) {
		if (module != null)
			return module.getName().substring(0, module.getName().indexOf("App.ear"));
		return "";
	}
	
	/**
	 * Adds all the entries from String[] into the list of SystemNames.
	 * 
	 * @param systemNamesList
	 * @return
	 */
	public String[] addSystemNames(String[] systemNamesList) {
		StringBuffer buffer = new StringBuffer();
		for (String systemName : systemNamesList) {
			// Legger til SystemName
			findOrCreateSystemRecord(systemName);
			buffer.append(systemName + ":");
		}
		
		String kladd = buffer.toString();
		if (!"".equals(kladd)) {
			kladd = kladd.substring(0, kladd.length()-1);
			return kladd.split(":");
		}
		
		return null;
	}
}
