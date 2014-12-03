package com.seva.towerdefence.models;

import java.awt.Point;

import com.seva.towerdefence.frame.MainFrame;

public class Enemy extends GameObject {
	public int life;
	public double speed;
	private Point velocity;
	private Point MainR;
	private int myWay;
	private double Nomalization;

	public Enemy(MainFrame main, int imgIndex) {
		super(main, imgIndex, main.getGameCanvas().getMap().getPositionRouting(0).x,
				main.getGameCanvas().getMap().getPositionRouting(0).y);
		this.main = main;
		myWay = 0;
		speed = 3;
		life = 100;
		MainR = new Point(
				main.getGameCanvas().getMap().getPositionRouting(myWay + 1));
		Nomalization = 1 / this.position.distance(MainR);
		velocity = MultiplePoint(
				subPoint(
						main.getGameCanvas().getMap().getPositionRouting(myWay + 1),
						this.position), Nomalization * speed);

	}

	public void move() {
		run();
		animate(3);
		if (life <= 0) {
			Destroy = true;
		}
	}

	protected void run() {
		if (this.position.distance(MainR) < 5) { // 정점과의 거리가 5보다 작아지면, 다음 가야할
													// 방향을 설정한다.
			myWay++;
			if (myWay < main.getGameCanvas().getMap().Routing.size() - 1) {
				MainR = new Point(
						main.getGameCanvas().getMap().getPositionRouting(myWay + 1));
				Nomalization = 1 / this.position.distance(MainR);
				velocity = MultiplePoint(
						subPoint(
								main.getGameCanvas().getMap()
										.getPositionRouting(myWay + 1),
								this.position), Nomalization * speed);
			} else {
				main.setLife(main.getLife() - 1);
				Destroy = true;
			}
		} else {
			this.position.x += velocity.x;
			this.position.y += velocity.y;
		}
	}
}
