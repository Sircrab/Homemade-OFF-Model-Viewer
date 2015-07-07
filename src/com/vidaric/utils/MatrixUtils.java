package com.vidaric.utils;

import org.joml.Matrix4f;

public class MatrixUtils {
	public static void sendMVPtoShader(Matrix4f model, Matrix4f view, Matrix4f projection,int shaderProgram){
		MyUtils.setUniformWithMatrix4f(model, shaderProgram, "model");
		MyUtils.setUniformWithMatrix4f(view, shaderProgram, "view");
		MyUtils.setUniformWithMatrix4f(projection, shaderProgram, "projection");
	}
}
