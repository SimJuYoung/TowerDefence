package com.seva.towerdefence;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Vector;

import com.seva.towerdefence.frame.MainFrame;
import com.seva.towerdefence.image.ImageBox;
import com.seva.towerdefence.models.GameObject;

public class GameCanvas extends Canvas {
	private static final long serialVersionUID = 1L;
	MainFrame main;
	GameObject start, over, title, MainUI, Logo, Menu, cotton;
	private GameObject titleControl;
	private Map map;
	ImageBox ImgBox;

	Image dblbuff;
	Graphics gc;
	Font font;
	int step = 0;

	public GameCanvas(MainFrame main){
		this.main=main;
		font=new Font("Default",Font.PLAIN,9);
		start = new GameObject(main, 0, 0,0);
		//
		Logo = new GameObject(main, 1,main.getWidth()/2, main.getHeight() + 130);
		title = new GameObject(main, 2, main.getWidth()/2, main.getHeight()/2);
		Menu = new GameObject(main, 3, main.getWidth()*3/4, -100);
		cotton = new GameObject(main, 126, main.getWidth()/2, (main.getHeight()/2) + 50);
		//
		map = new Map(main,6,main.getWidth()/2, main.getHeight()/2);
		MainUI = new GameObject(main, 4,0,0);
		titleControl = new GameObject(main, 5, main.getWidth()*3/4, -100);
		ImgBox = new ImageBox(this);
	}

	@Override
	public void paint(Graphics g) {
		if (gc == null) {
			dblbuff = createImage(main.getgScreenWidth(),
					main.getgScreenHeight());// 더블
			// 버퍼링
			if (dblbuff == null)
				System.out.println("버퍼 생성 실패");
			else
				gc = dblbuff.getGraphics();
			return;
		}
		update(g);
	}

	@Override
	public void update(Graphics g) {
		if (gc == null)
			return;
		dblpaint();
		g.drawImage(dblbuff, 0, 0, this);
	}

	public void dblpaint() {
		switch (main.getStatus()) {
		case 0:// 타이틀화면
			gcDrawImage(title, main.getWidth(), main.getHeight(), 30);
			gcDrawImage(getTitleControl());
			gc.setColor(new Color(0));
			// gc.drawString("start Game : " + main.gameControl, 340, 200);
			// gc.drawString("exit Game", 340, 100);
			Draw_Title();
			break;
		case 1:// 게임 스타트
			Draw_BG();
			Draw_UI();
			break;
		case 2:// 게임화면
			Draw_BG();
			Draw_Control();
			Draw_gameObject(main.getTowers(), 2 * this.getWidth() / 28,
					(int) (0.5 * this.getHeight() / 5), 15);
			Draw_gameObject(main.getEnemys(), 60, 60);
			Draw_gameObject(main.getBullets(), 1 * this.getWidth() / 28,
					(int) (0.2 * this.getHeight() / 5), 15);
			Draw_gameObject(main.getEffects());
			Draw_gameObject(main.getUIs(), 2 * this.getWidth() / 28,
					(int) (0.5 * this.getHeight() / 5), 15);
			Draw_UI();
			break;
		case 4:// 일시정지
			Draw_BG();
			Draw_gameObject(main.getTowers());
			Draw_gameObject(main.getEnemys(), 60, 60);
			Draw_gameObject(main.getBullets());
			Draw_gameObject(main.getEffects());
			Draw_gameObject(main.getUIs(), 2 * this.getWidth() / 28,
					(int) (0.5 * this.getHeight() / 5), 15);
			Draw_UI();
			break;
		case 3:// 게임오버
			Draw_BG();
			Draw_gameObject(main.getTowers());
			Draw_gameObject(main.getEnemys(), 60, 60);
			Draw_gameObject(main.getBullets());
			Draw_gameObject(main.getEffects());
			gcDrawImage(over);
			Draw_UI();
			break;
		default:
			break;
		}
	}

	private void Draw_BG() {
		gcDrawImage(getMap(), main.getWidth(), main.getHeight() - 30, 15);
	}

