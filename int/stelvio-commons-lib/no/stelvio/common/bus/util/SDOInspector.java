/*
 * SDOInspector.java Created on Sep 28, 2006 Author: persona2c5e3b49756 Schnell
 * (test@example.com)
 * 
 * This is a utility class that provides pretty-print capability for SDOs,
 * useful for debug and trace statements in SCA/SDO code.
 */
package no.stelvio.common.bus.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import commonj.sdo.DataObject;
import commonj.sdo.Property;
import commonj.sdo.Type;

/*
 * <p> This is a utility class that provides pretty-print capability for SDOs, useful for debug and trace statements in SCA/SDO
 * code. </p>
 * 
 * @usage <p> commonj.sdo.DataObject obj = ...; no.stelvio.common.bus.util.SDOInspector sdoppt = new SDOInspector(2,"-");
 * 
 * Alternative 1: String pptString = sdoppt.sdoPrettyPrint(obj); System.out.println(pptString);
 * 
 * Alternative 2: sdoppt.sdoPrettyPrint(obj,<Your logger/PrintWriter>);
 * 
 * Don't use System.out it is only an example.
 * 
 * </p>
 * 
 * @author persona2c5e3b49756 Schnell, test@example.com
 * 
 */
public class SDOInspector {

	/**
	 * The amount of spaces/characters to increase the indent of each child level of the tree
	 */
	private int baseIndent = 0;

	/**
	 * The String to use as the indent character for printing
	 */
	private String baseIndentStr = "";

	private static Logger m_logger = Logger.getLogger(SDOInspector.class.getName());

	/**
	 * <p>
	 * Initialize the pretty printer with the indent characters and number of times to print for each indent level.
	 * </p>
	 * 
	 * @param indent
	 *            number of times to repeat the indentString for each child level
	 * @param indentString
	 *            string to print representing a child level (eg. " " or "-")
	 */
	public SDOInspector(int indent, String indentString) {
		m_logger.entering("SDOInspector", "SDOInspector(int indent,String indentString)");
		baseIndent = indent;
		baseIndentStr = indentString;
		m_logger.exiting("SDOInspector", "SDOInspector(int indent,String indentString)");
	}

	/**
	 * <p>
	 * Pretty print the sdo and all children.
	 * </p>
	 * 
	 * @param sdo
	 *            The SDO object to pretty print
	 * @param out
	 *            The PrintWriter to print the output to
	 */
	public void sdoPrettyPrint(DataObject sdo, PrintWriter out) {
		m_logger.entering("SDOInspector", "sdoPrettyPrint(DataObject sdo, PrintWriter out)");
		printDataObject(sdo, null, out, 0, baseIndentStr);
		m_logger.exiting("SDOPrettyPrinter", "sdoPrettyPrint(DataObject sdo, PrintWriter out)");
	}

	/**
	 * <p>
	 * Pretty print the sdo and all children.
	 * </p>
	 * 
	 * @param sdo
	 *            The SDO object to pretty print
	 * @return String with the pretty print output of the SDO
	 */
	public String sdoPrettyPrint(DataObject sdo) {
		m_logger.entering("SDOInspector", "sdoPrettyPrint(DataObject sdo)");
		String result = null;
		try {
			StringWriter sW = new StringWriter();
			PrintWriter pW = new PrintWriter(sW);
			sdoPrettyPrint(sdo, pW);
			result = sW.toString();
			sW.close();
			pW.close();
		} catch (IOException e) {
			m_logger.severe("IOException in writer.close(): " + e.getMessage());
		}
		m_logger.exiting("SDOInspector", "sdoPrettyPrint(DataObject sdo)", result);
		return result;
	}

	/**
	 * Used internally by the pretty print methods to print SDO DataObjects
	 * 
	 * @param sdo
	 *            The data object to print
	 * @param out
	 *            The PrintWriter to use to print the data object
	 * @param indent
	 *            The size of the indent for the current level
	 * @param indentStr
	 *            The string to use for indentation
	 */
	protected void printDataObject(DataObject sdo, String instanceName, PrintWriter out, int indent, String indentStr) {
		m_logger.entering("SDOInspector", "printDataObject(DataObject sdo, PrintWriter out, int indent, String indentStr)");
		Type sdoType = sdo.getType();
		String sdoName = sdoType.getName();
		String sdoURI = sdoType.getURI();
		String sep = getIndentStr(indent, indentStr);
		out.println(sep + " " + (instanceName != null ? instanceName : sdoName) + " = " + "[ " + sdoType.getName() + " : "
				+ sdoURI + "]");
		List sdoPropertyList = sdoType.getProperties();
		Iterator propIterator = sdoPropertyList.iterator();
		while (propIterator.hasNext()) {
			Property property = (Property) propIterator.next();
			printProperty(sdo, property, out, indent + baseIndent, indentStr);
		}
		m_logger.exiting("SDOInspector", "printDataObject(DataObject sdo, PrintWriter out, int indent, String indentStr)");
	}

	/**
	 * Prints an SDO child object from the SDO and Property object
	 * 
	 * @param sdo
	 *            The SDO that this is a child of
	 * @param prop
	 *            The property that represents the SDO child
	 * @param out
	 *            The PrintWriter to output the SDO to
	 * @param indent
	 *            The size of the indent
	 * @param indentStr
	 *            The string to use for the indent
	 */
	protected void printProperty(DataObject sdo, Property prop, PrintWriter out, int indent, String indentStr) {
		m_logger.entering("SDOInspector",
				"printProperty(DataObject sdo, Property prop, PrintWriter out, int indent, String indentStr)");
		String name = prop.getName();
		Object child = sdo.get(name);
		printObject(child, name, prop, out, indent, indentStr);

		m_logger.exiting("SDOInspector",
				"printProperty(DataObject sdo, Property prop, PrintWriter out, int indent, String indentStr)");
	}

	/**
	 * Prints an object in the tree. Depending on if the child is a DataObject, List or primitive type, it calls the appropriate
	 * methods.
	 * 
	 * @param child
	 *            The object to print
	 * @param name
	 *            The name of the object
	 * @param prop
	 *            The properties obect for the given type
	 * @param out
	 *            The PrintWriter to write to
	 * @param indent
	 *            The amount of indent to use
	 * @param indentStr
	 *            The string to use for indentation
	 */
	protected void printObject(Object child, String name, Property prop, PrintWriter out, int indent, String indentStr) {
		String sep = getIndentStr(indent, indentStr);
		if (child instanceof DataObject) {
			printDataObject((DataObject) child, name, out, indent, indentStr);
		} else if (child instanceof List) {
			List list = (List) child;
			out.println(sep + " " + name + ": list[" + list.size() + "]");
			Iterator it = list.iterator();
			for (int i = 0; it.hasNext(); i++) {
				Object listItem = it.next();
				printObject(listItem, name + "[" + i + "]", prop, out, indent + baseIndent, indentStr);
			}
		} else {
			out.println(sep + " " + name + " = " + child + " [" + prop.getType().getName() + " : " + prop.getType().getURI()
					+ "]");
		}
	}

	/**
	 * Returns a string with the right size and characters
	 * 
	 * @param indent
	 *            the number of indent
	 * @param indentStr
	 *            the string to use for indentation
	 * @return a string with the right size and type of characters
	 */
	protected String getIndentStr(int indent, String indentStr) {
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < indent; i++) {
			sb.append(indentStr);
		}
		return sb.toString();
	}
}
