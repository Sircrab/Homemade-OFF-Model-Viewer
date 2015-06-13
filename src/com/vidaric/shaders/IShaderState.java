package com.vidaric.shaders;

import com.vidaric.main.MainClass;

public interface IShaderState {
	public void displayShader();
	public void nextShader(MainClass context);
	public void previousShader(MainClass context);
	public int getShaderProgram();
}
