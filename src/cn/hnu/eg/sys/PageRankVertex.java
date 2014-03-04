package cn.hnu.eg.sys;

import kilim.Mailbox;
import kilim.Pausable;
import kilim.Task;
import cn.hnu.eg.base.Vertex;
import cn.hnu.eg.util.Signal;
import cn.hnu.eg.util.State;

public class PageRankVertex extends Vertex {

	@Override 
	public void execute() throws Pausable{
		
		
	}

	private void printSolutions() throws Pausable {
		System.out.println(Task.getCurrentTask().id() + " : " + this.getValue());

	}

	@Override
	public void compute() {
		// TODO 自动生成的方法存根
		
	}
}
