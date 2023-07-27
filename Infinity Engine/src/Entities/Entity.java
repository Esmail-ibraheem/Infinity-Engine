package Entities;

import org.lwjgl.util.vector.Vector3f;

import ModelData.TexturedModel;

public class Entity {
	private TexturedModel texturedModel;
	private Vector3f positions;
	private float roty ;
	private float scale;
	
	public void IncreaseRotation(float dy) {
		roty+=dy ;
	}

	public Entity(TexturedModel texturedModel, Vector3f positions, float roty, float scale) {
		this.texturedModel = texturedModel;
		this.positions = positions;
		this.roty = roty;
		this.scale = scale;
	}

	public TexturedModel getTexturedModel() {
		return texturedModel;
	}

	public Vector3f getPositions() {
		return positions;
	}

	public float getRoty() {
		return roty;
	}

	public float getScale() {
		return scale;
	}
	
	
}
