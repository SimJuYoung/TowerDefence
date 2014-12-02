package com.seva.towerdefence;
import java.awt.Point;


public class Controller extends gameObject{

	private	int localx;
	private int localy;
	public int state;
	private gameObject tower1_red, tower2_yellow, tower3_blue, tower4_black, tower5_cancel;
	private gameObject ControllCube;
	private float UImove;
	public int timer;		//timer per frame
	
	Controller(mainFrame main, int x, int y) {
		super(main, x, y);
		localx = localy = state = timer = 0;
		UImove = 0;
	}
	
	Controller(mainFrame main, int ImgNum, int x, int y) {
		super(main,ImgNum, x, y);
		localx = localy = state = timer = 0;
		UImove = 0;
	}
	
	Point getLocalPosition(){
		Point p = new Point(localx, localy);
		return p;
	}	
	
	void setLocalPosition(Point p){
		localx += p.x;
		localy += p.y;
	}
	
	void createTowerUI(){
		tower1_red = new gameObject(main,8, position.x, position.y);
		tower2_yellow = new gameObject(main,9, position.x, position.y);
		tower3_blue = new gameObject(main,10, position.x, position.y);
		tower4_black = new gameObject(main,11, position.x, position.y);
		tower5_cancel = new gameObject(main,12, position.x, position.y);
		main.UIs.add(tower1_red);
		main.UIs.add(tower2_yellow);
		main.UIs.add(tower3_blue);
		main.UIs.add(tower4_black);
		main.UIs.add(tower5_cancel);
	}
	void movePosTowerUI(){
		tower1_red.position.y = position.y + (int)UImove;
		tower2_yellow.position.y = position.x + (int)UImove;
		tower3_blue.position.y = position.x - (int)UImove;
		tower4_black.position.y = position.y - (int)UImove;

	}
	void deleteTowerUI(){
		main.UIs.clear();
	}
	
	public void move(){
		switch(state){
		case 0://기본상태
			UImove = 0;
			timer++;
			break;
		case 1://UI펼치기
			if(timer==0){
				createTowerUI();
			}
			UImove++;
			timer++;
			movePosTowerUI();
			if(60 <= timer){
				state++;
				timer = 0;
			}
			break;
		case 2://UI다펼침
			
			break;
		case 3://UI접기
			UImove--;
			timer++;
			movePosTowerUI();
			if(60 <= timer){
				timer = 0;
				state = 0;
				deleteTowerUI();
			}
			break;
		}
	}

}
