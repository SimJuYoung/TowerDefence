
public class Tower extends gameObject{
	int range;
	Bullet bul;
	Tower(mainFrame main, int imgIndex, int x, int y, int range){
		super(main, imgIndex, x, y);
		this.main=main;
		range=1500;
	}
}
