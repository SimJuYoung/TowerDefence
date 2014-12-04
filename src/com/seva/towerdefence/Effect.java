package com.seva.towerdefence;

import com.seva.towerdefence.frame.MainFrame;
import com.seva.towerdefence.models.GameObject;

public class Effect extends GameObject{
	public int DestroyFrame;
	private int timer;
	private int localImgIndex;
	
	public Effect(MainFrame main, int imgIndex, int x, int y){
		super(main, imgIndex, x, y);
		timer = 0;
		DestroyFrame = 50;
		localImgIndex = 1;
		main.Effects.addElement(this);
	}
	
	Effect(MainFrame main, int x, int y){
		super(main, x, y);
		timer = 0;
		DestroyFrame = 50;
		localImgIndex = 1;
		main.Effects.addElement(this);
	}
	
	@Override
	public void move(){
		anim();
	}
	
	public void anim(){
		timer++;
		if(localImgIndex * (DestroyFrame/10)< timer){//�ִϸ��̼� �������� 10����� ����
			localImgIndex++;
			imgIndex++;
		}
		if(DestroyFrame <= timer){
			Destroy = true;
		}
	}
}