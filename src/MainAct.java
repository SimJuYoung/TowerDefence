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
	Thread mainwork;//������ ��ü
	boolean roof=true;//������ ���� ����
	Random rnd = new Random();	 // ���� ����

	//���� ��� ���� ����
	int status;//������ ����
	int timer;//���� ����� ��Ʈ�� ����
	int delay;//���� ������. 1/1000�� ����.
	long pretime;//���� ������ �����ϱ� ���� �ð� üũ��
	int keybuff;//Ű ���۰�W

	//���ӿ� ����
	int gameControl;//���� �帧 ��Ʈ��
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
		delay=17;// 17/1000�� = 58 (������/��)
		keybuff=0;
		mainwork=new Thread(this);
		mainwork.start();
		gameControl = 0;
	}
	public void initialize(){
		gameCanvas.repaint();
	}
	public void run(){
		try{//������ ������ �ӵ��� �����ϵ��� ����� �ش�
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
				keybuff|=LEFT_PRESSED;//��ƼŰ�� ������ ó��
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
			keybuff&=~LEFT_PRESSED;//��ƼŰ�� ���� ó��
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
		case 0://Ÿ��Ʋȭ��
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
		case 1://��ŸƮ
			process_Start();
			break;
		case 2://����ȭ��
			process_GameObject(Enemys);
			process_GameObject(Bullets);
			process_GameObject(Towers);
			process_GameObject(Effects);
			process_GameObject(UIs);
			Control.move();
			
			/////�ý��۾ƿ�
			
			System.out.printf("%d\n", Control.timer);
			
			/////�ý��۾ƿ�
			
			
			switch(Control.state){
			case 0:	//�⺻����
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
			case 1:	//UI��ġ��
				break;
			case 2:	//UI����ħ
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
			case 3:	//UI����
				break;
			}
			
			break;
		case 3://���ӿ���
			process_GameObject(Enemys);
			process_GameObject(Bullets);
			process_GameObject(Towers);
			process_GameObject(Effects);
			if(gameControl++ >= 200 && keybuff==KeyEvent.VK_SPACE){
				status=0;
				keybuff=0;
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
				Vec.remove(i);//ȭ�� ������ ������ ����
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