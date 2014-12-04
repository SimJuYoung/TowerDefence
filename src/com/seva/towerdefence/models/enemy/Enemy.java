package com.seva.towerdefence.models.enemy;

import java.awt.Point;

import com.seva.towerdefence.frame.MainFrame;
import com.seva.towerdefence.models.GameObject;

public class Enemy extends GameObject{
	public int life;
	public double speed;
	private Point velocity;
	private Point MainR;
	private int myWay;
	private double Nomalization;

	Enemy(MainFrame main, int imgIndex){
		super(main, imgIndex, main.gameCanvas.getMap().getPositionRouting(0).x,  main.gameCanvas.getMap().getPositionRouting(0).y);
		this.main=main;
		myWay = 0;
		speed = 3;
		life = 100;
		ExVelocity();
		main.Enemys.addElement(this);
	}
	
	
	@Override
	public void move(){
		run();
		animate(4);
		process_Life();
	}
	
	public void Collision(Object gO){
		life -= (int)gO;
	}

	protected void process_Life(){
		if(life <= 0){
			Destroy = true;
		}
	}
	
	protected void run(){
		if(this.position.distance(MainR) < 5){		//정점과의 거리가 5보다 작아지면, 다음 가야할 방향을 설정한다.
			myWay ++;
			if(myWay < main.gameCanvas.getMap().Routing.size()-1){
				ExVelocity();
			}else{
				main.setLife(main.getLife() - 1);
				Destroy = true;
			}
		}else{
			this.position.x += velocity.x;
			this.position.y += velocity.y;
		}
		if(life <= 0){
			Destroy = true;
		}
	}
	
	protected void ExVelocity(){
		MainR = new Point(main.gameCanvas.getMap().getPositionRouting(myWay + 1));
		Nomalization = 1/this.position.distance(MainR);
		velocity = MultiplePoint(subPoint(main.gameCanvas.getMap().getPositionRouting(myWay + 1),this.position), Nomalization * speed);
	}
}
