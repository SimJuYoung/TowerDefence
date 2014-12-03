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
	int gScreenWidth=800;		//���� â ũ��
	int gScreenHeight=600;
	Thread mainwork;			//������ ��ü
	boolean roof=true;			//������ ���� ����

	//���� ��� ���� ����
	int status;					//������ ����
	int delay;					//���� ������. 1/1000�� ����.
	long pretime;				//���� ������ �����ϱ� ���� �ð� üũ��
	int keybuff;				//Ű ���۰�
	int timer;
	private int enemyWave;		//���� ���̺� ����
	private int enemyNumber;	//������ ���� ����
	
	//���ӿ� ����
	int gameControl;			//���� �帧 ��Ʈ��
	Controller Control;			//���� �߿� ������ �� �ֵ��� ����� �ִ� ��Ʈ�ѷ�
	int Gold;
	int Life;

	
	Enemy testenemy;
	
	Vector<gameObject> Enemys = new Vector<gameObject>();		//enemy���� ������ �迭
	Vector<gameObject> Towers = new Vector<gameObject>();		//Tower���� ������ �迭
	Vector<gameObject> Bullets = new Vector<gameObject>();		//Bullet���� ������ �迭
	Vector<gameObject> Effects = new Vector<gameObject>();		//Effect���� ������ �迭
	Vector<gameObject> UIs = new Vector<gameObject>();			//��Ʈ�ѷ� UI���� ������ �迭
	//Vector<gameObject> gO �ϳ����ٰ� �� �ټ������� ���� ��� ������ ���� �ְ�����
	//����ҽ� ���� ���� ���� �׸��� Ÿ���� �׸��� ������ �׸��� �̻����� �׸��� ����Ʈ�� �׸��� UI �� �׸����� �����ϱ� ����
	//���� ������ ���� ���� ���ٰ� �Ǵ�
	
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
		delay=17;// 17/1000�� = 58 (������/��)
		keybuff=0;
		mainwork=new Thread(this);
		mainwork.start();
		Gold = 300;	//ó�� ��
		gameControl = 0;
		gameCanvas.repaint();
		timer = 0;
		enemyNumber = 0;
		enemyWave = 0;
		Life = 20;
		Control = new Controller(this, 7, (int)(3*this.getWidth()/28), (int)(0.98*this.getHeight()/5));
	}
	public void run(){
		//run �Լ��� ��𿡵� ����Ǵ� �κ��� ����. �ƹ����� �ڵ������� ����Ǵ� ����̴�.
		try{//������ ������ �ӵ��� �����ϵ��� ����� �ش�
			while(roof){
				pretime=System.currentTimeMillis();
				gameCanvas.repaint();	//�ٽ� �׸���
				process();				//���� ����
				if(System.currentTimeMillis()-pretime<delay) Thread.sleep(delay-System.currentTimeMillis()+pretime);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	//Ű �Էº�
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
	//Ű �Էº�(end)
	
	
	private void process(){
		switch(status){
		case 0://Ÿ��Ʋȭ��
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
		case 1://��ŸƮ
			process_Start();
			break;
		case 2://����ȭ��
			process_GameObject(Enemys);
			process_GameObject(Bullets);
			process_GameObject(Towers);
			process_GameObject(Effects);
			process_GameObject(UIs);
			process_GameObject(Control);
			process_Game();

			
			break;
		case 3://���ӿ���
			process_GameObject(Enemys);
			process_GameObject(Bullets);
			process_GameObject(Towers);
			process_GameObject(Effects);
			if(gameControl++ >= 200 && keybuff==KeyEvent.VK_SPACE){
				status=0;
				Init_GAME();
			}
			break;
		case 4://�Ͻ�����
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
		if(Life <= 0){status = 3;}		//���ӿ���
		process_Enemy();
		process_Controller();
	}
	
	public void process_Controller(){
		switch(Control.state){
		case 0:	//�⺻����(UI�� �������� ���� ����)
			switch(keybuff){
			case 0:
				break;
			case FIRE_PRESSED:
				if(gameCanvas.map.Route[Control.getLocalPosition().x][Control.getLocalPosition().y] != 0){
					Control.state ++;	//UI�� ��ġ�� ���·� ����
				}
				break;
			case UP_PRESSED://�Ʒ��� �װ��� Ű �Է°����δ� �� ���̿� UI�� ������
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
		case 1:	//UI��ġ��
			//�� ���� �ƹ��� Ű ���� ���� �ʴ´�.(�Է¿� ���� ������ ���� �ʴ´�)
			break;
		case 2:	//UI����ħ
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
			case UP_PRESSED:	//��Ʈ�ѷ��� ���� ������
				keyPress = 1;
				keybuff = 0;
				break;
			case LEFT_PRESSED:	//��Ʈ�ѷ��� �������� ������
				keyPress = 3;
				keybuff = 0;
				break;
			case RIGHT_PRESSED:	//��Ʈ�ѷ��� ���������� ������
				keyPress = 4;
				keybuff = 0;
				break;
			case DOWN_PRESSED:	//��Ʈ�ѷ��� �Ʒ��� ������
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
		case 3:	//UI����
			//�� ���� �ƹ��� Ű ���� ���� �ʴ´�.(�Է¿� ���� ������ ���� �ʴ´�)
			break;
		}
	}

	private void CreateTower(int TowerNumber){		//TowerNumber 1:red, 2:yellow, 3:blue, 4:black
		/*
		if(TowerNumber == 1){
			int ElementNumber = gameCanvas.map.Route[Control.getLocalPosition().x][Control.getLocalPosition().y];
			//õ���ڸ��� �����ڸ� �� ���� main.Towers �ȿ��� �ڱⰡ ����ڸ��� �ִ����� �����ϰ�,
			//�����ڸ��� �����ڸ� �� ���� ���� Ÿ���� ������ �����Ѵ�.
			//ex) ElementNumber = 1203 -> Towers �迭�� 12��° Element�� red2 Ÿ���� Ÿ���� ������ �ִ�.
			//Ÿ���� ���� : 0:���� �������� ��(Ÿ���� ���� �� ����), 1:�ƹ�Ÿ������(Ÿ���� ���� �� ����)
			//			2:red1,		3:red2,		4:red3
			//			5:yellow1,	6:yellow2,	7:yellow3
			//			8:blue1,	9:blue2,	10:blue3
			//			11:black1,	12:black2,	13:black3
			//���� Ÿ�� class �� ��������� ���̹Ƿ� Ÿ�� class �� ��������� �� Ÿ�Ը����� �ٲߴϴ�.
			//�׽�Ʈ�� ���غ��Ŷ� �� �̳��׿�. �Ƹ� ��򰡿� ������ Ʋ���̴ϴ�.
			if(ElementNumber%100 == 1){
				redTower red = new redTower(main, imgNum, Control.position.x, Control.position.y);			//���� ��Ʈ�ѷ��� �ִ� ��ġ�� ������Ʈ�� ����
				if(red.Gold <= this.Gold){
					Towers.addElement(red);
					this.Gold -= red.Gold;
					gameCanvas.map.Route[Control.getLocalPosition().x][Control.getLocalPosition().y] = Towers.size()*100 + 2;
				}
			}else if(ElementNumber%100 == 2){																//�ѹ� ���� ���� �־ ���׷��̵带 �Ϸ��� ����
				redTower2 red = new redTower2(main, imgNum, Control.position.x, Control.position.y);		//���� ��Ʈ�ѷ��� �ִ� ��ġ�� ������Ʈ�� ����
				if(red.Gold <= this.Gold){																	//���� �ִ� Ÿ���� ����� ���ο� Ÿ���� �� �ڸ��� ���´�
					Towers.insertElementAt(red, ElementNumber/100);
					Towers.removeElementAt((ElementNumber/100) + 1);
					this.Gold -= red.Gold;
					gameCanvas.map.Route[Control.getLocalPosition().x][Control.getLocalPosition().y] = 100*(ElementNumber/100) + 3;
				}
			}else if(ElementNumber%100 == 3){																//�ѹ� ���׷��̵� �� ���� �־ ���������� ���׷��̵� �Ϸ��� ����
				redTower3 red = new redTower3(main, imgNum, Control.position.x, Control.position.y);		//���� ��Ʈ�ѷ��� �ִ� ��ġ�� ������Ʈ�� ����
				if(red.Gold <= this.Gold){																	//���� �ִ� Ÿ���� ����� ���ο� Ÿ���� �� �ڸ��� ���´�
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
		case 0:		//ù��° wave
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
		case 1:		//���� wave�� ���� ���� �ð�
			timer++;
			if(300 < timer){
				enemyWave--;
			}
			break;
		case 2:		//�� ������ �ٸ� Enemy�� �����ϸ� �������� ���� �ȸ���������Ƿ� ���
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