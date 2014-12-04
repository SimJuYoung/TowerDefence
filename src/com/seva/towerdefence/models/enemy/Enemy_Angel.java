package com.seva.towerdefence.models.enemy;

import com.seva.towerdefence.frame.MainFrame;

public class Enemy_Angel extends Enemy{
	Enemy_Angel(MainFrame main) {
		super(main, 32);
		speed = 2;
		life = 200;
		ExVelocity();
	}
}
