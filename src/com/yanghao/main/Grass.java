package com.yanghao.main;

import java.io.IOException;

import com.yanghao.utils.DrawUtils;

public class Grass extends Good{
	


	public Grass(int x, int y, int blood) {
		super(x, y, blood);
		this.resURL = "res/img/grass.gif";
		if(resURL!=null){
			try {
				int[] arr = DrawUtils.getSize(resURL);
				this.width = arr[0];
				this.height = arr[1];
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void draw() {
		// TODO 自动生成的方法存根
		try {
			DrawUtils.draw(resURL, x, y);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	//重新排序规则函数
		@Override
		public int getSortReguler() {
			// TODO 自动生成的方法存根
			return 2;
		}
		
		
	

}
