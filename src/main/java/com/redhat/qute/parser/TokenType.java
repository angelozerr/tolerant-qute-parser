package com.redhat.qute.parser;

public enum TokenType {

	// Comment token types
	StartComment, //
	Comment, //
	EndComment, //

	// Expressions token types
	StartExpression, //
	Expression, //
	EndExpression, //

	// Section tag token types
	StartTagOpen, //
	StartTag, //
	EndTagOpen, //
	StartTagSelfClose, //
	StartTagClose, //

	// Other token types
	Content, //
	Whitespace, //
	Unknown, //
	EOS;
}
