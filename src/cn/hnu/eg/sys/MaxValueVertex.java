package cn.hnu.eg.sys;

import cn.hnu.eg.base.Vertex;
import cn.hnu.eg.util.Signal;
import cn.hnu.eg.util.State;
import kilim.Mailbox;
import kilim.Pausable;
import kilim.Task;


/**
 * @author dodoro
 * @date 2013-11-12
 * guidline : 
 *
 */
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
