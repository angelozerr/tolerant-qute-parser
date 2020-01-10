package com.redhat.qute.parser;

public class SectionTag extends Node {

	private String tag;

	private int startTagOpenOffset;

	private int startTagCloseOffset;

	private int endTagOpenOffset;

	private int endTagCloseOffset;

	private boolean selfClosed;

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

	public int getStartTagCloseOffset() {
		return startTagCloseOffset;
	}

	void setStartTagCloseOffset(int startTagCloseOffset) {
		this.startTagCloseOffset = startTagCloseOffset;
	}

	public String getTag() {
		return tag;
	}

	void setTag(String tag) {
		this.tag = tag;
	}

	public int getEndTagOpenOffset() {
		return endTagOpenOffset;
	}

	void setEndTagOpenOffset(int endTagOpenOffset) {
		this.endTagOpenOffset = endTagOpenOffset;
	}

	public int getEndTagCloseOffset() {
		return endTagCloseOffset;
	}

	void setEndTagCloseOffset(int endTagCloseOffset) {
		this.endTagCloseOffset = endTagCloseOffset;
	}

	public boolean isSelfClosed() {
		return selfClosed;
	}

	void setSelfClosed(boolean selfClosed) {
		this.selfClosed = selfClosed;
	}
}
