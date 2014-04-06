package cn.hnu.eg.sys;

import java.io.*;
import java.util.Arrays;
import java.util.regex.Pattern;

import cn.hnu.eg.Exceptions.IllegalDataException;
import cn.hnu.eg.base.BaseVertex;
import cn.hnu.eg.ds.Edge;

public class GraphLoader {

	private Graph graph;

	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	public GraphLoader(Graph graph, String filename, String vertexClassName) {
		this.graph = graph;
		loadGraph(filename,vertexClassName);
	}

	@SuppressWarnings("unchecked")
	private void loadGraph(String filename, String vertexClassName) {
		Class<BaseVertex> cls = null;
		try {
			cls = (Class<BaseVertex>) Class.forName(vertexClassName);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		File f = new File(filename);
		if (!f.exists()) {
			System.err.println("File not exist !");
			System.exit(-1);
		}

		Pattern p = Pattern.compile("\\s");
		BufferedReader br = null;
		String[] parts = null;
		Edge e = null;
		BaseVertex obj_s_id = null;
		BaseVertex obj_d_id = null;
		BaseVertex v = null;
		try {
			br = new BufferedReader(new FileReader(filename));
			String line = null;
			while ((line = br.readLine()) != null) {
				parts = p.split(line);
				if (parts.length != 2) {
					System.out.println(Arrays.toString(parts));
					throw new IllegalDataException("Illegal data format");
				}
				int s_id = Integer.valueOf(parts[0]);
				int d_id = Integer.valueOf(parts[1]);
				e = (Edge) graph.geteMap().get(eHash(s_id, d_id));
				if (e == null) {
					e = new Edge(s_id, d_id);
					graph.geteMap().put(eHash(s_id, d_id), e);
				}
				obj_s_id = (BaseVertex) graph.getvMap().get(s_id);
				obj_d_id = (BaseVertex) graph.getvMap().get(d_id);
				if (obj_s_id != null) {
					obj_s_id.addOutEdge(e);
				} else {
					v = (BaseVertex) cls.newInstance();
					v.init(s_id);
					v.addOutEdge(e);
					graph.getvMap().put(s_id, v);
				}
				if (obj_d_id != null) {
					obj_d_id.addInEdge(e);
				} else {
					v = (BaseVertex) cls.newInstance();
					v.init(d_id);
					v.addInEdge(e);
					graph.getvMap().put(d_id, v);
				}

			}
			
			System.out.println("load finished!");
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	private int eHash(int s_id, int d_id) {
		return (s_id + "->" + d_id).hashCode();
	}

}
