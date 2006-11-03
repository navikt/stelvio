package no.stelvio.common.error;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo 
 */
public class DefaultExceptionHandlerFacade implements ExceptionHandlerFacade {
    public Err getError(Class<? extends Throwable> clazz) {
        return null;  // TODO: implement body
    }

    public Throwable handleError(Throwable e) {
        // TODO: handle it...
        return e;
    }

//     The error handling configuration file
//    private static final String CONFIGURATION_FILE = "error-handling.xml";
//
//     The error handler configuration key
//    private static final String HANDLER_KEY = "ErrorHandler";
//
//    The PerformanceMonitor instance
//     initialize here: lazy instantiation is not thread-safe
//    private static ErrorHandler theInstance = new ErrorHandler();
//
//     The current configured handler
//    private Config errorHandlerConfig;
//
//    /**
//     * Enforcing non instabillity using a private constructor.
//     */
//    private ErrorHandler() {
//
//         Use config to configure error handler
//        try {
//            errorHandlerConfig = Config.getConfig(CONFIGURATION_FILE);
//        } catch (Throwable t) {
//            getHandler().handleError(
//                new ConfigurationException(FrameworkError.ERROR_HANDLING_CONFIGURATION_ERROR, t, CONFIGURATION_FILE));
//        }
//    }
//
//    /**
//     * Handle the specified error.
//     *
//     * @param t the Throwable to handle
//     * @return the handled Throwable
//     */
//    public static Throwable handleError(Throwable t) {
//        return theInstance.getHandler().handleError(t);
//    }
//
//    /**
//     * Get the message describing the specified error.
//     *
//     * @param t the error to describe.
//     * @return the error message
//     * @see Handler#getMessage(java.lang.Throwable)
//     */
//    public static String getMessage(Throwable t) {
//        return theInstance.getHandler().getMessage(t);
//    }
//
//    /**
//     * Initializes the ErrorHandler
//     */
//    public static void init() {
//        theInstance.getHandler();
//    }
//
//    /**
//     * Returns the throwable's stacktrace as a String.
//     *
//     * @param t the throwable containing the stacktrace
//     * @return the string representation of the stacktrace
//     */
//    public static String getStacktraceAsString(Throwable t) {
//        if (null == t) {
//            return null;
//        } else {
//            StringWriter sw = new StringWriter();
//            PrintWriter pw = new PrintWriter(sw);
//            t.printStackTrace(pw);
//            pw.flush();
//            pw.close();
//            return sw.toString();
//        }
//    }
//
//    /**
//     * Returns the internal error handler implementation.
//     *
//     * @return the handler instance.
//     */
//    private DefaultHandlerImpl getHandler() {
//        DefaultHandlerImpl handler = null;
//        if (null == errorHandlerConfig) {
//            handler = new DefaultHandlerImpl();
//            handler.init();
//        } else {
//            try {
//                handler = (DefaultHandlerImpl) errorHandlerConfig.getBean(HANDLER_KEY);
//            } catch (Throwable t) {
//                handler = new DefaultHandlerImpl();
//                handler.init();
//                handler.handleError(
//                    new ConfigurationException(FrameworkError.ERROR_HANDLING_CONFIGURATION_ERROR, t, CONFIGURATION_FILE));
//            }
//        }
//        return handler;
//    }
//
}
