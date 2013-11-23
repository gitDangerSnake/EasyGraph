package cn.hnu.eg.sys;

import cn.hnu.eg.base.Vertex;
import cn.hnu.eg.util.Signal;
import cn.hnu.eg.util.State;
import kilim.ExitMsg;
import kilim.Mailbox;
import kilim.Pausable;
import kilim.Task;

/**
 * @author dodoro
 * @date 2013-11-12 guidline :
 * 
 */
public class MaxValueVertex extends Vertex {

	@Override
	public void execute() throws Pausable {	
		//System.out.println(Task.getCurrentTask().id() + " is running.....");

		while (true) {			
			//get orders from master			
			SupervisorMessage msg = this.getOrders().get();
			if(msg.isStart()){//tinystep 0 send messages at will
				for(Mailbox<Message> mb : getYellowBook()){
					mb.put(new InferiorMessage(State.ACTIVE,getValue()));
				}
				
				Master.getMaster().getStepbox().putnb(Signal.green);					
				
			}else if(msg.isDeath()){ // end compute
				writeSolutions();break;
			}else{//compute
				int old_value = this.getValue();
				while(this.getMailbox().hasMessage()){
					Message amsg = this.getMailbox().get();
					//System.out.println("got a message and it's value is " + amsg.toString());
					if(amsg.isActive()){
						this.setNowState(State.ACTIVE);
						if(this.getValue() < amsg.toValue())
							this.setValue(amsg.toValue());
					}else if(this.getNowState() == State.ACTIVE && amsg.isHalt()){
						this.setNowState(State.HALT);
					}
				}
				
				
				//send message among its adj vertices
				if(this.getValue() != old_value){
					for(Mailbox<Message> mb : getYellowBook()){
						mb.put(new InferiorMessage(State.ACTIVE,getValue()));
					}
				}else{
					for(Mailbox<Message> mb : getYellowBook()){
						mb.put(new InferiorMessage(State.HALT));
					}
				}
				
				//report to master with two messages
				//1. its state 2.mission over
				
				Master.getMaster().getMailbox().putnb(this.getNowState() == State.ACTIVE ? Signal.red : Signal.dark);
				Master.getMaster().getStepbox().putnb(Signal.green);
				
			}
			
		}

		//this.informOnExit(this.getExitmb());

		//Task.exit(0);
	}

	private void writeSolutions() throws Pausable {
		System.out
				.println(Task.getCurrentTask().id() + " : " + this.getValue());
	}

}
