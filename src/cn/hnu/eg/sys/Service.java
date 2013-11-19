package cn.hnu.eg.sys;

import cn.hnu.eg.base.Vertex;
import cn.hnu.eg.ds.Graph;
import cn.hnu.eg.util.EasyGraphConstant;

public class Service {

	public static void main(String[] args) {
		Graph graph = new Graph(EasyGraphConstant.vertexClassName,EasyGraphConstant.GraphFilePath);		
		Master master = Master.getMaster();
		master.setGraph(graph);
		
		master.start();
		for(Vertex v : graph.getListOfVertices()){
			v.start();
		}
		
	}

}
