package cn.hnu.eg.util;

import java.util.EventObject;

public class SignalEvent extends EventObject {

	private Object source;
	public SignalEvent(Object source) {
		super(source);
		this.source = source;
	}
	public Object getSource() {
		return source;
	}
	public void setSource(Object source) {
		this.source = source;
	}
	

}
