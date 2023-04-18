package com.cisco.framework.utilities.logging.html;

/**
 * @author Lance Yan
 */
public abstract class Element {

	public final static String	SPACE			= " ";
	public final static String	NEW_LINE		= System.getProperty("line.separator");
	public final static String	LINE_BREAK		= "<br>";
	public final static String	DOUBLE_QUOTES	= "\"";
	private String				tagName			= "";
	private String				tagLabel		= "";
	protected String			contents		= "";

	public Element() {
	}

	public Element(String tagName) {
		init(tagName);
	}

	public Element(String tagName, String tagAttributes) {
		init(tagName, tagAttributes);
	}

	public void append(Element element) {
		if (element != null) {
			contents += element.getContent();
		}
	}

	public void append(String item) {
		if (item != null) {
			contents += item;
			contents += NEW_LINE;
		}
	}

	public String getContents() {
		return contents;
	}

	public String getContent() {
		contents += "</" + this.tagName + ">";
		contents += NEW_LINE;
		return contents;
	}

	public String replace(String regex, String replacement) {
		contents = contents.replace(regex, replacement);
		return contents;
	}

	protected String createAttribute(String attributeName, String attributeValue) {
		String attributes = "";
		if ((attributeName != null) && (attributeValue != null)) {
			if (!attributeName.isEmpty() && !attributeValue.isEmpty()) {
				attributes = attributeName + " = " + DOUBLE_QUOTES + attributeValue + DOUBLE_QUOTES + SPACE;
			}
		}
		return attributes;
	}

	protected String createElementAttributes(String bgcolor, String align, String valign, int colspan, int rowspan, int width, int height) {
		String attributes = "";
		if (bgcolor != null) {
			if (!bgcolor.isEmpty()) {
				attributes += this.createAttribute("bgcolor", bgcolor);
			}
		}
		attributes += createAttribute("align", align);
		attributes += createAttribute("valign", valign);
		if (colspan > 0) {
			attributes += createAttribute("colspan", String.valueOf(colspan));
		}
		if (rowspan > 0) {
			attributes += createAttribute("rowspan", String.valueOf(rowspan));
		}
		if (width > 0) {
			attributes += createAttribute("width", String.valueOf(width) + "%");
		}
		if (height > 0) {
			attributes += createAttribute("height", String.valueOf(height) + "%");
		}
		return attributes;
	}

	protected void setTagLabel(String tagLabel) {
		if (tagLabel != null) {
			this.tagLabel = tagLabel;
		}
	}

	protected void init(String tagName) {
		this.tagName = tagName;
		contents += "<" + tagName + ">" + tagLabel;
		contents += NEW_LINE;
	}

	protected void init(String tagName, String tagAttributes) {
		this.tagName = tagName;
		if (tagAttributes != null) {
			if (!tagAttributes.isEmpty()) {
				contents += "<" + tagName + SPACE + tagAttributes.trim() + ">";
				contents += NEW_LINE;
			} else {
				init(tagName);
			}
		} else {
			init(tagName);
		}
	}
}