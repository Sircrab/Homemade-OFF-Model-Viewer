package com.vidaric.main;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import com.joml.Matrix4f;
import com.joml.Vector3f;

public class DrawingUtils {
	/**
	 * MODIFICA Y ENVIA LA MATRIZ DE MODEL, y dibuja las luces en phongLights en sus posiciones
	 * @param phongLights
	 * @param modelMatrix
	 * @param shaderProgram
	 */
	public static void drawPhongLights(PhongLight[] phongLights, Matrix4f modelMatrix, int shaderProgram){
		boolean first=true;
		for(PhongLight light : phongLights){
			Vector3f aux = new Vector3f(light.getPosition());
			Vector3f.mul(aux, MainClass.getScaleFactor(), aux);
			modelMatrix.identity().translate(aux);
			if(first){
				modelMatrix.scale(3f,3f,3f);
				first=false;
			}
			MyUtils.setUniformWithMatrix4f(modelMatrix, shaderProgram, "model");
			glBindVertexArray(light.getVAO_ID());
			glDrawArrays(GL_TRIANGLES, 0, 36);
			glBindVertexArray(0);
		}
	}
}
