package com.yanghao.main;

import java.io.IOException;

import com.yanghao.interfaces.Blockable;
import com.yanghao.utils.DrawUtils;

public class Water extends Good implements Blockable{
	
	public Water(int x, int y, int blood) {
		super(x, y, blood);
		this.resURL = "res/img/water.gif";
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
		try {
			DrawUtils.draw(resURL, x, y);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	

}
