package com.suny.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 
 * @author Frank suny 20141115
 * only sorted the map value or key 
 */
public class InvertedItemValue {

	/**
	 *  sorted by key, but the key cannot repeat 
	 */
    public void testMain(){  
	    HashMap<Integer, String> arr = new HashMap<Integer, String>();  
		arr.put(3,"c");  
		arr.put(2,"b");  
		arr.put(4,"d");  
		arr.put(1,"a");
		arr.put(3,"m");
		for(int key: arr.keySet())  
			System.out.println(key+"=>"+arr.get(key));  
	}  
    /** 
    * 使用 Map按key进行排序 
    * @param map 
    * @return 
    */  
    public static Map<String, String> sortMapByKey(Map<String, String> map) {  
         
    	if (map == null || map.isEmpty()) {  
             return null;  
        }  
        Map<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());  
        sortMap.putAll(map); 
         
        return sortMap;  
    }  

    /** 
    * 使用 Map按value进行排序 
    * @param map 
    * @return 
    */  
    public static Map<String, String> sortMapByValue(Map<String, String> map) {  
         
    	 if (map == null || map.isEmpty()) {  
              return null;  
         }  
    	 
         Map<String, String> sortedMap = new LinkedHashMap<String, String>();  
         List<Map.Entry<String, String>> entryList = new ArrayList<Map.Entry<String, String>>(map.entrySet());  
         Collections.sort(entryList, new MapValueComparator());  
         Iterator<Map.Entry<String, String>> iter = entryList.iterator();  
         
         Map.Entry<String, String> tmpEntry = null;  
         
         while (iter.hasNext()) {  
             tmpEntry = iter.next();  
             sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());  
         }  
         
         return sortedMap;  
    }  

    /** 
     * 使用 Map按value进行排序, double类型的
     * @param map 
     * @return 
     */  
     public static Map<String, Double> sortMapByDoubleValue(Map<String, Double> map) {  
          
     	 if (map == null || map.isEmpty()) {  
               return null;  
          }  
     	 
          Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();  
          List<Map.Entry<String, Double>> entryList = new ArrayList<Map.Entry<String, Double>>(map.entrySet());  
          Collections.sort(entryList, new MapDoubleValueComparator());  
          Iterator<Map.Entry<String, Double>> iter = entryList.iterator();  
          
          Map.Entry<String, Double> tmpEntry = null;  
          
          while (iter.hasNext()) {  
              tmpEntry = iter.next();  
              sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());  
          }  
          
          return sortedMap;  
     } 
     /** 
      * 使用 Map按value进行排序, integer类型的
      * @param map 
      * @return  按着键值倒序排序的结果
      */  
      public static Map<String, Integer> sortMapByIntegerValue(Map<String, Integer> map) {  
           
      	 if (map == null || map.isEmpty()) {  
                return null;  
           }  
      	 
           Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();  
           List<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());  
           Collections.sort(entryList, new MapIntegerValueComparator());  
           Iterator<Map.Entry<String, Integer>> iter = entryList.iterator();  
           
           Map.Entry<String, Integer> tmpEntry = null;  
           
           while (iter.hasNext()) {  
               tmpEntry = iter.next();  
               sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());  
           }  
           
           return sortedMap;  
      }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		InvertedItemValue iiv = new InvertedItemValue();
		//iiv.testMain();
		/*Map<String, String> arr = new HashMap<String, String>();  
		arr.put("w","0.36");  
		arr.put("x","5.26");  
		arr.put("y","100.4");  
		arr.put("z","9.3");
		arr.put("p","4.2");
		for(String key: arr.keySet())  {
			System.out.println(key+"=>"+arr.get(key));  
		}
		
		Map<String, String> retDict = new HashMap<String, String>();
		System.out.println("the key sort..."); 
		retDict = iiv.sortMapByKey(arr);
		for(String key: retDict.keySet())  {
			System.out.println(key+"=>"+retDict.get(key));  
		}
		
		System.out.println("the value sort...");
		retDict = iiv.sortMapByValue(arr);
		for(String key: retDict.keySet())  {
			System.out.println(key+"=>"+retDict.get(key));  
		}
		*/
		/*
		System.out.println("the initinal value sort...");
		Map<String, Double> arrDouble = new HashMap<String, Double>();  
		arrDouble.put("suny", 1.256);
		arrDouble.put("li", 8.6958423);
		arrDouble.put("xu", 6.0);
		arrDouble.put("xuze", 0.0854);
		arrDouble.put("rui", 6.0);
		for(String key: arrDouble.keySet())  {
			System.out.println(key+"=>"+arrDouble.get(key));  
		}
		
		Map<String, Double> retDict = new HashMap<String, Double>();
		System.out.println("the doube value sort..."); 
		retDict = iiv.sortMapByDoubleValue(arrDouble);
		for(String key: retDict.keySet())  {
			System.out.println(key+"=>"+retDict.get(key));  
		}
		*/
		System.out.println("the initinal value sort...");
		Map<String, Integer> arrDouble = new HashMap<String, Integer>();  
		arrDouble.put("suny", 1);
		arrDouble.put("li", 8);
		arrDouble.put("xu", 6);
		arrDouble.put("xuze", 10);
		arrDouble.put("rui", 6);
		arrDouble.put("ruihao", 16);
		for(String key: arrDouble.keySet())  {
			System.out.println(key+"=>"+arrDouble.get(key));  
		}
		
		Map<String, Integer> retDict = new HashMap<String, Integer>();
		System.out.println("the doube value sort..."); 
		retDict = iiv.sortMapByIntegerValue(arrDouble);
		for(String key: retDict.keySet())  {
			System.out.println(key+"=>"+retDict.get(key));  
		}
	}
}
