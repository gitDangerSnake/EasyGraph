package cn.hnu.eg.util;

import cn.hnu.eg.ds.Edge;

public enum Rule {
	valuechange , outboundschange , inboundschange , weightchange ;
	
	private Rule(){}
	private int id;
	private Rule(int id){
		this.id = id;
	}
	
	public int getId(){
		return id;
	}
}
