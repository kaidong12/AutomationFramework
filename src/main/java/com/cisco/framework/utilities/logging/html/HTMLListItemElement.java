package com.cisco.framework.utilities.logging.html;

/**
 * @author Lance Yan
 */
public class HTMLListItemElement extends Element {
	public HTMLListItemElement() {
		super("li");
	}

	public HTMLListItemElement(String tagLabel) {
		this.setTagLabel(tagLabel);
		this.init("li");
	}
}