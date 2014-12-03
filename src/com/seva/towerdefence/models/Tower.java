package com.seva.towerdefence.models;

import com.seva.towerdefence.frame.MainFrame;

public class Tower extends GameObject {

	Bullet bul;
	Enemy target_Enemy;

	String name_Tower;
	int damage;
	int range;
	int speed;
	int level = 1;

	public Tower(MainFrame main, int imgIndex, int x, int y, int range) {
		super(main, imgIndex, x, y);
		this.main = main;
		range = 1500;
	}

	public boolean upgrade() {
		if (level == 3) {
			return false;
		} else {
			++level;
			return true;
		}
	}
}
