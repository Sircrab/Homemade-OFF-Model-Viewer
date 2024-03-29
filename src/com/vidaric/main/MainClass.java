package com.vidaric.main;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetDropCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GLContext;

import com.vidaric.light.AmbientLight;
import com.vidaric.light.PhongLight;
import com.vidaric.shaders.CelShader;
import com.vidaric.shaders.IShaderState;
import com.vidaric.shaders.PhongShader;
import com.vidaric.utils.DrawingUtils;
import com.vidaric.utils.MatrixUtils;
import com.vidaric.utils.MyUtils;
import com.vidaric.vaos.OFFVao;

public class MainClass{
	private static FPCamera camera;
	private static OFFFileManager fileManager;
	private static long window;
	private static GeneralCallbacks generalCallbacks;
	private static float scaleFactor=1f;
	private static float scaleSpeed=0.2f;
	private static CallBackContainer callbackContainer;
	private static IShaderState state;
	private static HashMap<String, IShaderState> stateMap;
	
	public static final int WIDTH=1024;
	public static final int HEIGHT=768;
	public static OFFVao offvao;
	public static Vector3f upVector;
	public static Vector3f xAxis;
	public static ArrayList<Movable> movablesList;
	
	public static void main(String[] args) {
		new MainClass().run();
	}
	private static Matrix4f modelMatrix;
	private static Matrix4f normalMatrix;
	private static Matrix4f perspectiveMatrix;
	private static Matrix4f viewMatrix;
	private static AmbientLight ambientLight;
	private static PhongLight[] phongLights;

	public void run(){
		movablesList = new ArrayList<Movable>();
		window = init("Hello");
		glSetup();
		gameSetup();
		loop();
		glfwTerminate();
		System.exit(0);
	}

	private void loop(){
		setAmbientLight(new AmbientLight(new Vector3f(0f,0f,0f), new Vector3f(1f,1f,1f), 0.2f));
		phongLights = new PhongLight[]{new PhongLight(new Vector3f(15f,15f,15f), new Vector3f(1f,1f,1f), 0.75f, 0.75f,0f,0f),
													new PhongLight(new Vector3f(-3f,2f,-1f), new Vector3f(0f,1f,0f), 1f, 1f,0.09f,0.032f),
													new PhongLight(new Vector3f(3f,2f,-1f), new Vector3f(0f,0f,1f), 1f, 1f,0.09f,0.032f),
													new PhongLight(new Vector3f(3f,-2f,1f), new Vector3f(1f,1f,0f), 1f, 1f,0.09f,0.032f),
													new PhongLight(new Vector3f(-3f,-2f,-1f), new Vector3f(0f,1f,1f), 1f, 1f,0.09f,0.032f)};
		

		
		for(Movable light : phongLights){
			movablesList.add(light);
		}
		phongLights[1].enableMovement();
		phongLights[2].enableMovement();
		phongLights[3].enableMovement();
		phongLights[4].enableMovement();
		
		int lightVertexShader = MyUtils.createVertexShaderFrom("lightVertexShader.vsh");
		int lightFragmentShader = MyUtils.createFragmentShaderFrom("lightFragmentShader.fsh");
		int lightShaderProgram = MyUtils.createProgramFromShaders(new int[]{lightVertexShader,lightFragmentShader});
		
		int cubeVertexShader = MyUtils.createVertexShaderFrom("normalmapping.vsh");
		int cubeFragmentShader = MyUtils.createFragmentShaderFrom("normalmapping.fsh");
		int cubeShaderProgram = MyUtils.createProgramFromShaders(new int[]{cubeVertexShader,cubeFragmentShader});
		
		Image image = new Image("textures/texture_4.png");
		Image normalMap = new Image("textures/normal_4.png");
		
		CubeModel cube =  new CubeModel(new Vector3f(0f,0f,0f), image, normalMap, cubeShaderProgram);
		
		fileManager.loadModelToScreen("offmodels/m222.off",false);
		
		modelMatrix = new Matrix4f().identity();
		normalMatrix = new Matrix4f(); MyUtils.setNormalMatrix(normalMatrix, modelMatrix);
		perspectiveMatrix = new Matrix4f().perspective(45f, (float)WIDTH/(float)HEIGHT, 0.01f, 2000f);
		viewMatrix = new Matrix4f().identity();MyUtils.setViewMatrix4f(viewMatrix, camera.getPosition(), camera.getTheta(), camera.getPhi(), upVector, xAxis);

		while(glfwWindowShouldClose(window) == GL_FALSE){
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glfwPollEvents();
			moveMovables(movablesList);
			normalMatrix.identity();MyUtils.setNormalMatrix(normalMatrix, modelMatrix);
			viewMatrix.identity();MyUtils.setViewMatrix4f(viewMatrix, camera.getPosition(), camera.getTheta(), camera.getPhi(), upVector, xAxis);
			
			
			//OFF//
			state.displayShader();
			modelMatrix.identity();
			modelMatrix.translate(new Vector3f(0f,0f,0f)).scale(scaleFactor);
			int shaderProgram = state.getShaderProgram();
			MatrixUtils.sendMVPtoShader(modelMatrix, viewMatrix, perspectiveMatrix, shaderProgram);
			MyUtils.setUniformWithMatrix4f(normalMatrix, shaderProgram, "normalMatrix");
			MyUtils.setUniformWithVector3f(camera.getPosition(), shaderProgram, "cameraPos");
			MyUtils.setUniformWithVector3f(ambientLight.getResultingColor(), shaderProgram, "ambientLight");
			MyUtils.sendUniformPhongLight(phongLights, shaderProgram);
			glBindVertexArray(fileManager.getVao().getVAO_ID());
			glDrawArrays(GL_TRIANGLES,0,OFFParser.getLastGeneratedBufferSize());
			glBindVertexArray(0);
			//////
			 
		
		
			cube.draw();
			DrawingUtils.drawPhongLights(phongLights, modelMatrix, perspectiveMatrix, viewMatrix, lightShaderProgram);
			
			//////
			glfwSwapInterval(1);
			glfwSwapBuffers(window);
		}
	}

