package com.vidaric.main;

import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;

import com.joml.Matrix4f;
import com.joml.Vector3f;


public class MyUtils {
	/**
	 * 
	 * @param filename : el nombre del archivo
	 * @return código fuente del shader
	 * @throws Exception: error durante la lectura de filename
	 */
	public static String readShader(String filename) throws Exception {
		String code = "",line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("shaders/"+filename)));
		while ((line = reader.readLine()) != null) {
			code += line + "\n";
		}
		reader.close();
		return code;
	}
	
	/**
	 * 
	 * @param shaderFile : el archivo que contiene el código fuente del vertex shader
	 * @return el handle del vertex shader
	 */
	public static int createVertexShaderFrom(String shaderFile){
		int vertexShader = glCreateShader(GL_VERTEX_SHADER);
		createShader(vertexShader, shaderFile);
		return vertexShader;
	}
	
	/**
	 * 
	 * @param shaderFile : el archivo que contiene el código fuente del fragment shader
	 * @return el handle del fragment shader
	 */
	public static int createFragmentShaderFrom(String shaderFile){
		int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		createShader(fragmentShader, shaderFile);
		return fragmentShader;
	}
	
	/**
	 * 
	 * @param shaders: lista de handles de shaders (compilados y creados!)
	 * @return el programa que se obtiene tras unir la lista de shaders
	 */
	public static int createProgramFromShaders(int[] shaders){
		int program = glCreateProgram();
		try{
			for(int shader : shaders){
				glAttachShader(program, shader);
			}
			glLinkProgram(program);
			for(int shader: shaders){
				glDeleteShader(shader);
			}
		}catch(Exception e){
			System.err.println("createProgramFromShaders - No se pudo armar el shader program");
		}
		return program;
	}
	
	/**
	 * 
	 * @param vertices : lista de vértices
	 * @return un buffer que contiene los vértices en vertices
	 */
	public static FloatBuffer createFloatBufferFrom(float[] vertices){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
		buffer.put(vertices);
		buffer.position(0);
		return buffer;
	}
	
	/**
	 * 
	 * @param vertices : lista de vértices
	 * @return un buffer que contiene los vértices en vertices
	 */
	public static IntBuffer createIntBufferFrom(int[] vertices){
		IntBuffer buffer = BufferUtils.createIntBuffer(vertices.length);
		buffer.put(vertices);
		buffer.position(0);
		return buffer;
	}
	
	public static void setUniformWithMatrix4f(Matrix4f matrix, int shaderProgram, String uniformName){
		try{
			FloatBuffer fb = BufferUtils.createFloatBuffer(16);
			matrix.get(fb);
			int uniformLocation = glGetUniformLocation(shaderProgram, uniformName);
			glUniformMatrix4fv(uniformLocation, false, fb);
		}catch(Exception e){
			System.err.println("setUniformWithMatrix4f - error");
			glfwTerminate();
			System.exit(0);
		}
	}
	
	public static void setUniformWithVector3f(Vector3f vector, int shaderProgram, String uniformName){
		try{
			int uniformLocation = glGetUniformLocation(shaderProgram, uniformName);
			FloatBuffer bf = BufferUtils.createFloatBuffer(3);
			bf.put(new float[]{vector.x,vector.y,vector.z});
			bf.position(0);
			glUniform3fv(uniformLocation, bf);
		}catch(Exception e){
			System.err.println("setUniformWithVector3f - error");
			glfwTerminate();
			System.exit(0);
		}
	}
	
	public static void setUniformWithFloat(float value, int shaderProgram, String uniformName){
		try{
			int uniformLocation = glGetUniformLocation(shaderProgram, uniformName);
			glUniform1f(uniformLocation, value);
		}catch(Exception e){
			System.err.println("setUniformWithFloat - error");
			glfwTerminate();
			System.exit(0);
		}
	}
	
	public static void sendUniformPhongLight(PhongLight[] phongLights, int shaderProgram){
		for(int i=0; i<phongLights.length;i++){
			PhongLight currentPhongLight = phongLights[i];
			int colorUniformLocation = glGetUniformLocation(shaderProgram, "phongLights[" + i + "].phongLightColor");
			int positionUniformLocation = glGetUniformLocation(shaderProgram, "phongLights[" + i + "].position");
			int specularStrengthUniformLocation = glGetUniformLocation(shaderProgram, "phongLights[" + i + "].specularStrength");
			int diffuseStrengthUniformLocation = glGetUniformLocation(shaderProgram, "phongLights[" + i + "].diffuseStrength");
			int linearLocation = glGetUniformLocation(shaderProgram, "phongLights[" + i + "].linear");
			int quadraticLocation = glGetUniformLocation(shaderProgram, "phongLights[" + i + "].quadratic");
			glUniform3f(colorUniformLocation, currentPhongLight._color.x, currentPhongLight._color.y, currentPhongLight._color.z);
			glUniform3f(positionUniformLocation, currentPhongLight._position.x, currentPhongLight._position.y, currentPhongLight._position.z);
			glUniform1f(specularStrengthUniformLocation, currentPhongLight.getSpecularStrength());
			glUniform1f(diffuseStrengthUniformLocation, currentPhongLight.getDiffuseStrength());
			glUniform1f(linearLocation, currentPhongLight.getLinear());
			glUniform1f(quadraticLocation, currentPhongLight.getQuadratic());
		}
	}
	
	public static String getLogInfo(int obj) {
        return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }
	
	public static void setViewMatrix4f(Matrix4f currentMatrix, Vector3f cameraPosition, float theta, float phi, Vector3f worldUp, Vector3f xAxis){
		currentMatrix.identity();
		currentMatrix.rotate(-phi, xAxis).rotate(theta+90, worldUp).translate(-cameraPosition.x,-cameraPosition.y,-cameraPosition.z);
	}
	
	public static Matrix4f setNormalMatrix(Matrix4f currentMatrix, Matrix4f modelMatrix){
		currentMatrix.identity();
		Matrix4f.mul(currentMatrix, modelMatrix, currentMatrix);
		currentMatrix.invert();
		currentMatrix.transpose();
		return currentMatrix;
	}
	
	public static Vector3f toCartesian(float theta, float phi, float radius){
		Vector3f start = new Vector3f();
		start.x = (float) (radius*(Math.cos(Math.toRadians(phi))*Math.cos(Math.toRadians(theta))));
		start.y = (float) (radius*(Math.sin(Math.toRadians(phi))));
		start.z = (float) (radius*(Math.cos(Math.toRadians(phi))*Math.sin(Math.toRadians(theta))));
		return start;
	}
	
	public static Vector3f toSpherical(Vector3f start){
		Vector3f result = new Vector3f();
		float x = start.x; float y = start.y; float z = start.z;
		result.x = (float) Math.sqrt(x*x+y*y+z*z);
		result.y = (float) Math.toDegrees(Math.atan(z/x));
		if(Float.isNaN(result.y)){
			if(result.y==Float.POSITIVE_INFINITY){
				result.y=0.0f;
			}else{
				result.y=180f;
			}
		}else if(Float.toString(result.y).charAt(0)=='-'){
			result.y=180f;
		}
		result.y = (result.y+360)%360;
		result.z = (float) Math.toDegrees(Math.asin(y/result.x));	
		return result;
	}
	
	private static void createShader(int shader, String shaderFile){
		String shaderSource = null;
		try{
			shaderSource = readShader(shaderFile);
		}catch(Exception e){
			System.err.println("createShader - No se pudo parsear el shader " + shaderFile);
			glfwTerminate();
			System.exit(1);
		}
		glShaderSource(shader,shaderSource);
		glCompileShader(shader);
	}
	
	public static String convertToWindowsPath(String path){
		String result = "";
		String[] split = path.split("\\\\");
		for(String s : split){
			result = result + s + "//";
		}
		if(result.length()<2){return "";}
		return result.substring(0, result.length()-2);
	}
}
