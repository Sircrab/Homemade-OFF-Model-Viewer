package com.vidaric.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.FloatBuffer;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import org.lwjgl.BufferUtils;

import com.joml.Vector3f;
import com.joml.Vector4f;

public class OFFParser {
	private static final Vector4f defaultColor = new Vector4f(0.75f,0.75f,0.75f,1.0f);
	private static int lastSize = -1;
	
	public static FloatBuffer generateFloatBufferFrom(String fileName){
		FloatBuffer floatBuffer;
		ArrayList<Vector4f> verticesAndColors4f = generateVector4fArray(fileName);
		int size = verticesAndColors4f.size();
		floatBuffer = BufferUtils.createFloatBuffer(10*size/3);
		int cnt=0;
		for(Vector4f vector : verticesAndColors4f){
			if(cnt==0 || cnt==2){
				//vertex o normal
				floatBuffer.put(vector.x);
				floatBuffer.put(vector.y);
				floatBuffer.put(vector.z);
			}else if(cnt==1){
				//color
				floatBuffer.put(vector.x);
				floatBuffer.put(vector.y);
				floatBuffer.put(vector.z);
				floatBuffer.put(vector.w);
			}
			cnt = (cnt + 1)%3;
		}
		floatBuffer.position(0);
		lastSize = 10*size/3;
		return floatBuffer;
	}
	
	private static ArrayList<Vector4f> generateVector4fArray(String fileName) {
		ArrayList<Vector4f> result = new ArrayList<Vector4f>();
		try {
			Scanner scanner = new Scanner(new FileInputStream(fileName));
			if(!scanner.nextLine().equals("OFF")){
				scanner.close();
				throw new InvalidParameterException();
			}
			while(!scanner.hasNextInt()){scanner.nextLine();}
			int numVertices = scanner.nextInt();
			int numFaces = scanner.nextInt();
			scanner.nextLine();
			ArrayList<Vector3f> verticesPositions = new ArrayList<Vector3f>();
			ArrayList<Vector3f> normalsOfFace = new ArrayList<Vector3f>();
			ArrayList<Vector3f> normalsOfVertex = new ArrayList<Vector3f>();
			ArrayList< HashSet<Integer> > facesListOfVertex = new ArrayList<HashSet<Integer>>();
			for(int i=0; i < numVertices; i++){
				facesListOfVertex.add(new HashSet<Integer>());
			}
			ArrayList<Vector4f> colorOfFace = new ArrayList<Vector4f>();
			ArrayList<Vertex> triangularizedListOfVertices = new ArrayList<Vertex>();
			readVertices(scanner, numVertices, verticesPositions);
			readFaces(scanner, numFaces, facesListOfVertex, triangularizedListOfVertices, colorOfFace, normalsOfFace, verticesPositions);
			normalsOfVertex = getInterpolatedNormals(normalsOfFace, facesListOfVertex);
			for(Vertex vertex : triangularizedListOfVertices){
				Vector4f position = new Vector4f(verticesPositions.get(vertex.id),1.0f);
				Vector4f color = colorOfFace.get(vertex.face);
				Vector4f normal = new Vector4f(normalsOfVertex.get(vertex.id),1.0f);
				result.add(position);
				result.add(color);
				result.add(normal);
			}
			if(result.isEmpty()){throw new Exception();}
			
			scanner.close();
		} catch (FileNotFoundException e) {
			System.err.println("generateVector4fArray - archivo no encontrado.");
		} catch (InvalidParameterException e){
			System.err.println("generateVector4fArray - formato inválido");
		} catch (Exception e) {
			System.err.println("generateVector4fArray - no pude generar nada");
		}
		return result;
	}
	
	
	private static ArrayList<Vector3f> getInterpolatedNormals(ArrayList<Vector3f> unInterpolatedNormals, ArrayList< HashSet<Integer> > facesListOfVertex){
		ArrayList<Vector3f> interpolatedNormals = new ArrayList<Vector3f>();
		for(int vertexId=0; vertexId < facesListOfVertex.size();vertexId++){
			HashSet<Integer> associatedFaces = facesListOfVertex.get(vertexId);
			associatedFaces.size();
			Vector3f currentNormal = new Vector3f(0f,0f,0f);
			int numFaces = associatedFaces.size();
			for(int faceId : associatedFaces){
				currentNormal.add(unInterpolatedNormals.get(faceId));
			}
			Vector3f.mul(currentNormal, (float)(1.0f/numFaces), currentNormal);
			interpolatedNormals.add(currentNormal);
		}
		return interpolatedNormals;
	}

