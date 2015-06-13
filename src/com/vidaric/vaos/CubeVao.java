package com.vidaric.vaos;

import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class CubeVao extends AbstractVAO {
	public CubeVao(FloatBuffer floatBuffer) {
		super(floatBuffer);
	}

	@Override
	protected void setAttributes() {
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 3, GL_FLOAT, false,Float.BYTES*5, 0);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1,2,GL_FLOAT,false,Float.BYTES*5,Float.BYTES*3);
	}
}
