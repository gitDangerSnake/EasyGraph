package cn.hnu.eg.base;

import java.io.Serializable;
import java.util.List;
import java.util.LinkedList;

import cn.hnu.eg.sys.InferiorMessage;
import cn.hnu.eg.sys.Master;
import cn.hnu.eg.sys.Message;
import cn.hnu.eg.sys.SupervisorMessage;
import cn.hnu.eg.util.Signal;
import cn.hnu.eg.util.State;
import cn.hnu.eg.Exceptions.IllegalDataException;
import cn.hnu.eg.cache.Chunk;
import cn.hnu.eg.ds.Edge;
import kilim.ExitMsg;
import kilim.Mailbox;
import kilim.Pausable;
import kilim.Task;

public abstract class BaseVertex extends Task implements
		Serializable {

	private static final long serialVersionUID = -6236420676620350939L;
	private Mailbox<Message> mailbox = new Mailbox<Message>(100);
	private Mailbox<Message> orders = new Mailbox<Message>(10); 
	private Mailbox<ExitMsg> exitmb = new Mailbox<ExitMsg>();
	private int id;
	private double val;
	private int superstep = 0;
	private List<Edge> adjs = new LinkedList<Edge>();
	private State currentstate = State.ACTIVE;
	private Message msg = null;
	private Chunk<Integer,BaseVertex> chunk;
	private StringBuffer sb = new StringBuffer();

	public Mailbox<Message> getMailbox() {
		return mailbox;
	}

	public Mailbox<Message> getOrders() {
		return orders;
	}

	public Mailbox<ExitMsg> getExitmb() {
		return exitmb;
	}

	public int getId() {
		return this.id;
	}

	public double getVal() {
		return val;
	}

	public void setVal(double val) {
		this.val = val;
	}

	public int getSuperstep() {
		return this.superstep;
	}

	public void setSuperstep(int s) {
		this.superstep = s;
	}

	public List<Edge> getAdjs() {
		return this.adjs;
	}

	public void addEdge(Edge e) {
		this.adjs.add(e);
	}

	public State getCurrentstate() {
		return this.currentstate;
	}

	public void setCurrentstate(State state) {
		this.currentstate = state;
	}

	public void init(int id, double val) {
		this.id = id;
		this.val = val;
	}

	public void init(int id) {
		this.id = id;
	}

	public void init(String str, String idSperatorId, String idSperatorValue,
			boolean flag/* if is weighted graph */) throws IllegalDataException {
		String[] vcontent = str.split(idSperatorId);
		String[] currentContent = null;
		if (vcontent[0].contains(idSperatorValue)) {
			currentContent = vcontent[0].split(idSperatorValue);

			if (Integer.valueOf(currentContent[0]) < 0) {
				throw new IllegalDataException("vertex id cannot be negative");
			}
			init(Integer.valueOf(currentContent[0]),
					 Double.valueOf(currentContent[1]));

		} else {

			if (Integer.valueOf(vcontent[0]) < 0)
				throw new IllegalDataException("vertex id cannot be negative");
			init(Integer.valueOf(vcontent[0]));
		}

		Edge e = null;

		for (int i = 1; i < vcontent.length; i++) {
			if (vcontent[i].contains(idSperatorValue)) {
				currentContent = vcontent[i].split(idSperatorValue);
				if (Integer.valueOf(currentContent[i]) < 0)
					throw new IllegalDataException(
							"vertex id cannot be negative");
				e = new Edge(this.id, Integer.valueOf(currentContent[0]),
						Double.valueOf(currentContent[i]));
			} else {
				if (Integer.valueOf(currentContent[i]) < 0)
					throw new IllegalDataException(
							"vertex id cannot be negative");

				e = new Edge(this.id, Integer.valueOf(vcontent[i]));
			}

			addEdge(e);
		}
	}

	// user need to implement this method , if user implements this method ,
	// it's hard to control message passing , if you do n.ot want to implement
	// this method , you can override the execute method which may be more hard
	// to program correctly
	public abstract void compute();

	public void execute() throws Pausable {
		SupervisorMessage msg = null;
		boolean running = false;
		msg = (SupervisorMessage)orders.get();
		if (msg.isStart())
			running = true;

		while (running) {
			while (mailbox.hasMessage()) {
				msg = (SupervisorMessage)orders.get();
				if (!msg.isStart()) {
					compute();
				} else if (msg.isDeath()) {
					running = false;
					break;
				} else {
					send();
				}
			}
			currentstate = State.HALT;
			Master.getMaster().getMailbox().putnb(Signal.dark);
			Master.getMaster().getStepbox().putnb(Signal.green);
		}
	}

	public void generateMsg() {
		this.msg = new InferiorMessage(currentstate, val);
	}

	public void send() throws Pausable {
		generateMsg();
		for (Edge e : adjs) {
			int d_id = e.getD_id();
			chunk.send(msg, d_id);
		}
	}

	public void writeSolution() {

	}

	public void notifyMaster(Signal s) {
		Master.getMaster().getMailbox().putnb(s);
	}

	@Override
	public String toString() {
		return id + ":" + val;
	}
}