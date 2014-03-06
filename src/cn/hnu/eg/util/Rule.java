package cn.hnu.eg.util;

/**
 * Rule 规定顶点处理消息的规则，提供一种粗粒度的消息控制模型，具体的细节控制视具体实现的算法而确定
 * 	valuechange : 当顶点的值发生变化时，通知其邻接顶点
 * 	outboundschange: 当出边发生变化时，通知其邻接顶点，这里的变化主要指，新增出边，删除已有的出边，
 * 	weightchange : 某条出边的权重发生变化时
 * */
public enum Rule {
	valuechange, outboundschange, weightchange;

	/*private Rule() {
	}

	private int id;

	private Rule(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	public void setId(int id){
		this.id = id;
	}*/
}
