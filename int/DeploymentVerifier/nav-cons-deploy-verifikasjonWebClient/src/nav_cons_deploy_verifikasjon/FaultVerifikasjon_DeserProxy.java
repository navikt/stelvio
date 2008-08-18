/**
 * FaultVerifikasjon_DeserProxy.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf170819.19 v52708210711
 */

package nav_cons_deploy_verifikasjon;

public class FaultVerifikasjon_DeserProxy  extends java.lang.Exception  {
    private java.lang.String errorMessage;
    private java.lang.String errorSource;
    private java.lang.String errorType;
    private java.lang.String rootCause;
    private java.lang.String dateTimeStamp;

    public FaultVerifikasjon_DeserProxy() {
    }

    public java.lang.String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(java.lang.String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public java.lang.String getErrorSource() {
        return errorSource;
    }

    public void setErrorSource(java.lang.String errorSource) {
        this.errorSource = errorSource;
    }

    public java.lang.String getErrorType() {
        return errorType;
    }

    public void setErrorType(java.lang.String errorType) {
        this.errorType = errorType;
    }

    public java.lang.String getRootCause() {
        return rootCause;
    }

    public void setRootCause(java.lang.String rootCause) {
        this.rootCause = rootCause;
    }

    public java.lang.String getDateTimeStamp() {
        return dateTimeStamp;
    }

    public void setDateTimeStamp(java.lang.String dateTimeStamp) {
        this.dateTimeStamp = dateTimeStamp;
    }

    public java.lang.Object convert() {
      nav_cons_deploy_verifikasjon.FaultVerifikasjon _e;
      _e = new nav_cons_deploy_verifikasjon.FaultVerifikasjon(
        getErrorMessage(),
        getErrorSource(),
        getErrorType(),
        getRootCause(),
        getDateTimeStamp());
      return _e;
    }
}
