package com.seva.towerdefence.models.tower;

import com.seva.towerdefence.frame.MainFrame;

public class RedTower extends Tower {

	public RedTower(MainFrame main, int imgIndex, int x, int y, int level) {
		super(main, 52, x, y, "Red Tower", 100, 100, 100);
		this.main = main;
		
		this.setBul_Image(122);
	}
}
