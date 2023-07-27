package EntityRendererEngine;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import Entities.Camera;
import Entities.Entity;
import EnvironmentMapRenderer.CubeMap;
import ModelData.RawModel;
import ModelData.TexturedModel;
import ToolBox.Maths;

public class EntityRenderer {
	private CubeMap environmentMap;
	private StaticShader Shader;
	
	public EntityRenderer(Matrix4f ProjectionMatrix, CubeMap environmentMap) {
		this.environmentMap = environmentMap;
		Shader = new StaticShader();
		Shader.StartShading();
		Shader.LoadProjectionMatrix(ProjectionMatrix);
		Shader.ConnectOtherUnits();
		Shader.StopShading();
	}
	public void render(List<Entity>Entities, Camera camera) {
		Shader.StartShading();
		Shader.LoadCameraMatrix(camera);
		BindEnvironmentMap();
		for(Entity entity:Entities) {
			TexturedModel model = entity.getTexturedModel();
			BindModelVertexArrayObject(model);
			LoadModelMatrix(entity);
			BindModelTexture(model);
			GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			UnBindModelVertexArrayObject();
		}
		Shader.StopShading();
	}
	public void CleanUp() {
		Shader.CleanMemory();
	}
	private void BindModelVertexArrayObject(TexturedModel model) {
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVertexArrayObject_ID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
	}
	private void BindEnvironmentMap() {
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, environmentMap.getTexture());
	}
	private void UnBindModelVertexArrayObject() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	private void BindModelTexture(TexturedModel model) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getModelTexture().getID());
	}
	private void LoadModelMatrix(Entity entity) {
		Matrix4f TransformaionMatrix = Maths.CreateTransformationMatrix(entity.getPositions(), 0, entity.getRoty(), 0, entity.getScale());
		Shader.LoadTransformationMatrix(TransformaionMatrix);
	}
}
