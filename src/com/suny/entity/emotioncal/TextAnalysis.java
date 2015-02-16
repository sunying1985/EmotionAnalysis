package com.suny.entity.emotioncal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.suny.sort.InvertedItemValue;
import com.suny.entity.text.trans.TextTradTransSimple;
import com.suny.mmseg.interfa.MMsegInterface;

public class TextAnalysis {

	/**
	* error logger information
	 */
	private static final Logger logInfo = Logger.getLogger(TextAnalysis.class.getName());
	
	/**
	 * segment handle
	 */
	private  MMsegInterface segHandle = null;

	// 附加词典加载句柄
	private VocabluaryHandle   exterDictHandle = null;
	// 繁简体转化句柄词典
	private TextTradTransSimple textTransHandle = null;
	

    // 单个词语命中得频率
	private ItemFreq posiSimpRulesFreq = null;
	private ItemFreq negaSimpRulesFreq = null;
	
	// 符合组合命中频率
	private ItemFreq posiCombRulesFreq = null;
	private ItemFreq negaCombRulesFreq = null;
	
	public TextAnalysis() {
		this.init();
	}
	/**
	 * Initialization function
	 */
	private void init() {
		
		// segment dictionary file name
		this.segHandle = new MMsegInterface(); 
		
		this.exterDictHandle = new VocabluaryHandle();
		this.exterDictHandle.initdict();
		// suny 20141128
		this.textTransHandle = new TextTradTransSimple();
		this.textTransHandle.initDictionary(this.exterDictHandle.tradSimpDict);
		
		// the simple rules 
        this.posiSimpRulesFreq = new ItemFreq();
        this.negaSimpRulesFreq = new ItemFreq();
        
        // the combination reles
        this.posiCombRulesFreq = new ItemFreq();
        this.negaCombRulesFreq = new ItemFreq();
	}
	
