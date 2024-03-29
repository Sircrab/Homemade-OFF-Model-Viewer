package com.vidaric.shaders;

import com.vidaric.main.MainClass;

public class CelShader extends AbstractShaderState {
	
	public CelShader(int shaderProgram) {
		super(shaderProgram);
	}

	@Override
	public void nextShader(MainClass context) {
		context.setState(context.getStateMap().get("PhongShader"));
	}

	@Override
	public void previousShader(MainClass context) {
		context.setState(context.getStateMap().get("PhongShader"));
	}
}
