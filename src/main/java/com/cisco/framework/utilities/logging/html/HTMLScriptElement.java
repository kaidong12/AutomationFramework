package com.cisco.framework.utilities.logging.html;

import com.cisco.framework.utilities.logging.Log;

/**
 * @author Francesco Ferrante
 */
public class HTMLScriptElement extends Element {
	public HTMLScriptElement() {
		this(Log.MKTREE_JS);
	}
	public HTMLScriptElement(String src) {
		String attributes = this.createAttribute("src", src);
		attributes += this.createAttribute("language", "JavaScript");
		super.init("script", attributes);
	}
}