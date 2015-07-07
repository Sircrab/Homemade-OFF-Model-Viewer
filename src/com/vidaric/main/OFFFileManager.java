package com.vidaric.main;

import java.nio.FloatBuffer;

import com.vidaric.vaos.OFFVao;

public class OFFFileManager {
	private static OFFVao modelvao;
	public OFFFileManager(OFFVao vao){modelvao=vao;}
	public void loadModelToScreen(String modelName, boolean callback){
		FloatBuffer floatBuffer;
		if(callback){
			floatBuffer = OFFParser.generateFloatBufferFrom(modelName, callback);
		}else{
			floatBuffer = OFFParser.generateFloatBufferFrom(modelName, callback);
		}
		modelvao = new OFFVao(floatBuffer);
		modelvao.bindAndConfigure();
	}
	
	public OFFVao getVao(){return modelvao;}
}
