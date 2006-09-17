package no.stelvio.common.framework.report.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * DOCUMENT ME!
 *
 * @author Ole-Kristian Hagen
 * @version $Revision: 1595 $, $Date: 2004-11-25 15:14:24 +0100 (Thu, 25 Nov 2004) $
 */
public class FileUtils {
    //  Returns the contents of the file in a byte array.
    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("File is too large. A array can't be longer than Integer.MAX_VALUE (" + Integer.MAX_VALUE + ")");
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;

        while ((offset < bytes.length) && ((numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        // Close the input stream and return bytes
        is.close();

        return bytes;
    }

    /**
     * DOCUMENT ME!
     *
     * @param strFile DOCUMENT ME!
     * @param ext DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String changeFileExtention(String strFile, String ext) {
        int indexOfDot = strFile.lastIndexOf('.');

        return strFile.substring(0, indexOfDot + 1) + ext;
    }
}