	/**
	 * word is family name
	 * @param  item   the judge word
	 * @return true   if the item is family name
	 */
	private boolean isFamilyName(String item) {
		if (item.isEmpty()) {
			return false;
		}
		
		if (true == this.exterDictHandle.faimlyNameDict.contains(item)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * word is stop words
	 * @param  item   the judge word
	 * @return true   if the item is stop words
	 */
	private boolean isStopWords(String item) {
		if (item.isEmpty()) {
			return false;
		}
		
		if (true == this.exterDictHandle.stopWordsDict.contains(item)) {
			return true;
		}
		else {
			return false;
		}
	}
	/**
	 * merger stop words and family name juage
	 * @param item
	 * @return
	 */
	private boolean isSpecialWords(String item) {
		if (item.isEmpty()) {
			return false;
		}
		if (true == this.isStopWords(item) || true == this.isFamilyName(item)) {
			return true;
		}
		else {
			return false;
		}
	}
	/**
	 * judage the one size string is punction is or not
	 * @param word  the input word to judage
	 * @return
	 */
	private boolean isPuntLables(String word) {
		if (word.isEmpty() == true) {
			return false;
		}
		for (int i = 0; i < this.exterDictHandle.puntLables.length; i++){   
		   if (word.equals(this.exterDictHandle.puntLables[i]) == true ||
			   word.indexOf(this.exterDictHandle.puntLables[i]) != -1){
		     return true;
		   }
		}
		return false;
	}
    /**
     *  judage the chinese full number
     */
	private boolean isChineseNumber(String word) {
		if (word.length() != 1) {
			return false;
		}
		for (int i = 0; i < this.exterDictHandle.chineseNumbers.length; i++){   
		   if (word.equals(this.exterDictHandle.chineseNumbers[i]) == true ||
			   word.indexOf(this.exterDictHandle.chineseNumbers[i]) != -1){
		     return true;
		   }
		}
		return false;
	}
	/** 判断一个字符串由：全英文字符/数字组成
	 * 去除这样的影响：BAC	BAC Mono
	 * @param item
	 * @return
	 */
	public boolean isEngCharNumber(String item) {
		if (item.isEmpty() == true) {
			return false;
		}
		if(item.length() == 1) {
			if (true == this.isChineseNumber(item)) {
				return true;
			}
		}
		
		if (true == this.isPuntLables(item)) {
			return true;
		}
		int i = 0;
		int itemSize = item.length();
		
		for (; i < itemSize; i++) {
			
			if (Character.isDigit(item.charAt(i)) == true || 
				Character.isLowerCase(item.charAt(i)) == true ||
				Character.isUpperCase(item.charAt(i)) == true ||
				item.charAt(i) == ' ' || item.charAt(i) == '-'){
			   return true;
			}
		}
		
		return false;
	}
	
	/**
	 * judge one word is positive 
	 * @param word  the input word to judge
	 * @return
	 */
	private boolean isPositiveWord(String word) {
		if (word.isEmpty() == true) {
			return false;
		}
		if (this.exterDictHandle.posiWordDict.contains(word) == true) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * judge one word is negative 
	 * @param word  the input word to judge
	 * @return
	 */
	private boolean isNegativeWord(String word) {
		if (word.isEmpty() == true) {
			return false;
		}
		if (this.exterDictHandle.negaWordDict.contains(word) == true) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * judge one word is level words
	 * @param word  the input word to judge
	 * @return
	 */
	private boolean isLevelDegreeWord(String word) {
		if (word.isEmpty() == true) {
			return false;
		}
		if (this.exterDictHandle.rulesWordDict.superLevel.contains(word) == true ||
			this.exterDictHandle.rulesWordDict.extremelyLevel.contains(word) == true ||
			this.exterDictHandle.rulesWordDict.veryLevel.contains(word) == true ||
			this.exterDictHandle.rulesWordDict.fairlyLevel.contains(word) == true ||
			this.exterDictHandle.rulesWordDict.slightlyLevel.contains(word) == true ||
			this.exterDictHandle.rulesWordDict.oweLevel.contains(word) == true)  {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * judge word is deny words
	 * @param word  the input word to judge
	 * @return
	 */
	private boolean isDenyWord(String word) {
		if (word.isEmpty() == true) {
			return false;
		}
		if (this.exterDictHandle.rulesWordDict.denyWords.contains(word) == true)  {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * judge word is double deny words
	 * @param word  the input word to judge
	 * @return
	 */
	private boolean isDoubleDenyWord(String word) {
		if (word.isEmpty() == true) {
			return false;
		}
		if (this.exterDictHandle.rulesWordDict.doubleDenyWords.contains(word) == true)  {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * judge word is double deny words
	 * @param word  the input word to judge
	 * @return
	 */
	private boolean isPerceptionWord(String word) {
		if (word.isEmpty() == true) {
			return false;
		}
		if (this.exterDictHandle.rulesWordDict.perceptionWords.contains(word) == true)  {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * loading file, only for test
	 */
    public String  loadingDict(String infile) {
		
    	if (infile.isEmpty() == true) {
    		return "";
    	}
		String allContext = "";
		try {
			FileInputStream fis = new FileInputStream(infile);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
			BufferedReader br = new BufferedReader(isr);
					
			String textLine = "";
			while (( textLine = br.readLine()) != null) {
				allContext += textLine;
			}
			br.close();
			isr.close();
			br.close();
			//System.out.println(this.itemsDict.size());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return allContext;
	}
	/**
	 * statistics words rules from text
	 * @param  text  the input string
	 * @return 
	 */
	public boolean getHitRulesFreq(String title,String content) {
		
		String text = title + content;
		if (text.isEmpty() == true) {
			return false;
		}
		
		int textSize = text.length();
		int [] segTag = null;
		// Frank Adolf suny 20150202 add
		text = this.textTransHandle.transformText(text);
		
		segTag = this.segHandle.textWordSegLable(text);
		
		if (segTag == null) {
			return false;
		}
        String headWord = "";
        String currentWord = "";
        int wordNumber = 0;
        
		for (int index = 0; index < textSize; index++) {
			if (segTag[index] == 0) {
				break;
			}
			wordNumber++;
			if (index == 0) {
				
				String tempBuf = text.substring(0, segTag[index]);
				if (false == this.isEngCharNumber(tempBuf) && false == this.isSpecialWords(tempBuf)) {
					if (this.isPositiveWord(tempBuf) == true) {
						
						this.posiSimpRulesFreq.addItemsAndFreq(tempBuf, 1);
						
					}
					else if (this.isNegativeWord(tempBuf) == true) {
						
						this.negaSimpRulesFreq.addItemsAndFreq(tempBuf, 1);
					}
					else {
						/*if (this.isFamilyName(tempBuf) == false && 
							true == this.isEngCharNumber(tempBuf) == false) {
							headWord = tempBuf;
						}
						else {
							headWord = "";
						}
						*/
						headWord = tempBuf;
					}
				}
			}
			else 
			{
				// 中间词语必须再词典中,目前考虑,确保有后文
				if (segTag[index] != 0) {
					// head and current all before words
					if (headWord.isEmpty() == true) {
						currentWord = text.substring(segTag[index - 1], segTag[index]);
						
						if (this.isPositiveWord(currentWord) == true) {
							
							this.posiSimpRulesFreq.addItemsAndFreq(currentWord, 1);
						}
						else if (this.isNegativeWord(currentWord) == true) {
							
							this.negaSimpRulesFreq.addItemsAndFreq(currentWord, 1);
						}
						else {
							if (this.isFamilyName(currentWord) == false && 
								this.isEngCharNumber(currentWord) == false) {
								headWord = currentWord;
							}
							else {
								headWord = "";
							}
						}
					}
					else {

						currentWord = text.substring(segTag[index - 1], segTag[index]);
					
						if (true == this.isEngCharNumber(currentWord) ||
							true == this.isFamilyName(currentWord)) {
							headWord = "";
						}
						else {
							
							if (this.isPositiveWord(currentWord) == true) {
								//this.posiSimpRulesFreq.addItemsAndFreq(currentWord, 1);
								if (this.isDenyWord(headWord) == true) {
									
									this.negaCombRulesFreq.addItemsAndFreq(headWord + currentWord, 1);
								}
								else if (this.isDoubleDenyWord(headWord) == true) {
									
									this.posiCombRulesFreq.addItemsAndFreq(headWord + currentWord, 1);
								}
								else if (this.isLevelDegreeWord(headWord) == true) {
									
									this.posiCombRulesFreq.addItemsAndFreq(headWord + currentWord,1);
								}
								else {
									
									this.posiSimpRulesFreq.addItemsAndFreq(currentWord, 1);
								}
							}
							else if (this.isNegativeWord(currentWord) == true) {
								//this.negaSimpRulesFreq.addItemsAndFreq(currentWord, 1);
								if (this.isDenyWord(headWord) == true) {
									
									this.posiCombRulesFreq.addItemsAndFreq(headWord + currentWord, 1);
								}
								else if (this.isDoubleDenyWord(headWord) == true) {
									
									this.negaCombRulesFreq.addItemsAndFreq(headWord + currentWord, 1);
								}
								else if (this.isLevelDegreeWord(headWord) == true) {
								
									this.negaCombRulesFreq.addItemsAndFreq(headWord + currentWord,1);
								}
								else {
									this.negaSimpRulesFreq.addItemsAndFreq(currentWord, 1);
								}
							}
							else {
								/*if (this.isFamilyName(currentWord) == false && 
									this.isEngCharNumber(currentWord) == false) {
									headWord = currentWord;
								}
								else {
									headWord = "";
								}
								*/
								headWord = currentWord;
							}
						}
					}
				}
			}
		}
		// only for test
		//this.outputStatisticsResult("");
		
		// 如何进行归一化
		// 策略：level 从重要程度高的级别到低的级别：6-1
		// 按着舆情词语的长度进行加权，长度越长代表的语义更大，权重越高
		System.out.println("the change values:" + this.simpleDValue(wordNumber));
		return true;
	}
	
	
	private double simpleDValue(int wordsNumber) {
		if (wordsNumber < 0) {
			return 0.0;
		}
		
		double dValue = 0.0;
		dValue = (double)(this.posiCombRulesFreq.totalNumber + this.posiSimpRulesFreq.totalNumber
				          - this.negaSimpRulesFreq.totalNumber - this.negaCombRulesFreq.totalNumber);///(double)wordsNumber;
		
		return dValue;
	}
	/**
	 * output the result into file
	 * @param outfile
	 * @return true if successful
	 */
	public  boolean outputStatisticsResult(String outfile) {
		
		try {
			// output the result to file
			FileOutputStream fos = new FileOutputStream(outfile,false); // clear the initinal content
			OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF-8");
			BufferedWriter bw = new BufferedWriter(osw);
			
			InvertedItemValue iivHandle = new InvertedItemValue();
			
			Map<String, Integer> sortedDict = new HashMap<String,Integer>();
			sortedDict = iivHandle.sortMapByIntegerValue(this.posiSimpRulesFreq.itemFre);
			bw.write("the simple positive words:" + "\n");
			if (sortedDict != null) {
				for(String headKey : sortedDict.keySet()) {
					bw.write(headKey + ":" + sortedDict.get(headKey) + ";");
				}
				bw.write("\n");
			}
			
			
			sortedDict = iivHandle.sortMapByIntegerValue(this.posiCombRulesFreq.itemFre);
			bw.write("the combination positive words:" + "\n");
			if (sortedDict != null) {
				for(String headKey : sortedDict.keySet()) {
					bw.write(headKey + ":" + sortedDict.get(headKey) + ";");
				}
				bw.write("\n");
			}
			
			sortedDict = iivHandle.sortMapByIntegerValue(this.negaSimpRulesFreq.itemFre);
			bw.write("the simple negative words:" + "\n");
			if (sortedDict != null) {
				for(String headKey : sortedDict.keySet()) {
					bw.write(headKey + ":" + sortedDict.get(headKey) + ";");
				}
				bw.write("\n");
			}
			
			sortedDict = iivHandle.sortMapByIntegerValue(this.negaCombRulesFreq.itemFre);
			bw.write("the combination negative words:" + "\n");
			if (sortedDict != null) {
				for(String headKey : sortedDict.keySet()) {
					bw.write(headKey + ":" + sortedDict.get(headKey) + ";");
				}
				bw.write("\n");
			}
			bw.close();
			osw.close();
			fos.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TextAnalysis ta = new TextAnalysis();
		String title = ta.loadingDict("E:\\my_work\\SentimentAnalysis\\testCorpus\\title.txt");
		String content = ta.loadingDict("E:\\my_work\\SentimentAnalysis\\testCorpus\\content.txt");
		
		ta.getHitRulesFreq(title, content);
		ta.outputStatisticsResult("E:\\my_work\\SentimentAnalysis\\testCorpus\\output.txt");
	}

}
