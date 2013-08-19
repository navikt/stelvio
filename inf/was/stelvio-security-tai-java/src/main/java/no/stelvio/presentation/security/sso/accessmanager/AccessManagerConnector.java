package no.stelvio.presentation.security.sso.accessmanager;

/**
 * Represents a connector to an external access-manager which in order to convert a principal representation must have a
 * connection to decode it.
 * 
 * @author persondab2f89862d3
 */
public interface AccessManagerConnector {
    /**
     * Gets a StelvioPrincipal from an encoded principal representation.
     * 
     * @param representation
     *            the representation to decode.
     * @return the correponding StelvioPrincipal for the principal representation.
     * @throws PrincipalNotValidException
     *             if the representation is not valid
     */
    StelvioPrincipal getTransformedPrincipal(Object representation) throws PrincipalNotValidException;
}
