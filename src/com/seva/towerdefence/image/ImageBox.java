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

	public ImageBox(GameCanvas canvas) {
		images.clear();
		addImages();
	}

	void addImages() {
		addImage("start.png");			 // 0
		addImage("over.jpg"); 			// 1
		addImage("title.png"); // 2
		addImage("Menu.png");// 3
		addImage("MainUI.jpg");          // 4
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
		addImage("Brave_2.png");		//
		addImage("Brave_3.png");		//
		addImage("Brave_4.png");		//
		addImage("Brave_1r.png");		//28
		addImage("Brave_2r.png");		//
		addImage("Brave_3r.png");		//
		addImage("Brave_4r.png");		//
		addImage("engel_1.png");		//32
		addImage("engel_2.png");		//
		addImage("engel_3.png");		//
		addImage("engel_4.png");		//
		addImage("engel_1r.png");		//36
		addImage("engel_2r.png");		//
		addImage("engel_3r.png");		//
		addImage("engel_4r.png");		//
		addImage("girl_1.png");			//40
		addImage("girl_2.png");			//
		addImage("girl_3.png");			//
		addImage("girl_4.png");			//
		addImage("santa_1.png");		//44
		addImage("santa_2.png");		//
		addImage("santa_3.png");		//
		addImage("santa_4.png");		//
		addImage("santa_1r.png");		//48
		addImage("santa_2r.png");		//
		addImage("santa_3r.png");		//
		addImage("santa_4r.png");		//
		addImage("redLevel1_1.png");	//52
		addImage("redLevel1_2.png");	//
		addImage("redLevel1_3.png");	//
		addImage("redLevel1_4.png");	//
		addImage("redLevel1_5.png");	//
		addImage("redLevel2_1.png");	//57
		addImage("redLevel2_2.png");	//
		addImage("redLevel2_3.png");	//
		addImage("redLevel2_4.png");	//
		addImage("redLevel2_5.png");	//
		addImage("redLevel3_1.png");	//62
		addImage("redLevel3_2.png");	//
		addImage("redLevel3_3.png");	//
		addImage("redLevel3_4.png");	//
		addImage("redLevel3_5.png");	//
		addImage("yellowLevel1_1.png");	//67
		addImage("yellowLevel1_2.png");	//
		addImage("yellowLevel1_3.png");	//
		addImage("yellowLevel1_4.png");	//
		addImage("yellowLevel1_5.png");	//
		addImage("yellowLevel2_1.png");	//72
		addImage("yellowLevel2_2.png");	//
		addImage("yellowLevel2_3.png");	//
		addImage("yellowLevel2_4.png");	//
		addImage("yellowLevel2_5.png");	//
		addImage("yellowLevel3_1.png");	//77
		addImage("yellowLevel3_2.png");	//
		addImage("yellowLevel3_3.png");	//
		addImage("yellowLevel3_4.png");	//
		addImage("yellowLevel3_5.png");	//
		addImage("blueLevel1_1.png");	//82
		addImage("blueLevel1_2.png");	//
		addImage("blueLevel1_3.png");	//
		addImage("blueLevel1_4.png");	//
		addImage("blueLevel1_5.png");	//
		addImage("blueLevel2_1.png");	//87
		addImage("blueLevel2_2.png");	//
		addImage("blueLevel2_3.png");	//
		addImage("blueLevel2_4.png");	//
		addImage("blueLevel2_5.png");	//
		addImage("blueLevel3_1.png");	//92
		addImage("blueLevel3_2.png");	//
		addImage("blueLevel3_3.png");	//
		addImage("blueLevel3_4.png");	//
		addImage("blueLevel3_5.png");	//
		addImage("blackLevel1_1.png");	//97
		addImage("blackLevel1_2.png");	//
		addImage("blackLevel1_3.png");	//
		addImage("blackLevel1_4.png");	//
		addImage("blackLevel1_5.png");	//
		addImage("blackLevel2_1.png");	//102
		addImage("blackLevel2_2.png");	//
		addImage("blackLevel2_3.png");	//
		addImage("blackLevel2_4.png");	//
		addImage("blackLevel2_5.png");	//
		addImage("blackLevel3_1.png");	//107
		addImage("blackLevel3_2.png");	//
		addImage("blackLevel3_3.png");	//
		addImage("blackLevel3_4.png");	//
		addImage("blackLevel3_5.png");	//
		addImage("exp0.png");			//112
		addImage("exp1.png");			//113
		addImage("exp2.png");			//114
		addImage("exp3.png");			//115
		addImage("exp4.png");			//116
		addImage("exp5.png");			//117
		addImage("exp6.png");			//118
		addImage("exp7.png");			//119
		addImage("exp8.png");			//120
		addImage("exp9.png");			//121
		addImage("redBullet.png");		//122
		addImage("yellowBullet.png");	//123
		addImage("blueBullet.png");		//124
		addImage("blackBullet.png");	//125
		addImage("cotton.png");   //126
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






