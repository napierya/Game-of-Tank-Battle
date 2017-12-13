package com.yanghao.main;

import java.io.IOException;

import com.yanghao.utils.DrawUtils;
import com.yanghao.utils.SoundUtils;

public class Boom extends Good{
	private String[] arr = {"res/img/blast_1.gif","res/img/blast_2.gif","res/img/blast_3.gif","res/img/blast_4.gif","res/img/blast_5.gif"};
	private long startTime;
	public Boom(int x, int y) {
		this.x = x;
		this.y = y;
		startTime = System.currentTimeMillis();
		try {
			SoundUtils.play("res/snd/blast.wav");
		} catch (IOException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
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
			for (String resURL : arr) {
				DrawUtils.draw(resURL, x, y);
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}

	@Override
	public boolean isDestory() {
		if((System.currentTimeMillis()-startTime)>30)
			return true;
		return false;	
	}
	
	

	
}
