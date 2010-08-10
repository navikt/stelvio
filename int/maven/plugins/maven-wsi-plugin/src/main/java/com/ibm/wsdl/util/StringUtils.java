package com.ibm.wsdl.util;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class StringUtils
{

    public StringUtils()
    {
    }

    public static String cleanString(String s)
    {
        if(s == null)
            return null;
        char ac[] = s.toCharArray();
        StringBuffer stringbuffer = new StringBuffer();
        for(int i = 0; i < ac.length; i++)
            switch(ac[i])
            {
            case 34: // '"'
                stringbuffer.append("\\\"");
                break;

            case 92: // '\\'
                stringbuffer.append("\\\\");
                break;

            case 10: // '\n'
                stringbuffer.append("\\n");
                break;

            case 13: // '\r'
                stringbuffer.append("\\r");
                break;

            default:
                stringbuffer.append(ac[i]);
                break;
            }

        return stringbuffer.toString();
    }

    public static String getClassName(Class class1)
    {
        String s = class1.getName();
        return class1.isArray() ? parseDescriptor(s) : s;
    }

    public static InputStream getContentAsInputStream(URL url)
        throws SecurityException, IllegalArgumentException, IOException
    {
        if(url == null)
            throw new IllegalArgumentException("URL cannot be null.");
        try
        {
            Object obj = url.getContent();
            if(obj == null)
                throw new IllegalArgumentException("No content.");
            if(obj instanceof InputStream)
                return (InputStream)obj;
            else
                throw new IllegalArgumentException((obj instanceof String) ? (String)obj : "This URL points to a: " + getClassName(obj.getClass()));
        }
        catch(SecurityException _ex)
        {
            throw new SecurityException("Your JVM's SecurityManager has disallowed this.");
        }
        catch(FileNotFoundException _ex)
        {
            throw new FileNotFoundException("This file was not found: " + url);
        }
    }

    public static String getNMTokens(List list)
    {
        if(list != null)
        {
            StringBuffer stringbuffer = new StringBuffer();
            int i = list.size();
            for(int j = 0; j < i; j++)
            {
                String s = (String)list.get(j);
                stringbuffer.append((j <= 0 ? "" : " ") + s);
            }

            return stringbuffer.toString();
        } else
        {
            return null;
        }
    }

    public static URL getURL(URL url, String s)
        throws MalformedURLException
    {
        if(url != null)
        {
            File file = new File(s);
            if(file.isAbsolute())
                return file.toURI().toURL();
        }
        try
        {
            return new URL(url, s);
        }
        catch(MalformedURLException malformedurlexception)
        {
            if(url == null)
                return (new File(s)).toURI().toURL();
            else
                throw malformedurlexception;
        }
    }

    private static String parseDescriptor(String s)
    {
        char ac[] = s.toCharArray();
        int i = 0;
        int j;
        for(j = 0; ac[j] == '['; j++)
            i++;

        StringBuffer stringbuffer = new StringBuffer();
        switch(ac[j++])
        {
        case 66: // 'B'
            stringbuffer.append("byte");
            break;

        case 67: // 'C'
            stringbuffer.append("char");
            break;

        case 68: // 'D'
            stringbuffer.append("double");
            break;

        case 70: // 'F'
            stringbuffer.append("float");
            break;

        case 73: // 'I'
            stringbuffer.append("int");
            break;

        case 74: // 'J'
            stringbuffer.append("long");
            break;

        case 83: // 'S'
            stringbuffer.append("short");
            break;

        case 90: // 'Z'
            stringbuffer.append("boolean");
            break;

        case 76: // 'L'
            stringbuffer.append(ac, j, ac.length - j - 1);
            break;
        }
        for(int k = 0; k < i; k++)
            stringbuffer.append("[]");

        return stringbuffer.toString();
    }

    public static List parseNMTokens(String s)
    {
        StringTokenizer stringtokenizer = new StringTokenizer(s, " ");
        Vector vector = new Vector();
        for(; stringtokenizer.hasMoreTokens(); vector.add(stringtokenizer.nextToken()));
        return vector;
    }

    public static final String lineSeparator = System.getProperty("line.separator", "\n");
    public static final String lineSeparatorStr = cleanString(lineSeparator);

}