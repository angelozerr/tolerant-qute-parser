package test;

import com.redhat.qute.parser.scanner.QuteScanner;
import com.redhat.qute.parser.scanner.Scanner;
import com.redhat.qute.parser.scanner.TokenType;

public class QuteScannerTest {

	public static void main(String[] args) {
		scan("{@org.acme.Foo foo}\r\n" + //
				"<!DOCTYPE html>\r\n" + //
				"<html>\r\n" + //
				"<head>\r\n" + //
				"<meta charset=\"UTF-8\">\r\n" + //
				"<title>Qute Hello</title>\r\n" + //
				"</head>\r\n" + //
				"<body>\r\n" + //
				"  <h1>{foo.message}</h1> \r\n" + //
				"  {#for foo in baz.foos}\r\n" + //
				"    <p>Hello {foo.message}!</p> \r\n" + //
				"  {/for}\r\n" + //
				"</body>\r\n" + //
				"</html>");
		scan("{! This is a comment !}");
		scan("{#if item.price > 100}");
		scan("Hello {name}!");
		scan("Hello {name!");
	}

	private static void scan(String content) {
		System.err.println("--->" + content);
		Scanner scanner = QuteScanner.createScanner(content);
		TokenType token = scanner.scan();
		display(token, scanner, content);
		while (token != TokenType.EOS) {
			token = scanner.scan();
			display(token, scanner, content);
		}
	}

	private static void display(TokenType token, Scanner scanner, String content) {
		String tokenContent = content.substring(scanner.getTokenOffset(), scanner.getTokenEnd());
		System.err.println(token + " at (" + scanner.getTokenOffset() + "," + scanner.getTokenEnd() + ") : ["
				+ tokenContent + "]");
	}
}