	private void Draw_Control() {
		gcDrawImage(main.getControl(), main.getWidth() / 12,
				main.getHeight() / 8, 30);
	}

	private void Draw_UI() {
		Draw_Numbers(main.getGold(), main.getWidth() - 50,
				main.getHeight() - 60, 60);
		Draw_Numbers(main.getLife(), 150, 30, 60);
		// Draw_Numbers(main.Time,main.getWidth()- 50,main.getHeight() - 60,
		// 60);v
	}

	private void Draw_gameObject(Vector<? extends GameObject> vec) {
		int i;
		GameObject buff;
		for (i = 0; i < vec.size(); i++) {
			buff = (vec.elementAt(i));
			gcDrawImage(buff);
		}
	}

	private void Draw_gameObject(Vector<GameObject> vec, int x, int y) {
		int i;
		GameObject buff;
		for (i = 0; i < vec.size(); i++) {
			buff = (vec.elementAt(i));
			gcDrawImage(buff, x, y);
		}
	}

	private void Draw_gameObject(Vector<GameObject> vec, int x, int y,
			int height_transformation) {
		int i;
		GameObject buff;
		for (i = 0; i < vec.size(); i++) {
			buff = (vec.elementAt(i));
			gcDrawImage(buff, x, y, height_transformation);
		}
	}

	public void Draw_Number(int Num, int x, int y, int scale) {
		Image img = ImgBox.getImage(Num + 14);
		gc.drawImage(img, x - scale / 2, main.getHeight() - (scale / 2) - y,
				scale, scale, this);
	}

	public void Draw_Numbers(int Num, int x, int y, int scale) {
		int NumCount = 0; // 자릿수
		int virtualNum = Num;
		while (true) { // 자릿수 구하기
			if (0 < virtualNum) {
				NumCount++;
				virtualNum = virtualNum / 10;
			} else {
				break;
			}
		}
		virtualNum = Num;
		for (int i = 0; i < NumCount; i++) {
			Draw_Number(virtualNum % 10, x - (scale * 2 * i / 5), y, scale);
			virtualNum = virtualNum / 10;
		}
	}

	private void gcDrawImage(GameObject gO) {
		Image img = ImgBox.getImage(gO.imgIndex);
		gc.drawImage(img, gO.position.x - img.getWidth(null) / 2,
				main.getHeight() - (img.getHeight(null) / 2) - gO.position.y,
				this);
	}

	private void gcDrawImage(GameObject gO, int width, int height) {
		Image img = ImgBox.getImage(gO.imgIndex);
		gc.drawImage(img, gO.position.x - width / 2, main.getHeight()
				- (height / 2) - gO.position.y, width, height, this);
	}

	private void gcDrawImage(GameObject gO, int width, int height,
			int height_transformation) {
		Image img = ImgBox.getImage(gO.imgIndex);
		gc.drawImage(img, gO.position.x - width / 2, main.getHeight()
				- (height / 2) - gO.position.y + height_transformation, width,
				height, this);
	}
	
	private void Draw_Title(){
		gcDrawImage(title, main.getWidth(), main.getHeight(), 30);
		gcDrawImage(Logo, main.getWidth()*2/3, main.getHeight()/2, 30);
		gcDrawImage(Menu, main.getWidth()/4, main.getHeight()/5, 30);
		gcDrawImage(cotton, main.getWidth(), main.getHeight(), 30);
		gcDrawImage(titleControl, (main.getWidth())*3/7, main.getHeight()/5, -4);
	}

	public GameObject getTitleControl() {
		return titleControl;
	}

	public void setTitleControl(GameObject titleControl) {
		this.titleControl = titleControl;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public GameObject getLogo() {
		return Logo;
	}

	public void setLogo(GameObject logo) {
		Logo = logo;
	}

	public GameObject getMenu() {
		return Menu;
	}

	public void setMenu(GameObject menu) {
		Menu = menu;
	}

	public GameObject getCotton() {
		return cotton;
	}

	public void setCotton(GameObject cotton) {
		this.cotton = cotton;
	}
}
