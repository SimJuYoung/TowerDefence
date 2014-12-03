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
import com.seva.towerdefence.models.Enemy;
import com.seva.towerdefence.models.GameObject;
import com.seva.towerdefence.models.RedTower;
import com.seva.towerdefence.models.Tower;
import com.seva.towerdefence.models.TowerType;

public class MainFrame extends Frame implements KeyListener, Runnable {

	private static final long serialVersionUID = 1L;
	public final static int UP_PRESSED = 0x001;
	public final static int DOWN_PRESSED = 0x002;
	public final static int LEFT_PRESSED = 0x004;
	public final static int RIGHT_PRESSED = 0x008;
	public final static int FIRE_PRESSED = 0x010;

	private GameCanvas gameCanvas;
	private int gScreenWidth = 800; // ���� â ũ��
	private int gScreenHeight = 600;
	Thread mainwork; // ������ ��ü
	boolean roof = true; // ������ ���� ����

	// ���� ��� ���� ����
	private int status; // ������ ����
	int delay; // ���� ������. 1/1000�� ����.
	long pretime; // ���� ������ �����ϱ� ���� �ð� üũ��
	int keybuff; // Ű ���۰�
	int timer;
	private int enemyWave; // ���� ���̺� ����
	private int enemyNumber; // ������ ���� ����

	// ���ӿ� ����
	int gameControl; // ���� �帧 ��Ʈ��
	private Controller control; // ���� �߿� ������ �� �ֵ��� ����� �ִ� ��Ʈ�ѷ�
	private int gold;
	private int life;

	Enemy testenemy;

	private Vector<GameObject> enemys = new Vector<GameObject>(); // enemy���� ������
																	// �迭
	private Vector<Tower> towers = new Vector<Tower>(); // Tower���� ������
														// �迭
	private Vector<GameObject> bullets = new Vector<GameObject>(); // Bullet����
																	// ������ �迭
	private Vector<GameObject> effects = new Vector<GameObject>(); // Effect����
																	// ������ �迭
	private Vector<GameObject> uIs = new Vector<GameObject>(); // ��Ʈ�ѷ� UI���� ������
																// �迭

	// Vector<gameObject> gO �ϳ����ٰ� �� �ټ������� ���� ��� ������ ���� �ְ�����
	// ����ҽ� ���� ���� ���� �׸��� Ÿ���� �׸��� ������ �׸��� �̻����� �׸��� ����Ʈ�� �׸��� UI �� �׸����� �����ϱ� ����
	// ���� ������ ���� ���� ���ٰ� �Ǵ�

	public MainFrame() {
		setBackground(new Color(0xffffff));
		setTitle("Tower Defence");
		setLayout(null);
		setBounds(100, 100, getgScreenWidth(), getgScreenHeight());
		setResizable(false);
		setVisible(true);
		addKeyListener(this);
		addWindowListener(new MyWindowAdapter());
		setGameCanvas(new GameCanvas(this));
		gameCanvas.setBounds(0, 0, getgScreenWidth(), getgScreenHeight());
		add(gameCanvas);
		init();
	}

	public void init() {
		setStatus(0);
		delay = 17;// 17/1000�� = 58 (������/��)
		keybuff = 0;
		mainwork = new Thread(this);
		mainwork.start();
		setGold(300); // ó�� ��
		gameControl = 0;
		gameCanvas.repaint();
		timer = 0;
		enemyNumber = 0;
		enemyWave = 0;
		setLife(20);
		setControl(new Controller(this, 7, (int) (3 * this.getWidth() / 28),
				(int) (0.98 * this.getHeight() / 5)));
	}

