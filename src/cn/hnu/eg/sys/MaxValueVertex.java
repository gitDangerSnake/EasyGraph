package cn.hnu.eg.sys;

import cn.hnu.eg.base.BaseVertex;
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
public class MaxValueVertex extends BaseVertex {

	@Override
	public void execute() throws Pausable {	
		//System.out.println(Task.getCurrentTask().id() + " is running.....");

		while (this.getMailbox().hasMessage()) {		
			//get orders from master			
			SupervisorMessage msg = (SupervisorMessage) this.getOrders().get();
			if(msg.isStart()){//tinystep 0 send messages at will
				/*for(Mailbox<Message> mb : getYellowBook()){
					mb.put(new InferiorMessage(State.ACTIVE,getValue()));
				}*/
				this.send();
				
				Master.getMaster().getStepbox().putnb(Signal.green);					
				
			}else if(msg.isDeath()){ // end compute
				writeSolutions();break;
			}else{//compute
				double old_value = this.getVal();
				while(this.getMailbox().hasMessage()){
					Message amsg = this.getMailbox().get();
					//System.out.println("got a message and it's value is " + amsg.toString());
					if(amsg.isActive()){
						this.setCurrentstate(State.ACTIVE);
						if(this.getVal() < amsg.toValue())
							this.setVal(amsg.toValue());
					}else if(this.getCurrentstate() == State.ACTIVE && amsg.isHalt()){
						this.setCurrentstate(State.HALT);
					}
				}
				
				
				//send message among its adj vertices
				if(this.getVal() != old_value){
					for(Mailbox<Message> mb : getYellowBook()){
						mb.put(new InferiorMessage(State.ACTIVE,getVal()));
					}
				}else{
					for(Mailbox<Message> mb : getYellowBook()){
						mb.put(new InferiorMessage(State.HALT));
					}
				}
				
				//report to master with two messages
				//1. its state 2.mission over
				
				Master.getMaster().getMailbox().putnb(this.getCurrentstate() == State.ACTIVE ? Signal.red : Signal.dark);
				Master.getMaster().getStepbox().putnb(Signal.green);
				
			}
			
		}
		
		//processing exit transaction
		

		//this.informOnExit(this.getExitmb());

		//Task.exit(0);
	}

	private void writeSolutions() throws Pausable {
		System.out
				.println(Task.getCurrentTask().id() + " : " + this.getVal());
	}

	@Override
	public void compute() {
		// TODO Auto-generated method stub
		
	}

}
