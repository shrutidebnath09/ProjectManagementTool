package com.taskmanager.authentification;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.taskmanager.models.UserProfile;

/**
 * Helper class for authentication
 * 
 */
public class AuthUtil {

	private static final String HEADER_AUTH_DELIMITTER = ":";

	private static int derivedKeyLength = 160;
	private static int iterations = 20000;
	private static String ENCRYPTION_ALGORITHM = "PBKDF2WithHmacSHA1";
	private static String SALT_ALGORITHM = "SHA1PRNG";
	private static int SALT_LENGTH = 8;

	public static String[] parseClientAuthHeader(final String headerAuth) {
		if (headerAuth == null) {
			return null;
		}

		final String cleanedHeaderAuth = headerAuth.replaceFirst("[B|b]asic ",
				"");

		final String decodedAuth = CodecUtil.base64ToString(cleanedHeaderAuth);

		return decodedAuth.split(HEADER_AUTH_DELIMITTER, 2);
	}

	public static String createClientAuthHash(final UserProfile profile) {

		if (profile == null) {
			return null;
		}

		final String serverHash = createAuthHash(profile);

		if (serverHash == null) {
			return null;
		}

		final StringBuilder hashBuilder = new StringBuilder();
		hashBuilder.append(profile.getId());
		hashBuilder.append(HEADER_AUTH_DELIMITTER);
		hashBuilder.append(serverHash);

		return CodecUtil.stringToBase64(hashBuilder.toString());
	}

	public static byte[] encryptPasswordWithSalt(String password, byte[] salt) {

		try {
			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt,
					iterations, derivedKeyLength);

			SecretKeyFactory f = SecretKeyFactory
					.getInstance(ENCRYPTION_ALGORITHM);

			return f.generateSecret(spec).getEncoded();
		} catch (final NoSuchAlgorithmException exc) {
			exc.printStackTrace();
		} catch (final InvalidKeySpecException exc) {
			exc.printStackTrace();
		}

		return null;
	}


	public static byte[] generateSalt() {
		try {
			SecureRandom random = SecureRandom.getInstance(SALT_ALGORITHM);

			byte[] salt = new byte[SALT_LENGTH];
			random.nextBytes(salt);

			return salt;
		} catch (final NoSuchAlgorithmException exc) {
			exc.printStackTrace();
		}

		return null;
	}


	public static String generateNewClientAuthHashSalt() {
		final byte[] key = AuthUtil.generateSalt();
		return CodecUtil.byteToHex(key);
	}


	public static String createAuthHash(final UserProfile profile) {
		if (profile == null) {
			return null;
		}

		return HashUtil.hmacSha256(profile.getAuthHashSalt(),
				profile.getPassword());
	}
}
