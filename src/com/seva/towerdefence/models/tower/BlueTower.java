package com.seva.towerdefence.models.tower;

import com.seva.towerdefence.frame.MainFrame;

public class BlueTower extends Tower {

	public BlueTower(MainFrame main, int imgIndex, int x, int y, int level) {
		super(main, 82, x, y, "Blue Tower", 100, 100, 100);
		this.main = main;
		
		this.setBul_Image(124);
	}	
}