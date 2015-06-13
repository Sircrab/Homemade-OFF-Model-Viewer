package com.vidaric.main;

import static org.lwjgl.glfw.GLFW.*;

import com.joml.Vector3f;
import com.vidaric.utils.MyUtils;

public class FPCamera extends Camera {
	private float speed = 0.25f;
	private float mouseSensitivity = 0.1f;
	private Vector3f _up = new Vector3f(0f,1f,0f);
	private Vector3f _right = new Vector3f();
	private Vector3f _front;
	private float theta=0;
	private float phi=0;
	
	private boolean[] keys;
	
	public FPCamera(Vector3f position) {
		super(position);
		_front = new Vector3f(0f,0f,-1f);
		keys = CallBackContainer.getInstance().keys;
		updateCameraVectors();
	}
	
	private void updateCameraVectors() {
		_front = MyUtils.toCartesian(theta, phi, 1);
		_front.normalize();
		Vector3f.cross(_front, MainClass.upVector, _right);
		_right.normalize();
		Vector3f.cross(_right,_front, _up);
		_up.normalize();
	}

	private void walkForward(){
		Vector3f aux = new Vector3f();
		Vector3f.mul(_front,speed,aux);
		_position.add(aux);
	}
	private void walkBackwards(){
		Vector3f aux = new Vector3f();
		Vector3f.mul(_front,speed,aux);
		_position.sub(aux);
	}
	
	private void strafeLeft(){
		Vector3f aux = new Vector3f();
		Vector3f.cross(_front, _up, aux);
		aux.normalize();
		aux.mul(speed);
		_position.sub(aux);
	}	
	
	private void strafeRight(){
		Vector3f aux = new Vector3f();
		Vector3f.cross(_front, _up, aux);
		aux.normalize();
		aux.mul(speed);
		_position.add(aux);
	}
	
	private void moveUp(){
		Vector3f aux = new Vector3f();
		Vector3f.mul(_up,speed,aux);
		_position.add(aux);
	}
	private void moveDown(){
		Vector3f aux = new Vector3f();
		Vector3f.mul(_up,-speed,aux);
		_position.add(aux);
	}
	
	@Override
	public void move(){
		if(canMove()){
			if(keys[GLFW_KEY_W]){walkForward();}
			if(keys[GLFW_KEY_S]){walkBackwards();}
			if(keys[GLFW_KEY_D]){strafeRight();}
			if(keys[GLFW_KEY_A]){strafeLeft();}
			if(keys[GLFW_KEY_SPACE]){moveUp();}
			if(keys[GLFW_KEY_X]){moveDown();}
		}
	}
	
	
	protected void lookAround(float dx, float dy){
		theta += (dx*mouseSensitivity);
		phi -= (dy*mouseSensitivity);
		if(phi>89.0f){phi=89f;}
		if(phi<-89.0f){phi=-89f;}
		updateCameraVectors();
	}
	
	public Vector3f getFront(){return _front;}
	public float getTheta(){return theta;}
	public float getPhi(){return phi;}
}
