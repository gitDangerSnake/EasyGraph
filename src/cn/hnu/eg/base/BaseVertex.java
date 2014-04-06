package cn.hnu.eg.base;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.LinkedList;

import cn.hnu.eg.sys.Master;
import cn.hnu.eg.util.EGConstant;
import cn.hnu.eg.util.Signal;
import cn.hnu.eg.util.SignalEvent;
import cn.hnu.eg.util.SignalListener;
import cn.hnu.eg.util.State;
import cn.hnu.eg.Exceptions.IllegalDataException;
import cn.hnu.eg.Message.*;
import cn.hnu.eg.ds.Edge;
import kilim.Mailbox;
import kilim.Pausable;
import kilim.Task;

public abstract class BaseVertex extends Task implements Serializable {

	private static final long serialVersionUID = -6236420676620350939L;

	// 接收来自Master的控制信息
	private Mailbox<Signal> orders = new Mailbox<Signal>(5);
	// 当前顶点的标识
	private int id;
	// 顶点当前的值
	private double val;
	
	
	private double old_val;
	// 超级步
	private int superstep = 0;

	public static int totalVertices = 0;

	// 出边邻接表，入边邻接表
	private List<Edge> outEdges = new LinkedList<Edge>();
	private List<Edge> inEdges = new LinkedList<Edge>();

	private SignalListener sl = Master.getMaster();

	// 当前顶点的状态
	private State currentstate = State.ACTIVE;

	/** getters / setters */
	public double getOld_val() {
		return old_val;
	}

	public void setOld_val(double old_val) {
		this.old_val = old_val;
	}


	public int getId() {
		return this.id;
	}

	public double getVal() {
		return val;
	}

	public void setVal(double val) {
		this.val = val;
	}

	public Mailbox<Signal> getOrders() {
		return orders;
	}

	public void setOrders(Mailbox<Signal> orders) {
		this.orders = orders;
	}

	public int getSuperstep() {
		return this.superstep;
	}

	public void setSuperstep(int s) {
		this.superstep = s;
	}

	public List<Edge> getOutBounds() {
		return this.outEdges;
	}

	public List<Edge> getInBounds() {
		return this.inEdges;
	}

	public void addOutEdge(Edge e) {
		this.outEdges.add(e);
	}

	public void addInEdge(Edge e) {
		this.inEdges.add(e);
	}

	public State getCurrentstate() {
		return this.currentstate;
	}

	public void setCurrentstate(State state) {
		this.currentstate = state;
	}

	// end of getter and setter

	// 初始化顶点
	public void init(int id, double val) {
		this.id = id;
		this.val = val;
		totalVertices++;
	}

	public void init(int id) {
		this.id = id;
		totalVertices++;
	}

	public void init(String str, String idSperatorId, String idSperatorValue,
			boolean flag/* if is weighted graph */) throws IllegalDataException {
		String[] vcontent = str.split(idSperatorId);
		String[] currentContent = null;
		if (vcontent[0].contains(idSperatorValue)) {
			currentContent = vcontent[0].split(idSperatorValue);

			if (Integer.valueOf(currentContent[0]) < 0) {
				throw new IllegalDataException("vertex id cannot be negative");
			}
			init(Integer.valueOf(currentContent[0]),
					Double.valueOf(currentContent[1]));
		} else {

			if (Integer.valueOf(vcontent[0]) < 0)
				throw new IllegalDataException("vertex id cannot be negative");
			init(Integer.valueOf(vcontent[0]));
		}

		Edge e = null;

		for (int i = 1; i < vcontent.length; i++) {
			if (vcontent[i].contains(idSperatorValue)) {
				currentContent = vcontent[i].split(idSperatorValue);
				if (Integer.valueOf(currentContent[i]) < 0)
					throw new IllegalDataException(
							"vertex id cannot be negative");
				e = new Edge(this.id, Integer.valueOf(currentContent[0]),
						Double.valueOf(currentContent[i]));
			} else {
				if (Integer.valueOf(currentContent[i]) < 0)
					throw new IllegalDataException(
							"vertex id cannot be negative");

				e = new Edge(this.id, Integer.valueOf(vcontent[i]));
			}

			addOutEdge(e);
		}

		totalVertices++;

	}

	public abstract void compute(Message msg);

	public abstract void sendMsgs();

	public abstract void initComppute();

	@Override
	public void execute() throws Pausable {
		boolean busy = false;
		Signal signal = orders.get();
		// System.out.println(Thread.currentThread().getName() + signal);
		if (signal == Signal.red) {
			running = true;
			initComppute(); // 主要用来进行第一个超级步时的初始化
			notifyMaster(Signal.green);
		}

		Message msg = null;
		while (running) {
			signal = orders.get();
			if (signal == Signal.dark) {
				running = false;
				break;
			} else if (signal == Signal.red) {
				superstep++;
				for (Edge e : inEdges) {
					msg = e.getMessage();
					e.setMessage(null);
					if (msg != null) {
						busy = true;
						compute(msg);
					}
				}
				writeSolution();
				sendMsgs();
				if (busy) {
					busy = false;
					notifyMaster(Signal.green);
				} else {
					notifyMaster(Signal.dark);
				}
			}
		}
	}

	public void writeSolution() {
		String path = EGConstant.SolutionPath + superstep ;
		File dir = new File(path);
		if(dir.exists()){
			File file = new File(dir,id+".dat");
			System.out.println(file);
			if(file.exists()) file.delete();
			try {
				if (file.createNewFile()) {
					BufferedWriter bw;
					try {
						bw = new BufferedWriter(new FileWriter(file));
						bw.write(id + ":" + val);
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void notifyMaster(Signal s) throws Pausable {
		Master.getMaster().getSlot().put(s);

	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(id + ":" + val + " ");
		for (Edge e : outEdges) {
			sb.append(e.toString()).append(" ");
		}
		return sb.toString();

	}

}
