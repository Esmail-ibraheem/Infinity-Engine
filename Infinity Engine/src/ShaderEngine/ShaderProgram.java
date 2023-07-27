package ShaderEngine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public abstract class ShaderProgram {
	private int VertexShader_ID ;
	private int FragmentShader_ID;
	private int ShaderProgram_ID;
	private static FloatBuffer MatrixBuffer = BufferUtils.createFloatBuffer(16) ;
	
	public ShaderProgram(String VertexShaderFile, String FragmentShaderFile) {
		VertexShader_ID = loadShader(VertexShaderFile, GL20.GL_VERTEX_SHADER);
		FragmentShader_ID = loadShader(FragmentShaderFile, GL20.GL_FRAGMENT_SHADER);
		ShaderProgram_ID = GL20.glCreateProgram();
		GL20.glAttachShader(ShaderProgram_ID, VertexShader_ID);
		GL20.glAttachShader(ShaderProgram_ID, FragmentShader_ID);
		BindAllAttributes() ;
		GL20.glLinkProgram(ShaderProgram_ID);
		GL20.glValidateProgram(ShaderProgram_ID);
		GetAllUniformLocations();
	}
	public void StartShading() {
		GL20.glUseProgram(ShaderProgram_ID);
	}
	public void StopShading() {
		GL20.glUseProgram(0);
	}
	protected abstract void BindAllAttributes() ;
	protected abstract void GetAllUniformLocations();
	protected void BindAttribute(int AttributeNumber, String AttributeName) {
		GL20.glBindAttribLocation(ShaderProgram_ID, AttributeNumber, AttributeName);
	}
	protected int GetUniformLocation(String UniformName) {
		return GL20.glGetUniformLocation(ShaderProgram_ID, UniformName);
	}
	protected void LoadInt(int Location, int Value) {
		GL20.glUniform1i(Location, Value);
	}
	
	protected void LoadVector(int Location, Vector3f Vector) {
		GL20.glUniform3f(Location, Vector.x, Vector.y, Vector.z);
	}
	protected void LoadMatrix(int Location, Matrix4f matrix) {
		matrix.store(MatrixBuffer);
		MatrixBuffer.flip();
		GL20.glUniformMatrix4(Location, false, MatrixBuffer);
	}
	private static int loadShader(String file, int type){
		StringBuilder shaderSource = new StringBuilder();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine())!=null){
				shaderSource.append(line).append("//\n");
			}
			reader.close();
		}catch(IOException e){
			e.printStackTrace();
			System.exit(-1);
		}
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE){
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader!");
			System.exit(-1);
		}
		return shaderID;
	}
	public void CleanMemory() {
		StopShading();
		GL20.glDetachShader(ShaderProgram_ID, VertexShader_ID);
		GL20.glDetachShader(ShaderProgram_ID, FragmentShader_ID);
		GL20.glDeleteShader(VertexShader_ID);
		GL20.glDeleteShader(FragmentShader_ID);
		GL20.glDeleteProgram(ShaderProgram_ID);
	}
}
