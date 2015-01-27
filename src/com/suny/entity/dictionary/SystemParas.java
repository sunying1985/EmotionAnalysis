package com.suny.entity.dictionary;


/**
 * 系统配置参数加载类
 * 
 * @author Frank Adolf suny
 * @date   20141222 
 * @note   
 */
public class SystemParas {
	/***************   emotional  analysis dictionary        ****************/
	
	/**
	 * the absolute path of emotional dictionary 
	 */
	public static String emotion_posi_nega_dict = ReadConfigUtil.getValue("");
	
	/**
	 * the absolute path of family name and frequency dictionary
	 */
	public static String family_name_fre_dict = ReadConfigUtil.getValue("family_name_freq_dict");
	
	/**
	 * Traditional and simplified different characters
	 */
	public static String trad_diff_simpl_dict = ReadConfigUtil.getValue("trad_simple_dict_file");
	
	/**
	 * current running file path
	 */
	public static String currentRunPath = null;
	
	/**
	 * construction function 
	 */
	public SystemParas() {
		// load all the dictionary file
		this.init();
	}
	
	/**
	 * this function using initinal the file path
	 */
	public  void init() {
		if (null == currentRunPath) {
			currentRunPath = System.getProperty("user.dir")+"/data_entity";
		}
		
		this.setDictName();
	}
	
	/**
	 * set the dictionary name automatic
	 * @param 
	 */
	public  void setDictName() {
		
		// 1.emotional dictionary 
		if (emotion_posi_nega_dict.isEmpty() == true) {
			emotion_posi_nega_dict = currentRunPath + "/" + "";
		}
	}
}
