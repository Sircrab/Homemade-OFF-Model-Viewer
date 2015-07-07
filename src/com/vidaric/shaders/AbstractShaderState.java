package com.vidaric.shaders;

import static org.lwjgl.opengl.GL20.*;

public abstract class AbstractShaderState implements IShaderState {
	protected int _shaderProgram;
	public AbstractShaderState(int shaderProgram){
		_shaderProgram = shaderProgram;
	}
	@Override
	public void displayShader() {
		glUseProgram(_shaderProgram);
	}
	@Override
	public int getShaderProgram(){return _shaderProgram;}
	
}
