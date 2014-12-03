package com.seva.towerdefence;
import java.awt.Point;


public class Bullet extends gameObject{
	Point velocity;//총알의 이동 속도
	gameObject to;//총알을 누구에게 쏘는가
	int damage;
	Effect explosion;
	
	Bullet(mainFrame main, int imgURL, int x, int y, gameObject to, Point velocity){
		super(main, imgURL, x, y);
		this.to=to;
		this.velocity=velocity;
	}
	
	Bullet(mainFrame main, int x, int y, gameObject to, Point velocity){
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
