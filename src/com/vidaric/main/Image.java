package com.vidaric.main;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import org.lwjgl.BufferUtils;

import com.vidaric.utils.MyUtils;

import static org.lwjgl.stb.STBImage.*;

public class Image {
	private ByteBuffer imageBuffer;
	private ByteBuffer image;
	private int w;
	private int h;
	private int comp;
	private int texID;
	
	public Image(String fileName){
		try {
			imageBuffer = MyUtils.ioResourceToByteBuffer(fileName, 8 * 1024);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		IntBuffer w = BufferUtils.createIntBuffer(1);
		IntBuffer h = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);
		if ( stbi_info_from_memory(imageBuffer, w, h, comp) == 0 )
			throw new RuntimeException("Failed to read image information: " + stbi_failure_reason());
		image = stbi_load_from_memory(imageBuffer, w, h, comp, 0);
		if ( image == null )
			throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
		
		this.w = w.get(0);
		this.h = h.get(0);
		this.comp = comp.get(0);
		texID = glGenTextures();
		configureTexture();
	}
	
	private void configureTexture(){
		glBindTexture(GL_TEXTURE_2D, texID);
		if ( comp == 3 )
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, w, h, 0, GL_RGB, GL_UNSIGNED_BYTE, image);
		else {
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		}
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glBindTexture(GL_TEXTURE_2D,0);
	}

	
	public int getTextureId(){
		return texID;
	}
}
