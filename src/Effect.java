
public class Effect extends gameObject{
	public int DestroyFrame;
	private int Framer;
	
	Effect(mainFrame main, int imgIndex, int x, int y){
		super(main, imgIndex, x, y);
		Framer = 0;
		DestroyFrame = 10;
	}
	
	Effect(mainFrame main, int x, int y){
		super(main, x, y);
		Framer = 0;
		DestroyFrame = 10;
	}
	
	public void setDestroyFrame(int frames){
		DestroyFrame = frames;
	}
	
	public void move(){
		Framing();
	}
	
	public void Framing(){
		Framer++;
		if(Framer >= DestroyFrame){
			Destroy = true;
		}
	}
}