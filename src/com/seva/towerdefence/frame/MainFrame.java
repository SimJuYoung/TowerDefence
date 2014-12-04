package com.seva.towerdefence.frame;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Comparator;
import java.util.Vector;

import com.seva.towerdefence.Controller;
import com.seva.towerdefence.GameCanvas;
import com.seva.towerdefence.models.bullet.*;
import com.seva.towerdefence.models.GameObject;
import com.seva.towerdefence.models.enemy.Enemy;
import com.seva.towerdefence.models.enemy.Enemy_Brave;
import com.seva.towerdefence.models.tower.BlackTower;
import com.seva.towerdefence.models.tower.BlueTower;
import com.seva.towerdefence.models.tower.RedTower;
import com.seva.towerdefence.models.tower.Tower;
import com.seva.towerdefence.models.tower.TowerType;
import com.seva.towerdefence.models.tower.YellowTower;

public class MainFrame extends Frame implements KeyListener, Runnable {

	private static final long serialVersionUID = 1L;
	public final static int UP_PRESSED = 0x001;
	public final static int DOWN_PRESSED = 0x002;
	public final static int LEFT_PRESSED = 0x004;
	public final static int RIGHT_PRESSED = 0x008;
	public final static int FIRE_PRESSED = 0x010;

	public GameCanvas gameCanvas;
	int gScreenWidth=800;		//���� â ũ��
	int gScreenHeight=600;
	Thread mainwork;			//������ ��ü
	boolean roof=true;			//������ ���� ����

	//���� ��� ���� ����
	int status;					//������ ����
	int titleStatus;        	//title ����
	int delay;					//���� ������. 1/1000�� ����.
	long pretime;				//���� ������ �����ϱ� ���� �ð� üũ��
	int keybuff;				//Ű ���۰�
	private int timer;
	private int enemyWave;		//���� ���̺� ����
	private int enemyNumber;	//������ ���� ����

	//���ӿ� ����
	int gameControl;			//���� �帧 ��Ʈ��
	Controller Control;			//���� �߿� ������ �� �ֵ��� ����� �ִ� ��Ʈ�ѷ�
	int gold;
	int life;

	Enemy testenemy;
	
	public Vector<GameObject> Enemys = new Vector<GameObject>();		//enemy���� ������ �迭
	public Vector<GameObject> Towers = new Vector<GameObject>();		//Tower���� ������ �迭
	public Vector<GameObject> Bullets = new Vector<GameObject>();		//Bullet���� ������ �迭
	public Vector<GameObject> Effects = new Vector<GameObject>();		//Effect���� ������ �迭
	public Vector<GameObject> UIs = new Vector<GameObject>();			//��Ʈ�ѷ� UI���� ������ �迭
	//Vector<GameObject> gO �ϳ����ٰ� �� �ټ������� ���� ��� ������ ���� �ְ�����
	//����ҽ� ���� ���� ���� �׸��� Ÿ���� �׸��� ������ �׸��� �̻����� �׸��� ����Ʈ�� �׸��� UI �� �׸����� �����ϱ� ����
	//���� ������ ���� ���� ���ٰ� �Ǵ�

	public MainFrame() {
		setBackground(new Color(0xffffff));
		setTitle("Tower Defence");
		setLayout(null);
		setBounds(100,100,gScreenWidth,gScreenHeight);
		setResizable(false);
		setVisible(true);
		addKeyListener(this);
		addWindowListener(new MyWindowAdapter());
		gameCanvas = new GameCanvas(this);
		gameCanvas.setBounds(0,0,gScreenWidth,gScreenHeight);
		add(gameCanvas);
		init();
	}

	public void init(){
		status = 0;
		titleStatus = 0;
		delay = 17;					// 17/1000�� = 58 (������/��)
		keybuff = 0;
		mainwork = new Thread(this);
		mainwork.start();
		gold = 300;					// ó�� ��
		gameControl = 0;
		gameCanvas.repaint();
		setTimer(0);
		enemyNumber = 0;
		enemyWave = 0;
		life = 20;
		Control = new Controller(this, 7, 3*this.getWidth()/28, (int)(0.98*this.getHeight()/5));
	}

