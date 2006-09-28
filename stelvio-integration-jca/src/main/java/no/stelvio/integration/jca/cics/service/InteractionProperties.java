package no.stelvio.integration.jca.cics.service;

import no.stelvio.common.FrameworkError;
import no.stelvio.common.error.SystemException;

import java.util.List;


/**
 * A class for configuring the InteractionSpec for new CICS records. Will be configured in the Spring configuration file for
 * each copy book.
 * 
 * @author Thomas Kvalvag, Accenture
 * @version $Id: InteractionProperties.java 2748 2006-02-01 08:27:32Z skb2930 $ 
 */
public class InteractionProperties {

	/** Holds the function name to call
	 */
	private String functionName = null;

	/** Holds the length of the CICS commarea 
	 */
	private int commareaLength = 0;

	/** Holds the size of the reply message
	 */
	private int replyLength = 0;

	/** Holds the execution timout of the interaction 
	 */
	private int executeTimeout = 0;

	/** Holds the mapping file name 
	 */
	private List mappingFiles = null;

	/** Hold fixed record mapping file 
	 */
	private String frmappingfileIn = null;

	/** Hold fixed record mapping file 
	 */
	private String frmappingfileOut = null;

	/** Hold string-formatter name 
	 */
// TODO: Formater is inside the hibernate module which is removed for now
//	private Formater stringFormater = null;

	/**
	 * Validates the configuration of this service and performs further initialization. This method
	 * should be called after all the properties have been set.
	 */
	public void init() {
		if (null == functionName || "".equals(functionName)) {
			throw new SystemException(FrameworkError.JCA_SERVICE_PROPERTY_MISSING, "functionName");
		}

		if (0 >= commareaLength) {
			throw new SystemException(FrameworkError.JCA_SERVICE_PROPERTY_MISSING, "commareaLength");
		}

		if (0 >= replyLength) {
			throw new SystemException(FrameworkError.JCA_SERVICE_PROPERTY_MISSING, "replyLength");
		}

		if (0 >= executeTimeout) {
			throw new SystemException(FrameworkError.JCA_SERVICE_PROPERTY_MISSING, "executeTimeout");
		}

		if (null == mappingFiles && (null == frmappingfileIn || null == frmappingfileOut)) {
			throw new SystemException(FrameworkError.JCA_SERVICE_PROPERTY_MISSING, "mappingFiles");
		}

// TODO: Uses the hibernate module which is removed for now
/*
		if (null == stringFormater) {
			throw new SystemException(FrameworkError.JCA_SERVICE_PROPERTY_MISSING, "stringFormater");
		}
*/
	}

	/**
	 * Getter for CommareaLength
	 * @return int - The length of the commarea
	 */
	public int getCommareaLength() {
		return commareaLength;
	}

	/**
	 * Getter for Execution Timeout
	 * @return int - The execution Timeout
	 */
	public int getExecuteTimeout() {
		return executeTimeout;
	}

	/**
	 * Getter for FunctionName
	 * @return String - Name of function to call
	 */
	public String getFunctionName() {
		return functionName;
	}

	/**
	 * Getter for Mapping file
	 * @return String - Name for mapping file
	 */
	public List getMappingFiles() {
		return mappingFiles;
	}

	/**
	 * Getter for replylength
	 * @return int - The reply length
	 */
	public int getReplyLength() {
		return replyLength;
	}

	/**
	 * Setter for commareaLength
	 * @param commareaLength - The commarea length
	 */
	public void setCommareaLength(int commareaLength) {
		this.commareaLength = commareaLength;
	}

	/**
	 * Setter for executeTimeout
	 * @param executeTimeout - The execution timeout
	 */
	public void setExecuteTimeout(int executeTimeout) {
		this.executeTimeout = executeTimeout;
	}

	/**
	 * Setter for functionName
	 * @param functionName - The function name
	 */
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	/**
	 * Setter for mappingFile
	 * @param mappingFiles - Name of the mapping file
	 */
	public void setMappingFiles(List mappingFiles) {
		this.mappingFiles = mappingFiles;
	}

	/**
	 * Setter for replyLength
	 * @param replyLength - The reply length
	 */
	public void setReplyLength(int replyLength) {
		this.replyLength = replyLength;
	}

	/**
	 * Getter for Fixed Record mapper file 
	 * @return string - mapping file
	 */
	public String getFrmappingfileIn() {
		return frmappingfileIn;
	}

	/**
	 * Setter for Fixed Record mapper file
	 * 
	 * @param string navn
	 */
	public void setFrmappingfileIn(String string) {
		frmappingfileIn = string;
	}

	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}

		if (null == obj || getClass() != obj.getClass()) {
			return false;
		}

		final InteractionProperties interactionProperties = (InteractionProperties) obj;

		return !(null != mappingFiles
		         ? !mappingFiles.equals(interactionProperties.mappingFiles)
		         : null != interactionProperties.mappingFiles);

	}

	public int hashCode() {
		return null == mappingFiles ? 0 : mappingFiles.hashCode();
	}
	/**
	 * Getter for frmapping file out
	 * 
	 * @return frmapping file out
	 */
	public String getFrmappingfileOut() {
		return frmappingfileOut;
	}

	/**
	 * Setter for frmapping file out
	 * 
	 * @param string frmapping file out
	 */
	public void setFrmappingfileOut(String string) {
		frmappingfileOut = string;
	}

// TODO: Formater is inside the hibernate module which is removed for now
//	/**
//	 * Getter stringFormater
//	 *
//	 * @return stringFormater
//	 *
//	 */
//	public Formater getStringFormater() {
//		return stringFormater;
//	}
//
//	/**
//	 * Setter stringFormater
//	 *
//	 * @param formater stringFormater
//	 *
//	 */
//	public void setStringFormater(Formater formater) {
//		stringFormater = formater;
//	}
}
