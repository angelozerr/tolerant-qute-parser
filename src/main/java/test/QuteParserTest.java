package test;

import com.redhat.qute.parser.QuteScanner;
import com.redhat.qute.parser.Scanner;
import com.redhat.qute.parser.TokenType;

public class QuteParserTest {

	public static void main(String[] args) {
		scan("Hello {name}!");
		scan("Hello {name!");
	}

	private static void scan(String content) {
		System.err.println("--->" + content);
		Scanner scanner = QuteScanner.createScanner(content);
		TokenType token = scanner.scan();
		display(token, scanner);
		while (token != TokenType.EOS) {
			token = scanner.scan();
			display(token, scanner);
		}
	}

	private static void display(TokenType token, Scanner scanner) {
		System.err.println(token + " at " + scanner.getTokenOffset());
	}
}
