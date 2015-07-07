package com.vidaric.main;

import static org.lwjgl.glfw.GLFW.*;

public class GeneralCallbacks {
	private boolean cursorEnabled = false;
	private MainClass context;
	public GeneralCallbacks(MainClass mainClass) {
		context = mainClass;
	}
	protected void update(boolean[] keys, long window){
		if(keys[GLFW_KEY_ESCAPE]){
			glfwTerminate();
			System.exit(0);
		}
		if(keys[GLFW_KEY_C]){
			toggleCursor(window);
		}
		if(keys[GLFW_KEY_UP]){
			increaseScaleFactor();
		}
		if(keys[GLFW_KEY_DOWN]){
			decreaseScaleFactor();
		}
		if(keys[GLFW_KEY_RIGHT]){
			context.getShaderState().nextShader(context);
		}
		if(keys[GLFW_KEY_LEFT]){
			context.getShaderState().previousShader(context);
		}
	}
	private void enableCursor(long window){
		cursorEnabled = true;
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
	}
	private void disableCursor(long window){
		cursorEnabled = false;
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
	}
	private void increaseScaleFactor(){
		float newfactor = MainClass.getScaleFactor() + MainClass.getScaleSpeed();
		MainClass.setScaleFactor(newfactor);
	}
	private void decreaseScaleFactor(){
		float newfactor = MainClass.getScaleFactor() - MainClass.getScaleSpeed();
		if(newfactor<0f){
			newfactor=0f;
		}
		MainClass.setScaleFactor(newfactor);
	}
	public void toggleCursor(long window){
		if(cursorEnabled){disableCursor(window);}
		else{enableCursor(window);}
	}
	public boolean hasCursorEnabled(){return cursorEnabled;}
}
