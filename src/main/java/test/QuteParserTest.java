package test;

import com.redhat.qute.parser.Node;
import com.redhat.qute.parser.QuteParser;
import com.redhat.qute.parser.Template;

public class QuteParserTest {

	public static void main(String[] args) {
		parse("{@org.acme.Foo foo}\r\n" + //
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
		// parse("Hello {name}!");
	}

	private static void parse(String content) {
		Template template = QuteParser.parse(content);
		display(template, 0, content);
	}

	private static void display(Node node, int indent, String content) {
		StringBuilder indentText = new StringBuilder();
		for (int i = 0; i < indent; i++) {
			indentText.append('\t');
		}
		String nodeContent = content.substring(node.getStart(), node.getEnd());
		System.err.println(indentText.toString() + node.getKind() + " at (" + node.getStart() + "," + node.getEnd()
				+ ")" + ", closed=" + node.isClosed());
		for (Node child : node.getChildren()) {
			display(child, indent + 1, content);
		}
	}
}
