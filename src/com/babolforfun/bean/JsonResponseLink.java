package com.babolforfun.bean;

/**
 * Mapper for URLs 
 * @author babolForFun
 *
 */
public class JsonResponseLink {

	private String href;
	private String rel;
	private String method;
	
	public JsonResponseLink(){
		super();
	}
	
	public JsonResponseLink(String href, String rel, String method) {
		super();
		this.href = href;
		this.rel = rel;
		this.method = method;
	}

	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getRel() {
		return rel;
	}
	public void setRel(String rel) {
		this.rel = rel;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	
	
	
}
