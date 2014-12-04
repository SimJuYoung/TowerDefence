package com.seva.towerdefence.models.tower;

import com.seva.towerdefence.frame.MainFrame;

public class BlackTower extends Tower {

	public BlackTower(MainFrame main, int imgIndex, int x, int y, int level) {
		super(main, 97, x, y, "Black Tower", 100, 100, 100);
		this.main = main;
		
		this.setBul_Image(125);
	}	
}