package cn.hnu.eg.sys;

import cn.hnu.eg.base.Vertex;
import cn.hnu.eg.ds.Graph;
import cn.hnu.eg.util.EGConstant;

public class Service {

	public static void main(String[] args) {
		Graph graph = new Graph(EGConstant.vertexClassName,EGConstant.GraphFilePath,EGConstant.idSperatorValue,EGConstant.idSperatorId);
		
		
		System.out.println("now start vertices");
		for (Integer i : graph.getChunk().asMap().keySet()) {
			graph.getChunk().asMap().get(i).start();
		}
		
		System.out.println("now start master");
		Master.getMaster().start();
		
	}

}
