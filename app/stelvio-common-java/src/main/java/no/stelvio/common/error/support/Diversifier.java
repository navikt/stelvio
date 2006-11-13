package no.stelvio.common.error.support;

/**
 * Just used for the copy constructor so it wont be confused with the constructor taking a cause.
 *
 * @todo Better javadoc
 */
public class Diversifier {
    /**
     * Creates an instance of the class. Is private so no one can instantiate it.
     */
    private Diversifier() {

    }

    public static final Diversifier INSTANCE = new Diversifier();
}
