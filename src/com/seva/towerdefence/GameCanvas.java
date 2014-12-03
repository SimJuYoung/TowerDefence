package com.seva.towerdefence;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Vector;

import com.seva.towerdefence.image.ImageBox;


public class GameCanvas extends Canvas{
	private static final long serialVersionUID = 1L;
	mainFrame main;
	gameObject start, over, title, MainUI, titleControl;
	Map map;
	ImageBox ImgBox;
	
	Image dblbuff;
	Graphics gc;
	Font font;
	int step=0;

	
	GameCanvas(mainFrame main){
		this.main=main;
		font=new Font("Default",Font.PLAIN,9);
		start = new gameObject(main, 0, 0,0);
		over = new gameObject(main, 1,main.getWidth()/2, main.getHeight()/2);
		title = new gameObject(main, 2, main.getWidth()/2, main.getHeight()/2);
		map = new Map(main,6,main.getWidth()/2, main.getHeight()/2);
		MainUI = new gameObject(main, 4,0,0);
		titleControl = new gameObject(main, 5, main.getWidth()/2, main.getHeight()/3);
		ImgBox = new ImageBox(this);
	}
	
	public void paint(Graphics g){
		if(gc==null) {
			dblbuff=createImage(main.gScreenWidth,main.gScreenHeight);//더블 버퍼링
			if(dblbuff==null) System.out.println("버퍼 생성 실패");
			else gc=dblbuff.getGraphics();
			return;
		}
		update(g);
	}
	public void update(Graphics g){
		if(gc==null) return;
		dblpaint();
		g.drawImage(dblbuff,0,0,this);
	}
	public void dblpaint(){
		switch(main.status){
		case 0://타이틀화면
			gcDrawImage(title, main.getWidth(), main.getHeight(), 30);
			gcDrawImage(titleControl);
			gc.setColor(new Color(0));
			//gc.drawString("start Game : " + main.gameControl, 340, 200);
			//gc.drawString("exit Game", 340, 100);
			break;
		case 1://게임 스타트
			Draw_BG();
			Draw_UI();
			break;
		case 2://게임화면
			Draw_BG();
			Draw_Control();
			Draw_gameObject(main.Towers);
			Draw_gameObject(main.Enemys, 60, 60);
			Draw_gameObject(main.Bullets);
			Draw_gameObject(main.Effects);
			Draw_gameObject(main.UIs, (int)(2*this.getWidth()/28), (int)(0.5*this.getHeight()/5), 15);
			Draw_UI();
			break;
		case 4://일시정지
			Draw_BG();
			Draw_gameObject(main.Towers);
			Draw_gameObject(main.Enemys, 60, 60);
			Draw_gameObject(main.Bullets);
			Draw_gameObject(main.Effects);
			Draw_gameObject(main.UIs, (int)(2*this.getWidth()/28), (int)(0.5*this.getHeight()/5), 15);
			Draw_UI();
			break;
		case 3://게임오버
			Draw_BG();
			Draw_gameObject(main.Towers);
			Draw_gameObject(main.Enemys, 60, 60);
			Draw_gameObject(main.Bullets);
			Draw_gameObject(main.Effects);
			gcDrawImage(over);
			Draw_UI();
			break;
		default:
			break;
		}
	}
		
	private void Draw_BG(){
		gcDrawImage(map, main.getWidth(), main.getHeight()-30, 15);
	}
	private void Draw_Control(){
		gcDrawImage(main.Control, main.getWidth()/12, main.getHeight()/8, 30);
	}
	private void Draw_UI(){
		Draw_Numbers(main.Gold,main.getWidth()- 50,main.getHeight() - 60, 60);
		Draw_Numbers(main.Life,150,30, 60);
		//Draw_Numbers(main.Time,main.getWidth()- 50,main.getHeight() - 60, 60);v
	}
	
	private void Draw_gameObject(Vector<gameObject> vec){
		int i;
		gameObject buff;
		for(i=0;i<vec.size();i++){
			buff=(gameObject)(vec.elementAt(i));
			gcDrawImage(buff);
		}
	}
	private void Draw_gameObject(Vector<gameObject> vec, int x, int y){
		int i;
		gameObject buff;
		for(i=0;i<vec.size();i++){
			buff=(gameObject)(vec.elementAt(i));
			gcDrawImage(buff, x, y);
		}
	}
	private void Draw_gameObject(Vector<gameObject> vec, int x, int y, int height_transformation){
		int i;
		gameObject buff;
		for(i=0;i<vec.size();i++){
			buff=(gameObject)(vec.elementAt(i));
			gcDrawImage(buff, x, y, height_transformation);
		}
	}
	public void Draw_Number(int Num, int x, int y, int scale){
		Image img = ImgBox.getImage(Num + 14);
		gc.drawImage(img,x- scale/2, main.getHeight() - (scale/2) - y,scale,scale,this);
	}
	
	public void Draw_Numbers(int Num, int x, int y, int scale){
		int NumCount = 0;		//자릿수
		int virtualNum = Num;
		while(true){			//자릿수 구하기
			if(0 < virtualNum){
				NumCount++;
				virtualNum = (int)virtualNum/10;
			}else{break;}
		}
		virtualNum = Num;
		for(int i = 0; i < NumCount ; i++){
			Draw_Number(virtualNum%10, x-(scale*2*i/5), y, scale);
			virtualNum = (int)virtualNum/10;
		}
	}
	
	private void gcDrawImage(gameObject gO){
		Image img = ImgBox.getImage(gO.imgIndex);
		gc.drawImage(img,gO.position.x-img.getWidth(null)/2, main.getHeight() - (img.getHeight(null)/2) - gO.position.y,this);
	}
	private void gcDrawImage(gameObject gO, int width, int height){
		Image img = ImgBox.getImage(gO.imgIndex);
		gc.drawImage(img,gO.position.x-width/2, main.getHeight() - (height/2) - gO.position.y, width, height,this);
	}
	private void gcDrawImage(gameObject gO, int width, int height, int height_transformation){
		Image img = ImgBox.getImage(gO.imgIndex);
		gc.drawImage(img,gO.position.x-width/2, main.getHeight() - (height/2) - gO.position.y+height_transformation, width, height,this);
	}
	

}
