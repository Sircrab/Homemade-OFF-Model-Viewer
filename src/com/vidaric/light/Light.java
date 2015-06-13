package com.vidaric.light;

import com.joml.Vector3f;
import com.vidaric.main.MovableGameObject;

public abstract class Light extends MovableGameObject {
	protected Vector3f _color;
	protected float _strength;
	
	public Light(Vector3f newPosition, Vector3f initialColor, float initialStrength) {
		super(newPosition);
		_color = initialColor;
		_strength = initialStrength;
	}
	
	public Vector3f getResultingColor(){
		Vector3f result = new Vector3f();
		Vector3f.mul(_color,_strength,result);
		return result;
	}
	
	public float getStrength(){return _strength;}
	public void setStrengthTo(float newStrength){_strength = newStrength;}
	public Vector3f getColor(){return _color;}
	public void setColorTo(Vector3f newColor){_color = newColor;}
	
}
