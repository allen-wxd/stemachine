/*

 */

package com.library.core.util;

import java.io.IOException;
import java.io.InputStream;

public class ZLInputStreamWithOffset extends InputStream {
	private final InputStream myDecoratedStream;
	private int myOffset = 0;
	
	public ZLInputStreamWithOffset(InputStream stream) {
		myDecoratedStream = stream;
	}
	
	public int available() throws IOException {
		return myDecoratedStream.available();
	}

	public long skip(long n) throws IOException {
		long shift = myDecoratedStream.skip(n);
		if (shift > 0) {
			myOffset += (int)shift;
		}
		while ((shift < n) && (read() != -1)) {
			++shift;
		}
		return shift;
	}

	public int read() throws IOException {
		int result = myDecoratedStream.read();
		if (result != -1) {
			++myOffset;
		}
		return result;
	}

	public void close() throws IOException {
		myOffset = 0;
		myDecoratedStream.close();
	}

	public int read(byte[] b, int off, int len) throws IOException {
		final int shift = myDecoratedStream.read(b, off, len);
		if (shift > 0) {
			myOffset += shift;
		}
		return shift;
	}

	public int read(byte[] b) throws IOException {
		final int shift = myDecoratedStream.read(b);
		if (shift > 0) {
			myOffset += shift;
		}
		return shift;
	}

	public void reset() throws IOException {
		myOffset = 0;
		myDecoratedStream.reset();
	}

	public int offset() {
		return myOffset;
	}
}
