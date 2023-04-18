package com.cisco.framework.utilities.logging.html;
/**
 * @author Lance Yan
 */
public class HTMLTableHeaderElement extends Element {
	public HTMLTableHeaderElement() {
		this.init("th");
	}
	public HTMLTableHeaderElement(String bgcolor, String align, String valign, int colspan, int rowspan, int width, int height) {
		this.init("th", createElementAttributes(bgcolor,align,valign,colspan,rowspan,width,height));
	}
}