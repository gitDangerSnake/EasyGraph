package examples;

import kilim.Pausable;
import kilim.Task;

public class TestConcurrencyWithKilim extends Task{
	
	private int task_id;
	
	public TestConcurrencyWithKilim(int id){
		task_id = id;
	}

	public static void main(String[] args) {
		
		long begin = System.currentTimeMillis();
		if(args.length != 1){
			System.out.println("usage : java filename numoftasks");
			return;
		}
		
		int num = Integer.parseInt(args[0]);
		
		for(int i=0;i<num;i++){
			TestConcurrencyWithKilim test = new TestConcurrencyWithKilim(i);
			test.start();
		}
long end = System.currentTimeMillis();
		
		System.out.println(end - begin);
	}
	
	@Override
	public void execute() throws Pausable{
		for(int i=0;i<100;i++){
			//Task.sleep();
		}
		
		System.out.println("Task "+task_id+" is compeleted!");
	}

}
