package examples;

import kilim.Mailbox;
import kilim.Task;

public class TestCalculator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Mailbox<Calculation> mailbox = new Mailbox<Calculation>();
		
		Task deferredTask = new DefferedDivision(mailbox);
		Task calculator = new Calculator(mailbox);
		
		deferredTask.start();
		calculator.start();
	}

}
