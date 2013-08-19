package no.stelvio.presentation.security.sso;

import java.util.Properties;

public class DebugHelper {

    public static boolean debug;

    public static void print(Class clazz, String message) {
        if (debug) {
            String front = clazz + " : ";
            System.out.println(front + message);
        }
    }

    public static void print(Class clazz, String message, Object... params) {
        if (debug) {
            String front = clazz + " : ";
            System.out.println(front + message + params.toString());
        }
    }

    public static void listProperties(Properties props) {
        if (debug) {
            props.list(System.out);
        }
    }

}
