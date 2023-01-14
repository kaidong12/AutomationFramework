package com.cisco.framework.utilities.logging.html;

/**
 * @author Francesco Ferrante
 */
public class HTMLTextAreaElement extends Element {
	public HTMLTextAreaElement(int rows, int cols, boolean disabled, boolean readonly) {
		String attributes = "";
		if(rows > 0) {
			attributes += this.createAttribute("rows", String.valueOf(rows));
		}
		if(cols > 0) {
			attributes += this.createAttribute("cols", String.valueOf(cols));
		}
		if(disabled) {
			attributes += this.createAttribute("disabled", "disabled");
		}
		if(readonly) {
			attributes += this.createAttribute("readonly", "readonly");
		}
		this.init("textarea", attributes);
	}
	
	@Override
	public void append(String item) {
		super.append(item);
		this.contents = this.contents.trim();
	}
}