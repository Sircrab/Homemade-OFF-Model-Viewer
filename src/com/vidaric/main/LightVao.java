package com.vidaric.main;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.FloatBuffer;

public class LightVao extends AbstractVAO{
	public LightVao(FloatBuffer floatBuffer){
		super(floatBuffer);
	}
	
	@Override
	protected void setAttributes() {
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, Float.BYTES*6, 0);
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, Float.BYTES*6, Float.BYTES*3);
	}
}
