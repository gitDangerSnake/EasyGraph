package cn.hnu.eg.sys;

import java.io.Serializable;

import cn.hnu.eg.util.State;

public class InferiorMessage implements Message , Serializable{
	
	
	private static final long serialVersionUID = 1L;
	private int value ;
	private State state;
	
	public InferiorMessage(){
		
	}
	
	public InferiorMessage(State state,int value){
		this.state = state;	
		this.value = value;
	}
	
	public InferiorMessage(State state){
		this.state = state;
		this.value = Integer.MAX_VALUE;
	}
	
	@Override
	public int toValue() {
		return value;
	}	

	@Override
	public boolean isHalt() {		
		return this.state == State.HALT ? true : false;
	}

	@Override
	public boolean isActive() {
		return this.state == State.ACTIVE ? true : false;
	}

	@Override
	public boolean isDeath() {
		return this.state == State.DEATH ? true : false;		
	}
	
	@Override public String toString(){
		return this.state + "---"+this.value;
	}
	
	

}
