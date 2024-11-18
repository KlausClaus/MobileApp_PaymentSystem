package com.example.tuitionpayment.alipay;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * Utility class for signing data using RSA or RSA2 algorithms.
 */
public class SignUtils {

	/**
	 * The RSA algorithm used for signing.
	 */
	private static final String ALGORITHM = "RSA";

	/**
	 * The SHA1 with RSA algorithm for signing.
	 */
	private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	/**
	 * The SHA256 with RSA algorithm for signing.
	 */
	private static final String SIGN_SHA256RSA_ALGORITHMS = "SHA256WithRSA";

	/**
	 * The default character set used for encoding data.
	 */
	private static final String DEFAULT_CHARSET = "UTF-8";

	/**
	 * Determines the appropriate algorithm based on whether RSA2 is used.
	 *
	 * @param rsa2 Whether RSA2 is used.
	 * @return The algorithm name.
	 */
	private static String getAlgorithms(boolean rsa2) {
		return rsa2 ? SIGN_SHA256RSA_ALGORITHMS : SIGN_ALGORITHMS;
	}

	/**
	 * Signs the given content using the specified private key and algorithm.
	 *
	 * @param content    The content to be signed.
	 * @param privateKey The private key used for signing.
	 * @param rsa2       Whether to use RSA2 for signing.
	 * @return The signed data as a Base64-encoded string, or null if an error occurs.
	 */
	public static String sign(String content, String privateKey, boolean rsa2) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
					Base64.decode(privateKey));
			KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature
					.getInstance(getAlgorithms(rsa2));

			signature.initSign(priKey);
			signature.update(content.getBytes(DEFAULT_CHARSET));

			byte[] signed = signature.sign();

			return Base64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
