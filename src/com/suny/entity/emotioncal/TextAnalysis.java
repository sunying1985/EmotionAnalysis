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

import com.suny.ambiguous.ExternalDictionary;
import com.suny.ambiguous.ItemContextInform;
import com.suny.ambiguous.ItemContextStatistics;
import com.suny.ambiguous.TextTradTransSimple;
import com.suny.ambiguous.WordLoad;
import com.suny.mmseg.main.ComplexSeg;
import com.suny.mmseg.main.Dictionary;
import com.suny.mmseg.main.MMSeg;
import com.suny.mmseg.main.Seg;
import com.suny.mmseg.main.Word;
import com.suny.sort.InvertedItemValue;

public class TextAnalysis {

	/**
	* error logger information
	 */
	private static final Logger logInfo = Logger.getLogger(ItemContextStatistics.class.getName());
	
	/**
	 * segment handle
	 */
	private  Seg segHandle = null;
	// 即将进行统计的词语
	public  WordLoad wordsDict = null;
	// 附加词典加载句柄
	private ExternalDictionary   exterDictHandle = null;
	// 繁简体转化句柄词典
	private TextTradTransSimple textTransHandle = null;
	/**
	 * 保存统计结果的内容
	 * key   key1keykey2:freq;key_ikeykey_j:freq
	 */
	private  Map<String,ItemContextInform> statisResult = null;
	
	/**
	 *  保存没有歧义的上下文环境
	 */
	private Set<String>  noAmbiguousContext = null;
	
	public ItemContextStatistics() {
		this.init();
	}
	/**
	 * Initialization function
	 */
	private void init() {
		
		// segment dictionary file name
		Dictionary dic = Dictionary.getInstance();
		
		// Forward maximum matching
		//this.segHandle = new SimpleSeg(dic);
		this.segHandle = new ComplexSeg(dic); 
		
		/** 
		 * load statistics words resource
		*/
		this.wordsDict = new WordLoad();
		
		this.exterDictHandle = new ExternalDictionary();
		this.exterDictHandle.initdict();
		// suny 20141128
		this.textTransHandle = new TextTradTransSimple();
		this.textTransHandle.initDictExternal(this.exterDictHandle.tradSimpDict);
		
		// 结果保存 20150109 Frank Adolf suny
		this.statisResult = new HashMap<String,ItemContextInform>();
		
		// 20150204 Frank Adolf
		this.noAmbiguousContext = new HashSet<String>();
	}
	
