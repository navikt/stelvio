package no.nav.common.framework.performance;

/**
 * AbstractMonitor should be extended by all monitor implementations
 * in order for them to be able to check the current configuration of
 * the monitoring level.
 * <p/>
 * The table below illustrates how performance is monitored based on 
 * how the level is configured. 
 * <p/>
 * <table width="90%" border="1" cellpadding="0" cellspacing="0">
 * 	<tr>
 * 		<th> Level </th>
 * 		<th> Web </th>
 * 		<th> Business </th>
 * 		<th> Integration </th>
 * 		<th> Resource </th>
 * 		<th> Additional </th>
 * 	</tr>
 * 	<tr>
 * 		<td> 0 </td>
 * 		<td colspan="5"><font color="red"> Off </font></td>
 * 	</tr>
 * 	<tr>
 * 		<td> 1 </td>
 * 		<td colspan="4"><font color="green"> On </font></td>
 * 		<td><font color="red"> Off </font></td>
 * 	</tr>
 * 	<tr>
 * 		<td> 2 </td>
 * 		<td><font color="green"> On </font></td>
 * 		<td colspan="3"><font color="green"> On </font></td>
 * 		<td><font color="red"> Off </font></td>
 * 	</tr>
 * 	<tr>
 * 		<td> 3 </td>
 * 		<td><font color="green"> On </font></td>
 * 		<td><font color="green"> On </font></td>
 * 		<td colspan="2"><font color="green"> On </font></td>
 * 		<td><font color="red"> Off </font></td>
 * 	</tr>
 * 	<tr>
 * 		<td> 4 </td>
 * 		<td><font color="green"> On </font></td>
 * 		<td><font color="green"> On </font></td>
 * 		<td><font color="green"> On </font></td>
 * 		<td><font color="green"> On </font></td>
 * 		<td><font color="red"> Off </font></td>
 * 	</tr>
 * 	<tr>
 * 		<td> 5 </td>
 * 		<td><font color="green"> On </font></td>
 * 		<td><font color="green"> On </font></td>
 * 		<td><font color="green"> On </font></td>
 * 		<td><font color="green"> On </font></td>
 * 		<td><font color="green"> On </font></td>
 * 	</tr>
 * </table>
 * 
 * <p/>
 * 
 * The table above shows that when monitoring level is 1,
 * the total execution time in all tiers are monitored. If
 * monitoring level is 2, the web tier execution time and total 
 * execution time in all of the lower tiers are monitored.
 * 
 * @author person7553f5959484
 * @version $Revision: 318 $ $Author: psa2920 $ $Date: 2004-05-23 17:23:21 +0200 (Sun, 23 May 2004) $
 */
public abstract class AbstractMonitor implements Monitor {

	/** Default level is no monitoring */
	protected int level = 0;

	/**
	 * Assign configured monitoring level.
	 * 
	 * @param level an integer in the range 0 - 10.
	 */
	public void setLevel(int level) {
		this.level = level;
	}

}
