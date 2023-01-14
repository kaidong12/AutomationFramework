package com.cisco.framework.utilities.logging.html;

/**
 * @author Francesco Ferrante
 */
public class HTMLImageElement extends Element {
	public HTMLImageElement(String src, String alt, String title, int width, int height) {
		String attributes = this.createAttribute("src", src);
		attributes += this.createAttribute("alt", alt);
		attributes += this.createAttribute("title", title);
		attributes += this.createAttribute("width", String.valueOf(width));
		attributes += this.createAttribute("height", String.valueOf(height));
		this.init("img", attributes);
	}
}