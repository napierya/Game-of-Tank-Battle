package com.yanghao.interfaces;

import com.yanghao.main.Good;

public interface Moveable {
	
	//可以移动就会发生碰撞
	public abstract boolean checkCollsion(Good good);
}
