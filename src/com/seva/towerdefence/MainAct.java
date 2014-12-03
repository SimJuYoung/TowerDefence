package com.seva.towerdefence;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Vector;


public class MainAct {
	public static void main(String[] args) {
		mainFrame mf=new mainFrame();
	}
}
class mainFrame extends Frame implements KeyListener, Runnable{
	
	private static final long serialVersionUID = 1L;
	public final static int UP_PRESSED		= 0x001;
	public final static int DOWN_PRESSED	= 0x002;
	public final static int LEFT_PRESSED	= 0x004;
	public final static int RIGHT_PRESSED	= 0x008;
	public final static int FIRE_PRESSED	= 0x010;
	
	GameCanvas gameCanvas;
	int gScreenWidth=800;		//게임 창 크기
	int gScreenHeight=600;
	Thread mainwork;			//스레드 객체
	boolean roof=true;			//스레드 루프 정보

	//게임 제어를 위한 변수
	int status;					//게임의 상태
	int delay;					//루프 딜레이. 1/1000초 단위.
	long pretime;				//루프 간격을 조절하기 위한 시간 체크값
	int keybuff;				//키 버퍼값
	int timer;
	private int enemyWave;		//적의 웨이브 순서
	private int enemyNumber;	//생성된 적의 숫자
	
	//게임용 변수
	int gameControl;			//게임 흐름 컨트롤
	Controller Control;			//게임 중에 조작할 수 있도록 만들어 주는 컨트롤러
	int Gold;
	int Life;

	
	Enemy testenemy;
	
	Vector<gameObject> Enemys = new Vector<gameObject>();		//enemy들을 저장할 배열
	Vector<gameObject> Towers = new Vector<gameObject>();		//Tower들을 저장할 배열
	Vector<gameObject> Bullets = new Vector<gameObject>();		//Bullet들을 저장할 배열
	Vector<gameObject> Effects = new Vector<gameObject>();		//Effect들을 저장할 배열
	Vector<gameObject> UIs = new Vector<gameObject>();			//컨트롤러 UI들을 저장할 배열
	//Vector<gameObject> gO 하나에다가 이 다섯가지를 전부 묶어서 관리할 수도 있겠지만
	//출력할시 제일 먼저 맵을 그리고 타워를 그리고 유닛을 그리고 미사일을 그리고 이펙트를 그리고 UI 를 그리도록 설계하기 위해
	//따로 나누어 놓는 것이 좋다고 판단
	
