/**
 *  Copyright (c) 2018 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package com.redhat.qute.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Multi line stream.
 *
 */
public class MultiLineStream {

	private static final Predicate<Character> WHITESPACE_PREDICATE = ch -> {
		return ch == ' ' || ch == '\t' || ch == '\n' || ch == '\f' || ch == '\r';
	};

	private final String source;
	private final int len;
	private int position;
	private final Map<Pattern, Matcher> regexpCache;

	public MultiLineStream(String source, int position) {
		this.source = source;
		this.len = source.length();
		this.position = position;
		this.regexpCache = new HashMap<>();
	}

	public boolean eos() {
		return this.len <= this.position;
	}

	public String getSource() {
		return this.source;
	}

	public int pos() {
		return this.position;
	}

	public void goBackTo(int pos) {
		this.position = pos;
	}

	public void goBack(int n) {
		this.position -= n;
	}

	public void advance(int n) {
		this.position += n;
	}

	public void goToEnd() {
		this.position = len;
	}

	public char peekChar() {
		return peekChar(0);
	}

	/**
	 * Peeks at next char at position + n. peekChar() == peekChar(0)
	 * 
	 * @param n
	 * @return
	 */
	public char peekChar(int n) {
		int pos = this.position + n;
		if (pos >= len) {
			return 0;
		}
		return this.source.charAt(pos);
	}

	/**
	 * Peeks at the char at position 'offset' of the whole document
	 * 
	 * @param offset
	 * @return
	 */
	public char peekCharAtOffset(int offset) {
		if (offset >= len || offset < 0) {
			return 0;
		}
		return this.source.charAt(offset);
	}

	public boolean advanceIfChar(int ch) {
		if (ch == peekChar()) {
			this.position++;
			return true;
		}
		return false;
	}

	public boolean advanceIfChars(int... ch) {
		int i;
		if (this.position + ch.length > this.len) {
			return false;
		}
		for (i = 0; i < ch.length; i++) {
			if (peekChar(i) != ch[i]) {
				return false;
			}
		}
		this.advance(i);
		return true;
	}

	public int advanceIfAnyOfChars(int... ch) {
		int i;
		if (this.position + 1 > this.len) {
			return -1;
		}
		for (i = 0; i < ch.length; i++) {
			if (advanceIfChar(ch[i])) {
				return ch[i];
			}
		}
		return -1;
	}

	public String advanceIfRegExp(Pattern regex) {
		Matcher match = getCachedMatcher(regex);
		// Initialize start region where search must be started.
		match.region(this.position, this.len);
		if (match.find()) {
			this.position = match.end();
			return match.group(0);
		}
		return "";
	}

	/**
	 * Advances stream on regex, but will grab the first group
	 * 
	 * @param regex
	 * @return
	 */
	public String advanceIfRegExpGroup1(Pattern regex) {
		Matcher match = getCachedMatcher(regex);
		// Initialize start region where search must be started.
		match.region(this.position, this.len);
		if (match.find()) {
			this.position = match.end(1);
			return match.group(1);
		}
		return "";
	}

	/**
	 * Advances stream.position no matter what until it hits ch or eof(this.len)
	 * 
	 * @return boolean: was the char found
	 */
	public boolean advanceUntilChar(int ch) {
		while (this.position < this.len) {
			if (peekChar() == ch) {
				return true;
			}
			this.advance(1);
		}
		return false;
	}

	/**
	 * Will advance until any of the provided chars are encountered
	 */
	public boolean advanceUntilAnyOfChars(int... ch) {
		while (this.position < this.len) {
			for (int i = 0; i < ch.length; i++) {
				if (peekChar() == ch[i]) {
					return true;
				}
			}

			this.advance(1);
		}
		return false;
	}

	public boolean advanceUntilChars(int... ch) {
		while (this.position + ch.length <= this.len) {
			int i = 0;
			for (; i < ch.length && peekChar(i) == ch[i]; i++) {
			}
			if (i == ch.length) {
				return true;
			}
			this.advance(1);
		}
		this.goToEnd();
		return false;
	}

	/**
	 * Advances until it reaches a whitespace character
	 */
	public boolean skipWhitespace() {
		int n = this.advanceWhileChar(WHITESPACE_PREDICATE);
		return n > 0;
	}

	public int advanceWhileChar(Predicate<Character> condition) {
		int posNow = this.position;
		while (this.position < this.len && condition.test(peekChar())) {
			this.position++;
		}
		return this.position - posNow;
	}

	/**
	 * Returns the cached matcher from the given regex.
	 * 
	 * @param regex the regex pattern.
	 * @return the cached matcher from the given regex.
	 */
	private Matcher getCachedMatcher(Pattern regex) {
		Matcher matcher = regexpCache.get(regex);
		if (matcher == null) {
			matcher = regex.matcher(source);
			regexpCache.put(regex, matcher);
		} else {
			matcher.reset(); // Cached regex caused issues, needed to reset it.
		}
		return matcher;
	}

	public int getLastNonWhitespaceOffset() {
		int posNow = this.position;
		while (posNow > 0 && WHITESPACE_PREDICATE.test(peekCharAtOffset(posNow - 1))) {
			posNow--;
		}
		return posNow;
	}
}