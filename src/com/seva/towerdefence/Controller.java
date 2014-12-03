package com.seva.towerdefence;
import java.awt.Point;


public class Controller extends gameObject{

	private	int localx;
	private int localy;
	public int state;
	public int ControllCubeState;	//0:cancel, 1:up, 2:down, 3:left, 4:right
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
		ControllCube = new gameObject(main, 13, position.x, position.y);
		main.UIs.addElement(tower1_red);
		main.UIs.addElement(tower2_yellow);
		main.UIs.addElement(tower3_blue);
		main.UIs.addElement(tower4_black);
		main.UIs.addElement(tower5_cancel);
		main.UIs.addElement(ControllCube);
	}
	void movePosTowerUI(){
		tower1_red.position.y = position.y + (int)UImove;
		tower2_yellow.position.x = position.x + (int)UImove;
		tower3_blue.position.x = position.x - (int)UImove;
		tower4_black.position.y = position.y - (int)UImove;

	}
	void deleteTowerUI(){
		main.UIs.clear();
	}
	
	//����� ���� �����δ�.
	void movePosControlBox(int input){	//0:cancel, 1:up, 2:down, 3:left, 4:right
		switch(ControllCubeState){
		case 0:	//��� ���� ��
			switch(input){
			case 1:	//���� �����ٸ�
				ControllCube.position = tower1_red.position;
				ControllCubeState = 1;
				break;
			case 2:	//�Ʒ��� �����ٸ�
				ControllCube.position = tower4_black.position;
				ControllCubeState = 2;
				break;
			case 3:	//������ �����ٸ�
				ControllCube.position = tower3_blue.position;
				ControllCubeState = 3;
				break;
			case 4:	//�������� �����ٸ�
				ControllCube.position = tower2_yellow.position;
				ControllCubeState = 4;
				break;
			}
			break;
		case 1:	//���� ���� ��
			switch(input){
			case 2:	//�Ʒ��� �����ٸ�
				ControllCube.position = tower5_cancel.position;
				ControllCubeState = 0;
				break;
			case 3:	//������ �����ٸ�
				ControllCube.position = tower3_blue.position;
				ControllCubeState = 3;
				break;
			case 4:	//�������� �����ٸ�
				ControllCube.position = tower2_yellow.position;
				ControllCubeState = 4;
				break;
			}
			break;
		case 2:	//�Ʒ��� ���� ��
			switch(input){
			case 1:	//���� �����ٸ�
				ControllCube.position = tower5_cancel.position;
				ControllCubeState = 0;
				break;
			case 3:	//������ �����ٸ�
				ControllCube.position = tower3_blue.position;
				ControllCubeState = 3;
				break;
			case 4:	//�������� �����ٸ�
				ControllCube.position = tower2_yellow.position;
				ControllCubeState = 4;
				break;
			}
			break;
		case 3:	//���ʿ� ���� ��
			switch(input){
			case 1:	//���� �����ٸ�
				ControllCube.position = tower1_red.position;
				ControllCubeState = 1;
				break;
			case 2:	//�Ʒ��� �����ٸ�
				ControllCube.position = tower4_black.position;
				ControllCubeState = 2;
				break;
			case 4:	//�������� �����ٸ�
				ControllCube.position = tower5_cancel.position;
				ControllCubeState = 0;
				break;
			}
			break;
		case 4:	//�����ʿ� ���� ��
			switch(input){
			case 1:	//���� �����ٸ�
				ControllCube.position = tower1_red.position;
				ControllCubeState = 1;
				break;
			case 2:	//�Ʒ��� �����ٸ�
				ControllCube.position = tower4_black.position;
				ControllCubeState = 2;
				break;
			case 3:	//������ �����ٸ�
				ControllCube.position = tower5_cancel.position;
				ControllCubeState = 0;
				break;
			}
			break;
		}
	}
	
	public void move(){
		switch(state){
		case 0://�⺻����
			UImove = 0;
			break;
		case 1://UI��ġ��
			if(timer==0){
				createTowerUI();
			}
			UImove += 5-(float)timer/5;
			timer++;
			movePosTowerUI();
			if(30 <= timer){
				state++;
				timer = 0;
			}
			break;
		case 2://UI����ħ
			
			break;
		case 3://UI����
			UImove -= 5-(float)(29 - timer)/5;
			timer++;
			movePosTowerUI();
			if(30 <= timer){
				timer = 0;
				state = 0;
				deleteTowerUI();
			}
			break;
		}
	}

}
