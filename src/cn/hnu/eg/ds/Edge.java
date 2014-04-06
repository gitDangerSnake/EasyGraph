package cn.hnu.eg.ds;

import cn.hnu.eg.Message.Message;
import cn.hnu.eg.util.EGConstant;

public class Edge {
	private int s_id; // id of the source vertex
	private int d_id; // id of the destination vertex
	private double weight; // weight of the edge
	private Message msg = null;

	public Message getMessage() {
		return msg;
	}

	public void setMessage(Message msg) {
		this.msg = msg;
	}

	/**
	 * initialize an edge
	 * @param s_id : source vertex id
	 * @param d_id : destination vertex id
	 * */
	public Edge(int s_id, int d_id) {
		this.s_id = s_id;
		this.d_id = d_id;
	}
	
	/**
	 * initialize an edge
	 * @param s_id : source vertex id
	 * @param d_id : destination vertex id
	 * @param value : the weight of the edge
	 * */
	public Edge(int s_id, int d_id , double value) {
		this.s_id = s_id;
		this.d_id = d_id;
		this.weight = value;
	}

	/**
	 * @return return source vertex id
	 * */
	public int getS_id() {
		return s_id;
	}

	/**
	 * set source vertex id
	 * */
	public void setS_id(int s_id) {
		this.s_id = s_id;
	}
	/**
	 * @return return destination vertex id
	 * */
	public int getD_id() {
		return d_id;
	}
	/**
	 * set destination vertex id
	 * */
	public void setD_id(int d_id) {
		this.d_id = d_id;
	}
	/**
	 * @return return weight of the current edge
	 * */
	public double getWeight() {
		return weight;
	}

	/**
	 * 	set weight of the current edge
	 * */
	public void setWeight(double weight) {
		this.weight = weight;
	}
	

	/**
	 * override object tostring method
	 * */
	@Override
	public String toString() {
		return s_id + "->" + d_id + ":(" +weight+")" + " with message " +msg; 
	}

}
