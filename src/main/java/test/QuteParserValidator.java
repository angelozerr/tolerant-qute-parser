package test;

import com.redhat.qute.parser.Node;
import com.redhat.qute.parser.QuteParser;
import com.redhat.qute.parser.Template;

public class QuteParserValidator {

	public static void main(String[] args) {		
		validate("{#if } {#each }");
		validate("Hello {name");
		validate("{#custom /}");
	}

	private static void validate(String content) {
		Template template = QuteParser.parse(content);
		System.err.println("Validate --> " + content);
		Node parent = template;
		validate(parent);
	}

	private static void validate(Node parent) {
		if (!parent.isClosed()) {
			System.err.println(parent.getKind() + parent.getNodeName() + " (from,to) (" + parent.getStart() + ","
					+ parent.getEnd() + ") is not closed");
		}
		for (Node child : parent.getChildren()) {
			validate(child);
		}
	}
}
