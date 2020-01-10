package com.redhat.qute.parser;

import com.redhat.qute.parser.scanner.QuteScanner;
import com.redhat.qute.parser.scanner.Scanner;
import com.redhat.qute.parser.scanner.TokenType;

public class QuteParser {

	private static CancelChecker DEFAULT_CANCEL_CHECKER = () -> {
	};

	public static Template parse(String content) {
		return parse(content, DEFAULT_CANCEL_CHECKER);
	}

	public static Template parse(String content, CancelChecker cancelChecker) {
		if (cancelChecker == null) {
			cancelChecker = DEFAULT_CANCEL_CHECKER;
		}
		Template template = new Template();

		Scanner scanner = QuteScanner.createScanner(content);
		TokenType token = scanner.scan();
		while (token != TokenType.EOS) {
			cancelChecker.checkCanceled();
			token = scanner.scan();
		}
		return template;
	}

}
