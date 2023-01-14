package com.cisco.framework.utilities.logging.html;

import com.cisco.framework.utilities.logging.Log;

/**
 * @author Francesco Ferrante
 */
public class HTMLLinkDocumentElement extends Element {
	public HTMLLinkDocumentElement() {
		this(Log.MKTREE_CSS);
	}
	public HTMLLinkDocumentElement(String href) {
		String attributes = this.createAttribute("rel", "stylesheet");
		attributes += this.createAttribute("href", href);
		super.init("link", attributes);
	}
}