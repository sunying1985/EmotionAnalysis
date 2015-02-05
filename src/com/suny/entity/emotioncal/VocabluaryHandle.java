package com.suny.entity.emotioncal;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.suny.entity.dictionary.SystemParas;
import com.suny.entity.encryption.EncryptDictionary;

public class VocabluaryHandle {

	 /**
	 * 全角的数字
     * */
    //public static final String [] chineseNumbers = {"１","２","３","４","５","６","７","８","９","０"};
    
    /**
     * the punction
     */
	/*public static final String [] puntLables = {"，","。",",","：","；","？","）","?","（","、",".","·","！","!","(",")","／","｜",
		                                      "∼","～","［","］","｛","｝","《","》","‘","'","“","”","〔","〕","…","―","【","】","<",">"};
	*/
	/**
	 * the family name dictionary name
	 */
	public Set<String> faimlyNameDict = null;
	/**
	 * 中文停用词
	 */
	public Set<String> stopWordsDict = null;
	/** 
	 * the traditional and simple character dictionary  
	 * */
	public Map<Character, Character> tradSimpDict = null;
	
	/**
	 * the positive words dictionary
	 */
	public Set<String> posiWordDict = null;
	
	/**
	 * the negative words dictionary
	 */
	public Set<String> negaWordDict = null;
	
	/**
	 * the special word dictionary
	 */
	public LevelWords  rulesWordDict = null;
	
	/**
	 * construction function
	 */
	public VocabluaryHandle() {
		
		this.faimlyNameDict = new HashSet<String>();
		//  traditional and suny add 20141127
		this.tradSimpDict = new HashMap<Character, Character>();
		this.stopWordsDict = new HashSet<String>();
		
		this.posiWordDict = new HashSet<String>();
		this.negaWordDict = new HashSet<String>();
		
	}
	
