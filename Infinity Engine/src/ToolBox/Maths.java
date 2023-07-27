package ToolBox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import Entities.Camera;

public class Maths {
	
	public static Matrix4f CreateTransformationMatrix(Vector3f Translate, float rotx, float roty, float rotz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(Translate, matrix, matrix) ;
		Matrix4f.rotate((float) Math.toRadians(rotx), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(roty), new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotz), new Vector3f(0,0,1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale,scale,scale), matrix, matrix) ;
		return matrix ;
	}
	public static Matrix4f CreateCameraMatrix(Camera camera) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0,0,1), matrix, matrix);
		Vector3f CameraPos = camera.getPositions();
		Vector3f NegativeCameraPos = new Vector3f(-CameraPos.x, -CameraPos.y, -CameraPos.z);
		Matrix4f.translate(NegativeCameraPos, matrix, matrix) ;
		return matrix ;
	}
}
