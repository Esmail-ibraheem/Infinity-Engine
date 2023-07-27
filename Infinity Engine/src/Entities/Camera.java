package Entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	private float DistanceFromPlayer = 10;
	private float AngleAroundPlayer = 0;
	private Vector3f Positions = new Vector3f(0,0,0) ;
	private float Pitch = 10;
	private float Yaw = 0;
	private float Roll ;
	
	public Camera() {
		
	}
	public void CameraMove() {
		CalculatePitch();
		CalculateAngleAroundPlayer();
		float HorizentalDistance = CalculateHorizentalDistance();
		float VerticalDistance = CalculateVerticalDistance();
		CalculateCameraPositions(HorizentalDistance, VerticalDistance);
		this.Yaw = 360 - AngleAroundPlayer;
		Yaw%=360 ;
	}
	public Vector3f getPositions() {
		return Positions;
	}
	public float getPitch() {
		return Pitch;
	}
	public float getYaw() {
		return Yaw;
	}
	public float getRoll() {
		return Roll;
	}
	private void CalculateCameraPositions(float HorizentalDistance, float VerticalDistance) {
		float Theta = AngleAroundPlayer;
		float OffsetX = (float) (HorizentalDistance * Math.sin(Math.toRadians(Theta)));
		float OffsetZ = (float) (HorizentalDistance * Math.cos(Math.toRadians(Theta)));
		Positions.x = OffsetX;
		Positions.z = OffsetZ;
		Positions.y = VerticalDistance+2 ;
	}
	private float CalculateHorizentalDistance() {
		return (float) (DistanceFromPlayer * Math.cos(Math.toRadians(Pitch)));
	}
	private float CalculateVerticalDistance() {
		return (float) (DistanceFromPlayer * Math.sin(Math.toRadians(Pitch)));
	}
	private void CalculatePitch() {
		if(Mouse.isButtonDown(1)) {
			float PitchChange = Mouse.getDY()*0.2f;
			Pitch-=PitchChange ;
			if(Pitch<4) {
				Pitch = 4;
			}else if(Pitch>90) {
				Pitch = 90;
			}
		}
	}
	private void CalculateAngleAroundPlayer() {
		if(Mouse.isButtonDown(0)) {
			float AngleAroundPlayerChange = Mouse.getDX()*0.3f;
			AngleAroundPlayer-=AngleAroundPlayerChange;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_R)) {
			AngleAroundPlayer+=10f;
		}
	}
}
