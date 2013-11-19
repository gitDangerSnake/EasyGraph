package cn.hnu.eg.base;

import java.util.LinkedList;
import java.util.List;

import cn.hnu.eg.sys.Message;
import cn.hnu.eg.util.State;
import kilim.Mailbox;
import kilim.Pausable;
import kilim.Task;


/**
 * @author dodoro
 * @date 2013-11-11
 * abstract class Vertex 
 */
public abstract class Vertex extends Task {
	
	private Mailbox<Message> mailbox = new Mailbox<Message>();
	
	private int v_id;
	private int value;
	private List<Mailbox<Message>> yellowBook = new LinkedList<Mailbox<Message>>();
	private State nowState = State.ACTIVE;
	
	public void init(int vid,int value){
		this.v_id = vid;
		this.value = value;
	}
	public Mailbox<Message> getMailbox() {
		return mailbox;
	}

	public void setMailbox(Mailbox<Message> mailbox) {
		this.mailbox = mailbox;
	}

	public int getV_id() {
		return v_id;
	}

	public void setV_id(int v_id) {
		this.v_id = v_id;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public List<Mailbox<Message>> getYellowBook() {
		return yellowBook;
	}

	public void setYellowBook(List<Mailbox<Message>> yellowBook) {
		this.yellowBook = yellowBook;
	}

	
	public State getNowState() {
		return nowState;
	}

	public void setNowState(State nowState) {
		this.nowState = nowState;
	}

	
	
	@Override public String toString(){
		return v_id + ":"+ value;
	}
}
