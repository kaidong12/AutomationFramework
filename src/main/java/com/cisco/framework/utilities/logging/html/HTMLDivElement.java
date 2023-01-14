package com.cisco.framework.utilities.logging.html;

/**
 * @author Francesco Ferrante
 */
public class HTMLDivElement extends Element {
	public HTMLDivElement(String align, int width, int height) {
		String styleValue = "";
		if (align != null) {
			if (!align.isEmpty()) {
				if (!styleValue.isEmpty()) {
					styleValue += ";";
				}
				styleValue += "text-align:" + align;
			}
		}
		// if (width > 0) {
		// if (!styleValue.isEmpty()) {
		// styleValue += ";";
		// }
		// styleValue += "width:" + String.valueOf(width) + "px";
		// }
		// if (height > 0) {
		// if (!styleValue.isEmpty()) {
		// styleValue += ";";
		// }
		// styleValue += "height:" + String.valueOf(height) + "px";
		// }
		if ((width > 0) || (height > 0)) {
			if (!styleValue.isEmpty()) {
				styleValue += ";";
			}
			styleValue += "overflow:auto";
		}
		this.init("div", this.createAttribute("style", styleValue));
	}

	public static void main(String[] args) {
		HTMLDivElement div1 = new HTMLDivElement(null, 0, 0);
		System.out.println(div1.getContent());

		HTMLDivElement div2 = new HTMLDivElement("left", 0, 0);
		System.out.println(div2.getContent());

		HTMLDivElement div3 = new HTMLDivElement(null, 430, 0);
		System.out.println(div3.getContent());

		HTMLDivElement div4 = new HTMLDivElement(null, 0, 120);
		System.out.println(div4.getContent());

		HTMLDivElement div5 = new HTMLDivElement("left", 430, 0);
		System.out.println(div5.getContent());

		HTMLDivElement div6 = new HTMLDivElement("left", 0, 120);
		System.out.println(div6.getContent());

		HTMLDivElement div7 = new HTMLDivElement(null, 430, 120);
		System.out.println(div7.getContent());

		HTMLDivElement div8 = new HTMLDivElement("left", 430, 120);
		System.out.println(div8.getContent());

		HTMLDivElement div9 = new HTMLDivElement("left", 430, 120);
		HTMLPreElement pre = new HTMLPreElement();
		pre.append("This is a test");
		div9.append(pre);
		System.out.println(div9.getContent());
	}
}
