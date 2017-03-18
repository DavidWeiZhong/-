package common.aes128;

import java.io.IOException;

public interface IEDCoder {
	public void put(byte b) throws IOException;

	public void put(byte[] b) throws IOException;

	public void put(byte[] b, int offset, int length) throws IOException;

	public void digest() throws IOException;

	public void reset();

}
