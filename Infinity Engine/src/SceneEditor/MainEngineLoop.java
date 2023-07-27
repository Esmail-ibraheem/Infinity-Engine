package SceneEditor;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import Entities.Camera;
import Entities.Entity;
import ModelData.RawModel;
import ModelData.TexturedModel;
import ObjectLoaderEngine.ObjectLoader;
import RendererEngine.DataLoader;
import RendererEngine.DisplayManager;
import RendererEngine.MainRenderer;
import TexturesData.ModelTexture;

public class MainEngineLoop {

	public static void main(String[] args) {
		DisplayManager.CreateDisplay();
		Camera camera = new Camera();
		DataLoader Loader = new DataLoader() ;
		MainRenderer renderer = new MainRenderer(Loader);
		List<Entity>Entities = new ArrayList<Entity>() ;
		
		Entities.add(new Entity(loadModel("meta", Loader), new Vector3f(4, 1, 0), 0, 0.5f));
		Entities.add(new Entity(loadModel("tea", Loader), new Vector3f(0.3f, 1, 0), 0, 0.34f));
		Entities.add(new Entity(loadModel("dragon", Loader), new Vector3f(-4, 1, 0), 0, 0.3f));
		Entities.add(new Entity(loadModel("sphere", Loader), new Vector3f(3, 1, 0), 0, 0.3f));
		
		while(!Display.isCloseRequested()) {
			camera.CameraMove();
			renderer.render(Entities, camera);
			DisplayManager.UpdateDisplay();
		}
		
		renderer.CleanUp();
		DisplayManager.CloseDisplay();
	}
	private static TexturedModel loadModel(String fileName, DataLoader Loader){
		RawModel model = ObjectLoader.LoadTheObject(fileName, Loader);
		ModelTexture texture = new ModelTexture(Loader.LoadTexture(fileName));
		return new TexturedModel(model, texture);
	}

}