	@Override
	public void run() {
		// run �Լ��� ��𿡵� ����Ǵ� �κ��� ����. �ƹ����� �ڵ������� ����Ǵ� ����̴�. -> runnable ���
		try {// ������ ������ �ӵ��� �����ϵ��� ����� �ش�
			while (roof) {
				pretime = System.currentTimeMillis();
				gameCanvas.repaint(); // �ٽ� �׸���
				process(); // ���� ����
				if (System.currentTimeMillis() - pretime < delay)
					Thread.sleep(delay - System.currentTimeMillis() + pretime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Ű �Էº�
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

	public void keyTyped(KeyEvent e) {
	}

	// Ű �Էº�(end)

	private void process() {
		switch (getStatus()) {
		case 0:// Ÿ��Ʋȭ��
			if (keybuff == KeyEvent.VK_SPACE) {
				Init_GAME();
				if (gameControl == 1) {
					System.exit(1);
				} else {
					setStatus(1);
				}
			}
			if (keybuff == KeyEvent.VK_UP) {
				gameControl = 0;
				gameCanvas.getTitleControl().position.y = getgScreenHeight() / 3;
			}
			if (keybuff == KeyEvent.VK_DOWN) {
				gameControl = 1;
				gameCanvas.getTitleControl().position.y = getgScreenHeight() / 6;
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

	private void createTower(int towerNumber) { // TowerNumber 1:red, 2:yellow,
												// 3:blue, 4:black
		if (towerNumber == 1) {
			int elementNumber = gameCanvas.getMap().getRoute()[getControl()
					.getLocalPosition().x][getControl().getLocalPosition().y];
			// õ���ڸ��� �����ڸ� �� ���� main.Towers �ȿ��� �ڱⰡ ����ڸ��� �ִ����� �����ϰ�,
			// �����ڸ��� �����ڸ� �� ���� ���� Ÿ���� ������ �����Ѵ�.
			// ex) ElementNumber = 1203 -> Towers �迭�� 12��° Element�� red2 Ÿ���� Ÿ����
			// ������ �ִ�.
			// Ÿ���� ���� : 0:���� �������� ��(Ÿ���� ���� �� ����), 1:�ƹ�Ÿ������(Ÿ���� ���� �� ����)
			// 2:red1, 3:red2, 4:red3
			// 5:yellow1, 6:yellow2, 7:yellow3
			// 8:blue1, 9:blue2, 10:blue3
			// 11:black1, 12:black2, 13:black3
			// ���� Ÿ�� class �� ��������� ���̹Ƿ� Ÿ�� class �� ��������� �� Ÿ�Ը����� �ٲߴϴ�.
			// �׽�Ʈ�� ���غ��Ŷ� �� �̳��׿�. �Ƹ� ��򰡿� ������ Ʋ���̴ϴ�.
			if (elementNumber % 100 == 1) {
				if (TowerType.RED.getPrice() <= gold) {
					// TODO
					RedTower red = new RedTower(this, 0,
							getControl().position.x, getControl().position.y,
							300);
					// ���� ��Ʈ�ѷ��� �ִ� ��ġ�� ������Ʈ�� ����
					getTowers().addElement(red);
					gold -= TowerType.RED.getPrice();
					gameCanvas.getMap().getRoute()[getControl()
							.getLocalPosition().x][getControl()
							.getLocalPosition().y] = getTowers().size() * 100 + 2;
				}
			} else if (elementNumber % 100 == 2) { // �ѹ� ���� ���� �־ ���׷��̵带 �Ϸ��� ����
				if (TowerType.RED.getUpgradePrice() <= gold) {
					towers.elementAt(elementNumber / 100).upgrade();
					gold -= TowerType.RED.getUpgradePrice();
					gameCanvas.getMap().getRoute()[getControl()
							.getLocalPosition().x][getControl()
							.getLocalPosition().y] = 100 * (elementNumber / 100) + 3;
				}
			} else if (elementNumber % 100 == 3) {
				if (TowerType.RED.getFinalUpgradePrice() <= gold) {
					towers.elementAt(elementNumber / 100).upgrade();
					gold -= TowerType.RED.getUpgradePrice();
					gameCanvas.getMap().getRoute()[getControl()
							.getLocalPosition().x][getControl()
							.getLocalPosition().y] = 100 * (elementNumber / 100) + 3;
				}
			}
		}
		/*
		if (towerNumber == 2) {
			int ElementNumber = gameCanvas.getMap().getRoute()[getControl()
					.getLocalPosition().x][getControl().getLocalPosition().y];
			if (ElementNumber % 100 == 1) {
				yellowTower yellow = new yellowTower(main, imgNum,
						getControl().position.x, getControl().position.y);
				if (yellow.Gold <= gold) {
					getTowers().addElement(yellow);
					this.setGold(gold - yellow.Gold);
					gameCanvas.getMap().getRoute()[getControl()
							.getLocalPosition().x][getControl()
							.getLocalPosition().y] = getTowers().size() * 100 + 5;
				}
			} else if (ElementNumber % 100 == 2) {
				yellowTower2 yellow = new yellowTower2(main, imgNum,
						getControl().position.x, getControl().position.y);
				if (yellow.Gold <= gold) {
					getTowers().insertElementAt(yellow, ElementNumber / 100);
					getTowers().removeElementAt((ElementNumber / 100) + 1);
					this.setGold(gold - yellow.Gold);
					gameCanvas.getMap().getRoute()[getControl()
							.getLocalPosition().x][getControl()
							.getLocalPosition().y] = 100 * (ElementNumber / 100) + 6;
				}
			} else if (ElementNumber % 100 == 3) {
				yellowTower3 yellow = new yellowTower3(main, imgNum,
						getControl().position.x, getControl().position.y);
				if (yellow.Gold <= gold) {
					getTowers().insertElementAt(yellow, ElementNumber / 100);
					getTowers().removeElementAt((ElementNumber / 100) + 1);
					this.setGold(gold - yellow.Gold);
					gameCanvas.getMap().getRoute()[getControl()
							.getLocalPosition().x][getControl()
							.getLocalPosition().y] = 100 * (ElementNumber / 100) + 7;
				}
			}
		}
		if (towerNumber == 3) {
			int ElementNumber = gameCanvas.getMap().getRoute()[getControl()
					.getLocalPosition().x][getControl().getLocalPosition().y];
			if (ElementNumber % 100 == 1) {
				blueTower blue = new blueTower(main, imgNum,
						getControl().position.x, getControl().position.y);
				if (blue.Gold <= gold) {
					getTowers().addElement(blue);
					this.setGold(gold - blue.Gold);
					gameCanvas.getMap().getRoute()[getControl()
							.getLocalPosition().x][getControl()
							.getLocalPosition().y] = getTowers().size() * 100 + 8;
				}
			} else if (ElementNumber % 100 == 2) {
				blueTower2 blue = new blueTower2(main, imgNum,
						getControl().position.x, getControl().position.y);
				if (blue.Gold <= gold) {
					getTowers().insertElementAt(blue, ElementNumber / 100);
					getTowers().removeElementAt((ElementNumber / 100) + 1);
					this.setGold(gold - blue.Gold);
					gameCanvas.getMap().getRoute()[getControl()
							.getLocalPosition().x][getControl()
							.getLocalPosition().y] = 100 * (ElementNumber / 100) + 9;
				}
			} else if (ElementNumber % 100 == 3) {
				blueTower3 blue = new blueTower3(main, imgNum,
						getControl().position.x, getControl().position.y);
				if (blue.Gold <= gold) {
					getTowers().insertElementAt(blue, ElementNumber / 100);
					getTowers().removeElementAt((ElementNumber / 100) + 1);
					this.setGold(gold - blue.Gold);
					gameCanvas.getMap().getRoute()[getControl()
							.getLocalPosition().x][getControl()
							.getLocalPosition().y] = 100 * (ElementNumber / 100) + 10;
				}
			}
		}
		if (towerNumber == 4) {
			int ElementNumber = gameCanvas.getMap().getRoute()[getControl()
					.getLocalPosition().x][getControl().getLocalPosition().y];
			if (ElementNumber % 100 == 1) {
				blackTower black = new blackTower(main, imgNum,
						getControl().position.x, getControl().position.y);
				if (black.Gold <= gold) {
					getTowers().addElement(black);
					this.setGold(gold - black.Gold);
					gameCanvas.getMap().getRoute()[getControl()
							.getLocalPosition().x][getControl()
							.getLocalPosition().y] = getTowers().size() * 100 + 11;
				}
			} else if (ElementNumber % 100 == 2) {
				blackTower2 black = new blackTower2(main, imgNum,
						getControl().position.x, getControl().position.y);
				if (black.Gold <= gold) {
					getTowers().insertElementAt(black, ElementNumber / 100);
					getTowers().removeElementAt((ElementNumber / 100) + 1);
					this.setGold(gold - black.Gold);
					gameCanvas.getMap().getRoute()[getControl()
							.getLocalPosition().x][getControl()
							.getLocalPosition().y] = 100 * (ElementNumber / 100) + 12;
				}
			} else if (ElementNumber % 100 == 3) {
				blackTower3 black = new blackTower3(main, imgNum,
						getControl().position.x, getControl().position.y);
				if (black.Gold <= gold) {
					getTowers().insertElementAt(black, ElementNumber / 100);
					getTowers().removeElementAt((ElementNumber / 100) + 1);
					this.setGold(gold - black.Gold);
					gameCanvas.getMap().getRoute()[getControl()
							.getLocalPosition().x][getControl()
							.getLocalPosition().y] = 100 * (ElementNumber / 100) + 13;
				}
			}
		}*/
	}

	private void process_GameObject(Vector<? extends GameObject> Vec) {
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

	private void process_Enemy() {

		switch (enemyWave) {
		case 0: // ù��° wave
			if (timer == 0) {
				testenemy = new Enemy(this, 24);
				getEnemys().addElement(testenemy);
				timer++;
				enemyNumber++;
			} else {
				timer++;
				if (40 < timer) {
					timer = 0;
				}
			}
			if (10 <= enemyNumber) {
				enemyWave++;
				enemyNumber = 0;
				timer = 0;
			}
			break;
		case 1: // ���� wave�� ���� ���� �ð�
			timer++;
			if (300 < timer) {
				enemyWave--;
			}
			break;
		case 2: // �� ������ �ٸ� Enemy�� �����ϸ� �������� ���� �ȸ���������Ƿ� ���
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			break;
		}
	}

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
		life = life;
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

	public Vector<Tower> getTowers() {
		return towers;
	}

	public void setTowers(Vector<GameObject> towers) {
		towers = towers;
	}

	public Vector<GameObject> getEnemys() {
		return enemys;
	}

	public void setEnemys(Vector<GameObject> enemys) {
		enemys = enemys;
	}

	public Vector<GameObject> getBullets() {
		return bullets;
	}

	public void setBullets(Vector<GameObject> bullets) {
		bullets = bullets;
	}

	public Vector<GameObject> getEffects() {
		return effects;
	}

	public void setEffects(Vector<GameObject> effects) {
		effects = effects;
	}

	public Vector<GameObject> getUIs() {
		return uIs;
	}

	public void setUIs(Vector<GameObject> uIs) {
		uIs = uIs;
	}

	public Controller getControl() {
		return control;
	}

	public void setControl(Controller control) {
		control = control;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		gold = gold;
	}
}

class MyWindowAdapter extends WindowAdapter {
	MyWindowAdapter() {
	}

	public void windowClosing(WindowEvent e) {
		Window wnd = e.getWindow();
		wnd.setVisible(false);
		wnd.dispose();
		System.exit(0);
	}
}

class comparer implements Comparator<GameObject> {
	public int compare(GameObject arg0, GameObject arg1) {
		return arg0.position.y > arg1.position.y ? 1 : 0;
	}
}