	private void glSetup(){
		glViewport(0, 0, WIDTH, HEIGHT);
		glEnable(GL_ALPHA_TEST);
		glEnable(GL_DEPTH_TEST);
		glClearColor(0f, 0f, 0f, 1f);
		glEnable(GL_TEXTURE_2D);
	}

	private void gameSetup(){
		initShaders();
		state = stateMap.get("CelShader");
		callbackContainer = CallBackContainer.getInstance();
		upVector = new Vector3f(0f,1f,0f);
		xAxis = new Vector3f(1f,0f,0f);
		setCamera(new Vector3f(0f,0.0f,3f));
		setFileManager();
		setGeneralCallbacks();
		callbackContainer.setCamera(camera);
		callbackContainer.setFileManager(fileManager);
		callbackContainer.setGeneralCallbacks(generalCallbacks);
		glfwSetKeyCallback(window, callbackContainer.keyCallback);
		glfwSetCursorPosCallback(window, callbackContainer.cursorCallBack);
		glfwSetDropCallback(window, callbackContainer.dropCallback);
	}

	private void setCamera(Vector3f position){
		camera = new FPCamera(position);
		camera.enableMovement();
		movablesList.add(camera);
		glfwSetCursorPos(window, WIDTH/2, HEIGHT/2);
	}
	
	private void initShaders(){
		int vertexShader = MyUtils.createVertexShaderFrom("vertexshader.vsh");
		int fragmentShader = MyUtils.createFragmentShaderFrom("fragmentshader.fsh");
		int shaderProgram = MyUtils.createProgramFromShaders(new int[]{vertexShader,fragmentShader});
		PhongShader phongShaderState = new PhongShader(shaderProgram);
		
		int celVertexShader = MyUtils.createVertexShaderFrom("vertexshader.vsh");
		int celFragmentShader = MyUtils.createFragmentShaderFrom("celFragmentShader.fsh");
		int celShaderProgram = MyUtils.createProgramFromShaders(new int[]{celVertexShader,celFragmentShader});
		CelShader celShaderState = new CelShader(celShaderProgram);
		
		stateMap = new HashMap<String, IShaderState>();
		stateMap.put("PhongShader", phongShaderState);
		stateMap.put("CelShader", celShaderState);
	}
	
	private void setFileManager(){
		fileManager = new OFFFileManager(offvao);
	}
	private void setGeneralCallbacks(){
		generalCallbacks = new GeneralCallbacks(this);
	}

	private long init(String windowTitle){
		glfwInit();
		glfwWindowHint(GLFW_OPENGL_PROFILE,GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_RESIZABLE,GL_FALSE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR,3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR,3);
		long window = glfwCreateWindow(WIDTH, HEIGHT, "Hello", NULL, NULL);
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		glfwSwapInterval(1);
		glfwMakeContextCurrent(window);
		if(window==NULL){
			System.err.println("init() - No se pudo crear la ventana");
			glfwTerminate();
			System.exit(1);
		}
		GLContext.createFromCurrent();
		return window;
	}
	
	private void moveMovables(ArrayList<Movable> movablesList){
		for(Movable movable: movablesList){movable.move();}
	}
	
	public HashMap<String, IShaderState> getStateMap(){return stateMap;}
	public IShaderState getShaderState(){return state;}
	public void setState(IShaderState newstate){state = newstate;}
	public static float getScaleFactor(){return scaleFactor;}
	public static void setScaleFactor(float newfactor){scaleFactor = newfactor;}
	public static float getScaleSpeed(){return scaleSpeed;}
	public static Matrix4f getModel(){return modelMatrix;}
	public static Matrix4f getView(){return viewMatrix;}
	public static Matrix4f getProjection(){return perspectiveMatrix;}
	public static Matrix4f getNormal(){return normalMatrix;}
	public static Camera getCamera(){return camera;}

	public static AmbientLight getAmbientLight() {
		return ambientLight;
	}

	public static void setAmbientLight(AmbientLight ambientLight) {
		MainClass.ambientLight = ambientLight;
	}
	
	public static PhongLight[] getPhongLights() {
		return phongLights;
	}
}
