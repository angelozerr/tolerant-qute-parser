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
		Template template = new Template(0, content.length());
		Node curr = template;

		Scanner scanner = QuteScanner.createScanner(content);
		TokenType token = scanner.scan();
		while (token != TokenType.EOS) {
			cancelChecker.checkCanceled();

			switch (token) {

			case StartTagOpen: {
				if (!curr.isClosed() && curr.getParent() != null) {
					// The next node's parent (curr) is not closed at this point
					// so the node's parent (curr) will have its end position updated
					// to a newer end position.
					curr.setEnd(scanner.getTokenOffset());
				}
				if ((curr.isClosed() && curr.getKind() != NodeKind.Template)) {
					// The next node being considered is a child of 'curr'
					// and if 'curr' is already closed then 'curr' was not updated properly.
					curr = curr.getParent();
				}
				SectionTag child = new SectionTag(scanner.getTokenOffset(), scanner.getTokenEnd());
				child.setStartTagOpenOffset(scanner.getTokenOffset());
				curr.addChild(child);
				curr = child;
				break;
			}
			
			case StartTag: {
				SectionTag element = (SectionTag) curr;
				element.setTag(scanner.getTokenText());
				curr.setEnd(scanner.getTokenEnd());
				break;
			}

			case StartExpression: {
				int start = scanner.getTokenOffset();
				int end = scanner.getTokenEnd();
				Expression expression = new Expression(start, end);
				expression.setStartExpression(start);
				curr.addChild(expression);
				curr = expression;
				break;
			}

			case EndExpression: {
				int end = scanner.getTokenEnd();
				Expression expression = (Expression) curr;
				expression.setClosed(true);
				expression.setEndExpression(end);
				curr = curr.getParent();
				break;
			}

			case Content: {
				int start = scanner.getTokenOffset();
				int end = scanner.getTokenEnd();
				Text text = new Text(start, end);
				curr.addChild(text);
				break;
			}
			default:
			}
			token = scanner.scan();
		}
		while (curr.getParent() != null) {
			curr.setEnd(content.length());
			curr = curr.getParent();
		}
		return template;
	}

}