	/**
	 * only store the word ends position
	 * @param  text   the input string
	 * @return the    segment tag pos array
	 */
	public int [] textWordSegLable(String text) {
		if (text.isEmpty()) {
			return null;
		}
		
		int [] segLable = new int[text.length()];
		for (int i = 0; i < text.length(); i++) {
			segLable[i] = 0;
		}
		
		MMSeg mmSeg = new MMSeg(new StringReader(text), this.segHandle);
		Word word = null;
		int tag = 0;
		
		try {
			while((word = mmSeg.next()) != null) {
				/*System.out.print(word.getString()+" -> "+word.getStartOffset());
				//offset += word.length;
				System.out.println(", "+word.getEndOffset()+", "+word.getType());
				*/
				segLable[tag] = word.getEndOffset();
				tag++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return segLable;
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
	// 判断一个字符串由：全英文字符/数字组成
	// 去除这样的影响：BAC	BAC Mono
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
	 * loading file
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
	 * statistics words informations from text
	 * @param  text  the input string
	 * @return 
	 */
	public boolean getWordsContextInform(String title,String content) {
		
		String text = title + content;
		if (text.isEmpty() == true) {
			return false;
		}
		
		int textSize = text.length();
		int [] segTag = null;
		// Frank Adolf suny 20150202 add
		text = this.textTransHandle.transformText(text);
		
		segTag = this.textWordSegLable(text);
		
		if (segTag == null) {
			return false;
		}
        String headWord = "";
        String currentWord = "";
        
		for (int index = 0; index < textSize; index++) {
			if (segTag[index] == 0) {
				break;
			}
			if (index == 0) {
				
				String tempBuf = text.substring(0, segTag[index]);
				if (false == this.isEngCharNumber(tempBuf) && false == this.isSpecialWords(tempBuf)) {
					headWord = tempBuf;
				}
			}
			else 
			{
				// 中间词语必须再词典中,目前考虑,确保有后文
				if (index == 1) {
					 if (segTag[index] != 0 && segTag[index + 1] != 0 ) {
		               		
	               		currentWord = text.substring(segTag[index -1], segTag[index]);
	               		if (true == this.isEngCharNumber(currentWord) || 
	               			true == this.isSpecialWords(currentWord)) {
	               			headWord = "";
	               			currentWord = "";
	               		}
	               		else {
	               			if (true == this.isInWordsDict(currentWord) && headWord.isEmpty() == false) {

	                			// have head and tail words
                				String lastWord = text.substring(segTag[index], segTag[index + 1]);
                				if ( true == this.isEngCharNumber(lastWord) ||
                					 true == this.isSpecialWords(lastWord)) {
                					
                					headWord = currentWord;
                					currentWord = "";
                				}
                				else {
                					String context = headWord + currentWord + lastWord;
                					this.overlapAmbiguous(currentWord, context);
                    				
                    				headWord = currentWord;
                    				currentWord = "";
                				}
		   					}
	               			else {
	               				// there is not juage the words
			               		headWord = currentWord;
			               		currentWord = "";
	               			}
	               		}	
		            }
					else {
						break;
					}
				}
				else {
					if (segTag[index] != 0 && segTag[index + 1] != 0) {
						// head and current all before words
						if (headWord.isEmpty() == true) {
							if (currentWord.isEmpty() == false) {
								headWord = currentWord;
								currentWord = text.substring(segTag[index - 1], segTag[index]);
							}
							else {
								headWord = text.substring(segTag[index - 1], segTag[index]);
							}
						}
						else {

							currentWord = text.substring(segTag[index - 1], segTag[index]);
						
							if (true == this.isEngCharNumber(headWord) ||
								true == headWord.isEmpty() ||
								true == this.isSpecialWords(headWord)) {
								headWord = currentWord;
							}
							else {
								if (true == this.isInWordsDict(currentWord) && headWord.isEmpty() == false) {

		                			// have head and tail words
		            				String lastWord = text.substring(segTag[index], segTag[index + 1]);
		            				if (true == this.isEngCharNumber(lastWord) || 
		            					true == this.isSpecialWords(lastWord)) {
		            					currentWord = "";
		            					headWord = currentWord;
		            				}
		            				else {
		            					String context = headWord + currentWord + lastWord;
		            					this.overlapAmbiguous(currentWord, context);
		            					
			            				headWord = currentWord;
			            				currentWord = "";
		            				}
			   					}
		               			else {
		               				headWord = currentWord;
		               				currentWord = "";
		               			}
							}	
						}
					}
				}
			}
		}
		
		return true;
	}

	/**
	 * word in statistics word dictionary
	 * @param  word   the judge word
	 * @return true   if word is car brand name
	 */
	private boolean isInWordsDict(String word) {
		if (word.isEmpty()) {
			return false;	
		}
		
		if (true == this.wordsDict.itemsDict.contains(word)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * @param item         the current word
	 * @param contextText  the context of the words
	 */
	private void overlapAmbiguous(String item, String contextText) {
		
		if(item.isEmpty() == true || contextText.isEmpty() == true) {
			return ;
		}
		// not ambiguous in the context
		if (this.noAmbiguousContext.contains(contextText) == true ) {
			
			return ;
		}
		else {
			// 判断是否存在歧义
			int [] posArray = new int[3];
			int pos = contextText.indexOf(item);
			if (pos == -1) {
				return ;
			}
			posArray[0] = pos;
			posArray[1] = pos + item.length();
			posArray[2] = contextText.length();
			boolean haveAmbigus = false;
			int i = 0;
			
			for(int k = 0; k < 2; k++) {
				
				while(i < posArray[k]) {
					
					int j = posArray[k];
					while(j < posArray[k + 1]) {
						j++;
						
						String candWord = contextText.substring(i,j);
						if (true == this.isInWordsDict(candWord)) {
							
							if (this.statisResult.containsKey(candWord) == true ) {
								ItemContextInform itemContext = new ItemContextInform();
								itemContext = this.statisResult.get(candWord);
								itemContext.addItemsFreq(contextText, 1);
								
								this.statisResult.put(candWord, itemContext);
							}
							else {
								ItemContextInform itemContext = new ItemContextInform();
								itemContext.addItemsFreq(contextText, 1);
								this.statisResult.put(candWord, itemContext);
							}
							haveAmbigus = true;
						}
						//System.out.println(candWord);
					}
					i++;	
				}
				i = posArray[k];
			}
			
			if (false == haveAmbigus) {
				this.noAmbiguousContext.add(contextText);
			}
		}
	}
	
	/**
	 * output the result into file
	 * @param outfile
	 * @return true if successful
	 */
	public  boolean outputStatisticsResult(String outfile) {
		/*
		ItemContextStatistics itemSts = new ItemContextStatistics();
		itemSts.wordsDict.loadingDict("E:\\my_work\\WordsAmbiguousContext\\corpus\\words.txt");
		
		DataBaseTestInterface dbi = new DataBaseTestInterface();
		dbi.init("sunying", "Lkn4Z4og", "jdbc:mysql://10.38.11.79:3306/test?user=sunying&password=Lkn4Z4og&useUnicode=true&characterEncoding=utf-8", "test");

		try {
			
			if (dbi.connectionDataBase() == false) {
				System.out.println("connect database is error!");
			}
			//dbi.createTableEntrence("E:\\my_work\\RegionMining\\segtest\\createTable\\tableCreate.txt",dbi);
			
			dbi.TableContents("industry_data_copy", ItemContextStatistics);
			
			dbi.closeDataBase();
		} catch ( Exception se) {
			se.printStackTrace();
		}
		*/
		try {
			// output the result to file
			FileOutputStream fos = new FileOutputStream(outfile,false); // clear the initinal content
			OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF-8");
			BufferedWriter bw = new BufferedWriter(osw);
			
			InvertedItemValue iivHandle = new InvertedItemValue();
			
			for(String key : this.statisResult.keySet()) {
				
				bw.write(key + "\t");
				
				Map<String, Integer> sortedDict = new HashMap<String,Integer>();
				sortedDict = iivHandle.sortMapByIntegerValue(this.statisResult.get(key).itemContextInform);
				if (sortedDict != null) {
					for(String headKey : sortedDict.keySet()) {
						bw.write(headKey + ":" + sortedDict.get(headKey) + ";");
					}
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

	}

}
