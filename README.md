EasyGraph
=========
	
A quite simple graph processing framework based on the Kilim.

This framework takes advantage of the message-passing and a fake actor model to deal with high concurrency compute on large scale grpah.

Model
=====

Graph consists of Vertex and some reference of the mailboxes inside the vertices. During the computation , every vertex is taken as a Task @kilim.Task .
Like:
    public abstract class Vertex extends Task{
               @Override public void execute();
                public abstract void compute();
   }

Vertex State and Message
=====================

A vertex has two state : ACTIVE & HALT.
ACTIVE means the vertex is active and could do the compute now while HALT means the vertex stopping working now. 

Message : ACTIVE & HALT & DEATH

ACTIVE message : ACTIVE---COMPUTABLE_VALUE
HALT message : HALT----Integer.MAX_VALUE
DEATH message : an ExitMsg signal

HOW TO:
========

Extends the Vertex class . Remember generate the graph.dat file with specifed format. Like this : 
             How many vertices : numOfVertices
             Vertices:  v_id : value (repeat numOfVertices lines , v_id starts from 0)
             Edges : v_id -> v_id

Example:
=======

public class MaxValueVertex extends Vertex {

	@Override
	public void execute() throws Pausable {
		int incoming_value ;			
		for(Mailbox<Message> mb:this.getYellowBook()){
			mb.putnb(new InferiorMessage(State.ACTIVE,this.getValue()));
		}

		//System.out.println(Task.getCurrentTask().id() + " is working now ...");
		while (true) {
			try {
				Message msg = this.getMailbox().get();
				if (msg != null) {
					System.out.println(Task.getCurrentTask().id()+" said that I get a message and it's content is "+ msg.toString());

					if (msg.isDeath()) {
						writeSolutions();
						break;
					}

					if (!msg.isHalt()) {
						if(this.getNowState() == State.HALT){
							this.setNowState(State.ACTIVE);
							Master.getMaster().getMailbox().putnb(Signal.red);
						}
						incoming_value = msg.toValue();
						if (incoming_value > this.getValue()) {
							this.setValue(incoming_value);
														
							for (Mailbox<Message> mb : getYellowBook()) {
								mb.putnb(new InferiorMessage(State.ACTIVE,incoming_value));
							}
							
						} else {
							for (Mailbox<Message> mb : this.getYellowBook()) {
								mb.putnb(new InferiorMessage(State.HALT));
								//this.setNowState(State.HALT);
								Master.getMaster().getMailbox().putnb(Signal.dark);
							}
						}
						//msg = null;
					} else if (msg.isHalt()
							&& this.getNowState() == State.ACTIVE) {
						this.setNowState(State.HALT);
						Master.getMaster().getMailbox().putnb(Signal.dark);
						//msg = null;
					}
				}else{
					System.out.println("sorry, there is no message for you now....");
				}
				msg = null;
				
				//System.gc();
			} catch (Pausable e) {
				e.printStackTrace();
			}

		}

	}

	private void writeSolutions() throws Pausable {
		System.out.println(Task.getCurrentTask().id()+" : "+this.getValue());
	}

}




                   

