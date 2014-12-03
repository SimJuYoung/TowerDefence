package com.seva.towerdefence;

import java.awt.Point;

import com.seva.towerdefence.frame.MainFrame;
import com.seva.towerdefence.models.GameObject;

public class Controller extends GameObject {

	private int localx;
	private int localy;
	public int state;
	public int ControllCubeState; // 0:cancel, 1:up, 2:down, 3:left, 4:right
	private GameObject tower1_red, tower2_yellow, tower3_blue, tower4_black,
			tower5_cancel;
	private GameObject ControllCube;
	private float UImove;
	public int timer; // timer per frame

	public Controller(MainFrame main, int x, int y) {
		super(main, x, y);
		localx = localy = state = timer = 0;
		UImove = 0;
	}

	public Controller(MainFrame main, int ImgNum, int x, int y) {
		super(main, ImgNum, x, y);
		localx = localy = state = timer = 0;
		UImove = 0;
	}

	public Point getLocalPosition() {
		Point p = new Point(localx, localy);
		return p;
	}

	public void setLocalPosition(Point p) {
		localx += p.x;
		localy += p.y;
	}

	void createTowerUI() {
		tower1_red = new GameObject(main, 8, position.x, position.y);
		tower2_yellow = new GameObject(main, 9, position.x, position.y);
		tower3_blue = new GameObject(main, 10, position.x, position.y);
		tower4_black = new GameObject(main, 11, position.x, position.y);
		tower5_cancel = new GameObject(main, 12, position.x, position.y);
		ControllCube = new GameObject(main, 13, position.x, position.y);
		main.getUIs().addElement(tower1_red);
		main.getUIs().addElement(tower2_yellow);
		main.getUIs().addElement(tower3_blue);
		main.getUIs().addElement(tower4_black);
		main.getUIs().addElement(tower5_cancel);
		main.getUIs().addElement(ControllCube);
	}

	void movePosTowerUI() {
		tower1_red.position.y = position.y + (int) UImove;
		tower2_yellow.position.x = position.x + (int) UImove;
		tower3_blue.position.x = position.x - (int) UImove;
		tower4_black.position.y = position.y - (int) UImove;

	}

	void deleteTowerUI() {
		main.getUIs().clear();
	}

	// 노란색 선을 움직인다.
	public void movePosControlBox(int input) { // 0:cancel, 1:up, 2:down, 3:left,
										// 4:right
		switch (ControllCubeState) {
		case 0: // 가운데 있을 때
			switch (input) {
			case 1: // 위를 누른다면
				ControllCube.position = tower1_red.position;
				ControllCubeState = 1;
				break;
			case 2: // 아래를 누른다면
				ControllCube.position = tower4_black.position;
				ControllCubeState = 2;
				break;
			case 3: // 왼쪽을 누른다면
				ControllCube.position = tower3_blue.position;
				ControllCubeState = 3;
				break;
			case 4: // 오른쪽을 누른다면
				ControllCube.position = tower2_yellow.position;
				ControllCubeState = 4;
				break;
			}
			break;
		case 1: // 위에 있을 때
			switch (input) {
			case 2: // 아래를 누른다면
				ControllCube.position = tower5_cancel.position;
				ControllCubeState = 0;
				break;
			case 3: // 왼쪽을 누른다면
				ControllCube.position = tower3_blue.position;
				ControllCubeState = 3;
				break;
			case 4: // 오른쪽을 누른다면
				ControllCube.position = tower2_yellow.position;
				ControllCubeState = 4;
				break;
			}
			break;
		case 2: // 아래에 있을 때
			switch (input) {
			case 1: // 위를 누른다면
				ControllCube.position = tower5_cancel.position;
				ControllCubeState = 0;
				break;
			case 3: // 왼쪽을 누른다면
				ControllCube.position = tower3_blue.position;
				ControllCubeState = 3;
				break;
			case 4: // 오른쪽을 누른다면
				ControllCube.position = tower2_yellow.position;
				ControllCubeState = 4;
				break;
			}
			break;
		case 3: // 왼쪽에 있을 때
			switch (input) {
			case 1: // 위를 누른다면
				ControllCube.position = tower1_red.position;
				ControllCubeState = 1;
				break;
			case 2: // 아래를 누른다면
				ControllCube.position = tower4_black.position;
				ControllCubeState = 2;
				break;
			case 4: // 오른쪽을 누른다면
				ControllCube.position = tower5_cancel.position;
				ControllCubeState = 0;
				break;
			}
			break;
		case 4: // 오른쪽에 있을 때
			switch (input) {
			case 1: // 위를 누른다면
				ControllCube.position = tower1_red.position;
				ControllCubeState = 1;
				break;
			case 2: // 아래를 누른다면
				ControllCube.position = tower4_black.position;
				ControllCubeState = 2;
				break;
			case 3: // 왼쪽을 누른다면
				ControllCube.position = tower5_cancel.position;
				ControllCubeState = 0;
				break;
			}
			break;
		}
	}

	public void move() {
		switch (state) {
		case 0:// 기본상태
			UImove = 0;
			break;
		case 1:// UI펼치기
			if (timer == 0) {
				createTowerUI();
			}
			UImove += 5 - (float) timer / 5;
			timer++;
			movePosTowerUI();
			if (30 <= timer) {
				state++;
				timer = 0;
			}
			break;
		case 2:// UI다펼침

			break;
		case 3:// UI접기
			UImove -= 5 - (float) (29 - timer) / 5;
			timer++;
			movePosTowerUI();
			if (30 <= timer) {
				timer = 0;
				state = 0;
				deleteTowerUI();
			}
			break;
		}
	}

}
