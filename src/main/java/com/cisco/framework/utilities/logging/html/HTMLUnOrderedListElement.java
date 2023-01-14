package com.cisco.framework.utilities.logging.html;

/**
 * @author Francesco Ferrante
 */
public class HTMLUnOrderedListElement extends Element {
	public HTMLUnOrderedListElement() {
		super("ul");
	}
	public HTMLUnOrderedListElement(String className) {
		this.init("ul", this.createAttribute("class", className));
	}
}