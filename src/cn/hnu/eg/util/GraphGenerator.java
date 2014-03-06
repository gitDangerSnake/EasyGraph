package cn.hnu.eg.util;

import java.io.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GraphGenerator {

	
	/*generator graph files:
	 * 1. N :number of Vertices
	 * 2. N lines of Vertices with values
	 * 3. M lines of edges (this value can be very large)
	 * */
	public static void main(String[] args) {
		if(args.length != 2){
			System.exit(0);
		}
		
		int numOfVertices = Integer.valueOf(args[0]);
		System.out.println(numOfVertices);
		int maxValue = Integer.valueOf(args[1]);
		System.out.println(maxValue);
		
		Set<Integer> existingDest = new HashSet<Integer>();
		int numOfOutgoingEdges = -1;
		int destVertex = -1;
		
		BufferedWriter buffer = null;
		try {
			buffer = new BufferedWriter(new FileWriter(EGConstant.GraphFilePath));
			
			StringBuffer sb = new StringBuffer();
			sb.append(numOfVertices).append("\n");
			for(int i=0;i<numOfVertices;i++){
				int value = new Random().nextInt(maxValue);
				sb.append(i).append(EGConstant.idSperatorValue).append(value).append("\n");
			}
			for(int i=0;i<numOfVertices;i++){
				existingDest.clear();
				
				while (numOfOutgoingEdges == -1) {
					if (numOfVertices == 2) {
						numOfOutgoingEdges = 1;
					} else if (numOfVertices == 3) {
						numOfOutgoingEdges = new Random().nextInt(numOfVertices - 2) + 1;
					} else {
						numOfOutgoingEdges = new Random().nextInt(numOfVertices / 20) + 1;
					}
				}
				
				for(int j=0;j<numOfOutgoingEdges;j++){
					int count = 0;
					destVertex = new Random().nextInt(numOfVertices);

					while (existingDest.contains(destVertex) && count < 5) {

						destVertex = new Random().nextInt(numOfVertices);						
						count++;
					}
					if (count < 5) {
						existingDest.add(destVertex);
						sb.append(i).append(EGConstant.idSperatorId).append(destVertex).append("\n");
						destVertex = -1;

					}
				}
				
			}
			buffer.write(sb.toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			try {
				buffer.close();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		
		
		

	}

}
