package com.redhat.qute.parser;

public enum ScannerState {
	WithinContent, //
	WithinExpression, //
	WithinComment, //
	AfterOpeningStartTag, //
	WithinTag, //
	AfterOpeningEndTag;
}
