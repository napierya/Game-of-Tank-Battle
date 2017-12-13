package com.yanghao.main;

import java.io.IOException;

import com.yanghao.interfaces.Blockable;
import com.yanghao.interfaces.Moveable;
import com.yanghao.utils.CollsionUtils;
import com.yanghao.utils.DrawUtils;
import com.yanghao.vo.ConfigData;
import com.yanghao.vo.Direction;

public class Tank extends Good implements Moveable,Blockable{
	
//	private int x;
//	private int y;
//	private int width;
//	private int height;
	private Direction direction; //坦克方向
	private int speed;		//坦克移动速度
	private long lastTime;  //记录炮弹发射时间
	private boolean flag;  //是否碰撞标志
	private Direction hitDirection; //撞上方向
	
	public Direction getDirection() {
		return direction;
	}

	public int getSpeed() {
		return speed;
	}
	
	public Tank(int x, int y, int speed, int blood) {
		super(x,y,blood);
		this.speed = speed;
		direction = Direction.Up;
		resURL = ConfigData.TANK_UP_JPG;
		try {
			int[] arr = DrawUtils.getSize(resURL);
			this.width = arr[0];
			this.height = arr[1];
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}
	
	
	//坦克绘制
	@Override
	public void draw(){
		try {
			switch (direction) {
			case Up:
				DrawUtils.draw(ConfigData.TANK_UP_JPG, x, y);
				break;
			case Down:
				DrawUtils.draw(ConfigData.TANK_DOWN_JPG, x, y);
				break;
			case Left:
				DrawUtils.draw(ConfigData.TANK_LEFT_JPG, x, y);
				break;
			case Right:
				DrawUtils.draw(ConfigData.TANK_RIGHT_JPG, x, y);
				break;
			}
	
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
	
	//根据键盘方向信息改变方向并前进
	public void run(Direction direction){
		//做个判断
		if(hitDirection!=null&&hitDirection == direction){
			return;
		}
		
	
		//做个判断，如果当期方向与上一次方向不一致则只改变方向不增加或减少位移
		if(this.direction!=direction)
		{
			this.direction = direction;
			return;
		}
		
		switch (direction) {
		case Up:
			y-=speed;
			break;
		case Down:
			y+=speed;
			break;
		case Left:
			x-=speed;
			break;
		case Right:
			x+=speed;
			break;
		}
		
		//越界限制
		if(x<0)
		{x=0;}
		if(y<0)
		{y=0;}
		if(x>ConfigData.WIDTH-64)
		{
			x=ConfigData.WIDTH-64;
		}
		if(y>ConfigData.HEIGHT-64)
		{
			y=ConfigData.HEIGHT-64;
		}
	}
	
	//发射子弹
	public Bullet shot(){
		//设置炮弹发射间隔至少为300毫秒
		long currentTimeMillis = System.currentTimeMillis();
		if(currentTimeMillis-lastTime>=300&&this.getBlood()>0)
		{
			lastTime = currentTimeMillis;
			Bullet bullet = new Bullet(this);
			return bullet;
		}
		
		return null;
	}
	
	
	
	//重新排序规则函数
	@Override
	public int getSortReguler() {
		// TODO 自动生成的方法存根
		return 1;
	}

	@Override
	public boolean checkCollsion(Good good) {
		
		if(this==good)
		{
			return false;
		}
		
		int x1 = good.getX();;
		int y1 = good.getY();
		int w1 = good.getWidth();
		int h1 = good.getHeight();

		int x2 = x;
		int y2 = y;
		int w2 = width;
		int h2 = height;
		
		//碰撞偏移使能够像素相交就能返回true
		switch (this.direction) {
		case Up:
			y2-=speed;
			break;
			
		case Down:
			y2+=speed;
			break;
			
		case Left:
			x2-=speed;
			break;
			
		case Right:
			x2+=speed;
			break;
		}
			

		//碰撞检测工具类检测碰撞
		flag = CollsionUtils.isCollsionWithRect(x1, y1, w1, h1, x2, y2, w2, h2);
		//当发生碰撞时，记录当前方向，可在移动方法内做处理让其不移动
		if(flag)
		{
			hitDirection = direction;
		}
		else
		{
			hitDirection = null;
		}
		
		
		return flag;
		
	}
	
	@Override
	public boolean isDestory() {
		if(blood<=0)
			return true;
		return false;
	}


	
}
