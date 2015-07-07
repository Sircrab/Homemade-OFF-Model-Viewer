package com.vidaric.vaos;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.FloatBuffer;

public class CubeVao extends AbstractVAO {
	public CubeVao(FloatBuffer floatBuffer) {
		super(floatBuffer);
	}

	@Override
	protected void setAttributes() {
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 3, GL_FLOAT, false,Float.SIZE/8*14, 0);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1,2,GL_FLOAT,false,Float.SIZE/8*14,Float.SIZE/8*3);
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, Float.SIZE/8*14, Float.SIZE/8*5);
		glEnableVertexAttribArray(3);
		glVertexAttribPointer(3, 3, GL_FLOAT, false, Float.SIZE/8*14, Float.SIZE/8*8);
		glEnableVertexAttribArray(4);
		glVertexAttribPointer(4, 3, GL_FLOAT, false, Float.SIZE/8*14, Float.SIZE/8*11);
	}
}
