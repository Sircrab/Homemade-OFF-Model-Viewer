package com.vidaric.utils;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL20.*;

import com.joml.Matrix4f;
import com.joml.Vector3f;
import com.vidaric.light.PhongLight;
import com.vidaric.main.CubeModel;
import com.vidaric.main.MainClass;

public class DrawingUtils {
	/**
	 * MODIFICA Y ENVIA LA MATRIZ DE MODEL, y dibuja las luces en phongLights en sus posiciones
	 * @param phongLights
	 * @param modelMatrix
	 * @param shaderProgram
	 */
	public static void drawPhongLights(PhongLight[] phongLights, Matrix4f modelMatrix, Matrix4f projectionMatrix, Matrix4f viewMatrix, int shaderProgram){
		boolean first=true;
		for(PhongLight light : phongLights){
			glUseProgram(shaderProgram);
			Vector3f aux = new Vector3f(light.getPosition());
			Vector3f.mul(aux, MainClass.getScaleFactor(), aux);
			modelMatrix.identity().translate(aux);
			if(first){
				modelMatrix.scale(3f,3f,3f);
				first=false;
			}
			MatrixUtils.sendMVPtoShader(modelMatrix, viewMatrix, projectionMatrix, shaderProgram);
			glBindVertexArray(light.getVAO_ID());
			glDrawArrays(GL_TRIANGLES, 0, 36);
			glBindVertexArray(0);
		}
	}
	
	public static void drawCube(CubeModel cube, Matrix4f modelMatrix, Matrix4f viewMatrix, Matrix4f projectionMatrix, int shaderProgram){
		glUseProgram(shaderProgram);
		modelMatrix.identity().scale(MainClass.getScaleFactor());
		MatrixUtils.sendMVPtoShader(modelMatrix, viewMatrix, projectionMatrix, shaderProgram);
		glBindVertexArray(cube.getVaoId());
		glDrawArrays(GL_TRIANGLES, 0, 36);
		glBindVertexArray(0);
	}
}
