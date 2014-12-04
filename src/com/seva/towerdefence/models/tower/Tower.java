package com.seva.towerdefence.models.tower;

import java.lang.Math;

import com.seva.towerdefence.frame.MainFrame;
import com.seva.towerdefence.models.GameObject;
import com.seva.towerdefence.models.bullet.Bullet;
import com.seva.towerdefence.models.enemy.Enemy;

public class Tower extends GameObject {

	Bullet bul;
	Enemy target_Enemy;

	String name_Tower;
	int range_Tower;
	int damage_Tower;
	int speed_Tower;
	int bul_Image;

	int level = 1;
	
	int x_Tower;
	int y_Tower;
	
	private int timer_shoot = 0;
	private int check = 0;

	Tower(MainFrame main, int imgIndex, int x, int y, String n, int d, int r, int s) {
		super(main, imgIndex, x, y);
		this.main = main;
		name_Tower = n;
		damage_Tower = d;
		range_Tower = r;
		speed_Tower = s;
		x_Tower = x;
		y_Tower = y;
	}
	
	public GameObject detection_Enemy(int x, int y) {
		int selecting_Enemy = 0;
		
		for(int i = 0; i < (main.Enemys.size() + 1); i++) {
			if (range_Tower < cal_distance(i, x_Tower, y_Tower)) {
				selecting_Enemy = i;
				
				break;
			}
		}

		return main.Enemys.elementAt(selecting_Enemy);
	}
	
	public int cal_distance(int n, int x, int y) {
		int distance = 0;
		
		distance = (int) Math.ceil(Math.sqrt(Math.pow((x - main.Enemys.elementAt(n).position.x), 2)
				- Math.pow((y - main.Enemys.elementAt(n).position.y), 2)));
				
		return distance;
	}

	public boolean upgrade() {
		if (level == 3) {
			return false;
		} else {
			++level;
			return true;
		}
	}

	public int getDamage_Tower() {
		return damage_Tower;
	}

	public void setDamage_Tower(int damage_Tower) {
		this.damage_Tower = damage_Tower;
	}

	public int getRange_Tower() {
		return range_Tower;
	}

	public void setRange_Tower(int range_Tower) {
		this.range_Tower = range_Tower;
	}

	public int getSpeed_Tower() {
		return speed_Tower;
	}

	public void setSpeed_Tower(int speed_Tower) {
		this.speed_Tower = speed_Tower;
	}
	
	public int getBul_Image() {
		return bul_Image;
	}

	public void setBul_Image(int bul_Image) {
		this.bul_Image = bul_Image;
	}
	
	@Override
	public void move(){
		timer_shoot++;
		
		if (timer_shoot == 57) {
			timer_shoot = 0;
			check = (int) Math.floor(57 / speed_Tower);
		}			
		
		if (timer_shoot == check) {
			bul = new Bullet(main, bul_Image, this.x_Tower, this.y_Tower, 
					detection_Enemy(this.x_Tower, this.y_Tower));	
		} else if (timer_shoot > check) {
			check += (int) Math.floor(57 / speed_Tower);
		}
	}
}
