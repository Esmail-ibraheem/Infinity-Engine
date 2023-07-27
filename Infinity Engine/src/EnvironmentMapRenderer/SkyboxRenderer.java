package EnvironmentMapRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import Entities.Camera;
import ToolBox.Maths;

public class SkyboxRenderer {
	private CubeMap cubeMap ;
	private SkyboxShader Shader;
	private Matrix4f ProjectionMatrix ;
	
	public SkyboxRenderer(CubeMap cubeMap, Matrix4f ProjectionMatrix) {
		this.cubeMap = cubeMap;
		Shader = new SkyboxShader() ;
		this.ProjectionMatrix = ProjectionMatrix; 
	}
	public void render(Camera camera) {
		Shader.StartShading();
		ProjectionViewMatrix(camera);
		BindTexture();
		BindCubeVertexArrayObject();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cubeMap.getCube().getVertexCount());
		UnBindCubeVertexArrayObject();
		Shader.StopShading();
	}
	public void CleanUp() {
		Shader.CleanMemory();
	}
	private void BindCubeVertexArrayObject() {
		GL30.glBindVertexArray(cubeMap.getCube().getVertexArrayObject_ID());
		GL20.glEnableVertexAttribArray(0);
	}
	private void UnBindCubeVertexArrayObject() {
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
	private void BindTexture() {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, cubeMap.getTexture());
	}
	private void ProjectionViewMatrix(Camera camera) {
		Matrix4f ViewMatrix = Maths.CreateCameraMatrix(camera);
		Matrix4f ProjectionViewMatrix = Matrix4f.mul(ProjectionMatrix, ViewMatrix, null);
		Shader.LoadProjectionViewMatrix(ProjectionViewMatrix);
	}
}
