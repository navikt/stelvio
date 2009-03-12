/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.maven.plugin.eclipse.writers.wid;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.eclipse.EclipseSourceDir;
import org.apache.maven.plugin.eclipse.Messages;
import org.apache.maven.plugin.eclipse.writers.AbstractEclipseWriter;
import org.apache.maven.plugin.eclipse.writers.wtp.AbstractWtpResourceWriter;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.xml.PrettyPrintXMLWriter;
import org.codehaus.plexus.util.xml.XMLWriter;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.codehaus.plexus.util.xml.Xpp3DomWriter;

/**
 * Adapts the .classpath file for WID 6.1.
 * 
 * @author <a href="mailto:test@example.com">Bjorn Hilstad</a>
 */
public class WidClasspathWriter
    extends AbstractEclipseWriter
{

    private static final String CLASSPATH = "classpath";

    private static final String CLASSPATH_FILE = ".classpath";

    private static final String CLASSPATHENTRY = "classpathentry";
    
    private static final String ATTRIBUTES = "attributes";
    private static final String ATTRIBUTE = "attribute";
    private static final String OPTIONAL = "optional";

    private static final String KIND = "kind";

    private static final String LIB = "lib";

    private static final String OUTPUT = "output";

    private static final String PATH = "path";

    private static final String SRC = "src";

    private static final String VAR = "var";
    
    private static final String EXCLUDING = "excluding";


    /**
     * write the .classpath file to the project root directory.
     * 
     * @see AbstractWtpResourceWriter#write(EclipseSourceDir[], ArtifactRepository, File)
     * @param sourceDirs all eclipse source directorys
     * @param localRepository the local reposetory
     * @param buildOutputDirectory build output directory (target)
     * @throws MojoExecutionException when writing the config files was not possible
     */
    public void write()
        throws MojoExecutionException
    {
 	
    	log.debug("WidWriter start");
    	//TODO Package checking omitted. All modules will be processed 
    	//String packaging = config.getPackaging();
    	
        File classpathFile = new File( config.getEclipseProjectDirectory(), CLASSPATH_FILE );

        if ( !classpathFile.exists() )
        {
        	log.debug("Could not find file:" + config.getEclipseProjectDirectory() + "/" + CLASSPATH_FILE);
            return;
        }
      
        Xpp3Dom classpath = readXMLFile( classpathFile );
        // Modify existing entries in file
        Xpp3Dom[] children;
        children = classpath.getChildren();
        int index=0;
        for ( index = children.length - 1; index >= 0; index-- )
        {
        	// Remove default src folder added by eclipse:eclipse
        	if (children[index].getAttribute("including") != null) {
	        	if(children[index].getAttribute("including").contains("java")) {
	        		classpath.removeChild(index);
	        		log.info("Removing index " + index);
	        	}
        	}
        	// Set output to empty string
        	if (children[index].getAttribute("kind") != null) {
	        	if(children[index].getAttribute("kind").contains("output")) {
	        		classpath.removeChild(index);
	        		Xpp3Dom output = new Xpp3Dom( CLASSPATHENTRY );
	        		output.setAttribute( KIND, OUTPUT );        
	        		output.setAttribute( PATH, "" );
	                classpath.addChild( output );
	        		log.info("Setting output path to empty string.");
	        	}
        	}
        }
        

        // Add src folder root excluding gen/ and gen/src/
        Xpp3Dom newEntry = new Xpp3Dom( CLASSPATHENTRY );
        newEntry.setAttribute( EXCLUDING, "gen/|gen/src/" );
        newEntry.setAttribute( KIND, SRC );        
        newEntry.setAttribute( PATH, "" );
        classpath.addChild( newEntry );

        // Add src folder 
        newEntry = new Xpp3Dom( CLASSPATHENTRY );
        newEntry.setAttribute( KIND, SRC );        
        newEntry.setAttribute( PATH, "gen/src" );
        Xpp3Dom attributes = new Xpp3Dom( ATTRIBUTES);        
        Xpp3Dom attribute = new Xpp3Dom( ATTRIBUTE);
        attribute.setAttribute( "value", "true" );
        attribute.setAttribute( "name", OPTIONAL );        
        attributes.addChild( attribute );
        attributes.setValue("");
        newEntry.addChild( attributes );
        classpath.addChild( newEntry );

        classpath = orderClasspath( classpath );

        Writer w;
        try
        {
            w = new OutputStreamWriter( new FileOutputStream( classpathFile ), "UTF-8" );
        }
        catch ( IOException ex )
        {
            throw new MojoExecutionException( Messages.getString( "EclipsePlugin.erroropeningfile" ), ex ); //$NON-NLS-1$
        }
        XMLWriter writer = new PrettyPrintXMLWriter( w, "UTF-8", null );
        Xpp3DomWriter.write( writer, classpath );
        log.debug("WidWriter end");
        IOUtil.close( w );
    }

    /**
     * Determine which type this classpath entry is. This is used for sorting them.
     * 
     * @param classpathentry the classpath entry to sort
     * @return an integer identifieing the type
     * @see WidClasspathWriter#orderClasspath(Xpp3Dom)
     */
    private int detectClasspathEntryType( Xpp3Dom classpathentry )
    {
        String kind = classpathentry.getAttribute( KIND );
        String path = classpathentry.getAttribute( PATH );

        if ( kind == null || path == null )
        {
            return 6;
        }

        boolean absolutePath = path.startsWith( "\\" ) || path.startsWith( "/" );
        boolean windowsAbsolutePath = path.indexOf( ':' ) >= 0;
        boolean anyAbsolutePath = absolutePath || windowsAbsolutePath;

        if ( kind.equals( SRC ) && !absolutePath )
        {
            return 1;
        }
        else if ( kind.equals( LIB ) && !anyAbsolutePath )
        {
            return 2;
        }
        else if ( kind.equals( SRC ) )
        {
            return 3;
        }
        else if ( kind.equals( VAR ) )
        {
            return 4;
        }
        else if ( kind.equals( LIB ) )
        {
            return 5;
        }
        else if ( kind.equals( OUTPUT ) )
        {
            return 7;
        }
        else
        {
            return 6;
        }
    }

    /**
     * Order of classpath this is nessesary for the ejb's the generated classes are elsewise not found. 1 - kind=src
     * ohne starting '/' oder '\' 2 - kind=lib kein ':' und kein start mit '/' oder '\' 3 - kind=src mit ohne starting
     * '/' oder '\' 4 - kind=var 5 - kind=lib ein ':' oder start mit '/' oder '\' 6 - rest 7 - kind=output
     * 
     * @param classpath the classpath to sort
     * @return dom-tree representing ordered classpath
     */
    private Xpp3Dom orderClasspath( Xpp3Dom classpath )
    {
        Xpp3Dom[] children = classpath.getChildren();
        Arrays.sort( children, new Comparator()
        {
            public int compare( Object o1, Object o2 )
            {
                return detectClasspathEntryType( (Xpp3Dom) o1 ) - detectClasspathEntryType( (Xpp3Dom) o2 );
            }
        } );
        Xpp3Dom resultClasspath = new Xpp3Dom( CLASSPATH );
        for ( int index = 0; index < children.length; index++ )
        {
            resultClasspath.addChild( children[index] );
        }
        return resultClasspath;
    }

    /**
     * Read an xml file
     * 
     * @param xmlFile an xmlfile
     * @return dom-tree representing the file contents
     */
    private Xpp3Dom readXMLFile( File xmlFile )
    {
        try
        {
            Reader reader = new InputStreamReader( new FileInputStream( xmlFile ), "UTF-8" );
            Xpp3Dom applicationXmlDom = Xpp3DomBuilder.build( reader );
            return applicationXmlDom;
        }
        catch ( FileNotFoundException e )
        {
            return null;
        }
        catch ( Exception e )
        {
            log.error( Messages.getString( "EclipsePlugin.cantreadfile", xmlFile.getAbsolutePath() ) );
            // this will trigger creating a new file
            return null;
        }
    }


}
