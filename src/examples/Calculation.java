package examples;
import java.math.BigDecimal;

public class Calculation {
	private BigDecimal dividend;
	private BigDecimal divisor;
	private BigDecimal answer;
	public Calculation(BigDecimal dividend, BigDecimal divisor) {
		super();
		this.dividend = dividend;
		this.divisor = divisor;
	}
	public BigDecimal getAnswer() {
		return answer;
	}
	public void setAnswer(BigDecimal answer) {
		this.answer = answer;
	}
	public BigDecimal getDividend() {
		return dividend;
	}
	public BigDecimal getDivisor() {
		return divisor;
	}
	
	public String printAnswer() {
		 return "The answer of " + dividend + " divided by " + divisor + " is " + answer;
	}
	
	
	
	
	
	
}
