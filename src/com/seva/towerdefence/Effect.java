package com.seva.towerdefence;

import com.seva.towerdefence.frame.MainFrame;
import com.seva.towerdefence.models.GameObject;

public class Effect extends GameObject{
	public int DestroyFrame;
	private int Framer;
	
	Effect(MainFrame main, int imgIndex, int x, int y){
		super(main, imgIndex, x, y);
		Framer = 0;
		DestroyFrame = 10;
	}
	
	Effect(MainFrame main, int x, int y){
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