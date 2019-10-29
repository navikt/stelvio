package no.stelvio.common.security.ws;

import static org.junit.Assert.assertEquals;

import com.ibm.websphere.crypto.InvalidPasswordDecodingException;
import com.ibm.websphere.crypto.PasswordUtil;
import com.ibm.websphere.crypto.UnsupportedCryptoAlgorithmException;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Test class for SecurityHeader.
 * 
 * @author person08f1a7c6db2c, Accenture
 * 
 */
@Ignore("Liberty: can not find dependencies necessary to run PasswordUtil")  // FIXME PL-712
public class SecurityHeaderTest {

	/**
	 * Verifies that the xor-decryption works as expected. The password util can't be used due to classpath issues.
	 * 
	 * @throws InvalidPasswordDecodingException invalid password
	 * @throws UnsupportedCryptoAlgorithmException unsupported crypto algorithm
	 */
	@Test
	public void decodeShouldBeTheSameAsPasswordUtil() throws InvalidPasswordDecodingException,
			UnsupportedCryptoAlgorithmException {
		String pwd = "{xor}LDo8LTor";
		String stelvio = SecurityHeader.decode(pwd);
		String ibm = PasswordUtil.decode(pwd);
		assertEquals(stelvio, ibm);
	}
}
