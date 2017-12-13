package com.yanghao.main;

public abstract class Good {
	
	protected int x;
	protected int y;
	
	protected String resURL;
	protected boolean Destroy;
	
	protected int blood;
	public int getBlood() {
		return blood;
	}

	public void setBlood(int blood) {
		this.blood = blood;
	}

	protected int width;
	protected int height;
	
	public Good(){
	}
	
	public Good(int x, int y,int blood) {
		this.x = x;
		this.y = y;
		this.blood = blood;
		
	}
	
	//绘图函数
	public abstract void draw();
	//获取排序规则函数
	public int getSortReguler(){
		return 0;
	}
	//判断是否销毁
	public boolean isDestory(){
		return Destroy;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getResURL() {
		return resURL;
	}

	public void setResURL(String resURL) {
		this.resURL = resURL;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
