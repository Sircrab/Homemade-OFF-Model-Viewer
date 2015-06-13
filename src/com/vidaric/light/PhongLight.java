package com.vidaric.light;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;

import com.joml.Matrix4f;
import com.joml.Vector3f;
import com.joml.Vector4f;
import com.vidaric.main.CallBackContainer;
import com.vidaric.main.MainClass;
import com.vidaric.utils.MyUtils;
import com.vidaric.vaos.LightVao;

public class PhongLight extends Light{
	private final float angle_speed=1f;
	private DiffuseLight diffuseLight;
	private SpecularLight specularLight;
	private LightVao lightVao;
	private float[] vertices;
	private float linear = 0.09f;
	private float quadratic = 0.032f;
	private float theta=0f;
	private Vector3f initial_position;
	
	public PhongLight(Vector3f newPosition, Vector3f initialColor, float diffuseStrength, float specularStrength, float linearFactor, float quadraticFactor) {
		super(newPosition, initialColor, 0f);
		linear = linearFactor;
		quadratic = quadraticFactor;
		diffuseLight = new DiffuseLight(newPosition, initialColor, diffuseStrength);
		specularLight = new SpecularLight(newPosition, initialColor, specularStrength);
		generateVertices();
		if(vertices != null){
			generateVao();
		}
		initial_position = new Vector3f();
		initial_position.x = newPosition.x;
		initial_position.y = newPosition.y;
		initial_position.z = newPosition.z;
	}
	
	public Vector3f getDiffuseColor(){
		return diffuseLight.getResultingColor();
	}
	
	public float getSpecularStrength(){
		return specularLight.getStrength();
	}
	public float getDiffuseStrength(){
		return diffuseLight.getStrength();
	}
	
	private void generateVao(){
		FloatBuffer floatBuffer = MyUtils.createFloatBufferFrom(vertices);
		lightVao = new LightVao(floatBuffer);
		lightVao.bindAndConfigure();
	}
	
	private float[] generateVertices(){
		vertices = new float[]{
			    -0.5f, -0.5f, -0.5f,  0.0f, 0.0f, 0.0f,
			     0.5f, -0.5f, -0.5f,  0.0f, 0.0f, 0.0f,
			     0.5f,  0.5f, -0.5f,  0.0f, 0.0f, 0.0f,
			     0.5f,  0.5f, -0.5f,  0.0f, 0.0f, 0.0f,
			    -0.5f,  0.5f, -0.5f,  0.0f, 0.0f, 0.0f,
			    -0.5f, -0.5f, -0.5f,  0.0f, 0.0f, 0.0f,

			    -0.5f, -0.5f,  0.5f,  0.0f, 0.0f, 0.0f,
			     0.5f, -0.5f,  0.5f,  0.0f, 0.0f, 0.0f,
			     0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 0.0f,
			     0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 0.0f,
			    -0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 0.0f,
			    -0.5f, -0.5f,  0.5f,  0.0f, 0.0f, 0.0f,

			    -0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 0.0f,
			    -0.5f,  0.5f, -0.5f,  0.0f, 0.0f, 0.0f,
			    -0.5f, -0.5f, -0.5f,  0.0f, 0.0f, 0.0f,
			    -0.5f, -0.5f, -0.5f,  0.0f, 0.0f, 0.0f,
			    -0.5f, -0.5f,  0.5f,  0.0f, 0.0f, 0.0f,
			    -0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 0.0f,

			     0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 0.0f,
			     0.5f,  0.5f, -0.5f,  0.0f, 0.0f, 0.0f,
			     0.5f, -0.5f, -0.5f,  0.0f, 0.0f, 0.0f,
			     0.5f, -0.5f, -0.5f,  0.0f, 0.0f, 0.0f,
			     0.5f, -0.5f,  0.5f,  0.0f, 0.0f, 0.0f,
			     0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 0.0f,

			    -0.5f, -0.5f, -0.5f,  0.0f, 0.0f, 0.0f,
			     0.5f, -0.5f, -0.5f,  0.0f, 0.0f, 0.0f,
			     0.5f, -0.5f,  0.5f,  0.0f, 0.0f, 0.0f,
			     0.5f, -0.5f,  0.5f,  0.0f, 0.0f, 0.0f,
			    -0.5f, -0.5f,  0.5f,  0.0f, 0.0f, 0.0f,
			    -0.5f, -0.5f, -0.5f,  0.0f, 0.0f, 0.0f,

			    -0.5f,  0.5f, -0.5f,  0.0f, 0.0f, 0.0f,
			     0.5f,  0.5f, -0.5f,  0.0f, 0.0f, 0.0f,
			     0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 0.0f,
			     0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 0.0f,
			    -0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 0.0f,
			    -0.5f,  0.5f, -0.5f,  0.0f, 0.0f, 0.0f,
			};
		for(int i=0; i < vertices.length;i++){
			if(i%6 == 3){
				vertices[i] = diffuseLight._color.x;
			}else if(i%6 == 4){
				vertices[i] = diffuseLight._color.y;
			}else if(i%6 == 5){
				vertices[i] = diffuseLight._color.z;
			}
		}
		return vertices;
	}
	public float[] getVertices(){return vertices;}
	public DiffuseLight getDiffuseLight(){return diffuseLight;}
	public SpecularLight getSpecularLight(){return specularLight;}
	public int getVAO_ID(){return lightVao.getVAO_ID();}
	public float getQuadratic() {return quadratic;}
	public float getLinear() {return linear;}

	@Override
	public void move() {
		if(canMove()){
			CallBackContainer keyContainer = CallBackContainer.getInstance();
			boolean[] keys = keyContainer.keys;
			if(keys[GLFW_KEY_1]){orbitLeft();}
			if(keys[GLFW_KEY_2]){orbitRight();}
		}
	}
	private void orbitLeft(){
		theta-=angle_speed;
		updatePosition(theta);
	}
	private void orbitRight(){
		theta+=angle_speed;
		updatePosition(theta);
	}
	private void updatePosition(float angle){
		//Gram-Schmidt es un papi
		Vector3f normalizedInitialPosition = new Vector3f();
		Vector3f.normalize(initial_position, normalizedInitialPosition);
		Vector3f firstCross = new Vector3f();
		Vector3f.cross(normalizedInitialPosition, MainClass.upVector, firstCross);
		firstCross.normalize();
		Vector3f secondCross = new Vector3f();
		Vector3f.cross(normalizedInitialPosition, firstCross, secondCross);
		secondCross.normalize();
		if(Float.isNaN(secondCross.x) || Float.isNaN(secondCross.y) || Float.isNaN(secondCross.z)){
			secondCross.x=1f;secondCross.y=0f;secondCross.z=0f;
		}
		Matrix4f rotationMatrix = new Matrix4f().identity().rotate(angle, secondCross);
		Vector4f toRotate = new Vector4f(initial_position,1.0f);
		Vector4f.mul(toRotate, rotationMatrix, toRotate);
		_position.x = toRotate.x;
		_position.y = toRotate.y;
		_position.z = toRotate.z;
	}
	
}

