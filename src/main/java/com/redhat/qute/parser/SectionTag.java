package com.redhat.qute.parser;

public class SectionTag extends Node {

	private String tag;
	
	private int startTagOpenOffset;

	SectionTag(int start, int end) {
		super(start, end);
	}

	@Override
	public NodeKind getKind() {
		return NodeKind.SectionTag;
	}

	public int getStartTagOpenOffset() {
		return startTagOpenOffset;
	}

	void setStartTagOpenOffset(int startTagOpenOffset) {
		this.startTagOpenOffset = startTagOpenOffset;
	}
	
	public String getTag() {
		return tag;
	}
	
	void setTag(String tag) {
		this.tag = tag;
	}
}
