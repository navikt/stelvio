package no.nav.ant;

import java.io.File;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class ProjectSetTask extends  Task{

	String projectSetFile;
	String delimiter;
	String resultPropertyName;
		

	/**
	 * @return Returns the resPropertyName.
	 */
	public String getResultPropertyName() {
		return resultPropertyName;
	}
	/**
	 * @param resPropertyName The resPropertyName to set.
	 */
	public void setResultPropertyName(String resPropertyName) {
		this.resultPropertyName = resPropertyName;
	}
	/**
	 * @return Returns the delimiter.
	 */
	public String getDelimiter() {
		return delimiter;
	}
	/**
	 * @param delimiter The delimiter to set.
	 */
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	/**
	 * @return Returns the projectSetFile.
	 */
	public String getProjectSetFile() {
		return projectSetFile;
	}
	/**
	 * @param projectSetFile The projectSetFile to set.
	 */
	public void setProjectSetFile(String projectSetFile) {
		this.projectSetFile = projectSetFile;
	}
	public void execute() throws BuildException {
		//System.out.println("Finner refererte prosjektet fra workspacefil: "+projectSetFile);
		File psfFile = new File(projectSetFile);

		ProjectSetHandler handler = new ProjectSetHandler();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(psfFile, handler);
		} catch (Exception e) {
			throw new BuildException("Could not parse projectSetFile: "+e.getMessage());
		}
		List modules = handler.getResult();
		String ret = createReturnString(modules);
		
		getProject().setNewProperty(resultPropertyName,ret);
		
		
		
	}


	/**
	 * 
	 */
	private String createReturnString(List modules) {
		StringBuffer strBuf = new StringBuffer();
		for (int i=0;i<modules.size();i++) {
			ProjectSetData data = (ProjectSetData) modules.get(i);
			//System.out.println("svn= "+data.getSvnUrl()+". module= "+data.getModuleName());
			strBuf.append(data.getSvnUrl());
			strBuf.append(delimiter);
		}
		return strBuf.toString();
	}


}
