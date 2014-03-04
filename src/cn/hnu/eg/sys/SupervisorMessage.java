package cn.hnu.eg.sys;

import java.io.Serializable;

import cn.hnu.eg.util.State;

public class SupervisorMessage implements Message , Serializable {
	
	private static final long serialVersionUID = -173674230888539334L;
	private double value;
	private State state;

	public SupervisorMessage(double value, State state) {
		super();
		this.value = value;
		this.state = state;
	}

	public SupervisorMessage(State death) {
		state = State.DEATH;
		
	}

	@Override
	public double toValue() {		
		return value;
	}

	@Override
	public boolean isHalt() {		
		return state == State.HALT ? true : false;
	}

	@Override
	public boolean isActive() {		
		return state == State.ACTIVE ? true : false;
	}

	@Override
	public boolean isDeath() {
		return state == State.DEATH ? true : false;
	}
	
	public boolean isStart(){
		return state == State.START ? true : false;
	}
	
	@Override public String toString(){
		return this.state + "---"+this.value;
	}

}
