package com.cisco.framework.utilities.logging.html;

/**
 * @author Francesco Ferrante
 */
public class HTMLAnchorElement extends Element {
	public HTMLAnchorElement(String href, String target) {
		String attributes = this.createAttribute("href", href);
		attributes += this.createAttribute("target", target);
		this.init("a", attributes);
	}
}