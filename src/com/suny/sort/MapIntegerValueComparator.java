package com.suny.sort;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

public class MapIntegerValueComparator implements Comparator<Map.Entry<String, Integer>>{
	public int compare(Entry<String, Integer> me1, Entry<String, Integer> me2) {  
		  // sort by the positive sequence
	      //return me1.getValue().compareTo(me2.getValue()); 
		  // The reverse sort by value
		  return me2.getValue().compareTo(me1.getValue()); 
	 } 
}
