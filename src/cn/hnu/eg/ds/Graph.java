package cn.hnu.eg.ds;

import java.io.*;

import cn.hnu.eg.Exceptions.IllegalDataException;
import cn.hnu.eg.base.*;
import cn.hnu.eg.cache.Chunk;

/**
 * @author dodoro
 * @date 2013-11-11 Graph maintains the basic data structure of the graph
 */
public class Graph {

	private String filename;
	//private List<BaseVertex> listOfVertices;
	private String vertexClassName;
	private int numOfVertices;
	private Chunk<Integer, BaseVertex> chunk = Chunk.getChunk();

	public Graph(String vertexClassName, String filename,
			String idSperatorValue, String idSperatorId) {
		init(vertexClassName, filename, idSperatorValue, idSperatorId);
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	/*
		public List<BaseVertex> getListOfVertices() {
			return listOfVertices;
		}
	
		public void setListOfVertices(List<BaseVertex> listOfVertices) {
			this.listOfVertices = listOfVertices;
		}
	*/

	public String getVertexClassName() {
		return vertexClassName;
	}

	public void setVertexClassName(String vertexClassName) {
		this.vertexClassName = vertexClassName;
	}

	public Chunk<Integer, BaseVertex> getChunk(){
		return chunk;
	}

	public void setNumOfVertices(int numOfVertices) {
		this.numOfVertices = numOfVertices;
	}
	public int size() {
		return numOfVertices;
	}

	/*
	 * file format: 1.number of vertices 2.vertices with values, one vertex per
	 * line like vertex_id : vertex_value 3.Edges between vertices v_id -> v_id
	 * : @weight
	 * 
	 * @param vertexClassName specify the subclass file of Vertex
	 * 
	 * @param filename specify the location of the graph data file
	 * 
	 * @function : load the graph from disk into main memory
	 * 
	 * @author : dodoro
	 * 
	 * @date : 2013-11-11
	 */

	public void init(String vertexClassName, String filename,
			String idSperatorValue, String idSperatorId) {
		this.setFilename(filename);
		this.setVertexClassName(vertexClassName);
		//listOfVertices = new LinkedList<BaseVertex>();
		BufferedReader buffer = null;
		try {
			buffer = new BufferedReader(new FileReader(filename));
			int numOfVertices = Integer.valueOf(buffer.readLine());
			this.setNumOfVertices(numOfVertices);
			String line = null;
			int count = 0;
			while ((line = buffer.readLine()) != null && count != numOfVertices) {
				BaseVertex v = (BaseVertex) Class.forName(vertexClassName)
						.newInstance();
				v.init(line, idSperatorId, idSperatorValue, false);
				//listOfVertices.add(v);
				chunk.put(v.getId(), v);
				count++;
			}

		} catch (NumberFormatException | IOException | InstantiationException
				| IllegalAccessException | ClassNotFoundException
				| IllegalDataException e) {
			e.printStackTrace();
		} finally {
			try {
				buffer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (buffer != null) {
				buffer = null;
			}
		}
	}

	

}
