package com.seva.towerdefence.models.enemy;

import com.seva.towerdefence.frame.MainFrame;

public class Enemy_Brave extends Enemy{
	public Enemy_Brave(MainFrame main) {
		super(main, 24);
		speed = 4;
		life = 100;
		ExVelocity();
	}
}
