package no.stelvio.common.security.ws;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ibm.ws.security.util.InvalidPasswordDecodingException;
import com.ibm.ws.security.util.PasswordUtil;
import com.ibm.ws.security.util.UnsupportedCryptoAlgorithmException;

/**
 * Test class for SecurityHeader.
 * 
 * @author person08f1a7c6db2c, Accenture
 * 
 */
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
