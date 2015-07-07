package com.vidaric.main;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.vidaric.utils.MatrixUtils;
import com.vidaric.utils.MyUtils;
import com.vidaric.vaos.CubeVao;

public class CubeModel extends GameObject implements Drawable {
	private CubeVao _cubeVao;
	private float[] _vertices;
	private Image mainTexture;
	private Image normalMap;
	private int shaderProgram;
	private ArrayList<MyVertex> normalMappingVertices;
	private HashMap<MyVertex3f, MyVertex> vertexMap;
	public CubeModel(Vector3f newPosition, Image newMainTexture, Image newNormalMap, int newShaderProgram) {
		super(newPosition);
		generateVertices();
		mainTexture = newMainTexture;
		normalMap = newNormalMap;
		shaderProgram = newShaderProgram;
		generateNormalMappingVertices();
		if(_vertices!=null)generateVao();
	}
	
	private void generateVao(){
		FloatBuffer floatBuffer = createFloatBuffer();
		_cubeVao = new CubeVao(floatBuffer);
		_cubeVao.bindAndConfigure();
	}
	
	private FloatBuffer createFloatBuffer(){
		int i = 0;
		float[] everything = new float[_vertices.length*14/5];
		for(int face = 0; face < _vertices.length/15; face++){
			for(MyVertex vertex : normalMappingVertices){
				if(vertex.faces.contains(face)){
					everything[i] = vertex.position.x; i++;
					everything[i] = vertex.position.y; i++;
					everything[i] = vertex.position.z; i++;
					Vector2f uvCoords = vertex.uvCoords.get(0); vertex.uvCoords.remove(0);
					everything[i] = uvCoords.x; i++;
					everything[i] = uvCoords.y; i++;
					everything[i] = vertex.currentNormal.x; i++;
					everything[i] = vertex.currentNormal.y; i++;
					everything[i] = vertex.currentNormal.z; i++;
					everything[i] = vertex.currentTangent.x; i++;
					everything[i] = vertex.currentTangent.y; i++;
					everything[i] = vertex.currentTangent.z; i++;
					everything[i] = vertex.currentBiTangent.x; i++;
					everything[i] = vertex.currentBiTangent.y; i++;
					everything[i] = vertex.currentBiTangent.z; i++;
				}
			}
		}
		FloatBuffer floatBuffer = MyUtils.createFloatBufferFrom(everything);
		return floatBuffer;
	}

	private void generateVertices(){
		_vertices = new float[] {
				-0.5f, -0.5f, -0.5f,  0.0f, 0.0f,
				0.5f, -0.5f, -0.5f,  1.0f, 0.0f,
				0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
				0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
				-0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
				-0.5f, -0.5f, -0.5f,  0.0f, 0.0f,

				-0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
				0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
				0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
				0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
				-0.5f,  0.5f,  0.5f,  0.0f, 1.0f,
				-0.5f, -0.5f,  0.5f,  0.0f, 0.0f,

				-0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
				-0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
				-0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
				-0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
				-0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
				-0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

				0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
				0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
				0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
				0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
				0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
				0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

				-0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
				0.5f, -0.5f, -0.5f,  1.0f, 1.0f,
				0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
				0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
				-0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
				-0.5f, -0.5f, -0.5f,  0.0f, 1.0f,

				-0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
				0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
				0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
				0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
				-0.5f,  0.5f,  0.5f,  0.0f, 0.0f,
				-0.5f,  0.5f, -0.5f,  0.0f, 1.0f
		};
	}
	public int getVaoId(){
		return _cubeVao.getVAO_ID();
	}

	@Override
	public void draw() {
		_position.x = MainClass.getScaleFactor()*3;
		
		glUseProgram(shaderProgram);
		glActiveTexture(GL_TEXTURE0);
		glUniform1i(glGetUniformLocation(shaderProgram, "mainTexture"), 0);
		glBindTexture(GL_TEXTURE_2D,mainTexture.getTextureId());
		glActiveTexture(GL_TEXTURE1);
		glUniform1i(glGetUniformLocation(shaderProgram, "normalMapTexture"), 1);
		glBindTexture(GL_TEXTURE_2D,normalMap.getTextureId());
		
		MyUtils.setUniformWithMatrix4f(MainClass.getNormal(), shaderProgram, "normalMatrix");
		MyUtils.setUniformWithVector3f(MainClass.getCamera().getPosition(), shaderProgram, "cameraPos");
		
		
		MyUtils.setUniformWithVector3f(MainClass.getAmbientLight().getResultingColor(), shaderProgram, "ambientLight");
		MyUtils.sendUniformPhongLight(MainClass.getPhongLights(), shaderProgram);
		
		MainClass.getModel().identity().translate(this._position).scale(MainClass.getScaleFactor());
		MatrixUtils.sendMVPtoShader(MainClass.getModel(), MainClass.getView(), MainClass.getProjection(), shaderProgram);
		glBindVertexArray(this.getVaoId());
		glDrawArrays(GL_TRIANGLES, 0, 36);
		glBindVertexArray(0);
	}
	
