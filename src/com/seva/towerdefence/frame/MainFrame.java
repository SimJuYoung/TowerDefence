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
	private int gScreenWidth = 800; // 게임 창 크기
	private int gScreenHeight = 600;
	Thread mainwork; // 스레드 객체
	boolean roof = true; // 스레드 루프 정보

	// 게임 제어를 위한 변수
	private int status; // 게임의 상태
	int delay; // 루프 딜레이. 1/1000초 단위.
	long pretime; // 루프 간격을 조절하기 위한 시간 체크값
	int keybuff; // 키 버퍼값
	int timer;
	private int enemyWave; // 적의 웨이브 순서
	private int enemyNumber; // 생성된 적의 숫자

	// 게임용 변수
	int gameControl; // 게임 흐름 컨트롤
	private Controller control; // 게임 중에 조작할 수 있도록 만들어 주는 컨트롤러
	private int gold;
	private int life;

	Enemy testenemy;

	private Vector<GameObject> enemys = new Vector<GameObject>(); // enemy들을 저장할
																	// 배열
	private Vector<Tower> towers = new Vector<Tower>(); // Tower들을 저장할
														// 배열
	private Vector<GameObject> bullets = new Vector<GameObject>(); // Bullet들을
																	// 저장할 배열
	private Vector<GameObject> effects = new Vector<GameObject>(); // Effect들을
																	// 저장할 배열
	private Vector<GameObject> uIs = new Vector<GameObject>(); // 컨트롤러 UI들을 저장할
																// 배열

	// Vector<gameObject> gO 하나에다가 이 다섯가지를 전부 묶어서 관리할 수도 있겠지만
	// 출력할시 제일 먼저 맵을 그리고 타워를 그리고 유닛을 그리고 미사일을 그리고 이펙트를 그리고 UI 를 그리도록 설계하기 위해
	// 따로 나누어 놓는 것이 좋다고 판단

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
		delay = 17;// 17/1000초 = 58 (프레임/초)
		keybuff = 0;
		mainwork = new Thread(this);
		mainwork.start();
		setGold(300); // 처음 돈
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
		// run 함수는 어디에도 실행되는 부분이 없다. 아무래도 자동적으로 실행되는 모양이다. -> runnable 상속
		try {// 게임을 동일한 속도로 동작하도록 만들어 준다
			while (roof) {
				pretime = System.currentTimeMillis();
				gameCanvas.repaint(); // 다시 그리기
				process(); // 동작 실행
				if (System.currentTimeMillis() - pretime < delay)
					Thread.sleep(delay - System.currentTimeMillis() + pretime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 키 입력부
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

	// 키 입력부(end)

	private void process() {
		switch (getStatus()) {
		case 0:// 타이틀화면
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
		case 1:// 스타트
			process_Start();
			break;
		case 2:// 게임화면
			process_GameObject(getEnemys());
			process_GameObject(getBullets());
			process_GameObject(getTowers());
			process_GameObject(getEffects());
			process_GameObject(getUIs());
			process_GameObject(getControl());
			process_Game();

			break;
		case 3:// 게임오버
			process_GameObject(getEnemys());
			process_GameObject(getBullets());
			process_GameObject(getTowers());
			process_GameObject(getEffects());
			if (gameControl++ >= 200 && keybuff == KeyEvent.VK_SPACE) {
				setStatus(0);
				Init_GAME();
			}
			break;
		case 4:// 일시정지
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
		} // 게임오버
		process_Enemy();
		process_Controller();
	}

	public void process_Controller() {
		switch (getControl().state) {
		case 0: // 기본상태(UI가 펼쳐지지 않은 상태)
			switch (keybuff) {
			case 0:
				break;
			case FIRE_PRESSED:
				if (gameCanvas.getMap().getRoute()[getControl()
						.getLocalPosition().x][getControl().getLocalPosition().y] != 0) {
					getControl().state++; // UI를 펼치는 상태로 증가
				}
				break;
			case UP_PRESSED:// 아래의 네가지 키 입력값으로는 맵 사이에 UI를 움직임
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
		case 1: // UI펼치기
			// 이 때는 아무런 키 값을 받지 않는다.(입력에 따른 동작을 하지 않는다)
			break;
		case 2: // UI다펼침
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
			case UP_PRESSED: // 컨트롤러를 위로 움직임
				keyPress = 1;
				keybuff = 0;
				break;
			case LEFT_PRESSED: // 컨트롤러를 왼쪽으로 움직임
				keyPress = 3;
				keybuff = 0;
				break;
			case RIGHT_PRESSED: // 컨트롤러를 오른쪽으로 움직임
				keyPress = 4;
				keybuff = 0;
				break;
			case DOWN_PRESSED: // 컨트롤러를 아래로 움직임
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
		case 3: // UI접기
			// 이 때는 아무런 키 값을 받지 않는다.(입력에 따른 동작을 하지 않는다)
			break;
		}
	}

	private void createTower(int towerNumber) { // TowerNumber 1:red, 2:yellow,
												// 3:blue, 4:black
		if (towerNumber == 1) {
			int elementNumber = gameCanvas.getMap().getRoute()[getControl()
					.getLocalPosition().x][getControl().getLocalPosition().y];
			// 천의자리와 백의자리 두 수는 main.Towers 안에서 자기가 어느자리에 있는지를 저장하고,
			// 십의자리와 일의자리 두 수는 현재 타워의 종류를 저장한다.
			// ex) ElementNumber = 1203 -> Towers 배열의 12번째 Element에 red2 타입의 타워가
			// 지어져 있다.
			// 타워의 종류 : 0:적이 지나가는 길(타워를 지을 수 없음), 1:아무타워없음(타워를 지을 수 있음)
			// 2:red1, 3:red2, 4:red3
			// 5:yellow1, 6:yellow2, 7:yellow3
			// 8:blue1, 9:blue2, 10:blue3
			// 11:black1, 12:black2, 13:black3
			// 아직 타워 class 가 만들어지기 전이므로 타워 class 가 만들어지면 그 타입명으로 바꿉니다.
			// 테스트를 안해본거라 좀 겁나네요. 아마 어딘가에 문법이 틀릴겁니다.
			if (elementNumber % 100 == 1) {
				if (TowerType.RED.getPrice() <= gold) {
					// TODO
					RedTower red = new RedTower(this, 0,
							getControl().position.x, getControl().position.y,
							300);
					// 현재 컨트롤러가 있는 위치에 오브젝트를 생성
					getTowers().addElement(red);
					gold -= TowerType.RED.getPrice();
					gameCanvas.getMap().getRoute()[getControl()
							.getLocalPosition().x][getControl()
							.getLocalPosition().y] = getTowers().size() * 100 + 2;
				}
			} else if (elementNumber % 100 == 2) { // 한번 지은 적이 있어서 업그레이드를 하려는 상태
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
		case 0: // 첫번째 wave
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
		case 1: // 다음 wave가 오기 전의 시간
			timer++;
			if (300 < timer) {
				enemyWave--;
			}
			break;
		case 2: // 이 때부터 다른 Enemy가 등장하면 좋겠지만 아직 안만들어졌으므로 대기
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