package com.yanghao.main;

import java.io.IOException;
import java.util.Random;

import com.yanghao.interfaces.Blockable;
import com.yanghao.interfaces.Moveable;
import com.yanghao.utils.CollsionUtils;
import com.yanghao.utils.DrawUtils;
import com.yanghao.vo.ConfigData;
import com.yanghao.vo.Direction;

public class EnemyTank extends Good implements Moveable,Blockable{
	
//	private int x;
//	private int y;
//	private int width;
//	private int height;
	private Direction direction; //坦克方向
	private int speed;		//坦克移动速度
	private long lastTime;  //记录炮弹发射时间
	private boolean flag;  //是否碰撞标志
	private Direction hitDirection; //撞上方向
//	private Direction lastDir;
	
	public Direction getDirection() {
		return direction;
	}


	public void setDirection(Direction direction) {
		this.direction = direction;
	}


	public int getSpeed() {
		return speed;
	}


	public void setSpeed(int speed) {
		this.speed = speed;
	}


	public Direction getHitDirection() {
		return hitDirection;
	}


	public void setHitDirection(Direction hitDirection) {
		this.hitDirection = hitDirection;
	}

	
	
	public EnemyTank(int x, int y, int speed, int blood) {
		super(x,y,blood);
		this.speed = speed;
		direction = Direction.Up;
		resURL = ConfigData.ENEMY_TANK_UP_JPG;
		try {
			int[] arr = DrawUtils.getSize(resURL);
			this.width = arr[0];
			this.height = arr[1];
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		changeEnemyTankDirection();
	}
	
	//坦克绘制
	@Override
	public void draw(){
		try {
			switch (direction) {
			case Up:
				DrawUtils.draw(ConfigData.ENEMY_TANK_UP_JPG, x, y);
				break;
			case Down:
				DrawUtils.draw(ConfigData.ENEMY_TANK_DOWN_JPG, x, y);
				break;
			case Left:
				DrawUtils.draw(ConfigData.ENEMY_TANK_LEFT_JPG, x, y);
				break;
			case Right:
				DrawUtils.draw(ConfigData.ENEMY_TANK_RIGHT_JPG, x, y);
				break;
			}
	
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
	
	//根据键盘方向信息改变方向并前进
	public void run(){
		//做个判断
		if(hitDirection!=null&&hitDirection == direction){
			return;
		}
		
		//做个判断，如果当期方向与上一次方向不一致则只改变方向不增加或减少位移
//		if(lastDir==null)
//		lastDir = direction;
//		else
//		{
//			if(this.direction!=lastDir)
//			{
//				this.direction = lastDir;
//				return;
//			}
//		}
		
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
		{
			x = 0;
			direction = Direction.Right;
		}
		if(y<0)
		{
			y = 0;
			direction = Direction.Down;
		}
		if(x>ConfigData.WIDTH-64)
		{
			x = ConfigData.WIDTH-64;
			direction = Direction.Left;
		}
		if(y>ConfigData.HEIGHT-64)
		{
			y = ConfigData.HEIGHT-64;
			direction = Direction.Up;
		}
	}
	
	//发射子弹
	public EnemyBullet shot(){
		//设置炮弹发射间隔至少为300毫秒
		long currentTimeMillis = System.currentTimeMillis();
		if(currentTimeMillis-lastTime>=1000)
		{
			lastTime = currentTimeMillis;
			EnemyBullet bullet = new EnemyBullet(this);
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
		
		//碰撞偏移使像素相交就能返回true
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
			if(good instanceof EnemyTank){
				direction = reverDirection(direction);
			}
			hitDirection = direction;
		}
		else
		{
			hitDirection = null;
		}
		
		return flag;
		
	}
	//反转坦克方向
	public Direction reverDirection(Direction direc){
		
		if(direc==Direction.Up)
		{
			return Direction.Down;
		}
		else if(direc==Direction.Down)
		{
			return Direction.Up;
		}
		else if(direc==Direction.Left)
		{
			return Direction.Right;
		}
		else
		{
			return Direction.Left;
		}
	}
	
	
	//产生随机方向
	private void changeEnemyTankDirection() {
		new Thread(){
			
			@Override
			public void run() {
				while(true){
					Random random = new Random();
					int i = random.nextInt(4);
					if (i==1) {
						direction = Direction.Up;
					}else if (i==2) {
						direction = Direction.Down;
					}else if (i==3) {
						direction = Direction.Left;
					}else {
						direction = Direction.Right;
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}	
			}
			
		}.start();
	}
	
	@Override
	public boolean isDestory() {
		if(blood<=0)
			return true;
		return false;
	}


	
}
