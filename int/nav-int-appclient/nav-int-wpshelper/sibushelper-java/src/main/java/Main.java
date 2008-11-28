import no.nav.sibushelper.SIBUSHelper;

/**
 * @author persona2c5e3b49756 Schnell
 * just for launch the SIBUSHelper
 */
public class Main {


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SIBUSHelper sib = new SIBUSHelper(args);
		System.exit(sib.invokeHelper());
	}

}
