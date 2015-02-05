package com.suny.entity.emotioncal;

public class WordsValue {

	public WordsValue() {
		
	}
	public WordsValue(String e) {
		this.item = e;
	}
	public String item = "";
	
	public void setItem(String word) {
		this.item = word;
	}
	/**
	 * only get value with the length of words
	 * @return
	 */
	public double getValue() {
		if (this.item.isEmpty() == true) {
			return 0.0;
		}
		int itemLen = this.item.length();
		double scoreValue = 0.0;
		if (itemLen == 1) {
			scoreValue = 0.5;
		}
		else if(itemLen == 2) {
			scoreValue = 1;
		}
		else if(itemLen == 3) {
			scoreValue = 1.5;
		}
		else if(itemLen == 4) {
			scoreValue = 2;
		}
		else if(itemLen == 5) {
			scoreValue = 2.5;
		}
        else if(itemLen == 6) {
        	scoreValue = 3.0;
		}
        else if(itemLen > 6) {
        	scoreValue = 3.5;
		}
		
		return scoreValue;
	}
	/**
	 * only get value with the length of words
	 * @return
	 */
	public double getValue(String word) {
		if (word.isEmpty() == true) {
			return 0.0;
		}
		int itemLen = word.length();
		double scoreValue = 0.0;
		if (itemLen == 1) {
			scoreValue = 0.5;
		}
		else if(itemLen == 2) {
			scoreValue = 1;
		}
		else if(itemLen == 3) {
			scoreValue = 1.5;
		}
		else if(itemLen == 4) {
			scoreValue = 2;
		}
		else if(itemLen == 5) {
			scoreValue = 2.5;
		}
        else if(itemLen == 6) {
        	scoreValue = 3.0;
		}
        else if(itemLen > 6) {
        	scoreValue = 3.5;
		}
		
		return scoreValue;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
