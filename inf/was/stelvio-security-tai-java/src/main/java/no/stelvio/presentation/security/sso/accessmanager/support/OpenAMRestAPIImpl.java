package no.stelvio.presentation.security.sso.accessmanager.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OpenAMRestAPIImpl {

    private Logger log = Logger.getLogger("no.stelvio.presentation.security.sso.accessmanager.support.OpenAMRestAPIImpl");

    /**
     * Invoke OpenAM REST API to get information for given session
     *
     * @param sessionId
     *            OpenAM sessionid from cookie
     * @return response from OpenAM
     */
    public String invokeOpenAmRestApi(String sessionId, String openAMQueryTemplate) {

        String openAMQuery =  openAMQueryTemplate.replace("[TOKENID]", sessionId);
        StringBuffer result = new StringBuffer();
        URL url = null;
        log.fine("Invoking OpenAM REST interface: " + openAMQuery);

        try {
            url = new URL(openAMQuery);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                log.logp(Level.SEVERE, getClass().getName(), "invokeOpenAmRestApi",
                        "Error from openAM: " + conn.getResponseCode() + " " + conn.getResponseMessage());
                throw new RuntimeException("Failed: HTTP error code : " + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String output;
            while ((output = br.readLine()) != null) {
                result.append(output);
            }
            conn.disconnect();
        } catch (MalformedURLException e) {
            log.logp(Level.SEVERE, getClass().getName(), "invokeOpenAmRestApi", e.getMessage(), e);
            throw new RuntimeException("Malformed URL: " + url, e);
        } catch (IOException e) {
            log.logp(Level.SEVERE, getClass().getName(), "invokeOpenAmRestApi", e.getMessage(), e);
            throw new RuntimeException("Error when invoking openAM", e);
        }
        return result.toString();
    }
}
