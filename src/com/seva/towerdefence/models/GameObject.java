package com.seva.towerdefence.models;

import java.awt.Point;

import com.seva.towerdefence.frame.MainFrame;

public class GameObject {
	public Point position;
	public int imgIndex;
	public MainFrame main;
	public boolean Destroy = false;

	// 애니메이션 변수들
	private int animateTimer;
	private int localImageIndex;
	private int FirstlocalImageIndex;

	public GameObject(MainFrame main, int imgIndex, int x, int y) {
		this.main = main;
		position = new Point(x, y);
		this.imgIndex = imgIndex;
		animateTimer = 0;
		localImageIndex = 0;
		FirstlocalImageIndex = imgIndex;
	}

	public GameObject(MainFrame main, int x, int y) {
		this.main = main;
		position = new Point(x, y);
	}

	public void setImageIndex(int imgIndex) {
		this.imgIndex = imgIndex;
	}

	public void move() {
	}

	public void Collision(int damage) {
	}

	public Point Nomalize(Point point) {
		double r = Math.sqrt(point.x * point.x + point.y * point.y);
		Point p = new Point((int) (point.x / r), (int) (point.y / r));
		return p;
	}

	public Point subPoint(Point a, Point b) {
		Point c = new Point(a.x - b.x, a.y - b.y);
		return c;
	}

	protected void animate(int frame) {
		animateTimer++;
		if (7 < animateTimer) {
			animateTimer = 0;
			localImageIndex++;
			if (frame < localImageIndex) {
				localImageIndex = 0;
			}
			imgIndex = FirstlocalImageIndex + localImageIndex;
		}
	}

	public Point MultiplePoint(Point a, double d) {
		Point c = new Point((int) (a.x * d), (int) (a.y * d));
		return c;
	}
}
