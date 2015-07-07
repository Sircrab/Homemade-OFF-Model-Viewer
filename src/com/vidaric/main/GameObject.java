package com.vidaric.main;

import java.util.Observable;

import org.joml.Vector3f;

public class GameObject extends Observable {
	protected Vector3f _position;
	private boolean movementEnabled=false;
	public GameObject(Vector3f newPosition){
		_position = newPosition;
	}
	public Vector3f getPosition(){return _position;}
	public void setPosition(Vector3f newPosition){_position = newPosition;}
	public void enableMovement(){movementEnabled=true;}
	public void disableMovement(){movementEnabled=false;}
	public boolean canMove(){return movementEnabled;}
}
