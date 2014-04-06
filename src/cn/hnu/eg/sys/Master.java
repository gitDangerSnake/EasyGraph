package cn.hnu.eg.sys;

import java.io.File;
import java.nio.channels.FileChannel;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import kilim.Mailbox;
import kilim.Pausable;
import kilim.Task;
import cn.hnu.eg.base.BaseVertex;
import cn.hnu.eg.util.EGConstant;
import cn.hnu.eg.util.Signal;
import cn.hnu.eg.util.SignalEvent;
import cn.hnu.eg.util.SignalListener;

public class Master extends Task implements SignalListener {

	private Mailbox<Signal> slot = new Mailbox<Signal>(100);
	private int stepover = 0;
	private int over = 0;
	private int superstep = 0;
	private GraphLoader loader;

	public GraphLoader getLoader() {
		return loader;
	}

	public void setLoader(GraphLoader loader) {
		this.loader = loader;
	}

	private Master() {
	}

	public Mailbox<Signal> getSlot() {
		return slot;
	}

	public int getSuperstep() {
		return superstep;
	}

	public void setSuperstep(int superstep) {
		this.superstep = superstep;
	}

	

	public boolean isOver() throws Pausable {
		/*
		 * System.out.println(graph == null); return over.intValue() ==
		 * graph.num() ? true : false;
		 */
		/*
		 * return (Math.abs(graph.num() - over.intValue()) * 1.0) / graph.num()
		 * < 0.0001 ? true : false;
		 */

		return over == loader.getGraph().num() ? true : false;

	}

	public boolean isStepOver() {
		/* return stepover.intValue() == graph.num() ? true : false; */
		/*
		 * return (Math.abs(graph.num() - stepover.intValue()) * 1.0) /
		 * graph.num() < 0.0001 ? true : false;
		 */
		
		return stepover == loader.getGraph().num() ? true : false;
	}

	public void check() throws Pausable{
		//System.out.println("有没有信号？");
		Signal s = slot.get();
			stepover++;
		if(s == Signal.dark){
			over++;
		}
		//System.out.println("当前超级步有"+ stepover + "个已经完成" + "");
	}
	@Override
	public void execute() throws Pausable {
		while (!isOver()) {
			over = 0;
			String path = EGConstant.SolutionPath  + superstep+File.separator;
			File f = new File(path);
			if(f.exists()){
				f.delete();
			}
			if(f.mkdir()){
				sendOrders(Signal.red);
			}
			
			while (!isStepOver()) {
				check();
			}
			stepover = 0;
			superstep++;
			System.out.println(superstep);
		}
		System.out.println("OK, it's time to finish this!");

	}

	public void sendOrders(Signal signal) throws Pausable {
		System.out.println(loader.getGraph().getvMap().keySet().size());
		for (Integer i : loader.getGraph().getvMap().keySet()) {
			loader.getGraph().getvMap().get(i).getOrders().putnb(signal);
		}

	}

	private static class MasterHolder {
		public static Master master = new Master();
	}

	public static Master getMaster() {
		return MasterHolder.master;
	}

	@Override
	public void onEvent(SignalEvent se) {
				
	}


}
