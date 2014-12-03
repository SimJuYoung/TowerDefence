package com.seva.towerdefence;
import java.awt.Point;
import java.util.Vector;

import com.seva.towerdefence.frame.MainFrame;
import com.seva.towerdefence.models.GameObject;


public class Map extends GameObject{
	
	private int Route[][];		//적이 이동할 곳과 타워를 지을 수 있는 곳의 구분
	public Vector<Point> Routing;	//적이 이동해야할 순서
	private int stage;
	
	
	//constructer
	Map(MainFrame main, int x, int y) {super(main, x, y);	stage = 1; Routing = new Vector<Point>(); setRoute(); }
	Map(MainFrame main, int ImgNum, int x, int y) {super(main, ImgNum, x, y);	stage = 1;  Routing = new Vector<Point>(); setRoute(); }
	//constructer
	
	void setStage(int num){
		stage = num;
	}
	
	public Point getPositionRouting(int RoutingNumber){
		int x = (main.getWidth()*(Routing.elementAt(RoutingNumber).x+1))/14 - main.getWidth()/28;
		int y = (main.getHeight()-30)*(Routing.elementAt(RoutingNumber).y+1)/10 - (main.getHeight()-30)/20;
		Point Pos = new Point(x,y);
		return Pos;
	}
	
	void setRoute(){
		switch(stage){
		case 1:
			Route = new int[][]{
					{1, 1, 1, 1, 1, 1, 0, 1},
					{1, 0, 0, 0, 0, 1, 0, 1},
					{1, 0, 1, 1, 0, 1, 0, 1},
					{1, 0, 1, 1, 0, 1, 0, 1},
					{1, 0, 1, 1, 0, 1, 0, 1},
					{1, 0, 1, 0, 0, 1, 0, 1},
					{1, 0, 1, 0, 1, 1, 0, 1},
					{1, 0, 1, 0, 1, 1, 0, 1},
					{1, 0, 1, 0, 1, 1, 0, 1},
					{1, 0, 1, 0, 1, 1, 0, 1},
					{1, 0, 1, 0, 0, 0, 0, 1},
					{1, 0, 1, 1, 1, 1, 1, 1},
					};
				//Routing.clear();
				Routing.addElement(new Point(1, 7));	//0
				Routing.addElement(new Point(11, 7));	//1
				Routing.addElement(new Point(11, 4));	//2
				Routing.addElement(new Point(6, 4));	//3
				Routing.addElement(new Point(6, 5));	//4
				Routing.addElement(new Point(2, 5));	//5
				Routing.addElement(new Point(2, 2));	//6
				Routing.addElement(new Point(12, 2));	//7
			break;
		case 2:
			Route = new int[][]{
					{1,1,1,1,1,1,1,1,1,1,1,1},
					{0,0,0,0,0,0,0,0,0,0,0,1},
					{1,1,1,1,1,1,1,1,1,1,0,1},
					{1,0,0,0,0,0,1,1,1,1,0,1},
					{1,0,1,1,1,0,0,0,0,0,0,1},
					{1,0,1,1,1,1,1,1,1,1,1,1},
					{1,0,0,0,0,0,0,0,0,0,0,0},
					{1,1,1,1,1,1,1,1,1,1,1,1},};
				//Routing.clear();
			Routing.add(new Point(1, 7));	//0
			Routing.add(new Point(11, 7));	//1
			Routing.add(new Point(11, 4));	//2
			Routing.add(new Point(6, 4));	//3
			Routing.add(new Point(6, 5));	//4
			Routing.add(new Point(2, 5));	//5
			Routing.add(new Point(2, 2));	//6
			Routing.add(new Point(12, 2));	//7
			break;
		case 3:
			Route = new int[][]{
					{1,1,1,1,1,1,1,1,1,1,1,1},
					{0,0,0,0,0,0,0,0,0,0,0,1},
					{1,1,1,1,1,1,1,1,1,1,0,1},
					{1,0,0,0,0,0,1,1,1,1,0,1},
					{1,0,1,1,1,0,0,0,0,0,0,1},
					{1,0,1,1,1,1,1,1,1,1,1,1},
					{1,0,0,0,0,0,0,0,0,0,0,0},
					{1,1,1,1,1,1,1,1,1,1,1,1},};
				Routing.clear();
				Routing.add(new Point(1, 7));	//0
				Routing.add(new Point(11, 7));	//1
				Routing.add(new Point(11, 4));	//2
				Routing.add(new Point(6, 4));	//3
				Routing.add(new Point(6, 5));	//4
				Routing.add(new Point(2, 5));	//5
				Routing.add(new Point(2, 2));	//6
				Routing.add(new Point(12, 2));	//7
			break;
		case 4:
			Route = new int[][]{
					{1,1,1,1,1,1,1,1,1,1,1,1},
					{0,0,0,0,0,0,0,0,0,0,0,1},
					{1,1,1,1,1,1,1,1,1,1,0,1},
					{1,0,0,0,0,0,1,1,1,1,0,1},
					{1,0,1,1,1,0,0,0,0,0,0,1},
					{1,0,1,1,1,1,1,1,1,1,1,1},
					{1,0,0,0,0,0,0,0,0,0,0,0},
					{1,1,1,1,1,1,1,1,1,1,1,1},};
				Routing.clear();
				Routing.add(new Point(1, 7));	//0
				Routing.add(new Point(11, 7));	//1
				Routing.add(new Point(11, 4));	//2
				Routing.add(new Point(6, 4));	//3
				Routing.add(new Point(6, 5));	//4
				Routing.add(new Point(2, 5));	//5
				Routing.add(new Point(2, 2));	//6
				Routing.add(new Point(12, 2));	//7
			break;
		}
	}
	public int[][] getRoute() {
		return Route;
	}
	public void setRoute(int route[][]) {
		Route = route;
	}
	
}
