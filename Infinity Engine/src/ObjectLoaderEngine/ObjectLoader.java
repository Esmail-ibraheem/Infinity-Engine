package ObjectLoaderEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import ModelData.RawModel;
import RendererEngine.DataLoader;

public class ObjectLoader {
	public static RawModel LoadTheObject(String ObjectName, DataLoader Loader) {
		FileReader fileReader = null ;
		File ObjectFile = new File("res/Objects/"+ObjectName+".obj");
		try {
			fileReader = new FileReader(ObjectFile) ;
		} catch (FileNotFoundException e) {
			System.out.println("Could not find the object file");
			System.exit(-1);
		}
		BufferedReader reader = new BufferedReader(fileReader);
		String Line ;
		List<Vertex>Vertices = new ArrayList<Vertex>();
		List<Vector2f>Textures = new ArrayList<Vector2f>() ;
		List<Vector3f>Normals = new ArrayList<Vector3f>() ;
		List<Integer>Indices = new ArrayList<Integer>() ;
		
		try {
			while(true) {
				Line = reader.readLine();
				if(Line.startsWith("v ")) {
					String[]CurrentLine = Line.split(" ");
					Vector3f vertex = new Vector3f((float)Float.valueOf(CurrentLine[1]),(float)Float.valueOf(CurrentLine[2]),(float)Float.valueOf(CurrentLine[3]));
					Vertex newVertex = new Vertex(Vertices.size(), vertex);
					Vertices.add(newVertex);
				}else if(Line.startsWith("vt ")) {
					String[]CurrentLine = Line.split(" ");
					Vector2f texture = new Vector2f((float)Float.valueOf(CurrentLine[1]),(float)Float.valueOf(CurrentLine[2]));
					Textures.add(texture);
				}else if(Line.startsWith("vn ")) {
					String[]CurrentLine = Line.split(" ");
					Vector3f normal= new Vector3f((float)Float.valueOf(CurrentLine[1]),(float)Float.valueOf(CurrentLine[2]),(float)Float.valueOf(CurrentLine[3]));
					Normals.add(normal);
				}else if(Line.startsWith("f ")) {
					break;
				}
			}while(Line!=null && Line.startsWith("f ")) {
				String[]CurrentLine = Line.split(" ");
				String[]Vertex1 = CurrentLine[1].split("/");
				String[]Vertex2 = CurrentLine[2].split("/");
				String[]Vertex3 = CurrentLine[3].split("/");
				ProcessVertex(Vertex1, Vertices, Indices);
				ProcessVertex(Vertex2, Vertices, Indices);
				ProcessVertex(Vertex3, Vertices, Indices);
				Line = reader.readLine();
				
			}reader.close();
		}catch(Exception e) {
			System.out.println("Could not read the object file");
			System.exit(-1);
		}
		float[]VerticesArray = new float[Vertices.size()*3];
		float[]TexturesArray = new float[Vertices.size()*2];
		float[]NormalsArray = new float[Vertices.size()*3] ;
		ConvertOtherDataListsToArrays(Vertices, Textures, Normals, VerticesArray, TexturesArray, NormalsArray);
		int[]IndicesArray = ConvertIndicesListToArray(Indices);
		return Loader.LoadDataToVertexArrayObject(VerticesArray, TexturesArray, NormalsArray, IndicesArray);
	}
	private static void ProcessVertex(String[]vertex, List<Vertex>Vertices, List<Integer>Indices) {
		int Index = Integer.parseInt(vertex[0]) -1;
		Vertex CurrentVertex = Vertices.get(Index);
		int TextureIndex = Integer.parseInt(vertex[1]) -1;
		int NormalIndex = Integer.parseInt(vertex[2])-1;
		if(!CurrentVertex.IsSet()) {
			CurrentVertex.setTextureIndex(TextureIndex);
			CurrentVertex.setNormalIndex(NormalIndex);
			Indices.add(Index);
		}else {
			DealWithAlreadyProcessedVertex(CurrentVertex, TextureIndex, NormalIndex, Indices, Vertices);
		}
	}
	private static void DealWithAlreadyProcessedVertex(Vertex PreviousVertex, int newTextureIndex, int newNormalIndex, List<Integer>Indices, List<Vertex>Vertices) {
		if(PreviousVertex.HasTheSameTextureAndNormalIndex(newTextureIndex, newNormalIndex)) {
			Indices.add(PreviousVertex.getIndex());
		}else {
			Vertex anotherVertex= PreviousVertex.getDuplicateVertex();
			if(anotherVertex!=null) {
				DealWithAlreadyProcessedVertex(anotherVertex, newTextureIndex, newNormalIndex, Indices, Vertices);
			}else {
				Vertex duplicateVertex = new Vertex(Vertices.size(), PreviousVertex.getPositions());
				duplicateVertex.setTextureIndex(newTextureIndex);
				duplicateVertex.setNormalIndex(newNormalIndex);
				PreviousVertex.setDuplicateVertex(duplicateVertex);
				Vertices.add(duplicateVertex);
				Indices.add(duplicateVertex.getIndex());
			}
		}
	}
	private static int[] ConvertIndicesListToArray(List<Integer>Indices) {
		int[]IndicesArray = new int[Indices.size()];
		for(int i=0; i<IndicesArray.length; i++) {
			IndicesArray[i] = Indices.get(i);
		}
		return IndicesArray ;
	}
	private static float ConvertOtherDataListsToArrays(List<Vertex>Vertices, List<Vector2f>Textures, List<Vector3f>Normals, float[]VerticesArray, float[]TexturesArray, float[]NormalsArray) {
		float furthestPoint = 0;
		for(int i=0; i<Vertices.size(); i++) {
			Vertex CurrentVertex = Vertices.get(i);
			if(CurrentVertex.getLength()>furthestPoint) {
				furthestPoint = CurrentVertex.getLength();
			}
			Vector3f Positions = CurrentVertex.getPositions();
			Vector2f TextureVec = Textures.get(CurrentVertex.getTextureIndex());
			Vector3f NormalVec = Normals.get(CurrentVertex.getNormalIndex());
			VerticesArray[i*3] = Positions.x;
			VerticesArray[i*3 + 1] = Positions.y;
			VerticesArray[i*3 + 2] = Positions.z;
			TexturesArray[i*2] = TextureVec.x;
			TexturesArray[i*2 + 1] = 1 -TextureVec.y;
			NormalsArray[i*3] = NormalVec.x;
			NormalsArray[i*3 + 1] = NormalVec.y;
			NormalsArray[i*3 + 2] = NormalVec.z;
		}
		return furthestPoint ;
	}
}
