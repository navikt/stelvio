package no.trygdeetaten.common.framework.jmx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 * Java Management Extentions utilities.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 913 $ $Author: psa2920 $ $Date: 2004-07-12 15:02:25 +0200 (Mon, 12 Jul 2004) $
 */
public final class JMXUtils {

	/**
	 * Private constructor to avoid instantiations outside this class.
	 */
	private JMXUtils() {
		super();
	}

	/**
	 * Returns the MBeanServer for specified agentId.
	 * 
	 * @param 	agentId	The agent identifier of the MBeanServer to retrieve. 
	 * 						The id of a MBeanServer is the MBeanServerId attribute 
	 * 						of it's delegate MBean.
	 * 
	 * @return the MBeanServer instance.
	 */
	public static MBeanServer getMBeanServer(String agentId) {
		ArrayList servers = MBeanServerFactory.findMBeanServer(agentId);
		if (servers == null || servers.size() == 0) {
			return MBeanServerFactory.createMBeanServer();
		} else {
			return (MBeanServer) servers.get(0);
		}
	}

	/**
	 * Gets the names of MBeans controlled by the MBeanServer matching the query string.
	 * 
	 * @param 	server the MBeanServer controlling the MBeans 
	 * @param 	queryString the JMX formatted query string, [DomainName]:[Property1=Value1],[Property2=Value2],*
	 * @return	an array of Strings, null if no matches was found
	 * @throws MalformedObjectNameException if query string is improper formatted.
	 */
	public static String[] getMBeanNames(MBeanServer server, String queryString) throws MalformedObjectNameException {
		ObjectName objName = new ObjectName(queryString);
		Set mbeanResult = server.queryNames(objName, null);
		if (null == mbeanResult) {
			return null;
		} else {
			int count = mbeanResult.size();
			Iterator iter = mbeanResult.iterator();
			List mbList = new ArrayList(count);
			while (iter.hasNext()) {
				mbList.add(((ObjectName) iter.next()).toString());
			}
			return (String[]) mbList.toArray(new String[count]);
		}
	}

	/**
	 * Gets the ObjectNames of MBeans controlled by the MBeanServer matching the query string.
	 * 
	 * @param 	server the MBeanServer controlling the MBeans 
	 * @param 	queryString the JMX formatted query string, [DomainName]:[Property1=Value1],[Property2=Value2],*
	 * @return	an array of Strings, null if no matches was found
	 * @throws MalformedObjectNameException if query string is improper formatted.
	 */
	public static ObjectName[] getMBeans(MBeanServer server, String queryString) throws MalformedObjectNameException {
		ObjectName objName = new ObjectName(queryString);
		Set mbeanResult = server.queryNames(objName, null);
		if (null == mbeanResult) {
			return null;
		} else {
			int count = mbeanResult.size();
			Iterator iter = mbeanResult.iterator();
			List mbList = new ArrayList(count);
			while (iter.hasNext()) {
				mbList.add((ObjectName) iter.next());
			}
			return (ObjectName[]) mbList.toArray(new ObjectName[count]);
		}
	}

}
