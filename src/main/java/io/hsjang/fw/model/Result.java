package io.hsjang.fw.model;

@lombok.Data
public class Result {
	
	int code;
	String message;
	Data data;

	public Result() {
		this(1, "ok");
	}
	
	public Result(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public Result(String k, Object v) {
		this();
		add(k, v);
	}
	
	public Result(Data data) {
		this();
		setData(data);
	}
	
	public Result add(String k, Object v) {
		getData().add(k, v);
		return this;
	}
	
	public Data getData() {
		if(this.data==null) data = new Data();
		return this.data;
	}
	
}
