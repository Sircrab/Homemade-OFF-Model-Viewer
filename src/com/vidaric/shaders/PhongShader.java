package com.vidaric.shaders;

import com.vidaric.main.MainClass;

public class PhongShader extends AbstractShaderState {

	public PhongShader(int shaderProgram) {
		super(shaderProgram);
	}

	@Override
	public void nextShader(MainClass context) {
		context.setState(context.getStateMap().get("CelShader"));
	}

	@Override
	public void previousShader(MainClass context) {
		context.setState(context.getStateMap().get("CelShader"));
	}

}
