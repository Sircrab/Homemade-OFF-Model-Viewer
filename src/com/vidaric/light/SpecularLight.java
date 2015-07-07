package com.vidaric.light;

import org.joml.Vector3f;

public class SpecularLight extends Light {
	public SpecularLight(Vector3f newPosition, Vector3f initialColor, float initialStrength) {
		super(newPosition, initialColor, initialStrength);
	}

	@Override
	public void move() {//TODO:no implementado, est� ok
	}
}
