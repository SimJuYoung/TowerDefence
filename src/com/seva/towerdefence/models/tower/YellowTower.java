package com.seva.towerdefence.models.tower;

import com.seva.towerdefence.frame.MainFrame;

public class YellowTower extends Tower {

	public YellowTower(MainFrame main, int imgIndex, int x, int y, int level) {
		super(main, 67, x, y, "Yellow Tower", 100, 100, 100);
		this.main = main;
		
		this.setBul_Image(123);
	}	
}