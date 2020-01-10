package test;

import com.redhat.qute.parser.QuteParser;
import com.redhat.qute.parser.Template;

public class QuteParserTest {

	public static void main(String[] args) {
		Template template = QuteParser.parse("Hello {name}!");
		System.err.println(template);
	}
}
