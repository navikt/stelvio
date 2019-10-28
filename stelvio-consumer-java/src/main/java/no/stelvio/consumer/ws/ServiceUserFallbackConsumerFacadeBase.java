package no.stelvio.consumer.ws;

import com.ibm.websphere.security.auth.WSSubject;
import com.ibm.ws.webservices.multiprotocol.AgnosticService;

import no.stelvio.domain.person.Pid;

/**
 * An extension of the ConsumerFacadeBase that will check if the logged-on user (the WAS security subject) is an external user, i.e. identified by FNR instead of saksbehandler
 * ID/service user. LTPA tokens do not support users that are not in AD, so in this case it is required to use provided service user credentials.
 *
 * @deprecated see {@link ConsumerFacadeBase}
 */
@Deprecated
public class ServiceUserFallbackConsumerFacadeBase<T> extends ConsumerFacadeBase<T> {

    /**
     * @param serviceLocatorClass AgnosticService class
     */
    public ServiceUserFallbackConsumerFacadeBase(Class<? extends AgnosticService> serviceLocatorClass) {
        super(serviceLocatorClass);
    }

    @Override
    public boolean isUseUsernameToken() {
        if (super.isUseUsernameToken() || isEksternbruker()) {
            if (getServiceUsername() == null || getServicePassword() == null) {
                throw new IllegalArgumentException("Service user credentials must be set to use this configuration!");
            }
            return true;
        }

        return false;
    }

    protected boolean isEksternbruker() {
        return Pid.isValidPid(WSSubject.getCallerPrincipal());
    }
}
