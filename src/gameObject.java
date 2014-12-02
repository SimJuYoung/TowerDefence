import java.awt.Point;


public class gameObject {
	public Point position;
	public int imgIndex;
	public mainFrame main;
	public boolean Destroy = false;
	
	gameObject(mainFrame main, int imgIndex, int x, int y){
		this.main = main;
		position = new Point(x,y);
		this.imgIndex = imgIndex;
	}
	
	gameObject(mainFrame main, int x, int y){
		this.main = main;
		position = new Point(x,y);
	}
	
	public void setImageIndex(int imgIndex){
		this.imgIndex = imgIndex;
	}
	
	public void move(){
	}
	
	public void Collision(gameObject gO){
	}
	
	public float distance(gameObject dest){
		return (float) Math.sqrt(dest.position.x * this.position.x + dest.position.y * this.position.y);
	}
}