	private void generateNormalMappingVertices(){
		normalMappingVertices = new ArrayList<MyVertex>();
		vertexMap = new HashMap<MyVertex3f, MyVertex>();
		for(int face=0;face<_vertices.length/15;face++){
			MyVertex3f position1 = new MyVertex3f(_vertices[face*15],_vertices[face*15+1],_vertices[face*15+2]);
			MyVertex3f position2 = new MyVertex3f(_vertices[face*15+5], _vertices[face*15+6], _vertices[face*15+7]);
			MyVertex3f position3 = new MyVertex3f(_vertices[face*15+10], _vertices[face*15+11], _vertices[face*15+12]);
			
			Vector2f uv1 = new Vector2f(_vertices[face*15+3], _vertices[face*15+4]);
			Vector2f uv2 = new Vector2f(_vertices[face*15+8], _vertices[face*15+9]);
			Vector2f uv3 = new Vector2f(_vertices[face*15+13], _vertices[face*15+14]);
			
			Vector2f deltaUV1 = new Vector2f();
			Vector2f deltaUV2 = new Vector2f();
			MyVertex3f edge1 = new MyVertex3f();
			MyVertex3f edge2 = new MyVertex3f();
			
			MyVertex3f tangent = new MyVertex3f();
			MyVertex3f bitangent = new MyVertex3f();
			MyVertex3f normal = new MyVertex3f();
			
			Vector3f.sub(position2, position1, edge1);
			Vector3f.sub(position3, position1, edge2);
			
			Vector2f.sub(uv2, uv1, deltaUV1);
			Vector2f.sub(uv3, uv1, deltaUV2);
			
			float denom = 1.0f/(deltaUV1.x*deltaUV2.y - deltaUV2.x*deltaUV1.y);
			
			tangent.x = denom*(deltaUV2.y*edge1.x - deltaUV1.y*edge2.x);
			tangent.y = denom*(deltaUV2.y*edge1.y - deltaUV1.y*edge2.y);
			tangent.z = denom*(deltaUV2.y*edge1.z - deltaUV1.y*edge2.z);
			tangent.normalize();
			
			bitangent.x = denom*(-deltaUV2.x*edge1.x + deltaUV1.x*edge2.x);
			bitangent.y = denom*(-deltaUV2.x*edge1.y + deltaUV1.x*edge2.y);
			bitangent.z = denom*(-deltaUV2.x*edge1.z + deltaUV1.x*edge2.z);
			bitangent.normalize();
			
			Vector3f.cross(tangent, bitangent, normal);
			normal.normalize();
			addPositionTangentBitangentNormalFacesUV(vertexMap, position1, tangent, bitangent, normal, face, uv1);
			addPositionTangentBitangentNormalFacesUV(vertexMap, position2, tangent, bitangent, normal, face, uv2);
			addPositionTangentBitangentNormalFacesUV(vertexMap, position3, tangent, bitangent, normal, face, uv3);
		}
		for(MyVertex vertex : vertexMap.values()){
			vertex.currentNormal.normalize();
			vertex.currentBiTangent.normalize();
			vertex.currentTangent.normalize();
			normalMappingVertices.add(vertex);

		}
	}
	
	private void addPositionTangentBitangentNormalFacesUV(HashMap<MyVertex3f, MyVertex> vertexMap, MyVertex3f position, MyVertex3f tangent, MyVertex3f bitangent, MyVertex3f normal, int face, Vector2f uv){
		MyVertex currentVertex;
		if((currentVertex = vertexMap.get(position)) == null){
			currentVertex = new MyVertex();
			currentVertex.position = position;
			
			currentVertex.currentTangent = new MyVertex3f(0f,0f,0f);
			Vector3f.add(currentVertex.currentTangent, tangent, currentVertex.currentTangent);
			
			currentVertex.currentBiTangent = new MyVertex3f(0f,0f,0f);
			Vector3f.add(currentVertex.currentBiTangent, bitangent, currentVertex.currentBiTangent);
			
			currentVertex.currentNormal = new MyVertex3f(0f,0f,0f);
			Vector3f.add(currentVertex.currentNormal, normal, currentVertex.currentNormal);
			
			currentVertex.faces.add(face);
			currentVertex.uvCoords.add(uv);
			
			vertexMap.put((MyVertex3f) currentVertex.position, currentVertex);
		}else{
			Vector3f.add(currentVertex.currentTangent, tangent, currentVertex.currentTangent);
			Vector3f.add(currentVertex.currentBiTangent, bitangent, currentVertex.currentBiTangent);
			Vector3f.add(currentVertex.currentNormal, normal, currentVertex.currentNormal);
			currentVertex.faces.add(face);
			currentVertex.uvCoords.add(uv);
		}
	}
}
