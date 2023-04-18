package com.cisco.framework.utilities.logging.html;
/**
 * @author Lance Yan
 */
public class HTMLTableCellElement extends Element {
	public HTMLTableCellElement() {
		this.init("td");
	}
	public HTMLTableCellElement(String bgcolor) {
		this.init("td", createElementAttributes(bgcolor,"","",0,0,0,0));
	}
	public HTMLTableCellElement(String bgcolor, String align, String valign, int colspan, int rowspan, int width, int height) {
		this.init("td", createElementAttributes(bgcolor,align,valign,colspan,rowspan,width,height));
	}
}