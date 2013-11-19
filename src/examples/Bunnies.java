package examples;

public class Bunnies {
	static int count = 0;
	Bunnies(){
		while(count < 10){
			new Bunnies(++count);
		}
	}
	
	Bunnies(int x){
		super();
	}
	
	public int gcd(int a, int b){
		if(a == 0) return b;
		int r = b % a;
		return gcd(r,a);
	}
	public static void main(String[] args) {
	  
	 int c = new Bunnies().gcd(20, 5);
	 new Bunnies(count);
	 System.out.println(count++);
	 System.out.println(count);
	 System.out.println(c);
	 
	 
	}

}
