package com.redhat.qute.parser;

public class Template extends Node {

	Template(int start, int end) {
		super(start, end);
		super.setClosed(true);
	}

	@Override
	public NodeKind getKind() {
		return NodeKind.Template;
	}
	
	public String getNodeName() {
		return "#template";
	}
}
