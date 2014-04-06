package cn.hnu.eg.sys;

import cn.hnu.eg.Message.Message;
import cn.hnu.eg.base.BaseVertex;
import cn.hnu.eg.ds.Edge;

public class PageRankVertex extends BaseVertex {

	@Override
	public void compute(Message msg) {
		double sum = 0;
		for (Edge e : this.getInBounds()) {
			if (e.getMessage() != null) {
				sum += (double) e.getMessage().getVal();
			}
		}
		double newPageRank = 0.15 / totalVertices + 0.85 * sum;
		this.setOld_val(this.getVal());
		this.setVal(newPageRank);
	}

	@Override
	public void sendMsgs() {
		if (Math.abs(this.getVal() - this.getOld_val()) >= 0.0001) {
			for (Edge e : this.getOutBounds()) {
				e.setMessage(new Message(this.getVal()
						/ this.getOutBounds().size()));
			}
		}
	}

	@Override
	public void initComppute() {
		for (Edge e : this.getOutBounds()) {
			e.setMessage(new Message(0.0));
		}
	}

}
