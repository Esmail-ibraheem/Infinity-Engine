package ModelData;

public class RawModel {
	private int VertexArrayObject_ID;
	private int VertexCount ;
	
	public RawModel(int VertexArrayObject_ID, int VertexCount) {
		this.VertexArrayObject_ID = VertexArrayObject_ID;
		this.VertexCount = VertexCount;
	}

	public int getVertexArrayObject_ID() {
		return VertexArrayObject_ID;
	}

	public int getVertexCount() {
		return VertexCount;
	}
	
}
