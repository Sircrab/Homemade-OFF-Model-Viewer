package com.vidaric.main;

import com.joml.Vector3f;

public class AmbientLight extends Light {
	public AmbientLight(Vector3f newPosition, Vector3f initialColor, float initialStrength) {
		super(newPosition, initialColor, initialStrength);
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}
}