	/**
	 * load the dictionary resource
	 */
	public void initdict() {
		try {

			this.loadFamilyNameDictionary();
			this.loadTradSimpCharPairsDictionary(); // 20141127 SUNY ADD
			this.loadStopWordsDictionary();         // 20150112 suny add
			
			this.loadPositiveWordDictionary();
			this.loadNegativeWordDictionary();
			
			this.loadRulesWordDictionary();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * loading the family name dictionary
	 * @return  true if success otherwise is false
	 * @throws IOException
	 */
	public boolean loadFamilyNameDictionary() throws IOException  {
		
		String famiDictPath = SystemParas.family_name_fre_dict;
		if (famiDictPath.isEmpty()) {
			System.err.println("ConfigInfoError" + "logging family name  dictionary is error!");
			return false;
		}
		
		EncryptDictionary ed = new EncryptDictionary();
		if (true == ed.decryptWordFile(famiDictPath)) {
			//System.out.println(ed.wordVocab.size());
			Iterator<String> iter = ed.wordVocab.iterator();
			while (iter.hasNext()) {
				
				String textLine = iter.next().toString();
				
				if (false == textLine.startsWith("#")) {
					String word = "";
					int pos = textLine.indexOf("\t");
					if (pos != -1) {
						word = textLine.substring(0,pos);
						this.faimlyNameDict.add(word);
					}
					else {
						System.err.println("split tag is error!" + textLine);
					}
				}
			}
		}
		
		if (this.faimlyNameDict.size() == 0) {
			System.err.println("ConfigInfoError" + "logging family name dictionary SIZE is error!");
			return false;
		}

		return true;
	}
	
	/**
	 * loading the positive dictionary
	 * @return  true if success otherwise is false
	 * @throws IOException
	 */
	public boolean loadPositiveWordDictionary() throws IOException  {
		
		String posiDictPath = SystemParas.emotion_posi_dict;
		if (posiDictPath.isEmpty()) {
			System.err.println("ConfigInfoError" + "logging positive words dictionary is error!");
			return false;
		}
		
		EncryptDictionary ed = new EncryptDictionary();
		if (true == ed.decryptWordFile(posiDictPath)) {
			//System.out.println(ed.wordVocab.size());
			Iterator<String> iter = ed.wordVocab.iterator();
			while (iter.hasNext()) {
				
				String textLine = iter.next().toString();
				
				if (false == textLine.startsWith("#")) {
					String word = "";
					int pos = textLine.indexOf("\t");
					if (pos != -1) {
						word = textLine.substring(0,pos);
						this.posiWordDict.add(word);
					}
					else {
						word = textLine;
						this.posiWordDict.add(word);
					}
				}
			}
		}
		
		if (this.posiWordDict.size() == 0) {
			System.err.println("ConfigInfoError" + "logging positive words dictionary SIZE is error!");
			return false;
		}

		return true;
	}
	
	/**
	 * loading the negative words dictionary
	 * @return  true if success otherwise is false
	 * @throws IOException
	 */
	public boolean loadNegativeWordDictionary() throws IOException  {
		
		String negaDictPath = SystemParas.emotion_nega_dict;
		if (negaDictPath.isEmpty()) {
			System.err.println("ConfigInfoError" + "logging negative words dictionary is error!");
			return false;
		}
		
		EncryptDictionary ed = new EncryptDictionary();
		if (true == ed.decryptWordFile(negaDictPath)) {
			//System.out.println(ed.wordVocab.size());
			Iterator<String> iter = ed.wordVocab.iterator();
			while (iter.hasNext()) {
				
				String textLine = iter.next().toString();
				
				if (false == textLine.startsWith("#")) {
					String word = "";
					int pos = textLine.indexOf("\t");
					if (pos != -1) {
						word = textLine.substring(0,pos);
						this.negaWordDict.add(word);
					}
					else {
						word = textLine;
						this.negaWordDict.add(word);
					}
				}
			}
		}
		
		if (this.negaWordDict.size() == 0) {
			System.err.println("ConfigInfoError" + "logging negative words dictionary SIZE is error!");
			return false;
		}

		return true;
	}
	
	/**
	 * loading the stop words dictionary
	 * @return  true if success otherwise is false
	 * @throws IOException
	 */
	public boolean loadStopWordsDictionary() throws IOException  {
		
		String stopwordDictPath = SystemParas.stop_word_dict;
		if (stopwordDictPath.isEmpty()) {
			System.err.println("ConfigInfoError" + "logging stop words dictionary is error!");
			return false;
		}
		
		EncryptDictionary ed = new EncryptDictionary();
		if (true == ed.decryptWordFile(stopwordDictPath)) {
			//System.out.println(ed.wordVocab.size());
			Iterator<String> iter = ed.wordVocab.iterator();
			while (iter.hasNext()) {
				
				String textLine = iter.next().toString();
				
				if (false == textLine.startsWith("#")) {
					String word = "";
					int pos = textLine.indexOf("\t");
					if (pos != -1) {
						word = textLine.substring(0,pos);
						this.stopWordsDict.add(word);
					}
					else {
						word = textLine;
						this.stopWordsDict.add(word);
					}
				}
			}
		}
		
		if (this.stopWordsDict.size() == 0) {
			System.err.println("ConfigInfoError" + "logging stop words dictionary SIZE is error!");
			return false;
		}

		return true;
	}
	
	/**
	 * Frank suny 20141127 add
	 * loading the traditional and simple character pairs dictionary
	 * @return  true if success otherwise is false
	 * @throws IOException
	 */
	public boolean loadTradSimpCharPairsDictionary() throws IOException  {
		
		String tradSimpFilePath = SystemParas.trad_diff_simpl_dict;
		if (tradSimpFilePath.isEmpty()) {
			System.err.println("ConfigInfoError" + "logging traditional and simple char pairs dictionary is error!");
			return false;
		}

		EncryptDictionary ed = new EncryptDictionary();
		if (ed.decryptWordFile(tradSimpFilePath) == true) {
			if (ed.wordVocab.size() == 0) {
				System.err.println("ConfigInfoError" + "logging traditional and simple char pairs dictionary size is error!");
				return false;
			}
			
			Iterator<String> iter = ed.wordVocab.iterator();
			
			while( iter.hasNext()) {
				String textLine = iter.next().toString();
				
				if (textLine.startsWith("#") == false) {
					String [] arrs = textLine.split("	");
					Character trad = arrs[0].charAt(0);
					Character simp = arrs[1].charAt(0);
					this.tradSimpDict.put(trad, simp);
				}
			}
		}
		
		if (this.tradSimpDict.size() == 0) {
			System.err.println("ConfigInfoError" + "logging traditional and simple char pairs dictionary SIZE is error!");
			return false;
		}
		return true;
	}

	/**
	 * loading the special rules words dictionary
	 * @return  true if success otherwise is false
	 * @throws IOException
	 */
	public boolean loadRulesWordDictionary() throws IOException  {
		
		String specDictPath = SystemParas.special_words_dict;
		if (specDictPath.isEmpty()) {
			System.err.println("ConfigInfoError" + "logging special words dictionary is error!");
			return false;
		}
		
		EncryptDictionary ed = new EncryptDictionary();
		if (true == ed.decryptWordFile(specDictPath)) {
			//System.out.println(ed.wordVocab.size());
			Iterator<String> iter = ed.wordVocab.iterator();
			while (iter.hasNext()) {
				
				String textLine = iter.next().toString();
				
				if (false == textLine.startsWith("#")) {
					String word = "";
					int pos = textLine.indexOf("\t");
					if (pos != -1) {
						word = textLine.substring(0,pos);
						textLine = textLine.substring(pos + 1);
						String [] wordsArray = textLine.split(";");
						if (word.equals("超") == true) {
							for(String key : wordsArray) {
								this.rulesWordDict.superLevel.add(key);
							}
						}
						else if (word.equals("极其") == true) {
							for(String key : wordsArray) {
								this.rulesWordDict.extremelyLevel.add(key);
							}
						}
						else if (word.equals("很") == true) {
							for(String key : wordsArray) {
								this.rulesWordDict.veryLevel.add(key);
							}
						}
						else if (word.equals("较") == true) {
							for(String key : wordsArray) {
								this.rulesWordDict.fairlyLevel.add(key);
							}
						}
						else if (word.equals("稍") == true) {
							for(String key : wordsArray) {
								this.rulesWordDict.slightlyLevel.add(key);
							}
						}
						else if (word.equals("欠") == true) {
							for(String key : wordsArray) {
								this.rulesWordDict.oweLevel.add(key);
							}
						}
						else if (word.equals("一重") == true) {
							for(String key : wordsArray) {
								this.rulesWordDict.denyWords.add(key);
							}
						}
						else if (word.equals("双重") == true) {
							for(String key : wordsArray) {
								this.rulesWordDict.doubleDenyWords.add(key);
							}
						}
						else {
							for(String key : wordsArray) {
								this.rulesWordDict.perceptionWords.add(key);
							}
						}			
					}
				}
			}
		}
		
		if (this.rulesWordDict.superLevel.size() == 0 ||
			this.rulesWordDict.extremelyLevel.size() == 0 || 
			this.rulesWordDict.veryLevel.size() == 0 ||
		    this.rulesWordDict.fairlyLevel.size() == 0 ||
		    this.rulesWordDict.slightlyLevel.size() == 0 ||
		    this.rulesWordDict.oweLevel.size() == 0 ||
		    this.rulesWordDict.denyWords.size() == 0 ||
		    this.rulesWordDict.doubleDenyWords.size() == 0 ||
		    this.rulesWordDict.perceptionWords.size() == 0) {
			System.err.println("ConfigInfoError" + "logging special rules words dictionary SIZE is error!");
			return false;
		}

		return true;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}
}
