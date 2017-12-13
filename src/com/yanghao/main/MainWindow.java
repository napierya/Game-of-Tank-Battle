package com.yanghao.main;

import java.io.IOException;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Keyboard;

import com.yanghao.interfaces.Blockable;
import com.yanghao.interfaces.Moveable;
import com.yanghao.utils.DrawUtils;
import com.yanghao.utils.SoundUtils;
import com.yanghao.utils.Window;
import com.yanghao.vo.ConfigData;
import com.yanghao.vo.Direction;
/**
 * 
 * （发射子弹、移动、消失）
 * 坦克/敌方坦克
 * 子弹/敌方子弹
 * 爆炸物
 * 
 * （被打、消失、）
 * 墙
 * 铁
 * 水
 * 草
 * 
 * @author YanoHao
 *
 */
public class MainWindow extends Window{
	
	private Tank tank;
	private Bullet bullet;
	private Boss boss;
	public static CopyOnWriteArrayList<Good> list;
	public static CopyOnWriteArrayList<Good> enemyList;
	
	public int flag;
	public long last;
	
	
	
	public MainWindow(String title, int width, int height, int fps) {
		super(title, width, height, fps);
		

	}

	public static void main(String[] args) {
		MainWindow mainWindow = new MainWindow(ConfigData.TITLE, ConfigData.WIDTH, ConfigData.HEIGHT, ConfigData.FPS);
		mainWindow.start();
		try {
			SoundUtils.play("res/snd/start.wav");
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		
		
	}

	@Override
	protected void onCreate() {
		list = new CopyOnWriteArrayList<>();
		enemyList = new CopyOnWriteArrayList<>();
		//创建坦克对象（在窗户中间生成）
		tank = new Tank(ConfigData.WIDTH/2-64*3, ConfigData.HEIGHT-64, 32,15);
		list.add(tank);
		//创建敌方坦克
		for(int i = 0; i<5; i++)
		{
			EnemyTank enemyTank = new EnemyTank(64*i*4, 0, 5, 5);
			list.add(enemyTank);
			enemyList.add(enemyTank);
		}
		//添加BOSS
		boss = new Boss(8*64,64*9,1);
		list.add(boss);
		
		//创建墙
		
		for (int i = 0; i < 18; i++) {
			Wall wall = new Wall(64*i, 64*2, 3);
			list.add(wall);	
		}
		for (int i = 0; i < 18; i++) {
			Wall wall = new Wall(64*i, 64*3, 3);
			list.add(wall);	
		}
		//创建铁墙
		for (int i = 0; i < 18; i++) {
			Steel steel = new Steel(i*64, 4*64, 5);
			list.add(steel);	
		}
		for (int i = 0; i < 18; i+=2) {
			Steel steel = new Steel(i*64, 5*64, 5);
			list.add(steel);	
		}
		//创建草
		for (int i = 1; i < 9; i++) {
			Grass grass = new Grass(i*64, 6*64,Integer.MAX_VALUE);
			list.add(grass);	
		}		
		//创建小溪
		for (int i = 9; i < 17; i++) {
			Water water = new Water(i*64, 6*64, Integer.MAX_VALUE);
			list.add(water);	
		}
		//创建boos建筑
		for (int i = 7; i < 10; i++) {
			
			Steel steel = new Steel(i*64,64*8,5);
			list.add(steel);
		}
		for (int i = 7; i < 10; i+=2) {
			
			Steel steel = new Steel(i*64,64*9,5);
			list.add(steel);
		}
		
		

		
		//背景音乐
		try {
			SoundUtils.play("res/snd/start.wav");
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}	
	}

	@Override
	protected void onMouseEvent(int key, int x, int y) {
		
		

		
	}

	@Override
	protected void onKeyEvent(int key) {
		
		switch (key) {
		case Keyboard.KEY_UP:
			tank.run(Direction.Up);
			break;
		case Keyboard.KEY_DOWN:
			tank.run(Direction.Down);
			break;
		case Keyboard.KEY_LEFT:
			tank.run(Direction.Left);
			break;
		case Keyboard.KEY_RIGHT:
			tank.run(Direction.Right);
			break;		
		case Keyboard.KEY_SPACE:
			bullet = tank.shot();
			if(bullet!=null)
			list.add(bullet);
			break;
		}

	}
	

	//刷新函数
	@Override
	protected void onDisplayUpdate() {
		//判断输赢
		if(tank.isDestory()||enemyList.size()==0||boss.isDestory()){
			if(tank.isDestory()){
				try {
					DrawUtils.draw("res\\img\\over.png", 0, 0);
					list.clear();
					enemyList.clear();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			else if(enemyList.size()==0)
			{
				try {
					DrawUtils.draw("res\\img\\win.png", 0, 0);
					list.clear();
					enemyList.clear();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			else{
				try {
					DrawUtils.draw("res\\img\\over.png", 0, 0);
					list.clear();
					//enemyList.clear();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}			
			}
			//开启子线程3秒后退出虚拟机结束游戏
			endGame();
		}
		
		//对象排序实现物体按照正常逻辑层次显示
		list.sort(new Comparator<Good>() {

			@Override
			public int compare(Good goodAdd, Good goodOld) {
				return goodAdd.getSortReguler()-goodOld.getSortReguler();
			}
			
		});
		
		//给敌方坦克不断产生随机方向使其动起来
		
		if(System.currentTimeMillis()-last>50)
		{
			for (Good good : list) {
				if(good instanceof EnemyTank){
					((EnemyTank)good).run();
					EnemyBullet enemyBullet = ((EnemyTank)good).shot();
					if(enemyBullet!=null)
					{
						list.add(enemyBullet);
					}
				}
			}
			last = System.currentTimeMillis();
		}
		
		//对List里对象进行绘图显示在界面上,并判断当前对象是否要被销毁
		if(list!=null)
		{
			for (Good good : list) {
				good.draw();
				if(good.isDestory())
				{
					list.remove(good);
					if(good instanceof EnemyTank){
						enemyList.remove(good);
					}
				}
			}	
		}
		
		//不断对不同物体做碰撞检测,执行每个对象里的碰撞后的代码
		for (Good good : list) {
			for (Good good2 : list) {
				if(good instanceof Moveable&&good2 instanceof Blockable&&good!=good2)
				{  
					if(((Moveable)good).checkCollsion(good2)){
						
//						if(good instanceof EnemyTank && good2 instanceof EnemyTank){
//							
//							((EnemyTank)good).setDirection(((EnemyTank)good2).getDirection());
//						}
						
						
						break;	//当检测到与某物发生碰撞时，跳出当前循环，不再与后面的对象做检测
						//子弹和坦克发生自我碰撞，出去坦克的阻挡接口
					}
				}				
			}  
		}
	
		
	}

	public void endGame() {
		new Thread(){
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				
				System.exit(0);
			};
		}.start();
	}
		
		


}
