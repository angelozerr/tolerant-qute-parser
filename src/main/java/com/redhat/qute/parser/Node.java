package com.redhat.qute.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Node {

	private int start;
	private int end;
	private boolean closed;
	private Node parent;
	private List<Node> children;

	Node(int start, int end) {
		this.start = start;
		this.end = end;
		this.closed = false;
	};

	public abstract NodeKind getKind();

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	void setEnd(int end) {
		this.end = end;
	}

	void setClosed(boolean closed) {
		this.closed = closed;
	}

	public boolean isClosed() {
		return closed;
	}

	void addChild(Node child) {
		child.setParent(this);
		if (children == null) {
			children = new ArrayList<>();
		}
		children.add(child);
	}

	void setParent(Node parent) {
		this.parent = parent;
	}

	public Node getParent() {
		return parent;
	}

	public List<Node> getChildren() {
		if (children == null) {
			return Collections.emptyList();
		}
		return children;
	}

	public abstract String getNodeName();

}