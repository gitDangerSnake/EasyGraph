package cn.hnu.eg.sys;

import kilim.Mailbox;
import kilim.Pausable;
import kilim.Task;
import cn.hnu.eg.base.Vertex;
import cn.hnu.eg.ds.Graph;
import cn.hnu.eg.util.Signal;
import cn.hnu.eg.util.State;

public class Master extends Task{
	
	private Mailbox<Signal> mailbox = new Mailbox<Signal>();
	private int numOfHalt = 0;
	private Graph graph;
	
	
	private Master(){
		
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

	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	public boolean isDone(){
		return numOfHalt == graph.size() ? true : false;
	}
	
	@Override public void execute() throws Pausable{
		
		while(!isDone()){
			Signal s = mailbox.get();
			if(s.toString().equals("red")){
				numOfHalt -= 1;
			}else{
				numOfHalt += 1;
			}
			
			//System.out.println(numOfHalt);
		}
		System.out.println("OK, it's time to finish this!");
		Message death = new InferiorMessage(State.DEATH);
		for(Vertex v : graph.getListOfVertices()){
			v.getMailbox().putnb(death);
		}
	}
	
	
	public static class MasterHolder{
		public static Master master = new Master();
	}
	
	public static Master getMaster(){
		return MasterHolder.master;
	}
}
