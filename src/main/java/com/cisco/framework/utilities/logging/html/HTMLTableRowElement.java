package com.cisco.framework.utilities.logging.html;

/**
 * @author Francesco Ferrante
 */
public class HTMLTableRowElement extends Element {
	public HTMLTableRowElement() {
		this.init("tr");
	}
	public HTMLTableRowElement(String bgcolor) {
		this.init("tr", createElementAttributes(bgcolor,"","",0,0,0,0));
	}
	public HTMLTableRowElement(String bgcolor, String align, String valign) {
		this.init("tr", createElementAttributes(bgcolor,align,valign,0,0,0,0));
	}
}