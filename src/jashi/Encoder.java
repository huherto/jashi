package jashi;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encoder {

	static final String HEXES = "0123456789ABCDEF";

	public static String toHex(byte[] raw) {
		if (raw == null) {
			return null;
		}
		final StringBuilder hex = new StringBuilder(2 * raw.length);
		for (final byte b : raw) {
			hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(
					HEXES.charAt((b & 0x0F)));
		}
		return hex.toString();
	}
	
	public static String toSha1(byte[] convertme) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return toHex(md.digest(convertme)).toLowerCase();
	}

}
