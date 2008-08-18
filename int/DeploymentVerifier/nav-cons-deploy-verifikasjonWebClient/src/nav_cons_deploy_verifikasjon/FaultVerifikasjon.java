/**
 * FaultVerifikasjon.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf170819.19 v52708210711
 */

package nav_cons_deploy_verifikasjon;

public class FaultVerifikasjon  extends java.lang.Exception  {
    private java.lang.String errorMessage;
    private java.lang.String errorSource;
    private java.lang.String errorType;
    private java.lang.String rootCause;
    private java.lang.String dateTimeStamp;

    public FaultVerifikasjon(
           java.lang.String errorMessage,
           java.lang.String errorSource,
           java.lang.String errorType,
           java.lang.String rootCause,
           java.lang.String dateTimeStamp) {
        this.errorMessage = errorMessage;
        this.errorSource = errorSource;
        this.errorType = errorType;
        this.rootCause = rootCause;
        this.dateTimeStamp = dateTimeStamp;
    }

    public java.lang.String getErrorMessage() {
        return errorMessage;
    }

    public java.lang.String getErrorSource() {
        return errorSource;
    }

    public java.lang.String getErrorType() {
        return errorType;
    }

    public java.lang.String getRootCause() {
        return rootCause;
    }

    public java.lang.String getDateTimeStamp() {
        return dateTimeStamp;
    }

}
