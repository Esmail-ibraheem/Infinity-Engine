package RendererEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {
	private static final int DisplayWidth = 1920;
	private static final int DisplayHeight = 1080;
	private static final int FPS = 120;
	
	public static void CreateDisplay() {
		ContextAttribs Attribs = new ContextAttribs(3,2);
		Attribs.withForwardCompatible(true);
		Attribs.withProfileCore(true);
		try {
			Display.setDisplayMode(new DisplayMode(DisplayWidth, DisplayHeight));
			Display.create(new PixelFormat(), Attribs);
			Display.setTitle("Infinity Engine");
			Display.setResizable(true);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		GL11.glViewport(0, 0, DisplayWidth, DisplayHeight);
	}
	public static void UpdateDisplay() {
		Display.sync(FPS);
		Display.update();
	}
	public static void CloseDisplay() {
		Display.destroy();
	}
}
