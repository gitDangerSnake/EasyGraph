package examples;

public class TestConcurrencyWithJava implements Runnable{
	
	private int thread_id;
	
	

	public TestConcurrencyWithJava(int thread_id) {
		super();
		this.thread_id = thread_id;
	}

	public static void main(String[] args) {
		
		long begin = System.currentTimeMillis();
		if(args.length != 1){
			System.out.println("usage: Java filename parameter");
		}
		
		int num = Integer.parseInt(args[0]);
		for(int i=0;i<num;i++){
			TestConcurrencyWithJava test = new TestConcurrencyWithJava(i);
			Thread t = new Thread(test);
			t.start();
		}
		
		long end = System.currentTimeMillis();
		
		System.out.println(end - begin);

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		for(int i=0;i<100;i++){
			//Task.sleep();
		}
		
		System.out.println("Task "+thread_id+" is compeleted!");
	}

}
