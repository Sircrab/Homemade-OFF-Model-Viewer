package com.vidaric.vaos;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;

public abstract class AbstractVAO implements IVAO{
	protected int _vao_id;
	protected int _vbo_id;
	protected FloatBuffer _floatBuffer;
	protected abstract void setAttributes();
	
	public AbstractVAO(FloatBuffer floatBuffer){
		_vao_id = glGenVertexArrays();
		_vbo_id = glGenBuffers();
		_floatBuffer = floatBuffer;
	}
	
	@Override
	public void bindAndConfigure(){
		glBindVertexArray(_vao_id);
		glBindBuffer(GL_ARRAY_BUFFER, _vbo_id);
		glBufferData(GL_ARRAY_BUFFER, _floatBuffer, GL_STATIC_DRAW);
		setAttributes();
		glBindVertexArray(0);
		glBindBuffer(GL_ARRAY_BUFFER,0);
	}
	@Override
	public int getVAO_ID(){return _vao_id;}
}
