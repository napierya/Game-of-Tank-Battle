package com.yanghao.main;

import java.io.IOException;

import com.yanghao.interfaces.Blockable;
import com.yanghao.interfaces.Moveable;
import com.yanghao.utils.CollsionUtils;
import com.yanghao.utils.DrawUtils;
import com.yanghao.utils.SoundUtils;
import com.yanghao.vo.ConfigData;
import com.yanghao.vo.Direction;

public class EnemyBullet extends Good implements Moveable,Blockable{
	
//	private int x;
//	private int y;
//	private int width;
//	private int height;
	private Direction direction;
	private int speed;
	private boolean Destroy;
	
	
	public EnemyBullet(EnemyTank enemyTank) {
		super();
		resURL = "res/img/bullet_u.gif";
		this.direction = enemyTank.getDirection();
		if(resURL!=null){
			try {
				int[] arr = DrawUtils.getSize(resURL);
				this.width = arr[0];
				this.height = arr[1];
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//设置子弹速度
		this.speed = 10;
		switch (this.direction) {
		case Up:
			this.x = enemyTank.getX()+(enemyTank.getWidth()/2-this.width/2);
			this.y = enemyTank.getY()-this.height/2;
			break;
			
		case Down:
			this.x = enemyTank.getX()+(enemyTank.getWidth()/2-this.width/2);
			this.y = enemyTank.getY()+enemyTank.getHeight()-this.height/2;
			break;
			
		case Left:
			this.x = enemyTank.getX()-this.width/2;
			this.y = enemyTank.getY()+(enemyTank.getHeight()/2-this.height/2);
			break;
			
		case Right:
			this.x = enemyTank.getX()+enemyTank.getWidth()-this.width/2;
			this.y = enemyTank.getY()+(enemyTank.getHeight()/2-this.height/2);
			break;
		}
		try {
			SoundUtils.play("res/snd/fire.wav");
			int[] arr = DrawUtils.getSize(ConfigData.BULLET_UP_JPG);
			this.width = arr[0];
			this.height = arr[1];
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}
	

	//子弹飞绘画
	@Override
	public void draw(){
		try {
			switch (direction) {
			case Up:
				DrawUtils.draw(ConfigData.BULLET_UP_JPG, x, y);
				y-=speed;
				break;
			case Down:
				DrawUtils.draw(ConfigData.BULLET_DOWN_JPG, x, y);
				y+=speed;
				break;
			case Left:
				DrawUtils.draw(ConfigData.BULLET_LEFT_JPG, x, y);
				x-=speed;
				break;
			case Right:
				DrawUtils.draw(ConfigData.BULLET_RIGHT_JPG, x, y);
				x+=speed;
				break;
			}
		} catch (IOException e) {
			// TODO 自动生成的  catch 块
			e.printStackTrace();
		}
		
		//越界处理
		if(x<0||y<0||x>ConfigData.WIDTH||y>ConfigData.HEIGHT)
		{
			Destroy = true;
		}
	}
	
	//重新排序规则函数
		@Override
		public int getSortReguler() {
			// TODO 自动生成的方法存根
			return 3;
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
			//不与己方子弹或者坦克做碰撞检测
			if(good instanceof EnemyBullet||good instanceof EnemyTank)
			{
				return false;
			}
			//碰撞检测工具类检测碰撞
			boolean flag = CollsionUtils.isCollsionWithRect(x1, y1, w1, h1, x2, y2, w2, h2);
			if(flag&&!(good instanceof Water))
			{
				//System.out.println(x1+" "+y1);
				//MainWindow.list.add(new Boom(x1, y1));
				MainWindow.list.add(new Boom(x2-32, y2-32));
				this.Destroy = true;
				if(good instanceof Blockable){
					int blood = good.getBlood();
					good.setBlood(--blood);
				}
			}
				
			return flag;
			
		}


		@Override
		public boolean isDestory() {
			return Destroy;
		}


	

}
