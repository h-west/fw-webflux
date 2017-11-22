package io.hsjang.fw.model;

import java.util.HashMap;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Data extends HashMap<String, Object>{

	private static final long serialVersionUID = 1L;
	
	public Data() {
		super();
	}
	
	public Data(String k , Object v) {
		super();
		put(k,v);
	}
	
	public Data add(String k , Object v) {
		put(k,v);
		return this;
	}
	
	public String getString(String k) {
		return getString(k, "");
	}
	
	public String getString(String k, String d) {
		return containsKey(k) ? get(k).toString() : d;
	}
	
}
