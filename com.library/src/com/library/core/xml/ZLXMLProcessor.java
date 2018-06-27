/*

 */

package com.library.core.xml;

import java.util.*;
import java.io.*;

import com.library.core.filesystem.ZLFile;
import com.library.core.util.Util;

public abstract class ZLXMLProcessor {
	public static Map<String,char[]> getEntityMap(List<String> dtdList) {
		try {
			return ZLXMLParser.getDTDMap(dtdList);
		} catch (IOException e) {
			return Collections.emptyMap();
		}
	}

	public static boolean read(ZLXMLReader reader, InputStream stream, int bufferSize) {
		ZLXMLParser parser = null;
		try {
			parser = new ZLXMLParser(reader, stream, bufferSize);
			reader.startDocumentHandler();
			parser.doIt();
			reader.endDocumentHandler();
		} catch (IOException e) {
			//System.out.println(e);
			return false;
		} finally {
			if (parser != null) {
				parser.finish();
			}
		}
		return true;
	}

	public static boolean read(ZLXMLReader xmlReader, ZLFile file) {
		return read(xmlReader, file, 65536);
	}

	public static boolean read(ZLXMLReader xmlReader, ZLFile file, int bufferSize) {
		InputStream stream = null;
		try {
			stream = file.getInputStream();
			file.exists();
		} catch (IOException e) {
		}
		if (stream == null) {
			return false;
		}
		boolean code = read(xmlReader, stream, bufferSize);
		try {
			stream.close();
		} catch (IOException e) {
		}
		return code;
	}
}
