/*

 */

package com.library.core.util;

public abstract class ZLSearchUtil {
	private ZLSearchUtil() {
	}

	public static int find(char[] text, int offset, int length, final ZLSearchPattern pattern) {
		return find(text, offset, length, pattern, 0);
	}

	public static int find(char[] text, int offset, int length, final ZLSearchPattern pattern, int pos) {
		if (pos < 0) {
			pos = 0;
		}
		final char[] lower = pattern.LowerCasePattern;
		final int patternLength = lower.length;
		final int last = offset + length - patternLength;
		if (pattern.IgnoreCase) {
			final char[] upper = pattern.UpperCasePattern;
			final char firstCharLower = lower[0];
			final char firstCharUpper = upper[0];
			for (int i = offset + pos; i <= last; i++) {
				final char current = text[i];
				if ((current == firstCharLower) || (current == firstCharUpper)) {
					int j = 1;
					for (int k = i + 1; j < patternLength; ++j, ++k) {
						final char symbol = text[k];
						if ((lower[j] != symbol) &&
								(upper[j] != symbol)) {
							break;
						}
					}
					if (j >= patternLength) {
						return i - offset;
					}
				}
			}
		} else {
			final char firstChar = lower[0];
			for (int i = offset + pos; i <= last; i++) {
				if (text[i] == firstChar) {
					int j = 1;
					for (int k = i + 1; j < patternLength; ++j, ++k) {
						if (lower[j] != text[k]) {
							break;
						}
					}
					if (j >= patternLength) {
						return i - offset;
					}
				}
			}
		}
		return -1;
	}	
}
