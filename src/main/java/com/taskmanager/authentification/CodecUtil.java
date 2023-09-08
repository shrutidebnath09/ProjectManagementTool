package com.taskmanager.authentification;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.glassfish.jersey.internal.util.Base64;

/**
 * Helper class to encode and decode
 * 
 */
public class CodecUtil {

	public static String byteToString(byte[] input) {
		return new String(input);
	}

	public static String byteToHex(byte[] input) {
		return Hex.encodeHexString(input);
	}

	public static byte[] hexToByte(final String input) {
		try {
			return Hex.decodeHex(input.toCharArray());
		} catch (DecoderException e) {
			return new byte[0];
		}
	}
	
	public static String base64ToString(final String input) {
		return Base64.decodeAsString(input);
	}

	public static String stringToBase64(final String input) {
		return Base64.encodeAsString(input);
	}
}