	private static void readVertices(Scanner s, int numVertices, ArrayList<Vector3f> verticesPositions){
		for(int i=0;i<numVertices;i++){
			float x, y, z;
			x = s.nextFloat();
			y = s.nextFloat();
			z = s.nextFloat();
			s.nextLine();
			verticesPositions.add(new Vector3f(x,y,z));
		}
	}
	
	private static void readFaces(Scanner s, int numFaces, ArrayList< HashSet<Integer> > facesListOfVertex, ArrayList<Vertex> verticesIndexList, ArrayList<Vector4f> facesColors, ArrayList<Vector3f> normalsOfFace, ArrayList<Vector3f> verticesPositions){
		for(int i=0;i<numFaces;i++){
			int dimension = s.nextInt();
			if(dimension<3){continue;}
			int sourceVertex = s.nextInt();
			int secondVertex = s.nextInt();
			int thirdVertex = s.nextInt();
			normalsOfFace.add(getNormalOfVertices(sourceVertex, secondVertex, thirdVertex, verticesPositions));
			for(int j=0;j<dimension-2;j++){
				verticesIndexList.add(new Vertex(sourceVertex,i));
				verticesIndexList.add(new Vertex(secondVertex,i));
				verticesIndexList.add(new Vertex(thirdVertex,i));
				facesListOfVertex.get(sourceVertex).add(i);
				facesListOfVertex.get(secondVertex).add(i);
				facesListOfVertex.get(thirdVertex).add(i);
				if(j!=dimension-3){
					secondVertex = thirdVertex;
					thirdVertex = s.nextInt();
				}
			}
			//tiene color??
			String colorString;
			String colorSplit[];
			float red,green,blue,alpha;
			alpha = defaultColor.w;
			red =defaultColor.x; green=defaultColor.y;blue=defaultColor.z;
			if((colorString=s.findInLine("[0-1]{1}.\\d+ [0-1]{1}.\\d+ [0-1]{1}.\\d+")) != null){
				colorSplit = colorString.split(" ");
				red = Float.parseFloat(colorSplit[0]);
				green = Float.parseFloat(colorSplit[1]);
				blue = Float.parseFloat(colorSplit[2]);
				if((colorString=s.findInLine("[0-1]{1}.\\d+"))!=null){
					alpha = Float.parseFloat(colorString.split(" ")[0]);
				}
			}
			facesColors.add(new Vector4f(red,green,blue,alpha));
		}
	}
	
	private static Vector3f getNormalOfVertices(int firstVertex, int secondVertex, int thirdVertex, ArrayList<Vector3f> verticesPositions){
		Vector3f normal = new Vector3f();
		Vector3f firstCross = new Vector3f();
		Vector3f secondCross = new Vector3f();
		
		Vector3f firstPosition = verticesPositions.get(firstVertex);
		Vector3f secondPosition = verticesPositions.get(secondVertex);
		Vector3f thirdPosition = verticesPositions.get(thirdVertex);

		Vector3f.sub(secondPosition, firstPosition, firstCross);
		Vector3f.sub(thirdPosition, firstPosition, secondCross);
		Vector3f.cross(firstCross, secondCross, normal);
		return normal;
	}

	public static int getLastGeneratedBufferSize(){
		return lastSize;
	}
}
class Vertex{
	public int id;
	public int face;
	public Vertex(int i, int f){
		id=i;
		face=f;
	}
}
