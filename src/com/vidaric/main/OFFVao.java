package com.vidaric.main;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.FloatBuffer;

public class OFFVao extends AbstractVAO{
	public OFFVao(FloatBuffer floatBuffer){
		super(floatBuffer);
	}
	@Override
	protected void setAttributes() {
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, Float.BYTES*10, 0);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, Float.BYTES*10, Float.BYTES*7);
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(2, 4, GL_FLOAT, false, Float.BYTES*10, Float.BYTES*3);
	}
}