	mainFrame(){
		setBackground(new Color(0xffffff));
		setTitle("Tower Defence");
		setLayout(null);
		setBounds(100,100,gScreenWidth,gScreenHeight);
		setResizable(false);
		setVisible(true);
		addKeyListener(this);
		addWindowListener(new MyWindowAdapter());
		gameCanvas=new GameCanvas(this);
		gameCanvas.setBounds(0,0,gScreenWidth,gScreenHeight);
		add(gameCanvas);
		init();
	}
	public void init(){
		status=0;
		delay=17;// 17/1000초 = 58 (프레임/초)
		keybuff=0;
		mainwork=new Thread(this);
		mainwork.start();
		Gold = 300;	//처음 돈
		gameControl = 0;
		gameCanvas.repaint();
		timer = 0;
		enemyNumber = 0;
		enemyWave = 0;
		Life = 20;
		Control = new Controller(this, 7, (int)(3*this.getWidth()/28), (int)(0.98*this.getHeight()/5));
	}
	public void run(){
		//run 함수는 어디에도 실행되는 부분이 없다. 아무래도 자동적으로 실행되는 모양이다.
		try{//게임을 동일한 속도로 동작하도록 만들어 준다
			while(roof){
				pretime=System.currentTimeMillis();
				gameCanvas.repaint();	//다시 그리기
				process();				//동작 실행
				if(System.currentTimeMillis()-pretime<delay) Thread.sleep(delay-System.currentTimeMillis()+pretime);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	//키 입력부
	public void keyPressed(KeyEvent e) {
		if(status==2){
			switch(e.getKeyCode()){
			case KeyEvent.VK_SPACE:
				keybuff|=FIRE_PRESSED;
				break;
			case KeyEvent.VK_LEFT:
				keybuff|=LEFT_PRESSED;
				break;
			case KeyEvent.VK_UP:
				keybuff|=UP_PRESSED;
				break;
			case KeyEvent.VK_RIGHT:
				keybuff|=RIGHT_PRESSED;
				break;
			case KeyEvent.VK_DOWN:
				keybuff|=DOWN_PRESSED;
				break;
			default:
				break;
			}
		} else if(status!=2) keybuff=e.getKeyCode();
	}
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_SPACE:
			keybuff&=~FIRE_PRESSED;
			break;
		case KeyEvent.VK_LEFT:
			keybuff&=~LEFT_PRESSED;
			break;
		case KeyEvent.VK_UP:
			keybuff&=~UP_PRESSED;
			break;
		case KeyEvent.VK_RIGHT:
			keybuff&=~RIGHT_PRESSED;
			break;
		case KeyEvent.VK_DOWN:
			keybuff&=~DOWN_PRESSED;
			break;
		}
	}
	public void keyTyped(KeyEvent e) {}
	//키 입력부(end)
	
	
	private void process(){
		switch(status){
		case 0://타이틀화면
			if(keybuff==KeyEvent.VK_SPACE) {
				Init_GAME();
				if(gameControl == 1){
					System.exit(1);
				}else{
					status = 1;
				}
			}
			if(keybuff==KeyEvent.VK_UP) {
				gameControl = 0;
				gameCanvas.titleControl.position.y = gScreenHeight/3;
			}
			if(keybuff==KeyEvent.VK_DOWN) {
				gameControl = 1;
				gameCanvas.titleControl.position.y = gScreenHeight/6;
			}
			break;
		case 1://스타트
			process_Start();
			break;
		case 2://게임화면
			process_GameObject(Enemys);
			process_GameObject(Bullets);
			process_GameObject(Towers);
			process_GameObject(Effects);
			process_GameObject(UIs);
			process_GameObject(Control);
			process_Game();

			
			break;
		case 3://게임오버
			process_GameObject(Enemys);
			process_GameObject(Bullets);
			process_GameObject(Towers);
			process_GameObject(Effects);
			if(gameControl++ >= 200 && keybuff==KeyEvent.VK_SPACE){
				status=0;
				Init_GAME();
			}
			break;
		case 4://일시정지
			if(gameControl++>=200&&keybuff==KeyEvent.VK_SPACE) status=2;
			break;
		default:
			break;
		}
	}
	public void Init_GAME(){
		keybuff=0;
		Enemys.clear();
		Towers.clear();
		Bullets.clear();
		Effects.clear();
		Life = 20;
	}
	
	public void process_Start(){
		status=2;
	}
	
	public void process_Game(){
		if(Life <= 0){status = 3;}		//게임오버
		process_Enemy();
		process_Controller();
	}
	
	public void process_Controller(){
		switch(Control.state){
		case 0:	//기본상태(UI가 펼쳐지지 않은 상태)
			switch(keybuff){
			case 0:
				break;
			case FIRE_PRESSED:
				if(gameCanvas.map.Route[Control.getLocalPosition().x][Control.getLocalPosition().y] != 0){
					Control.state ++;	//UI를 펼치는 상태로 증가
				}
				break;
			case UP_PRESSED://아래의 네가지 키 입력값으로는 맵 사이에 UI를 움직임
				if(Control.getLocalPosition().y < 7){
					Control.setLocalPosition(new Point(0, 1));
					Control.position.y += (getHeight()-30)/10;
				}
				keybuff = 0;
				break;
			case LEFT_PRESSED:
				if(0 < Control.getLocalPosition().x){
					Control.setLocalPosition(new Point(-1, 0));
					Control.position.x -= getWidth()/14;
				}
				keybuff = 0;
				break;
			case RIGHT_PRESSED:
				if(Control.getLocalPosition().x < 11){
					Control.setLocalPosition(new Point(1, 0));
					Control.position.x += getWidth()/14;
				}
				keybuff = 0;
				break;
			case DOWN_PRESSED:
				if(0 < Control.getLocalPosition().y){
					Control.setLocalPosition(new Point(0, -1));
					Control.position.y -= (getHeight()-30)/10;
				}
				keybuff = 0;
				break;
			default:
				keybuff=0;
				break;
			}
			break;
		case 1:	//UI펼치기
			//이 때는 아무런 키 값을 받지 않는다.(입력에 따른 동작을 하지 않는다)
			break;
		case 2:	//UI다펼침
			int keyPress = -1;
			switch(keybuff){
			case 0:
				break;
			case FIRE_PRESSED:
				switch(Control.ControllCubeState){	//0:cancel, 1:up, 2:down, 3:left, 4:right
				case 0:	//cancel
					break;
				case 1:	//create redTower
					CreateTower(1);
					break;
				case 2:	//create blackTower
					CreateTower(4);
					break;
				case 3:	//create blueTower
					CreateTower(3);
					break;
				case 4:	//create yellowTower
					CreateTower(2);
					break;
				}
				Control.state ++;
				break;
			case UP_PRESSED:	//컨트롤러를 위로 움직임
				keyPress = 1;
				keybuff = 0;
				break;
			case LEFT_PRESSED:	//컨트롤러를 왼쪽으로 움직임
				keyPress = 3;
				keybuff = 0;
				break;
			case RIGHT_PRESSED:	//컨트롤러를 오른쪽으로 움직임
				keyPress = 4;
				keybuff = 0;
				break;
			case DOWN_PRESSED:	//컨트롤러를 아래로 움직임
				keyPress = 2;
				keybuff = 0;
				break;
			default:
				keybuff=0;
				break;
			}
			Control.movePosControlBox(keyPress);
			keyPress = -1;
			break;
		case 3:	//UI접기
			//이 때는 아무런 키 값을 받지 않는다.(입력에 따른 동작을 하지 않는다)
			break;
		}
	}

	private void CreateTower(int TowerNumber){		//TowerNumber 1:red, 2:yellow, 3:blue, 4:black
		/*
		if(TowerNumber == 1){
			int ElementNumber = gameCanvas.map.Route[Control.getLocalPosition().x][Control.getLocalPosition().y];
			//천의자리와 백의자리 두 수는 main.Towers 안에서 자기가 어느자리에 있는지를 저장하고,
			//십의자리와 일의자리 두 수는 현재 타워의 종류를 저장한다.
			//ex) ElementNumber = 1203 -> Towers 배열의 12번째 Element에 red2 타입의 타워가 지어져 있다.
			//타워의 종류 : 0:적이 지나가는 길(타워를 지을 수 없음), 1:아무타워없음(타워를 지을 수 있음)
			//			2:red1,		3:red2,		4:red3
			//			5:yellow1,	6:yellow2,	7:yellow3
			//			8:blue1,	9:blue2,	10:blue3
			//			11:black1,	12:black2,	13:black3
			//아직 타워 class 가 만들어지기 전이므로 타워 class 가 만들어지면 그 타입명으로 바꿉니다.
			//테스트를 안해본거라 좀 겁나네요. 아마 어딘가에 문법이 틀릴겁니다.
			if(ElementNumber%100 == 1){
				redTower red = new redTower(main, imgNum, Control.position.x, Control.position.y);			//현재 컨트롤러가 있는 위치에 오브젝트를 생성
				if(red.Gold <= this.Gold){
					Towers.addElement(red);
					this.Gold -= red.Gold;
					gameCanvas.map.Route[Control.getLocalPosition().x][Control.getLocalPosition().y] = Towers.size()*100 + 2;
				}
			}else if(ElementNumber%100 == 2){																//한번 지은 적이 있어서 업그레이드를 하려는 상태
				redTower2 red = new redTower2(main, imgNum, Control.position.x, Control.position.y);		//현재 컨트롤러가 있는 위치에 오브젝트를 생성
				if(red.Gold <= this.Gold){																	//원래 있던 타워를 지우고 새로운 타워를 그 자리에 놓는다
					Towers.insertElementAt(red, ElementNumber/100);
					Towers.removeElementAt((ElementNumber/100) + 1);
					this.Gold -= red.Gold;
					gameCanvas.map.Route[Control.getLocalPosition().x][Control.getLocalPosition().y] = 100*(ElementNumber/100) + 3;
				}
			}else if(ElementNumber%100 == 3){																//한번 업그레이드 한 적이 있어서 마지막으로 업그레이드 하려는 상태
				redTower3 red = new redTower3(main, imgNum, Control.position.x, Control.position.y);		//현재 컨트롤러가 있는 위치에 오브젝트를 생성
				if(red.Gold <= this.Gold){																	//원래 있던 타워를 지우고 새로운 타워를 그 자리에 놓는다
					Towers.insertElementAt(red, ElementNumber/100);
					Towers.removeElementAt((ElementNumber/100) + 1);
					this.Gold -= red.Gold;
					gameCanvas.map.Route[Control.getLocalPosition().x][Control.getLocalPosition().y] = 100*(ElementNumber/100) + 4;
				}
			}
		}
		if(TowerNumber == 2){
			int ElementNumber = gameCanvas.map.Route[Control.getLocalPosition().x][Control.getLocalPosition().y];
			if(ElementNumber%100 == 1){
				yellowTower yellow = new yellowTower(main, imgNum, Control.position.x, Control.position.y);
				if(yellow.Gold <= this.Gold){
					Towers.addElement(yellow);
					this.Gold -= yellow.Gold;
					gameCanvas.map.Route[Control.getLocalPosition().x][Control.getLocalPosition().y] = Towers.size()*100 + 5;
				}
			}else if(ElementNumber%100 == 2){
				yellowTower2 yellow = new yellowTower2(main, imgNum, Control.position.x, Control.position.y);
				if(yellow.Gold <= this.Gold){
					Towers.insertElementAt(yellow, ElementNumber/100);
					Towers.removeElementAt((ElementNumber/100) + 1);
					this.Gold -= yellow.Gold;
					gameCanvas.map.Route[Control.getLocalPosition().x][Control.getLocalPosition().y] = 100*(ElementNumber/100) + 6;
				}
			}else if(ElementNumber%100 == 3){
				yellowTower3 yellow = new yellowTower3(main, imgNum, Control.position.x, Control.position.y);
				if(yellow.Gold <= this.Gold){
					Towers.insertElementAt(yellow, ElementNumber/100);
					Towers.removeElementAt((ElementNumber/100) + 1);
					this.Gold -= yellow.Gold;
					gameCanvas.map.Route[Control.getLocalPosition().x][Control.getLocalPosition().y] = 100*(ElementNumber/100) + 7;
				}
			}
		}
		if(TowerNumber == 3){
			int ElementNumber = gameCanvas.map.Route[Control.getLocalPosition().x][Control.getLocalPosition().y];
			if(ElementNumber%100 == 1){
				blueTower blue = new blueTower(main, imgNum, Control.position.x, Control.position.y);
				if(blue.Gold <= this.Gold){
					Towers.addElement(blue);
					this.Gold -= blue.Gold;
					gameCanvas.map.Route[Control.getLocalPosition().x][Control.getLocalPosition().y] = Towers.size()*100 + 8;
				}
			}else if(ElementNumber%100 == 2){
				blueTower2 blue = new blueTower2(main, imgNum, Control.position.x, Control.position.y);
				if(blue.Gold <= this.Gold){
					Towers.insertElementAt(blue, ElementNumber/100);
					Towers.removeElementAt((ElementNumber/100) + 1);
					this.Gold -= blue.Gold;
					gameCanvas.map.Route[Control.getLocalPosition().x][Control.getLocalPosition().y] = 100*(ElementNumber/100) + 9;
				}
			}else if(ElementNumber%100 == 3){
				blueTower3 blue = new blueTower3(main, imgNum, Control.position.x, Control.position.y);
				if(blue.Gold <= this.Gold){
					Towers.insertElementAt(blue, ElementNumber/100);
					Towers.removeElementAt((ElementNumber/100) + 1);
					this.Gold -= blue.Gold;
					gameCanvas.map.Route[Control.getLocalPosition().x][Control.getLocalPosition().y] = 100*(ElementNumber/100) + 10;
				}
			}
		}
		if(TowerNumber == 4){
			int ElementNumber = gameCanvas.map.Route[Control.getLocalPosition().x][Control.getLocalPosition().y];
			if(ElementNumber%100 == 1){
				blackTower black = new blackTower(main, imgNum, Control.position.x, Control.position.y);
				if(black.Gold <= this.Gold){
					Towers.addElement(black);
					this.Gold -= black.Gold;
					gameCanvas.map.Route[Control.getLocalPosition().x][Control.getLocalPosition().y] = Towers.size()*100 + 11;
				}
			}else if(ElementNumber%100 == 2){
				blackTower2 black = new blackTower2(main, imgNum, Control.position.x, Control.position.y);
				if(black.Gold <= this.Gold){
					Towers.insertElementAt(black, ElementNumber/100);
					Towers.removeElementAt((ElementNumber/100) + 1);
					this.Gold -= black.Gold;
					gameCanvas.map.Route[Control.getLocalPosition().x][Control.getLocalPosition().y] = 100*(ElementNumber/100) + 12;
				}
			}else if(ElementNumber%100 == 3){
				blackTower3 black = new blackTower3(main, imgNum, Control.position.x, Control.position.y);
				if(black.Gold <= this.Gold){
					Towers.insertElementAt(black, ElementNumber/100);
					Towers.removeElementAt((ElementNumber/100) + 1);
					this.Gold -= black.Gold;
					gameCanvas.map.Route[Control.getLocalPosition().x][Control.getLocalPosition().y] = 100*(ElementNumber/100) + 13;
				}
			}
		}
		*/
	}
	
	private void process_GameObject(Vector<gameObject> Vec){
		gameObject buff;
		for(int i = 0 ; i < Vec.size() ; i ++){
			buff = Vec.elementAt(i);
			buff.move();
			if(buff.Destroy){
				Vec.remove(i);
			}
		}
	}
	
	private void process_GameObject(gameObject gO){
		gO.move();
	}
	
	private void process_Enemy(){
		
		switch(enemyWave){
		case 0:		//첫번째 wave
			if(timer == 0){
				testenemy = new Enemy(this, 24);
				Enemys.addElement(testenemy);
				timer++;
				enemyNumber++;
			}else{
				timer++;
				if(40 < timer){	timer =0;	}
			}
			if(10 <= enemyNumber){
				enemyWave++;
				enemyNumber = 0;
				timer = 0;
			}
			break;
		case 1:		//다음 wave가 오기 전의 시간
			timer++;
			if(300 < timer){
				enemyWave--;
			}
			break;
		case 2:		//이 때부터 다른 Enemy가 등장하면 좋겠지만 아직 안만들어졌으므로 대기
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			break;
		}
	}
}


class MyWindowAdapter extends WindowAdapter{
	MyWindowAdapter(){}
	public void windowClosing(WindowEvent e) {
		Window wnd = e.getWindow();
		wnd.setVisible(false);
		wnd.dispose();
		System.exit(0);
	}
}


class comparer implements Comparator{
	public int compare(Object arg0, Object arg1) {
		return ((gameObject)arg0).position.y > ((gameObject)arg1).position.y ? 1 : 0 ;
	}
}