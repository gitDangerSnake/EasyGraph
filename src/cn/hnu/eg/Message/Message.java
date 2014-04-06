package cn.hnu.eg.Message;

public class Message {			
	
	Object val;
	
	public Message(){}
	public Message(Object obj){
		this.val = obj;
	}
	
	public void setVal(Object obj){
		this.val = obj;
	}
	
	public Object getVal(){
		return val;
	}
	
	public String toString(){
		return val.toString();
	}
	
}
