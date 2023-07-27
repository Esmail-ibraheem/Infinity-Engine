package TexturesData;

import java.nio.ByteBuffer;

public class TextureData {
	private ByteBuffer buffer;
	private int TextureWidth;
	private int TextureHeight;
	
	public TextureData(ByteBuffer buffer, int TextureWidth, int TextureHeight) {
		this.buffer = buffer;
		this.TextureWidth = TextureWidth;
		this.TextureHeight = TextureHeight;
	}

	public ByteBuffer getBuffer() {
		return buffer;
	}

	public int getTextureWidth() {
		return TextureWidth;
	}

	public int getTextureHeight() {
		return TextureHeight;
	}
	
}
