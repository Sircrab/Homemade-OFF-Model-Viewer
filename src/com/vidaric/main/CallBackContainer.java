package com.vidaric.main;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.*;

import com.vidaric.utils.MyUtils;

public class CallBackContainer {
	public boolean[] keys;
	public GLFWKeyCallback keyCallback;
	public GLFWCursorPosCallback cursorCallBack;
	public GLFWDropCallback dropCallback;
	private static CallBackContainer uniqueInstance = null;
	private FPCamera currentCamera;
	private OFFFileManager fileManager;
	private GeneralCallbacks generalCallbacks;
	private CallBackContainer(){
		keys = new boolean[1024];
		setKeyCallback();
		setMouseCallback();
		setDropCallback();
	}
	public static CallBackContainer getInstance(){
		if(uniqueInstance==null){
			uniqueInstance=new CallBackContainer();
		}
		return uniqueInstance;
	}
	private void setKeyCallback(){
		keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if(action==GLFW_PRESS){
					keys[key]=true;
				}
				if(action == GLFW_RELEASE){
					keys[key]=false;
				}
				generalCallbacks.update(keys, window);
			}
		};
	}
	
	private void setMouseCallback(){
		cursorCallBack = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				float dx = (float) (xpos - MainClass.WIDTH/2);
				float dy = (float) (ypos - MainClass.HEIGHT/2);
				if(!generalCallbacks.hasCursorEnabled()){
					currentCamera.lookAround(dx,dy);
					glfwSetCursorPos(window, MainClass.WIDTH/2, MainClass.HEIGHT/2);
				}
			}
		};
	}
	
	private void setDropCallback(){
		dropCallback = new GLFWDropCallback() {
			@Override
			public void invoke(long window, int count, long names) {
				if(count==1){
					Callbacks.dropCallbackNamesApply(count, names, new Callbacks.DropConsumerString() {
						@Override
						public void accept(int index, String name) {
							fileManager.loadModelToScreen(MyUtils.convertToWindowsPath(name), true);
						}
					});
				}else{
					System.out.println("Sólo un archivo a la vez.");
				}
			}
		};
	}
	
	public void setCamera(FPCamera cam){
		this.currentCamera = cam;
	}
	
	
	
	public void setFileManager(OFFFileManager newFileManager){
		fileManager = newFileManager;
	}
	public void setGeneralCallbacks(GeneralCallbacks callback){
		generalCallbacks = callback;
	}
	
}
