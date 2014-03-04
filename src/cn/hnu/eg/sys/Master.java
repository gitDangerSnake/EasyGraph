package cn.hnu.eg.sys;

import kilim.Mailbox;
import kilim.Pausable;
import kilim.Task;
import cn.hnu.eg.ds.Graph;
import cn.hnu.eg.util.Signal;
import cn.hnu.eg.util.State;

public class Master extends Task {

	private Mailbox<Signal> mailbox = new Mailbox<Signal>();
	private Mailbox<Signal> stepbox = new Mailbox<Signal>();
	private int numOfHalt = 0;

	public Mailbox<Signal> getStepbox() {
		return stepbox;
	}

	public void setStepbox(Mailbox<Signal> stepbox) {
		this.stepbox = stepbox;
	}

	private int numOfStepOver = 0;
	private int tinyStep = 0;
	private Graph graph;

	private Master() {

	}

	public Mailbox<Signal> getMailbox() {
		return mailbox;
	}

	public void setMailbox(Mailbox<Signal> mailbox) {
		this.mailbox = mailbox;
	}

	public int getNumOfHalt() {
		return numOfHalt;
	}

	public void setNumOfHalt(int numOfHalt) {
		this.numOfHalt = numOfHalt;
	}

	public int getTinyStep() {
		return tinyStep;
	}

	public void setTinyStep(int tinyStep) {
		this.tinyStep = tinyStep;
	}

	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	public boolean isOver() throws Pausable {
		Signal s = mailbox.getnb();
		if (s == null) {
			return false;
		} else if (s.toString().equals("red")) {
			numOfHalt -= 1;
		} else {
			numOfHalt += 1;
		}
		return numOfHalt == graph.size() ? true : false;
	}

	@Override
	public void execute() throws Pausable {
		while (!isOver()) {
			//System.out.println("I am working");
			//System.out.println("This is step "+tinyStep);
			if (tinyStep == 0) {
				//System.out.println("I am going to sending message!");
				sendOrders(tinyStep, State.START);
				while (!isStepOver()) {
				}
				numOfStepOver = 0;
				tinyStep++;
			} else {
				sendOrders(tinyStep, State.ACTIVE);
				while (!isStepOver()) {
				}
				numOfStepOver = 0;
				tinyStep++;
			}
			// System.out.println(numOfHalt);
		}
		System.out.println("OK, it's time to finish this!");
		SupervisorMessage death = new SupervisorMessage(State.DEATH);		
		
		for (Integer i : graph.getChunk().asMap().keySet()) {
			graph.getChunk().asMap().get(i).getOrders()
					.putnb(death);
		}
	}

	private boolean isStepOver() throws Pausable {
		Signal s = stepbox.get();
		if (s.toString().equals("green")) {
			numOfStepOver += 1;
		}
		return numOfStepOver == graph.size() ? true : false;
	}

	public void sendOrders(int tinyStep, State state) {

		for (Integer i : graph.getChunk().asMap().keySet()) {
			graph.getChunk().asMap().get(i).getOrders()
					.putnb(new SupervisorMessage(tinyStep, state));
		}
	}

	private static class MasterHolder {
		public static Master master = new Master();
	}

	public static Master getMaster() {
		return MasterHolder.master;
	}
}
