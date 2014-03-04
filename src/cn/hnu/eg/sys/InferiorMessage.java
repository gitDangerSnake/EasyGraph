package cn.hnu.eg.sys;

import java.io.Serializable;

import cn.hnu.eg.util.State;

public class InferiorMessage implements Message , Serializable{
	
	private static final long serialVersionUID = 8203433086620056073L;
	private double value ;
	private State state;
	
	public InferiorMessage(){
		
	}
	
	public InferiorMessage(State state,double value){
		this.state = state;	
		this.value = value;
	}
	
	public InferiorMessage(State state){
		this.state = state;
		this.value = Double.MIN_VALUE;
	}
	
	public double toValue() {
		return value;
	}	

	
	public boolean isHalt() {		
		return this.state == State.HALT ? true : false;
	}

	public boolean isActive() {
		return this.state == State.ACTIVE ? true : false;
	}

	
	public boolean isDeath() {
		return this.state == State.DEATH ? true : false;		
	}
	
	@Override public String toString(){
		return this.state + "---"+this.value;
	}
	
	

}
