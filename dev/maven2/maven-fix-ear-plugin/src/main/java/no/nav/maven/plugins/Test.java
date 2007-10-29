/*
 * Created on Oct 10, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package no.nav.maven.plugins;

import java.io.File;

/**
 * @author utvikler
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Test {

	public static void main(String[] args) throws Exception{
		EarFixer fixer = new EarFixer();

		fixer.parseEnvironFile();
		
		fixer.execute();
		
		//fixer.extractEarFile(new File("F:/mojo/nav-pensjon-pselv-jee-1.0.29.D3.ear"));
		
		fixer.fixBndFile(new File("F:\\Temp\\nav-pensjon-pselv-jee-1.0.29.D3.ear\\META-INF\\ibm-application-bnd.xmi"));
		
		/*fixer.fixWebXml(new File("E:/Temp/war/WEB-INF/web.xml"));
		
		fixer.fixPrsCommon(new File("E:/Temp/war/WEB-INF/spring/prs-common-context.xml"));
		
		fixer.fixPrsSecurity(new File("E:/Temp/war/WEB-INF/security/prs-security-context.xml"));
		
		fixer.fixFacesSecurity(new File("E:/Temp/war/WEB-INF/security/faces-security-config.xml"));
		
		fixer.compressEarFile();
		
		fixer.parseEnvironFile();*/
	}
}
