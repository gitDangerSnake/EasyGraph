package examples;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;
import java.util.Random;

import kilim.Mailbox;
import kilim.Pausable;
import kilim.Task;

public class DefferedDivision extends Task{
	
	private Mailbox<Calculation> mailbox;
	
	public DefferedDivision(Mailbox<Calculation> mailbox){
		super();
		this.mailbox = mailbox;
	}
	
	@Override
	public void execute() throws Pausable{
		Random numbeRandom = new Random(new Date().getTime());
		
		MathContext context = new MathContext(8);
		
		while(true){
			System.out.println("I need to know the answer of something");
			
			mailbox.putnb(new Calculation(
					new BigDecimal(numbeRandom.nextDouble(),context),
					new BigDecimal(numbeRandom.nextDouble(),context)));
			
			Task.sleep(1000);
			
			Calculation answer = mailbox.getnb();
			
			if(answer != null && answer.getAnswer()!=null){
				System.out.println("Answer is:"+answer.printAnswer());
			}
		}
	}
	

}
