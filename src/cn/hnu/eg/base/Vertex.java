package cn.hnu.eg.base;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import cn.hnu.eg.Exceptions.IllegalDataException;
import cn.hnu.eg.ds.Edge;
import cn.hnu.eg.sys.InferiorMessage;
import cn.hnu.eg.sys.Master;
import cn.hnu.eg.sys.Message;
import cn.hnu.eg.sys.SupervisorMessage;
import cn.hnu.eg.util.EasyGraphConstant;
import cn.hnu.eg.util.Signal;
import cn.hnu.eg.util.State;
import kilim.ExitMsg;
import kilim.Mailbox;
import kilim.Pausable;
import kilim.Task;

/**
 * @author dodoro
 * @date 2013-11-11 abstract class Vertex extends Task @see {@Task}
 * 
 *       Each Vertex is taken as a task and scheduled separately , like the
 *       actor in the scala or Erlang Usage: public class XXXVertex<T> extends
 *       Vertex<T>{
 * @Override public void compute(){ // your implements } }
 */
@SuppressWarnings("hiding")
public abstract class Vertex<T extends Number> extends Task implements Serializable{

	/**
	 * store the directions from the master
	 * */
	private Mailbox<SupervisorMessage> orders = new Mailbox<SupervisorMessage>();

	/**
	 * store the messages from its in-bound edge vertices
	 * */
	private Mailbox<Message> mailbox = new Mailbox<Message>();

	/**
	 * receive exit message
	 **/
	private Mailbox<ExitMsg> exitmb = new Mailbox<ExitMsg>();

	
	private int v_id;
	private T value;

	
	private int superstep;

	/**
	 * adjacency vertices of out-bound edges
	 * 
	 * @deprecated yelloBook
	 * */
	private List<Mailbox<Message>> yellowBook = new LinkedList<Mailbox<Message>>();

	/**
	 * adjacency vertices of out-bound edges @see {@Edge}
	 * */
	private List<Edge> adjs = new LinkedList<Edge>();

	/**
	 * all the vertices is actived until the end of a super step , become HALT @see
	 * {@State} In the next super step if there are messages for the
	 * current vertex , the vertex is actived
	 * */
	private State nowState = State.ACTIVE;

	
	/**
	 * init a vertex
	 * 
	 * @param vid
	 *            : id of the vertex
	 * @param value
	 *            : data stored in the current vertex
	 * 
	 * */
	public void init(int vid, T value) {
		this.v_id = vid;
		this.value = value;
		this.superstep = 0;
	}

	public void init(int vid) {
		this.v_id = vid;
		this.superstep = 0;
	}

	/**
	 * init a vertex from a string like
	 * 
	 * @param str
	 *            : { id:value->id->id->id } or {id:value->id:value->id:value}
	 *            (the current vertex id):(the current vertex value)->(adjacency
	 *            vertex)->(adjacency vertex).... 
	 *            
	 *             (the current vertex id):(the
	 *            current vertex value)->(adjacency vertex):(edge
	 *            weight)->(adjacency vertex):(edge weight)....
	 * */
	public void init(String str) throws IllegalDataException {
		/**
		 * split the string with '->' the first part data used to init the
		 * current vertex's id and value the rest parts of the array are used to
		 * init the adjs
		 * */
		String[] vcontent = str.split(EasyGraphConstant.idSperatorId);

		String[] currentContent;

		// if the first part contains id:value
		if (vcontent[0].contains(":")) {
			currentContent = vcontent[0]
					.split(EasyGraphConstant.idSperatorValue);
			if (Integer.valueOf(currentContent[0]) < 0)
				throw new IllegalDataException("Id can't be negative");
			// TO DO : data adapter
			// init(Integer.valueOf(currentContent[0]),new
			// Number(currentContent[1]));
		} else {
			if (Integer.valueOf(vcontent[0]) < 0)
				throw new IllegalDataException("Id can't be negative");
			init(Integer.valueOf(vcontent[0]));
		}

		// init the adj edges
		Edge e = null;
		for (int i = 1; i < vcontent.length; i++) {
			if (vcontent[i].contains(EasyGraphConstant.idSperatorValue)) {
				currentContent = vcontent[i]
						.split(EasyGraphConstant.idSperatorValue);
				if (Integer.valueOf(currentContent[0]) < 0)
					throw new IllegalDataException("Id can't be negative");
				e = new Edge(this.v_id, Integer.valueOf(currentContent[0]),
						Double.valueOf(currentContent[1]));
			} else {
				if (Integer.valueOf(vcontent[i]) < 0)
					throw new IllegalDataException("Id can't be negative");
				e = new Edge(this.v_id, Integer.valueOf(vcontent[i]));
			}
			this.adjs.add(e);
		}

	}

