package com.seva.towerdefence;

public class Tower extends gameObject{
	
	Bullet bul;
	Enemy target_Enemy;
	
	String name_Tower;
	int damage;
	int range;
	int speed;
	
	
	
	Tower(mainFrame main, int imgIndex, int x, int y, int range){
		super(main, imgIndex, x, y);
		this.main=main;
		range=1500;
	}
}
