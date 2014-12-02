import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.util.Vector;


public class ImageBox {
	
	public Vector<Image> Images = new Vector<Image>();
	public String resourceFolder;
	private GameCanvas canvas;

	ImageBox(GameCanvas canvas){
		this.canvas = canvas;
		Images.clear(); 
		AddImages();
	}
	
	void AddImages(){
		resourceFolder = "..\\resource\\";
		AddImage("start.png");				//0
		AddImage("over.jpg");				//1
		AddImage("title.png");				//2
		AddImage("backGround.png");			//3
		AddImage("MainUI.jpg");				//4
		AddImage("titleControl.png");		//5
		AddImage("map1.png");				//6
		AddImage("controller_basic.png");	//7
		AddImage("UI_Tower1.png");			//8
		AddImage("UI_Tower2.png");			//9
		AddImage("UI_Tower3.png");			//10
		AddImage("UI_Tower4.png");			//11
		AddImage("UI_Cancel.png");			//12
	}
	
	void AddImage(String Addr){
		String Address = resourceFolder + Addr;
		Images.add(makeImage(Address));
	}
	
	public Image getImage(int Index){
		return Images.elementAt(Index);
	}
	
	private Image makeImage(String URL){
		Image img;
		Toolkit tk=Toolkit.getDefaultToolkit();
		img=tk.getImage(URL);
		return img;
	}
}
