package common.aes128;

import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Aes128Coder implements IEDCoder {

	public final static byte[] encode(byte[] content, byte[] key) {
		return cipher(content, key, Cipher.ENCRYPT_MODE);
	}

	public final static byte[] decode(byte[] content, byte[] key) {
		return cipher(content, key, Cipher.DECRYPT_MODE);
	}

	public final static byte[] encode(String content, byte[] key) {
		return cipher(content.getBytes(), key, Cipher.ENCRYPT_MODE);
	}

	public final static String encodeBase64(String content, byte[] key) {
		return Base64Coder.encode(cipher(content.getBytes(), key,
				Cipher.ENCRYPT_MODE));
	}

	public final static String decodeBase64(String base64Str, byte[] key) {
		return new String(cipher(Base64Coder.decode(base64Str), key,
				Cipher.DECRYPT_MODE));
	}

	private final static byte[] cipher(byte[] content, byte[] key, int mode) {
		try {
			SecretKeySpec secKey = new SecretKeySpec(key, "AES");
			Cipher aes = Cipher.getInstance("AES");
			aes.init(mode, secKey);
			return aes.doFinal(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Cipher aes;
	private byte[] key;

	private byte[] bf;
	private OutputStream out;
	private int i;
	private boolean hasResult;

	public Aes128Coder(byte[] key, int mode, OutputStream out) {
		this.key = key;
		SecretKeySpec secKey = new SecretKeySpec(key, "AES");
		try {
			aes = Cipher.getInstance("AES");
			aes.init(mode, secKey);
			bf = new byte[16];
			this.out = out;
			i = 0;
			hasResult = false;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public byte[] getKey() {
		return key;
	}

	@Override
	public void put(byte b) throws IOException {
		// TODO Auto-generated method stub
		bf[i] = b;
		++i;
		if (i == 16) {
			out.write(aes.update(bf));
			i = 0;
		}
	}

	@Override
	public void put(byte[] b) throws IOException {
		// TODO Auto-generated method stub
		this.put(b, 0, b.length);
	}

	@Override
	public void put(byte[] b, int offset, int length) throws IOException {
		// TODO Auto-generated method stub
		int end = offset + length;
		while (offset < end) {
			int need = 16 - i;
			int writeSize = need < length ? need : length;
			System.arraycopy(b, offset, bf, i, writeSize);
			offset += writeSize;
			i += writeSize;
			length -= writeSize;
			if (i == 16) {
				out.write(aes.update(bf));
				i = 0;
			}
		}
	}

	@Override
	public void digest() throws IOException {
		// TODO Auto-generated method stub
		if (!hasResult) {
			try {
				out.write(aes.doFinal(bf, 0, i));
			} catch (IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			hasResult = true;
		}
	}

	public OutputStream getOut() {
		return out;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		i = 0;
		hasResult = false;
	}
}
