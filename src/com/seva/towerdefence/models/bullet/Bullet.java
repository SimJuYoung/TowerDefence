package com.seva.towerdefence.models.bullet;
import java.awt.Point;

import com.seva.towerdefence.Effect;
import com.seva.towerdefence.frame.MainFrame;
import com.seva.towerdefence.models.GameObject;


public class Bullet extends GameObject{
	
	public double speed;
	public int damage;
	public Effect explosion;
	private Point GoVector2;	//총알의 이동 방향
	private GameObject to;		//총알을 누구에게 쏘는가

	
	public Bullet(MainFrame main, int imgURL, int x, int y, GameObject to){
		super(main, imgURL, x, y);
		this.to = to;
		speed = 10;
		damage = 50;
		main.Bullets.addElement(this);
	}
	
	Bullet(MainFrame main, int x, int y, GameObject to){
		super(main, x, y);
		this.to = to;
		speed = 10;
		damage = 50;
		main.Bullets.addElement(this);
	}
	
	private void ExVec(){
		double Nomalization = 1/this.position.distance(to.position);
		GoVector2 = MultiplePoint(subPoint(to.position, this.position),Nomalization * speed);
	}
	
	@Override
	public void move(){
		ExVec();
		this.position.x += GoVector2.x;
		this.position.y += GoVector2.y;
		
		if(position.distance(to.position) <= 4) {	// 총알이 명중
			this.Destroy = true;
			new Effect(main, 112, this.position.x, this.position.y);
			to.Collision(damage);
		}
	}
}
