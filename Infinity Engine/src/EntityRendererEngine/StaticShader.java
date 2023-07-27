package EntityRendererEngine;

import org.lwjgl.util.vector.Matrix4f;

import Entities.Camera;
import ShaderEngine.ShaderProgram;
import ToolBox.Maths;

public class StaticShader extends ShaderProgram{
	
	private static final String VertexShaderFile = "src/EntityRendererEngine/VertexShader.txt";
	private static final String FragmentShaderFile = "src/EntityRendererEngine/FragmentShader.txt" ;
	private int LocationTransformationMatrix;
	private int LocationProjectionMatrix;
	private int LocationCameraMatrix;
	private int LocationCameraPositions;
	private int LocationModel_Texture;
	private int Location_enviroMap ;
	
	public StaticShader() {
		super(VertexShaderFile, FragmentShaderFile);
	}

	@Override
	protected void BindAllAttributes() {
		super.BindAttribute(0, "position");
		super.BindAttribute(1, "textureCoordinates");
		super.BindAttribute(2, "normal");
		
	}
	protected void ConnectOtherUnits() {
		super.LoadInt(LocationModel_Texture, 0);
		super.LoadInt(Location_enviroMap, 1);
	}
	@Override
	protected void GetAllUniformLocations() {
		LocationTransformationMatrix = super.GetUniformLocation("transformationMatrix");
		LocationProjectionMatrix = super.GetUniformLocation("projectionMatrix");
		LocationCameraMatrix = super.GetUniformLocation("viewMatrix");
		LocationCameraPositions = super.GetUniformLocation("cameraPosition");
		LocationModel_Texture = super.GetUniformLocation("modelTexture");
		Location_enviroMap = super.GetUniformLocation("enviroMap") ;
	}
	public void LoadTransformationMatrix(Matrix4f matrix) {
		super.LoadMatrix(LocationTransformationMatrix, matrix);
	}
	public void LoadCameraMatrix(Camera camera) {
		Matrix4f ViewMatrix = Maths.CreateCameraMatrix(camera);
		super.LoadMatrix(LocationCameraMatrix, ViewMatrix);
		super.LoadVector(LocationCameraPositions, camera.getPositions());
	}
	public void LoadProjectionMatrix(Matrix4f matrix) {
		super.LoadMatrix(LocationProjectionMatrix, matrix);
	}

}
