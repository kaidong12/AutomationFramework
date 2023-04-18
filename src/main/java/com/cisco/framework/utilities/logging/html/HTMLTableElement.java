package com.cisco.framework.utilities.logging.html;

/**
 * @author Lance Yan
 */
public class HTMLTableElement extends Element {

	private String attributes = "";
	
	public HTMLTableElement(String frame, int border, int cellPadding, int cellSpacing, int width) {
		setTableLayoutToFixed(true);
		init(frame, border, cellPadding, cellSpacing, width);
	}
	
	public HTMLTableElement(String frame, int border, int cellPadding, int cellSpacing, int width, boolean isTableLayoutFixed) {
		setTableLayoutToFixed(isTableLayoutFixed);
		init(frame, border, cellPadding, cellSpacing, width);
	}
	
	public HTMLTableElement(String frame, String bgcolor, int border, int cellPadding, int cellSpacing, int width) {
		setTableLayoutToFixed(true);
		init(frame, bgcolor, border, cellPadding, cellSpacing, width);
	}
	
	public HTMLTableElement(String frame, String bgcolor, int border, int cellPadding, int cellSpacing, int width, boolean isTableLayoutFixed) {
		setTableLayoutToFixed(isTableLayoutFixed);
		init(frame, bgcolor, border, cellPadding, cellSpacing, width);
	}
	
	public HTMLTableElement() {
		setTableLayoutToFixed(true);
		init();
	}
	
	public HTMLTableElement(boolean isTableLayoutFixed) {
		setTableLayoutToFixed(isTableLayoutFixed);
		init();
	}
	
	private void init() {
		init("table", attributes);
	}
	
	private void init(String frame, int border, int cellPadding, int cellSpacing, int width) {
		attributes += createAttribute("frame", frame);
		if(border >= 0) {
			attributes += createAttribute("border", String.valueOf(border));
		}
		if(cellPadding >= 0) {
			attributes += createAttribute("cellpadding", String.valueOf(cellPadding));
		}
		if(cellSpacing >= 0) {
			attributes += createAttribute("cellspacing", String.valueOf(cellSpacing));
		}
		if(width >= 0) {
			attributes += createAttribute("width", String.valueOf(width) + "%");
		}
		init("table", attributes);
	}
	
	private void init(String frame, String bgcolor, int border, int cellPadding, int cellSpacing, int width) {
		attributes += createAttribute("frame", frame);
		attributes += createAttribute("bgcolor", bgcolor);
		if(border >= 0) {
			attributes += createAttribute("border", String.valueOf(border));
		}
		if(cellPadding >= 0) {
			attributes += createAttribute("cellpadding", String.valueOf(cellPadding));
		}
		if(cellSpacing >= 0) {
			attributes += createAttribute("cellspacing", String.valueOf(cellSpacing));
		}
		if(width >= 0) {
			attributes += createAttribute("width", String.valueOf(width) + "%");
		}
		init("table", attributes);
	}
	
	private void setTableLayoutToFixed(boolean isTableLayoutFixed) {
		if(isTableLayoutFixed) {
			attributes = createAttribute("style", "table-layout:fixed");
		}
	}
}