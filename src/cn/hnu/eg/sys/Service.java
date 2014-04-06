package cn.hnu.eg.sys;

import cn.hnu.eg.base.BaseVertex;
import cn.hnu.eg.util.EGConstant;

public class Service {

	public static void main(String[] args) throws InterruptedException {
		Graph graph = new Graph();
		System.out.println(graph.num());
		GraphLoader loader = new GraphLoader(graph,EGConstant.GraphFilePath,EGConstant.vertexClassName);
		System.out.println(graph.num());
		BaseVertex bv = null;
		System.out.println("now start master");
		Master m = Master.getMaster();
		m.setLoader(loader);
		m.start();
		for(Integer i : graph.getvMap().keySet()){
			bv = (BaseVertex)graph.getvMap().get(i);
			bv.start();
		}
		Thread.sleep(1000);
	}

}
