# tolerant-qute-parser

POC to manage Quarkus Qute tolerant parser

This project provides:

 * a Qute scanner to read Qute tokens.

```java
Scanner scanner = QuteScanner.createScanner("Hello {name}!");
TokenType token = scanner.scan();
while (token != TokenType.EOS) {
	token = scanner.scan();
}
```

For `Hello {name}!`, tokens are:

```
Content at (0,6) : [Hello ]
StartExpression at (6,7) : [{]
Expression at (7,11) : [name]
EndExpression at (11,12) : [}]
Content at (12,13) : [!]
EOS at (13,13) : []
```

See [QuteScannerTest.java](https://github.com/angelozerr/tolerant-qute-parser/blob/master/src/main/java/test/QuteScannerTest.java)

  * a Qute parser to build a Qute AST (by using the scanner)
  
```java
Template template = QuteParser.parse("Hello {name}!");
// template is an AST which host start, end offset and AST children (SectionTag, Text, Expression, etc).
```

For `Hello {name}!`, AST nodes are:

```
Template at (0,13), closed=true
	Text at (0,6), closed=true
	Expression at (6,12), closed=true
	Text at (12,13), closed=true

```

See [QuteParserTest.java](https://github.com/angelozerr/tolerant-qute-parser/blob/master/src/main/java/test/QuteParserTest.java)

The AST node have start/end position but for some node, you can have other information like for section tag:

 * start/end of the open section tag (ex : {#if  ... }
 * start/end of the close section tag (ex : {\if}

# Validator 

As parser is tolerant, it build an AST even if there are some errors. See [QuteParserValidator.java](https://github.com/angelozerr/tolerant-qute-parser/blob/master/src/main/java/test/QuteParserValidator.java) to see it in action

```java
public static void main(String[] args) {		
	validate("{#if } {#each }");
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
```

display 2 errors:

```java
Validate --> {#if } {#each }
SectionTag#if (from,to) (0,15) is not closed
SectionTag#each (from,to) (7,15) is not closed
```

# Limitation

 * works only with String (not with Reader). 
 * manage offset position instead of managing line/character position 