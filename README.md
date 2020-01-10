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

  * a Qute parser to build a Qute AST (by using the scanner)
  
```java
Template template = QuteParser.parse(content);
// template is an AST which host start, end offset and AST children (SectionTag, Text, Expression, etc).
```
  