package RendererEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import ModelData.RawModel;
import TexturesData.TextureData;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class DataLoader {
	private List<Integer>VertexArrayObjects = new ArrayList<Integer>() ;
	private List<Integer>VertexBufferObjects = new ArrayList<Integer>() ;
	private List<Integer>Textures = new ArrayList<Integer>() ;
	
	public RawModel LoadDataToVertexArrayObject(float[]Positions, float[]TextureUVs, float[]Normals, int[]Indices) {
		int VertexArrayObject_ID = CreateVertexArrayObject();
		CreateElementBufferObject(Indices);
		CreateVertexBufferObject(0, 3, Positions);
		CreateVertexBufferObject(1, 2, TextureUVs);
		CreateVertexBufferObject(2, 3, Normals);
		UnBindVertexArrayObject();
		return new RawModel(VertexArrayObject_ID, Indices.length);
	}
	
	public RawModel LoadDataToVertexArrayObject(float[]Positions) {
		int VertexArrayObject_ID = CreateVertexArrayObject();
		this.CreateVertexBufferObject(0, 3, Positions);
		UnBindVertexArrayObject();
		return new RawModel(VertexArrayObject_ID, Positions.length/3);
	}
	
	private int CreateVertexArrayObject() {
		int VertexArrayObject_ID = GL30.glGenVertexArrays();
		VertexArrayObjects.add(VertexArrayObject_ID);
		GL30.glBindVertexArray(VertexArrayObject_ID);
		return VertexArrayObject_ID;
	}
	private void UnBindVertexArrayObject() {
		GL30.glBindVertexArray(0);
	}
	private void CreateVertexBufferObject(int AttributeNum, int Dimensions, float[]Data) {
		int VertexBufferObject_ID = GL15.glGenBuffers();
		VertexBufferObjects.add(VertexBufferObject_ID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VertexBufferObject_ID);
		FloatBuffer buffer = StoreDataInFloatBuffer(Data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(AttributeNum, Dimensions, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	private FloatBuffer StoreDataInFloatBuffer(float[]data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	private void CreateElementBufferObject(int[]Indices) {
		int ElementBufferObject_ID = GL15.glGenBuffers();
		VertexBufferObjects.add(ElementBufferObject_ID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ElementBufferObject_ID);
		IntBuffer buffer = StoreDataInIntBuffer(Indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	private IntBuffer StoreDataInIntBuffer(int[]Indices) {
		IntBuffer buffer = BufferUtils.createIntBuffer(Indices.length);
		buffer.put(Indices);
		buffer.flip();
		return buffer;
	}
	public int LoadTexture(String TextureName) {
		Texture texture = null ;
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/textures/"+TextureName+".png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int texture_ID = texture.getTextureID();
		Textures.add(texture_ID);
		return texture_ID;
	}
	public int LoadCubeMap(String[]TexturesName) {
		int Texture_ID = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, Texture_ID);
		
		for(int i=0; i<TexturesName.length; i++) {
			TextureData data = decodeTextureFile("res/textures/"+TexturesName[i]+".png") ;
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X+i, 0, GL11.GL_RGBA, data.getTextureWidth(), data.getTextureHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
		}
		GL30.glGenerateMipmap(GL13.GL_TEXTURE_CUBE_MAP);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		
		Textures.add(Texture_ID);
		return Texture_ID;
	}
	private TextureData decodeTextureFile(String fileName) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		try {
			FileInputStream in = new FileInputStream(fileName);
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, Format.RGBA);
			buffer.flip();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Tried to load texture " + fileName + ", didn't work");
			System.exit(-1);
		}
		return new TextureData(buffer, width, height);
	}
	public void CleanMemory() {
		for(int VAO:VertexArrayObjects) {
			GL30.glDeleteVertexArrays(VAO);
		}
		for(int VBO:VertexBufferObjects) {
			GL15.glDeleteBuffers(VBO);
		}
		for(int Texture:Textures) {
			GL11.glDeleteTextures(Texture);
		}
	}
}
