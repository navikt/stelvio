package no.nav.maven.plugin.deploymentcodeplugin;

import org.apache.maven.plugin.logging.Log;

import java.io.*;

/**
 * Class that reads the given stream in a separate thread. This is to reduce the chance that streams will deadlock.
 * The output is sent to the logger as an info-message. 
 *
 * @author Steinar person7b6db0c03aa3 JProfessionals
 *
 */
public class StreamGobbler extends Thread
{
    InputStream is;
    Log logger;

    StreamGobbler(InputStream is, Log logger)
    {
        this.is = is;
        this.logger = logger;
    }

    public void run()
    {
        try
        {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null)
                logger.info(line);
            } catch (IOException ioe)
              {
                ioe.printStackTrace();
              }
    }
}
