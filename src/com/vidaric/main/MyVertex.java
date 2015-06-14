package com.vidaric.main;

import java.util.ArrayList;

import com.joml.Vector3f;
import com.joml.Vector2f;

public class MyVertex {
	public Vector3f position;
	public Vector3f currentTangent;
	public Vector3f currentBiTangent;
	public Vector3f currentNormal;
	public ArrayList<Vector2f> uvCoords = new ArrayList<Vector2f>();
	public ArrayList<Integer> faces = new ArrayList<Integer>();
}
