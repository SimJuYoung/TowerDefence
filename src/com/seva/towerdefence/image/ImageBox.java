package com.seva.towerdefence.image;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.seva.towerdefence.GameCanvas;

public class ImageBox {
	public static final String PACKAGE_NAME = "./com/seva/towerdefence/image/";

	private List<Image> images = new ArrayList<Image>();
	private GameCanvas canvas;

	public ImageBox(GameCanvas canvas) {
		this.canvas = canvas;
		images.clear();
		addImages();
	}

	void addImages() {
		addImage("start.png");			 // 0
		addImage("over.jpg"); 			// 1
		addImage("title.png"); 			// 2
		addImage("backGround.png"); 	// 3
		addImage("MainUI.jpg"); 		// 4
		addImage("titleControl.png");	// 5
		addImage("map1.png"); 			// 6
		addImage("controller_basic.png");// 7
		addImage("UI_Tower1.png");		 // 8
		addImage("UI_Tower2.png"); 		// 9
		addImage("UI_Tower3.png");		 // 10
		addImage("UI_Tower4.png");		 // 11
		addImage("UI_Cancel.png");		 // 12
		addImage("ControllCube.png");	//13
		addImage("Number0.png");		//14
		addImage("Number1.png");		//15
		addImage("Number2.png");		//16
		addImage("Number3.png");		//17
		addImage("Number4.png");		//18
		addImage("Number5.png");		//19
		addImage("Number6.png");		//20
		addImage("Number7.png");		//21
		addImage("Number8.png");		//22
		addImage("Number9.png");		//23
		addImage("Brave_1.png");		//24
		addImage("Brave_2.png");		//25
		addImage("Brave_3.png");		//26
		addImage("Brave_4.png");		//27
	}

	void addImage(String resourceName) {
		String resource = PACKAGE_NAME + resourceName;
		try {
			InputStream stream = getClass().getClassLoader()
					.getResourceAsStream(resource);
			Image image = makeImage(stream);
			stream.close();
			if (image != null) {
				images.add(image);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Image getImage(int Index) {
		return images.get(Index);
	}

	private Image makeImage(InputStream stream) {
		Image img = null;
		try {
			img = ImageIO.read(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}
}
