/*

 */

package com.library.core.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import com.library.core.util.ZLArrayUtils;

final class ZLDTDParser {
	private static final byte IGNORABLE_WHITESPACE = 0;
	private static final byte LANGLE = 1;
	private static final byte TAG_PREFIX = 2;
	private static final byte COMMENT = 3;
	private static final byte END_OF_COMMENT1 = 4;
	private static final byte END_OF_COMMENT2 = 5;
	private static final byte ENTITY = 6;
	private static final byte WAIT_NAME = 7;
	private static final byte NAME = 8;
	private static final byte WAIT_VALUE = 9;
	private static final byte VALUE = 10;
	private static final byte WAIT_END_OF_ENTITY = 11;

	public void doIt(final InputStream stream, final HashMap<String,char[]> entityMap) throws IOException 
	{
		final InputStreamReader streamReader = new InputStreamReader(stream, "us-ascii");

		char[] buffer = new char[8192];
		int startPosition = 0;
		String name = "";
		String value = "";
        boolean r=true;
		byte state = IGNORABLE_WHITESPACE;
errorLabel:
		while (true) {
			int count = streamReader.read(buffer);
			if (count <= 0) {
				streamReader.close();
				return;
			}
			if (count < buffer.length) {
				buffer = ZLArrayUtils.createCopy(buffer, count, count);
			}
			try {
				for (int i = -1;;) {
mainSwitchLabel:
					switch (state) {
						case IGNORABLE_WHITESPACE:
							
							while (r) {
								switch (buffer[++i]) {
									case '<':
										state = LANGLE;
										break mainSwitchLabel;
								}
							}
							break;
					case LANGLE:
							switch (buffer[++i]) {
								case '!':
									state = TAG_PREFIX;
									break mainSwitchLabel;
								default:
//									break errorLabel;
							}

							break errorLabel;
						case TAG_PREFIX:
							switch (buffer[++i]) {
								case 'E':
									state = ENTITY;
									break mainSwitchLabel;
								case '-':
									state = COMMENT;
									break mainSwitchLabel;
								default:
//									break errorLabel;
							}
							break errorLabel;
						case ENTITY:
							while (r) {
								switch (buffer[++i]) {
									case ' ':
									case 0x0008:
									case 0x0009:
									case 0x000A:
									case 0x000B:
									case 0x000C:
									case 0x000D:
										state = WAIT_NAME;
										break mainSwitchLabel;
								}
							}
							break;
						case WAIT_NAME:
							while (r) {
								switch (buffer[++i]) {
									case ' ':
									case 0x0008:
									case 0x0009:
									case 0x000A:
									case 0x000B:
									case 0x000C:
									case 0x000D:
										break;
									default:
										state = NAME;
										startPosition = i;
										break mainSwitchLabel;
								}
							}
							break;
						case NAME:
							while (r) {
								switch (buffer[++i]) {
									case ' ':
									case 0x0008:
									case 0x0009:
									case 0x000A:
									case 0x000B:
									case 0x000C:
									case 0x000D:
										state = WAIT_VALUE;
										name += new String(buffer, startPosition, i - startPosition);
										break mainSwitchLabel;
								}
							}
							break;
						case WAIT_VALUE:
							while (r) {
								switch (buffer[++i]) {
									case ' ':
									case 0x0008:
									case 0x0009:
									case 0x000A:
									case 0x000B:
									case 0x000C:
									case 0x000D:
										break;
									default:
										state = VALUE;
										startPosition = i;
										break mainSwitchLabel;
								}
							}
							break;
						case VALUE:
							while (r) {
								switch (buffer[++i]) {
									case '>':
										state = IGNORABLE_WHITESPACE;
										value += new String(buffer, startPosition, i - startPosition);
										final int len = value.length();
										if ((len > 2) &&
												(value.charAt(0) == '"') &&
												(value.charAt(len - 1) == '"')) {
											value = value.substring(1, len - 1);
											if (value.startsWith("&#") && value.endsWith(";")) {
												try {
													int number = 0;
													if (value.charAt(2) == 'x') {
														number = Integer.parseInt(value.substring(3, len - 3), 16);
													} else {
														for (int j = 2; j < len - 3; ++j) {
															number *= 10;
															number += value.charAt(j) - 48;
														}
													}
													entityMap.put(name, new char[] { (char)number });
												} catch (NumberFormatException e) {
												}
											} else {
												final char[] aValue = new char[len - 2];
												value.getChars(0, len - 2, aValue, 0);
												entityMap.put(name, aValue);
											}
										}
										name = "";
										value = "";
										break mainSwitchLabel;
									case ' ':
									case 0x0008:
									case 0x0009:
									case 0x000A:
									case 0x000B:
									case 0x000C:
									case 0x000D:
										state = WAIT_END_OF_ENTITY;
										value += new String(buffer, startPosition, i - startPosition);
										name = "";
										value = "";
										break mainSwitchLabel;
								}
							}
							break;
						case WAIT_END_OF_ENTITY:
							while (r) {
								switch (buffer[++i]) {
									case '>':
										state = IGNORABLE_WHITESPACE;
										break mainSwitchLabel;
								}
							}
							break;
						case COMMENT:
							while (r) {
								switch (buffer[++i]) {
									case '-':
										state = END_OF_COMMENT1;
										break mainSwitchLabel;
								}
							}
							break;
						case END_OF_COMMENT1:
							switch(buffer[++i]) {
								case '-':
									state = END_OF_COMMENT2;
									break mainSwitchLabel;
								default:
									state = COMMENT;
//									break mainSwitchLabel;
							}
							break mainSwitchLabel;
						case END_OF_COMMENT2:
							switch(buffer[++i]) {
								case '>':
									state = IGNORABLE_WHITESPACE;
									break mainSwitchLabel;
								default:
									state = COMMENT;
									
							}
							break mainSwitchLabel;
					}
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				switch (state) {
					case NAME:
						name = new String(buffer, startPosition, count - startPosition);
						startPosition = 0;
						break;
					case VALUE:
						value = new String(buffer, startPosition, count - startPosition);
						startPosition = 0;
						break;
				}
			}
		}
		streamReader.close();
	}
}