	@Override
	public void run() {	// run �Լ��� ��𿡵� ����Ǵ� �κ��� ����. �ƹ����� �ڵ������� ����Ǵ� ����̴�. -> runnable ���
		try {			// ������ ������ �ӵ��� �����ϵ��� ����� �ش�
			while (roof) {
				pretime = System.currentTimeMillis();
				gameCanvas.repaint(); 	// �ٽ� �׸���
				process(); 				// ���� ����
				if (System.currentTimeMillis() - pretime < delay)
					Thread.sleep(delay - System.currentTimeMillis() + pretime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Ű �Էº�
	@Override
	public void keyPressed(KeyEvent e) {
		if (getStatus() == 2) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_SPACE:
				keybuff |= FIRE_PRESSED;
				break;
			case KeyEvent.VK_LEFT:
				keybuff |= LEFT_PRESSED;
				break;
			case KeyEvent.VK_UP:
				keybuff |= UP_PRESSED;
				break;
			case KeyEvent.VK_RIGHT:
				keybuff |= RIGHT_PRESSED;
				break;
			case KeyEvent.VK_DOWN:
				keybuff |= DOWN_PRESSED;
				break;
			default:
				break;
			}
		} else if (getStatus() != 2)
			keybuff = e.getKeyCode();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_SPACE:
			keybuff &= ~FIRE_PRESSED;
			break;
		case KeyEvent.VK_LEFT:
			keybuff &= ~LEFT_PRESSED;
			break;
		case KeyEvent.VK_UP:
			keybuff &= ~UP_PRESSED;
			break;
		case KeyEvent.VK_RIGHT:
			keybuff &= ~RIGHT_PRESSED;
			break;
		case KeyEvent.VK_DOWN:
			keybuff &= ~DOWN_PRESSED;
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
	// Ű �Էº�(end)

	private void process() {
		switch (getStatus()) {
		case 0://Ÿ��Ʋȭ��
	         switch(titleStatus){
	         case 0:
	            setTimer(getTimer() + 1);
	            if(getTimer() < 100){
	               gameCanvas.getCotton().position.y += (getTimer()-10)*0.2f;
	            }else{
	               if(getTimer() < 150){
	                  gameCanvas.getLogo().position.y -= (150 - getTimer())*0.2f;
	                  gameCanvas.getMenu().position.y += (150 - getTimer())*0.2f;
	                  gameCanvas.getTitleControl().position.y += (150 - getTimer())*0.2f;
	               }else{
	                  titleStatus++;
	               }
	            }
	            break;
	         case 1:
	            if(keybuff==KeyEvent.VK_SPACE) {
	               Init_GAME();
	               if(gameControl == 1){
	                  System.exit(1);
	               }else{
	                  titleStatus++;
	                  setTimer(0);
	               }
	            }
	            if(keybuff==KeyEvent.VK_UP) {
	               gameControl = 0;
	               gameCanvas.getTitleControl().position.y = this.getHeight()/3 - 58;
	            }
	            if(keybuff==KeyEvent.VK_DOWN) {
	               gameControl = 1;
	               gameCanvas.getTitleControl().position.y = this.getHeight()/6 - 25;
	            }
	            break;
	         case 2:
	            setTimer(getTimer() + 1);
	            if(getTimer() < 100){
	               gameCanvas.getCotton().position.y -= (85 - getTimer())*0.2f;
	            }else{
	               if(120 < getTimer()){
	                  status = 1;
	                  setTimer(0);
	               }
	            }
	            break;
	         }
	         break;
		case 1:// ��ŸƮ
			process_Start();
			break;
		case 2:// ����ȭ��
			process_GameObject(getEnemys());
			process_GameObject(getBullets());
			process_GameObject(getTowers());
			process_GameObject(getEffects());
			process_GameObject(getUIs());
			process_GameObject(getControl());
			process_Game();

			break;
		case 3:// ���ӿ���
			process_GameObject(getEnemys());
			process_GameObject(getBullets());
			process_GameObject(getTowers());
			process_GameObject(getEffects());
			if (gameControl++ >= 200 && keybuff == KeyEvent.VK_SPACE) {
				setStatus(0);
				Init_GAME();
			}
			break;
		case 4:// �Ͻ�����
			if (gameControl++ >= 200 && keybuff == KeyEvent.VK_SPACE)
				setStatus(2);
			break;
		default:
			break;
		}
	}

	public void Init_GAME() {
		keybuff = 0;
		getEnemys().clear();
		getTowers().clear();
		getBullets().clear();
		getEffects().clear();
		setLife(20);
	}

	public void process_Start() {
		setStatus(2);
	}

	public void process_Game() {
		if (getLife() <= 0) {
			setStatus(3);
		} // ���ӿ���
		process_Enemy();
		process_Controller();
	}

	public void process_Controller() {
		switch (getControl().state) {
		case 0: // �⺻����(UI�� �������� ���� ����)
			switch (keybuff) {
			case 0:
				break;
			case FIRE_PRESSED:
				if (gameCanvas.getMap().getRoute()[getControl()
						.getLocalPosition().x][getControl().getLocalPosition().y] != 0) {
					getControl().state++; // UI�� ��ġ�� ���·� ����
				}
				break;
			case UP_PRESSED:// �Ʒ��� �װ��� Ű �Է°����δ� �� ���̿� UI�� ������
				if (getControl().getLocalPosition().y < 7) {
					getControl().setLocalPosition(new Point(0, 1));
					getControl().position.y += (getHeight() - 30) / 10;
				}
				keybuff = 0;
				break;
			case LEFT_PRESSED:
				if (0 < getControl().getLocalPosition().x) {
					getControl().setLocalPosition(new Point(-1, 0));
					getControl().position.x -= getWidth() / 14;
				}
				keybuff = 0;
				break;
			case RIGHT_PRESSED:
				if (getControl().getLocalPosition().x < 11) {
					getControl().setLocalPosition(new Point(1, 0));
					getControl().position.x += getWidth() / 14;
				}
				keybuff = 0;
				break;
			case DOWN_PRESSED:
				if (0 < getControl().getLocalPosition().y) {
					getControl().setLocalPosition(new Point(0, -1));
					getControl().position.y -= (getHeight() - 30) / 10;
				}
				keybuff = 0;
				break;
			default:
				keybuff = 0;
				break;
			}
			break;
		case 1: // UI��ġ��
			// �� ���� �ƹ��� Ű ���� ���� �ʴ´�.(�Է¿� ���� ������ ���� �ʴ´�)
			break;
		case 2: // UI����ħ
			int keyPress = -1;
			switch (keybuff) {
			case 0:
				break;
			case FIRE_PRESSED:
				switch (getControl().ControllCubeState) { // 0:cancel, 1:up,
															// 2:down,
															// 3:left, 4:right
				case 0: // cancel
					break;
				case 1: // create redTower
					createTower(1);
					break;
				case 2: // create blackTower
					createTower(4);
					break;
				case 3: // create blueTower
					createTower(3);
					break;
				case 4: // create yellowTower
					createTower(2);
					break;
				}
				getControl().state++;
				break;
			case UP_PRESSED: // ��Ʈ�ѷ��� ���� ������
				keyPress = 1;
				keybuff = 0;
				break;
			case LEFT_PRESSED: // ��Ʈ�ѷ��� �������� ������
				keyPress = 3;
				keybuff = 0;
				break;
			case RIGHT_PRESSED: // ��Ʈ�ѷ��� ���������� ������
				keyPress = 4;
				keybuff = 0;
				break;
			case DOWN_PRESSED: // ��Ʈ�ѷ��� �Ʒ��� ������
				keyPress = 2;
				keybuff = 0;
				break;
			default:
				keybuff = 0;
				break;
			}
			getControl().movePosControlBox(keyPress);
			keyPress = -1;
			break;
		case 3: // UI����
			// �� ���� �ƹ��� Ű ���� ���� �ʴ´�.(�Է¿� ���� ������ ���� �ʴ´�)
			break;
		}
	}

	private void createTower(int towerNumber) { 	// towerNumber 1:red, 2:yellow, 3:blue, 4:black
		if (towerNumber == 1) {
			int elementNumber = gameCanvas.getMap().getRoute()[getControl().getLocalPosition().x]
					[getControl().getLocalPosition().y];
			
			// ElementNumber = (Towers Array Number : 2 digit) + (Kind of Tower : 2 digit)
			// Kind of Tower 
			// 0 : Path for Enemy (Can not Build)			1 : Empty Space
			// 2 : RedTower1		3 : RedTower2			4 : RedTower3
			// 5 : YellowTower1		6 : YellowTower2		7 : YellowTower3
			// 8 : BlueTower1		9 : BlueTower2			10: BlueTower3
			// 11:BlackTower1		12: BlackTower2			13: BlackTower3

			if (elementNumber % 100 == 1) {						// Create RedTower1
				if (TowerType.RED.getPrice() <= gold) {
					RedTower red = new RedTower(this, 0, getControl().position.x, 
							getControl().position.y, 300);
					getTowers().addElement(red);
					gold -= TowerType.RED.getPrice();
					gameCanvas.getMap().getRoute()[getControl().getLocalPosition().x]
							[getControl().getLocalPosition().y] = getTowers().size() * 100 + 2;
				}
			} else if (elementNumber % 100 == 2) { 				// RedTower1 -> RedTower2
				if (TowerType.RED.getUpgradePrice() <= gold) {
					((Tower) Towers.elementAt(elementNumber / 100)).upgrade();
					gold -= TowerType.RED.getUpgradePrice();
					gameCanvas.getMap().getRoute()[getControl().getLocalPosition().x]
							[getControl().getLocalPosition().y] = 100 * (elementNumber / 100) + 3;
				}
			} else if (elementNumber % 100 == 3) {				// RedTower2 -> RedTower3
				if (TowerType.RED.getFinalUpgradePrice() <= gold) {
					((Tower) Towers.elementAt(elementNumber / 100)).upgrade();
					gold -= TowerType.RED.getUpgradePrice();
					gameCanvas.getMap().getRoute()[getControl().getLocalPosition().x]
							[getControl().getLocalPosition().y] = 100 * (elementNumber / 100) + 4;
				}
			}
		} else if (towerNumber == 2) {
			int elementNumber = gameCanvas.getMap().getRoute()[getControl().getLocalPosition().x]
					[getControl().getLocalPosition().y];

			if (elementNumber % 100 == 1) {						// Create YellowTower1
				if (TowerType.YELLOW.getPrice() <= gold) {
					YellowTower yellow = new YellowTower(this, 0, getControl().position.x, 
							getControl().position.y, 300);
					getTowers().addElement(yellow);
					gold -= TowerType.YELLOW.getPrice();
					gameCanvas.getMap().getRoute()[getControl().getLocalPosition().x]
							[getControl().getLocalPosition().y] = getTowers().size() * 100 + 5;
				}
			} else if (elementNumber % 100 == 5) { 				// YellowTower1 -> YellowTower2
				if (TowerType.YELLOW.getUpgradePrice() <= gold) {
					((Tower) Towers.elementAt(elementNumber / 100)).upgrade();
					gold -= TowerType.YELLOW.getUpgradePrice();
					gameCanvas.getMap().getRoute()[getControl().getLocalPosition().x]
							[getControl().getLocalPosition().y] = 100 * (elementNumber / 100) + 6;
				}
			} else if (elementNumber % 100 == 6) {				// YellowTower2 -> YellowTower3
				if (TowerType.YELLOW.getFinalUpgradePrice() <= gold) {
					((Tower) Towers.elementAt(elementNumber / 100)).upgrade();
					gold -= TowerType.YELLOW.getUpgradePrice();
					gameCanvas.getMap().getRoute()[getControl().getLocalPosition().x]
							[getControl().getLocalPosition().y] = 100 * (elementNumber / 100) + 7;
				}
			}
		} else if (towerNumber == 3) {				
			int elementNumber = gameCanvas.getMap().getRoute()[getControl().getLocalPosition().x]
					[getControl().getLocalPosition().y];

			if (elementNumber % 100 == 1) {						// Create BlueTower1
				if (TowerType.BLUE.getPrice() <= gold) {
					BlueTower blue = new BlueTower(this, 0, getControl().position.x, 
							getControl().position.y, 300);
					getTowers().addElement(blue);
					gold -= TowerType.BLUE.getPrice();
					gameCanvas.getMap().getRoute()[getControl().getLocalPosition().x]
							[getControl().getLocalPosition().y] = getTowers().size() * 100 + 8;
				}
			} else if (elementNumber % 100 == 8) { 				// BlueTower1 -> BlueTower2
				if (TowerType.BLUE.getUpgradePrice() <= gold) {
					((Tower) Towers.elementAt(elementNumber / 100)).upgrade();
					gold -= TowerType.BLUE.getUpgradePrice();
					gameCanvas.getMap().getRoute()[getControl().getLocalPosition().x]
							[getControl().getLocalPosition().y] = 100 * (elementNumber / 100) + 9;
				}
			} else if (elementNumber % 100 == 9) {				// BlueTower2 -> BlueTower3
				if (TowerType.BLUE.getFinalUpgradePrice() <= gold) {
					((Tower) Towers.elementAt(elementNumber / 100)).upgrade();
					gold -= TowerType.BLUE.getUpgradePrice();
					gameCanvas.getMap().getRoute()[getControl().getLocalPosition().x]
							[getControl().getLocalPosition().y] = 100 * (elementNumber / 100) + 10;
				}
			}
		} else if (towerNumber == 4) {
			int elementNumber = gameCanvas.getMap().getRoute()[getControl().getLocalPosition().x]
					[getControl().getLocalPosition().y];

			if (elementNumber % 100 == 1) {						// Create BlackTower1
				if (TowerType.BLACK.getPrice() <= gold) {
					BlackTower black = new BlackTower(this, 0, getControl().position.x, 
							getControl().position.y, 300);
					getTowers().addElement(black);
					gold -= TowerType.BLACK.getPrice();
					gameCanvas.getMap().getRoute()[getControl().getLocalPosition().x]
							[getControl().getLocalPosition().y] = getTowers().size() * 100 + 11;
				}
			} else if (elementNumber % 100 == 11) { 			// BlackTower1 -> BlackTower2
				if (TowerType.BLACK.getUpgradePrice() <= gold) {
					((Tower) Towers.elementAt(elementNumber / 100)).upgrade();
					gold -= TowerType.BLACK.getUpgradePrice();
					gameCanvas.getMap().getRoute()[getControl().getLocalPosition().x]
							[getControl().getLocalPosition().y] = 100 * (elementNumber / 100) + 12;
				}
			} else if (elementNumber % 100 == 12) {				// BlackTower2 -> BlackTower3
				if (TowerType.BLACK.getFinalUpgradePrice() <= gold) {
					((Tower) Towers.elementAt(elementNumber / 100)).upgrade();
					gold -= TowerType.BLACK.getUpgradePrice();
					gameCanvas.getMap().getRoute()[getControl().getLocalPosition().x]
							[getControl().getLocalPosition().y] = 100 * (elementNumber / 100) + 13;
				}
			}
		}
	}

	private void process_GameObject(Vector<GameObject> Vec) {
		GameObject buff;
		for (int i = 0; i < Vec.size(); i++) {
			buff = Vec.elementAt(i);
			buff.move();
			if (buff.Destroy) {
				Vec.remove(i);
			}
		}
	}

	private void process_GameObject(GameObject gO) {
		gO.move();
	}

	private void process_Enemy(){
		// process_Bullet();
		switch(enemyWave){
		case 0:		//ù��° wave
			if(getTimer() == 0){
				testenemy = new Enemy_Brave(this);
				setTimer(getTimer() + 1);
				enemyNumber++;
			}else{
				setTimer(getTimer() + 1);
				if(40 < getTimer()){	setTimer(0);	}
			}
			if(10 <= enemyNumber){
				enemyWave++;
				enemyNumber = 0;
				setTimer(0);
			}
			break;
		case 1:		//���� wave�� ���� ���� �ð�
			setTimer(getTimer() + 1);
			
			if(300 < getTimer()){
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
	
	/*
	private void process_Bullet(){	
		if(getTimer() == 30){
			new Bullet(this, 122, 0, 0, Enemys.elementAt(0));
		}
	}
	*/

	public GameCanvas getGameCanvas() {
		return gameCanvas;
	}

	public void setGameCanvas(GameCanvas gameCanvas) {
		this.gameCanvas = gameCanvas;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public int getgScreenWidth() {
		return gScreenWidth;
	}

	public void setgScreenWidth(int gScreenWidth) {
		this.gScreenWidth = gScreenWidth;
	}

	public int getgScreenHeight() {
		return gScreenHeight;
	}

	public void setgScreenHeight(int gScreenHeight) {
		this.gScreenHeight = gScreenHeight;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Vector<GameObject> getTowers() {
		return Towers;
	}

	public void setTowers(Vector<GameObject> towers) {
		this.Towers = new Vector<GameObject>();
		this.Towers.addAll(towers);
	}

	public Vector<GameObject> getEnemys() {
		return Enemys;
	}

	public void setEnemys(Vector<GameObject> enemys) {
		this.Enemys = new Vector<GameObject>();
		this.Enemys.addAll(enemys);
	}

	public Vector<GameObject> getBullets() {
		return Bullets;
	}

	public void setBullets(Vector<GameObject> bullets) {
		this.Bullets = bullets;
	}

	public Vector<GameObject> getEffects() {
		return Effects;
	}

	public void setEffects(Vector<GameObject> effects) {
		this.Effects = effects;
	}

	public Vector<GameObject> getUIs() {
		return UIs;
	}

	public void setUIs(Vector<GameObject> uIs) {
		this.UIs = uIs;
	}

	public Controller getControl() {
		return Control;
	}

	public void setControl(Controller control) {
		this.Control = control;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}
}

class MyWindowAdapter extends WindowAdapter {
	MyWindowAdapter() {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		Window wnd = e.getWindow();
		wnd.setVisible(false);
		wnd.dispose();
		System.exit(0);
	}
}

class comparer implements Comparator<GameObject> {
	@Override
	public int compare(GameObject arg0, GameObject arg1) {
		return arg0.position.y > arg1.position.y ? 1 : 0;
	}
}