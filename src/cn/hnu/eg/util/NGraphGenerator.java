package cn.hnu.eg.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class NGraphGenerator {

	public static void main(String[] args) throws IOException{
		// java NGraphGenerator how_many_chunks how_many_vertices_per_chunk max_weight
		
		if(args.length!=3){
			System.out.println("java NGraphGenerator how_many_chunks how_many_vertices_per_chunk weight");
			System.exit(0);
		}
		
		int chunks = Integer.valueOf(args[0]);
		int numOfVertices = Integer.valueOf(args[1]);
		int maxValue = Integer.valueOf(args[2]); 
		
		for(int i=0;i<chunks;i++){
			System.out.println(i);
			generator(i,numOfVertices,maxValue,chunks);
		}
	}
	
	private static void generator(int baseindex, int numOfVertices, int maxValue,int chunks) throws IOException {
		int totalVertices = numOfVertices * chunks;
		int startIndex = baseindex * numOfVertices;
		String filename = "chunks" + baseindex;
		File f = new File(EGConstant.GraphFilePath+filename);
		
		if(!f.exists()){
			f.createNewFile();
			System.out.println("creating file " + f.getAbsolutePath());
		}
		
		Set<Integer> existingDest = new HashSet<Integer>();
		int numOfOutgoingEdges = -1;
		int destVertex = -1;
		
		BufferedWriter buffer = null;
		try {
			buffer = new BufferedWriter(new FileWriter(f));
			
			StringBuffer sb = new StringBuffer();
			sb.append(numOfVertices).append("\n");
			for(int i=0;i<numOfVertices;i++){
				int value = new Random().nextInt(maxValue);
				sb.append(i+startIndex).append(EGConstant.idSperatorValue).append(value).append("\n");
			}
			for(int i=0;i<numOfVertices;i++){
				existingDest.clear();
				
				while (numOfOutgoingEdges == -1) {
					if (totalVertices == 2) {
						numOfOutgoingEdges = 1;
					} else {
						numOfOutgoingEdges = new Random().nextInt(chunks)+ 1;
					}
				}
				
				for(int j=0;j<numOfOutgoingEdges;j++){
					int count = 0;
					destVertex = new Random().nextInt(numOfVertices);

					while (existingDest.contains(destVertex) && count < 5) {

						destVertex = new Random().nextInt(numOfVertices)+baseindex*numOfVertices;						
						count++;
					}
					if (count < 5) {
						existingDest.add(destVertex);
						sb.append(i+baseindex*numOfVertices).append(EGConstant.idSperatorId).append(destVertex).append("\n");
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
