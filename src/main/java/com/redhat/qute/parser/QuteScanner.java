package com.redhat.qute.parser;

public class QuteScanner implements Scanner {

	public static Scanner createScanner(String input) {
		return createScanner(input, 0);
	}

	public static Scanner createScanner(String input, int initialOffset) {
		return createScanner(input, initialOffset, ScannerState.WithinContent);
	}

	public static Scanner createScanner(String input, int initialOffset, ScannerState initialState) {
		return new QuteScanner(input, initialOffset, initialState);
	}

	private final MultiLineStream stream;
	private ScannerState state;

	private int tokenOffset;

	private TokenType tokenType;
	private String tokenError;

	QuteScanner(String input, int initialOffset, ScannerState initialState) {
		stream = new MultiLineStream(input, initialOffset);
		state = initialState;
		tokenOffset = 0;
		tokenType = TokenType.Unknown;

	}

	public TokenType scan() {
		int offset = stream.pos();
		ScannerState oldState = state;
		TokenType token = internalScan();
		if (token != TokenType.EOS && offset == stream.pos()) {
			log("Scanner.scan has not advanced at offset " + offset + ", state before: " + oldState + " after: "
					+ state);
			stream.advance(1);
			return finishToken(offset, TokenType.Unknown);
		}
		return token;
	}

	private TokenType internalScan() {
		int offset = stream.pos();
		if (stream.eos()) {
			return finishToken(offset, TokenType.EOS);
		}

		String errorMessage = null;
		switch (state) {
		case WithinContent: {
			if (stream.advanceIfChar('{')) {
				state = ScannerState.WithinExpression;
				return finishToken(offset, TokenType.StartExpression);
			}
			stream.advanceUntilChar('{');
			return finishToken(offset, TokenType.Content);
		}

		case WithinExpression: {
			if (stream.skipWhitespace()) {
				return finishToken(offset, TokenType.Whitespace);
			}

			if (stream.advanceIfChar('}')) {
				state = ScannerState.WithinContent;
				return finishToken(offset, TokenType.EndExpression);
			}
			stream.advanceUntilChars('}');
			return finishToken(offset, TokenType.Expression);
		}

		default:
		}
		stream.advance(1);
		return finishToken(offset, TokenType.Unknown, errorMessage);
	}

	TokenType finishToken(int offset, TokenType type) {
		return finishToken(offset, type, null);
	}

	TokenType finishToken(int offset, TokenType type, String errorMessage) {
		tokenType = type;
		tokenOffset = offset;
		tokenError = errorMessage;
		return type;
	}

	@Override
	public TokenType getTokenType() {
		return tokenType;
	}

	/**
	 * Starting offset position of the current token
	 * 
	 * @return Starting offset position of the current token
	 */
	@Override
	public int getTokenOffset() {
		return tokenOffset;
	}

	@Override
	public int getTokenLength() {
		return stream.pos() - tokenOffset;
	}

	@Override
	/**
	 * Ending offset position of the current token
	 * 
	 * @return Ending offset position of the current token
	 */
	public int getTokenEnd() {
		return stream.pos();
	}

	@Override
	public String getTokenText() {
		return stream.getSource().substring(tokenOffset, stream.pos());
	}

	@Override
	public ScannerState getScannerState() {
		return state;
	}

	@Override
	public String getTokenError() {
		return tokenError;
	}

	private void log(String message) {
		System.err.println(message);
	}
}
