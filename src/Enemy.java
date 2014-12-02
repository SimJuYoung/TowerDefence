import java.awt.Point;

public class Enemy extends gameObject{
	int life;
	Point velocity;
	Enemy(mainFrame main, int imgIndex, int x, int y){
		super(main, imgIndex, x, y);
		this.main=main;
	}
	public void move(){
	
	}
}
