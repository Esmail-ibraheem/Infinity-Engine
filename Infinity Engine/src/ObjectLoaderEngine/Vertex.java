package ObjectLoaderEngine;

import org.lwjgl.util.vector.Vector3f;

public class Vertex {
	private static final int NOINDEX = -1;
	private Vertex DuplicateVertex;
	private Vector3f Positions;
	private int TextureIndex = NOINDEX;
	private int NormalIndex = NOINDEX;
	private int Index;
	private float Length;
	
	public Vertex(int Index, Vector3f Positions) {
		this.Index = Index;
		this.Positions = Positions;
		this.Length = Positions.length();
	}
	public boolean IsSet() {
		return TextureIndex!=NOINDEX && NormalIndex!=NOINDEX;
	}
	public boolean HasTheSameTextureAndNormalIndex(int newTextureIndex, int newNormalIndex) {
		return newTextureIndex==TextureIndex && newNormalIndex==NormalIndex;
	}
	public Vector3f getPositions() {
		return Positions;
	}
	public int getIndex() {
		return Index;
	}
	public float getLength() {
		return Length;
	}
	public Vertex getDuplicateVertex() {
		return DuplicateVertex;
	}
	public void setDuplicateVertex(Vertex duplicateVertex) {
		DuplicateVertex = duplicateVertex;
	}
	public int getTextureIndex() {
		return TextureIndex;
	}
	public void setTextureIndex(int textureIndex) {
		TextureIndex = textureIndex;
	}
	public int getNormalIndex() {
		return NormalIndex;
	}
	public void setNormalIndex(int normalIndex) {
		NormalIndex = normalIndex;
	}
	
}
