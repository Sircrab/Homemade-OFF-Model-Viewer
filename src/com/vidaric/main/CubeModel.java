package com.vidaric.main;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.nio.FloatBuffer;

import com.joml.Vector3f;
import com.vidaric.utils.MatrixUtils;
import com.vidaric.utils.MyUtils;
import com.vidaric.vaos.CubeVao;

public class CubeModel extends GameObject implements Drawable {
	private CubeVao _cubeVao;
	private float[] _vertices;
	private Image mainTexture;
	private Image normalMap;
	private int shaderProgram;
	public CubeModel(Vector3f newPosition, Image newMainTexture, Image newNormalMap, int newShaderProgram) {
		super(newPosition);
		generateVertices();
		mainTexture = newMainTexture;
		normalMap = newNormalMap;
		shaderProgram = newShaderProgram;
		if(_vertices!=null)generateVao();
	}
	
	private void generateVao(){
		FloatBuffer floatBuffer = MyUtils.createFloatBufferFrom(_vertices);
		_cubeVao = new CubeVao(floatBuffer);
		_cubeVao.bindAndConfigure();
	}

	private void generateVertices(){
		_vertices = new float[] {
				-0.5f, -0.5f, -0.5f,  0.0f, 0.0f,
				0.5f, -0.5f, -0.5f,  1.0f, 0.0f,
				0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
				0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
				-0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
				-0.5f, -0.5f, -0.5f,  0.0f, 0.0f,

				-0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
				0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
				0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
				0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
				-0.5f,  0.5f,  0.5f,  0.0f, 1.0f,
				-0.5f, -0.5f,  0.5f,  0.0f, 0.0f,

				-0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
				-0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
				-0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
				-0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
				-0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
				-0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

				0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
				0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
				0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
				0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
				0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
				0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

				-0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
				0.5f, -0.5f, -0.5f,  1.0f, 1.0f,
				0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
				0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
				-0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
				-0.5f, -0.5f, -0.5f,  0.0f, 1.0f,

				-0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
				0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
				0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
				0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
				-0.5f,  0.5f,  0.5f,  0.0f, 0.0f,
				-0.5f,  0.5f, -0.5f,  0.0f, 1.0f
		};
	}
	public int getVaoId(){
		return _cubeVao.getVAO_ID();
	}

	@Override
	public void draw() {
		glUseProgram(shaderProgram);
		glActiveTexture(GL_TEXTURE0);
		glUniform1i(glGetUniformLocation(shaderProgram, "mainTexture"), 0);
		glBindTexture(GL_TEXTURE_2D,mainTexture.getTextureId());
		glActiveTexture(GL_TEXTURE1);
		glUniform1i(glGetUniformLocation(shaderProgram, "normalMapTexture"), 1);
		glBindTexture(GL_TEXTURE_2D,normalMap.getTextureId());
		MainClass.getModel().identity().scale(MainClass.getScaleFactor());
		MatrixUtils.sendMVPtoShader(MainClass.getModel(), MainClass.getView(), MainClass.getProjection(), shaderProgram);
		glBindVertexArray(this.getVaoId());
		glDrawArrays(GL_TRIANGLES, 0, 36);
		glBindVertexArray(0);
	}
}
