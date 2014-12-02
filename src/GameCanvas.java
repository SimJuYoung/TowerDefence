import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Vector;


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
		over = new gameObject(main, 1,0,0);
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
			break;
		case 2://게임화면
			Draw_BG();
			Draw_gameObject(main.Towers);
			Draw_gameObject(main.Enemys);
			Draw_gameObject(main.Bullets);
			Draw_gameObject(main.Effects);
			Draw_gameObject(main.UIs, (int)(3*this.getWidth()/28), (int)(0.98*this.getHeight()/5));
		case 4://일시정지
			Draw_BG();
			Draw_gameObject(main.Towers);
			Draw_gameObject(main.Enemys);
			Draw_gameObject(main.Bullets);
			Draw_gameObject(main.Effects);
			Draw_gameObject(main.UIs, (int)(3*this.getWidth()/28), (int)(0.98*this.getHeight()/5));
			//Draw_UI();
			break;
		case 3://게임오버
			Draw_BG();
			Draw_gameObject(main.Towers);
			Draw_gameObject(main.Enemys);
			Draw_gameObject(main.Bullets);
			Draw_gameObject(main.Effects);
			gcDrawImage(over);
			Draw_UI();
			break;
		default:
			break;
		}
	}
		
	public void Draw_BG(){
		gcDrawImage(map, main.getWidth(), main.getHeight(), 30);
		gcDrawImage(main.Control, main.getWidth()/12, main.getHeight()/8);
	}
	public void Draw_gameObject(Vector<gameObject> vec){
		int i;
		gameObject buff;
		for(i=0;i<vec.size();i++){
			buff=(gameObject)(vec.elementAt(i));
			gcDrawImage(buff);
		}
	}
	public void Draw_gameObject(Vector<gameObject> vec, int x, int y){
		int i;
		gameObject buff;
		for(i=0;i<vec.size();i++){
			buff=(gameObject)(vec.elementAt(i));
			gcDrawImage(buff, x, y);
		}
	}
	public void Draw_UI(){
		gc.setColor(new Color(0xffffff));
		//gcDrawImage(MainUI);
		String str2 = "[1] Speed down   [2] Speed up   [3] Pause : ";
		gc.setColor(new Color(0));
		gc.drawString(str2, 10,main.gScreenHeight-10);
	}
	
	public void gcDrawImage(gameObject gO){
		Image img = ImgBox.getImage(gO.imgIndex);
		gc.drawImage(img,gO.position.x-img.getWidth(null)/2, main.getHeight() - (img.getHeight(null)/2) - gO.position.y,this);
	}
	public void gcDrawImage(gameObject gO, int width, int height){
		Image img = ImgBox.getImage(gO.imgIndex);
		gc.drawImage(img,gO.position.x-width/2, main.getHeight() - (height/2) - gO.position.y+30, width, height,this);
	}
	public void gcDrawImage(gameObject gO, int width, int height, int height_transformation){
		Image img = ImgBox.getImage(gO.imgIndex);
		gc.drawImage(img,gO.position.x-width/2, main.getHeight() - (height/2) - gO.position.y+30, width, height - height_transformation,this);
	}
	

}
