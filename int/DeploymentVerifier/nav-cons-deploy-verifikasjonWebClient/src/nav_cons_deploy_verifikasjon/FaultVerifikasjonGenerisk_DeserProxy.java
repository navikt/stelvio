/**
 * FaultVerifikasjonGenerisk_DeserProxy.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf170819.19 v52708210711
 */

package nav_cons_deploy_verifikasjon;

public class FaultVerifikasjonGenerisk_DeserProxy  extends nav_cons_deploy_verifikasjon.FaultVerifikasjon_DeserProxy  {

    public FaultVerifikasjonGenerisk_DeserProxy() {
    }

    public java.lang.Object convert() {
      nav_cons_deploy_verifikasjon.FaultVerifikasjonGenerisk _e;
      _e = new nav_cons_deploy_verifikasjon.FaultVerifikasjonGenerisk(
        getErrorMessage(),
        getErrorSource(),
        getErrorType(),
        getRootCause(),
        getDateTimeStamp());
      return _e;
    }
}
