package com.vidaric.main;

public interface IShaderState {
	public void displayShader();
	public void nextShader(MainClass context);
	public void previousShader(MainClass context);
	public int getShaderProgram();
}
