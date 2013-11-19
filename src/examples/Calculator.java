package examples;

import java.math.RoundingMode;
import kilim.Mailbox;
import kilim.Pausable;
import kilim.Task;

public class Calculator extends Task{
	
	private Mailbox<Calculation> mailbox;
	
	public Calculator(Mailbox<Calculation> mailbox){
		super();		
		this.mailbox = mailbox;
	}
	
	@Override
	
	public void execute() throws Pausable{
		while (true) {
			Calculation calculation = mailbox.get();
			if(calculation.getAnswer() == null){
				calculation.setAnswer(calculation.getDividend().divide(calculation.getDivisor(),8,RoundingMode.HALF_UP));
				System.out.println("Calculator determined answer");
				
				mailbox.putnb(calculation);
			}
			Task.sleep(1000);
		}
		
		
	}
	
}
