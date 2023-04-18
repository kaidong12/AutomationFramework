package com.cisco.framework.utilities.logging.html;

/**
 * @author Lance Yan
 */
public class HTMLUnOrderedListElement extends Element {
	public HTMLUnOrderedListElement() {
		super("ul");
	}
	public HTMLUnOrderedListElement(String className) {
		this.init("ul", this.createAttribute("class", className));
	}
}