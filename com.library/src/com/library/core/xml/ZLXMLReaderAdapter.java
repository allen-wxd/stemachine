/*
 */

package com.library.core.xml;

import java.util.*;
import java.io.InputStream;

import com.library.core.filesystem.ZLFile;

public class ZLXMLReaderAdapter implements ZLXMLReader {
	public boolean read(ZLFile file) {
		return ZLXMLProcessor.read(this, file);
	}
	
	public boolean read(InputStream stream) {
		return ZLXMLProcessor.read(this, stream, 65536);
	}
	
	public boolean dontCacheAttributeValues() {
		return false;
	}

	public boolean startElementHandler(String tag, ZLStringMap attributes) {
		return false;
	}
	
	public boolean endElementHandler(String tag) {
		return false;
	}
	
	public void characterDataHandler(char[] ch, int start, int length) {
	}

	public void characterDataHandlerFinal(char[] ch, int start, int length) {
		characterDataHandler(ch, start, length);	
	}

	public void startDocumentHandler() {
	}
	
	public void endDocumentHandler() {
	}

	public boolean processNamespaces() {
		return false;
	}

	public void namespaceMapChangedHandler(HashMap<String,String> namespaces) {
	}

	public void addExternalEntities(HashMap<String,char[]> entityMap) {
	}

	public List<String> externalDTDs() {
		return Collections.emptyList();
	}
}