	/**
	 * @return return current vertex's mailbox
	 * */
	public Mailbox<Message> getMailbox() {
		return mailbox;
	}

	/**
	 *  set current vertex's mailbox
	 *  @param mailbox :
	 * */
	public void setMailbox(Mailbox<Message> mailbox) {
		this.mailbox = mailbox;
	}

	/**
	 * @return return exit mailbox
	 * */
	public Mailbox<ExitMsg> getExitmb() {
		return exitmb;
	}

	public void setExitmb(Mailbox<ExitMsg> exitmb) {
		this.exitmb = exitmb;
	}

	/**
	 * 	@return return current vertex id
	 * */
	public int getV_id() {
		return v_id;
	}	
	
	/**
	 * @return return current vertex stored data
	 * */

	public T getValue() {
		return value;
	}

	/**
	 *  set super step of the current vertex
	 *  @param superstep : current super step
	 * */
	public void setSuperstep(int superstep) {
		this.superstep = superstep;
	}

	/**
	 * @return return current super step
	 * */
	public int getSuperstep() {
		return superstep;
	}

	/**
	 *  set value of the current vertex
	 *  @param value : new value
	 * */
	public void setValue(T value) {
		this.value = value;
	}

	/**
	 * @deprecated
	 * */
	public List<Mailbox<Message>> getYellowBook() {
		return yellowBook;
	}
	/**
	 * @deprecated
	 * */
	public void setYellowBook(List<Mailbox<Message>> yellowBook) {
		this.yellowBook = yellowBook;
	}

	/**
	 * @return return current vertex adj list
	 * */
	public List<Edge> getOutbounds() {
		return adjs;
	}

	/**
	 * set adj list
	 * @param outbounds
	 * */
	public void setOutbounds(List<Edge> outbounds) {
		this.adjs = outbounds;
	}

	/**
	 * @return get current vertex state
	 * */
	public State getNowState() {
		return nowState;
	}

	/**
	 * set state
	 * @param nowState 
	 * */
	public void setNowState(State nowState) {
		this.nowState = nowState;
	}
	
	@Override
	public void execute() throws Pausable{
		while(mailbox.hasMessage()){
			SupervisorMessage msg = orders.get();
			
			if(msg.isStart() /**this only happens in super step 0**/){
				send();
			}else if(msg.isDeath()/**this only happens when the computation over*/){
				writeSolution();
				break;
			}else{
				compute();
			}
		}
	}
	
	private void writeSolution() {
		// TODO 自动生成的方法存根
		
	}

	private void send() {
		for(Edge e : adjs){
			int t_id = e.getD_id();
			/*if(currentChunk.contains(t_id)){
				
			}*/
		}
		
	}

	/**
	 * abstract method compute() 
	 * when extends this class , programmer should implements this method
	 * */
	public abstract void compute();

	
	public void notifyMaster(Signal s){
		Master.getMaster().getMailbox().putnb(s);
	}
	/**
	 * for test purpose 
	 * */
	@Override
	public String toString() {
		//orders
		
		return v_id + ":" + value;
	}


}
