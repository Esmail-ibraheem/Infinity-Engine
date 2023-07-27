package EnvironmentMapRenderer;

import org.lwjgl.util.vector.Matrix4f;

import ShaderEngine.ShaderProgram;

public class SkyboxShader extends ShaderProgram{
	
	private static final String VertexShaderFile = "src/EnvironmentMapRenderer/SkyboxVertexShader.txt" ;
	private static final String FragmentShaderFile = "src/EnvironmentMapRenderer/SkyboxFragmentShader.txt" ;
	private int LocationProjectionViewMatrix ;
	
	public SkyboxShader() {
		super(VertexShaderFile, FragmentShaderFile);
		
	}

	@Override
	protected void BindAllAttributes() {
		super.BindAttribute(0, "position");
	}
	public void LoadProjectionViewMatrix(Matrix4f matrix) {
		super.LoadMatrix(LocationProjectionViewMatrix, matrix);
	}
	@Override
	protected void GetAllUniformLocations() {
		LocationProjectionViewMatrix = super.GetUniformLocation("projectionViewMatrix") ;
	}

}
