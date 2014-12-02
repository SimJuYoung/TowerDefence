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
	int gScreenWidth=800;
	int gScreenHeight=600;
	Thread mainwork;//스레드 객체
	boolean roof=true;//스레드 루프 정보
	Random rnd = new Random();	 // 랜덤 선언

	//게임 제어를 위한 변수
	int status;//게임의 상태
	int timer;//루프 제어용 컨트롤 변수
	int delay;//루프 딜레이. 1/1000초 단위.
	long pretime;//루프 간격을 조절하기 위한 시간 체크값
	int keybuff;//키 버퍼값W

	//게임용 변수
	int gameControl;//게임 흐름 컨트롤
	Controller Control;

	Vector<gameObject> Enemys = new Vector<gameObject>();
	Vector<gameObject> Towers = new Vector<gameObject>();
	Vector<gameObject> Bullets = new Vector<gameObject>();
	Vector<gameObject> Effects = new Vector<gameObject>();
	Vector<gameObject> UIs = new Vector<gameObject>();
	
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
		Control = new Controller(this, 7, (int)(3*this.getWidth()/28), (int)(0.98*this.getHeight()/5));
		systeminit();
		initialize();
	}
	public void systeminit(){
		status=0;
		timer=0;
		delay=17;// 17/1000초 = 58 (프레임/초)
		keybuff=0;
		mainwork=new Thread(this);
		mainwork.start();
		gameControl = 0;
	}
	public void initialize(){
		gameCanvas.repaint();
	}
	public void run(){
		try{//게임을 동일한 속도로 동작하도록 만들어 준다
			while(roof){
				pretime=System.currentTimeMillis();
				gameCanvas.repaint();
				process();
				if(System.currentTimeMillis()-pretime<delay) Thread.sleep(delay-System.currentTimeMillis()+pretime);
				if(status!=4) timer++;
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	public void keyPressed(KeyEvent e) {
		//if(status==2&&(mymode==2||mymode==0)){
		if(status==2){
			switch(e.getKeyCode()){
			case KeyEvent.VK_SPACE:
				keybuff|=FIRE_PRESSED;
				break;
			case KeyEvent.VK_LEFT:
				keybuff|=LEFT_PRESSED;//멀티키의 누르기 처리
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
			keybuff&=~LEFT_PRESSED;//멀티키의 떼기 처리
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
	private void process(){
		switch(status){
		case 0://타이틀화면
			if(keybuff==KeyEvent.VK_SPACE) {
				Init_GAME();
				Init_DATA();
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
			Control.move();
			
			/////시스템아웃
			
			System.out.printf("%d\n", Control.timer);
			
			/////시스템아웃
			
			
			switch(Control.state){
			case 0:	//기본상태
				switch(keybuff){
				case 0:
					break;
				case FIRE_PRESSED:
					Control.state ++;
					//System.out.printf("%d", Control.state);
					break;
				case UP_PRESSED:
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
				break;
			case 2:	//UI다펼침
				switch(keybuff){
				case 0:
					break;
				case FIRE_PRESSED:
					Control.state ++;
					//System.out.printf("%d", Control.state);
					break;
				case UP_PRESSED:
					keybuff = 0;
					break;
				case LEFT_PRESSED:
					keybuff = 0;
					break;
				case RIGHT_PRESSED:
					keybuff = 0;
					break;
				case DOWN_PRESSED:
					keybuff = 0;
					break;
				default:
					keybuff=0;
					break;
				}
				break;
			case 3:	//UI접기
				break;
			}
			
			break;
		case 3://게임오버
			process_GameObject(Enemys);
			process_GameObject(Bullets);
			process_GameObject(Towers);
			process_GameObject(Effects);
			if(gameControl++ >= 200 && keybuff==KeyEvent.VK_SPACE){
				status=0;
				keybuff=0;
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
	}
	public void Init_DATA(){
		keybuff=0;
	}
	public void process_Start(){
		status++;
	}
	
	public void process_GameObject(Vector<gameObject> Vec){
		gameObject buff;
		for(int i = 0 ; i < Vec.size() ; i ++){
			buff = Vec.elementAt(i);
			/*
			if( (buff.position.x < 10) || (buff.position.x > gScreenWidth+10) || (buff.position.y < 10) || (buff.position.y > gScreenHeight + 10) ){
				Vec.remove(i);//화면 밖으로 나가면 제거
				continue;
			}
			*/
			buff.move();
			if(buff.Destroy){
				Vec.remove(i);
			}
		}
		Collections.sort(Vec, new comparer() );
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