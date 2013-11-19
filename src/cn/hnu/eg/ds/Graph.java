package cn.hnu.eg.ds;

import java.util.LinkedList;
import java.util.List;
import java.io.*;

import kilim.Mailbox;
import cn.hnu.eg.base.*;
import cn.hnu.eg.sys.Message;
import cn.hnu.eg.util.EasyGraphConstant;


/**
 * @author dodoro
 * @date 2013-11-11
 * Graph maintains the basic data structure of the graph 
 */
public class Graph {

	private String filename;
	private List<Vertex> listOfVertices;	
	private String vertexClassName;
	private int numOfVertices;
	
	public Graph(String vertexClassName,String filename){
		init(vertexClassName, filename);
	}
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public List<Vertex> getListOfVertices() {
		return listOfVertices;
	}

	public void setListOfVertices(List<Vertex> listOfVertices) {
		this.listOfVertices = listOfVertices;
	}

	public String getVertexClassName() {
		return vertexClassName;
	}

	public void setVertexClassName(String vertexClassName) {
		this.vertexClassName = vertexClassName;
	}
	
	public int getNumOfVertices() {
		return numOfVertices;
	}

	public void setNumOfVertices(int numOfVertices) {
		this.numOfVertices = numOfVertices;
	}


	/*
	 * file format:
	 * 1.number of vertices
	 * 2.vertices with values, one vertex per line like vertex_id : vertex_value
	 * 3.Edges between vertices v_id -> v_id
	 * 
	 * @param vertexClassName specify the subclass file of Vertex
	 * @param filename specify the location of the graph data file
	 * 
	 * @function : load the graph from disk into main memory
	 * 
	 * @author : dodoro
	 * @date : 2013-11-11
	 * */	
	
	public void init(String vertexClassName,String filename){
		this.setFilename(filename);
		this.setVertexClassName(vertexClassName);
		listOfVertices = new LinkedList<Vertex>();
		BufferedReader buffer = null;
		try {
			buffer = new BufferedReader(new FileReader(filename));
			int numOfVertices = Integer.valueOf(buffer.readLine());
			this.setNumOfVertices(numOfVertices);
			String line = null;
			int count = 0;
			while((line=buffer.readLine())!=null && count != numOfVertices){
				Vertex v = (Vertex) Class.forName(vertexClassName).newInstance();
				String[] elems = line.split(EasyGraphConstant.idSperatorValue);
				int v_id = Integer.valueOf(elems[0]);
				int v_val = Integer.valueOf(elems[1]);
				v.init(v_id, v_val);
				listOfVertices.add(v);
				count++;
			}
			
			for(Vertex v : listOfVertices){
				System.out.println(v.toString());
			}
			
			while((line=buffer.readLine())!=null){				
				String[] edgeElems = line.split(EasyGraphConstant.idSperatorId);
				int s_id = Integer.valueOf(edgeElems[0]);
				int e_id = Integer.valueOf(edgeElems[1]);
				
				listOfVertices.get(s_id).getYellowBook().add(listOfVertices.get(e_id).getMailbox());
				/*
				Mailbox<Message> tmp_mailbox = listOfVertices.get(e_id).getMailbox();
				assert(tmp_mailbox!=null);
				System.out.println(tmp_mailbox);
				List<Mailbox<Message>> tmp_yellowbook = listOfVertices.get(s_id).getYellowBook();
				assert(tmp_yellowbook!=null);
				System.out.println(tmp_yellowbook);
				tmp_yellowbook.add(tmp_mailbox);
				*/
			}
		} catch (NumberFormatException | IOException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {			
			e.printStackTrace();
		}finally{
			try {
				buffer.close();
			} catch (IOException e) {				
				e.printStackTrace();
			}
			if(buffer!=null){
				buffer = null;
			}
		}
	}
	

	public int size() {		
		return numOfVertices;
	}

}
