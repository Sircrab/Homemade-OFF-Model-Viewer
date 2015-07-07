package com.vidaric.main;

import org.joml.Vector3f;

public abstract class MovableGameObject extends GameObject implements Movable {
	public MovableGameObject(Vector3f newPosition) {
		super(newPosition);
	}
	protected boolean movementEnabled = false;
	public boolean canMove(){return movementEnabled;}
	public void enableMovement(){movementEnabled=true;}
	public void disableMovement(){movementEnabled=false;}
}
