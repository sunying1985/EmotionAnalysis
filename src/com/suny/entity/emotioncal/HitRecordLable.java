package com.suny.entity.emotioncal;

import java.util.ArrayList;

/**
 * 记录词语命中结果，供输出和调试使用
 * @author Aldof Frank 20150310
 *
 */
public class HitRecordLable {
	// the initinal text string
	private String text = "";
	
	public int [] beginPos = null;
	
	public ArrayList<String> hitStrArray= null;
	
	public int curHitNumber = 0;
	public HitRecordLable(String initStr) {
		
		this.text = initStr;
		int arrSize = text.length();
		this.beginPos = new int[arrSize];
		for (int i = 0; i < arrSize; i++) {
			this.beginPos[i] = 0;
		}
		this.hitStrArray = new ArrayList<String>();
		
	}
	
	public boolean initData(String initStr) {
		if (text.isEmpty() == true) {
			return false;
		}
		
		this.text = initStr;
		int arrSize = text.length();
		this.beginPos = new int[arrSize];
		for (int i = 0; i < arrSize; i++) {
			this.beginPos[i] = 0;
		}
		this.hitStrArray = new ArrayList<String>();
		return true;
	}
	/**
	 * 
	 * @param index     begin pos of the String
	 * @param hitStr    the hit string words
	 * @return
	 */
	public boolean addIndexAndRules(int index, String hitStr) {
		if (index < 0 || hitStr.isEmpty() == true) {
			return false;
		}
		this.beginPos[this.curHitNumber] = index;
		this.curHitNumber++;
		this.hitStrArray.add(hitStr);
		return true;
	}
	
	public void outputHitResult() {
		int k = 0;
		while(this.beginPos[k] != 0 && k < this.curHitNumber) {
			//int beginIndex = this.beginPos[k];
			//System.out.println( this.text.substring(beginIndex, /*beginIndex + */this.hitStrArray.get(k).length() - 1));
			System.out.println( this.hitStrArray.get(k));
			k++;
		}
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
