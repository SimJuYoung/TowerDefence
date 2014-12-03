package com.seva.towerdefence.models;
import java.awt.Point;

import com.seva.towerdefence.Effect;
import com.seva.towerdefence.frame.MainFrame;


public class Bullet extends GameObject{
	Point velocity;//총알의 이동 속도
	GameObject to;//총알을 누구에게 쏘는가
	int damage;
	Effect explosion;
	
	Bullet(MainFrame main, int imgURL, int x, int y, GameObject to, Point velocity){
		super(main, imgURL, x, y);
		this.to=to;
		this.velocity=velocity;
	}
	
	Bullet(MainFrame main, int x, int y, GameObject to, Point velocity){
		super(main, x, y);
		this.to=to;
		this.velocity=velocity;
	}
	/*
	public void move(){
		position.x-=(velocity.x*100);
		position.y-=(velocity.y*100);
		if(distance(to) <= 1) {// 총알이 명중
			to.Collision(this);
		}
	}*/
}
