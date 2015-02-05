package com.suny.entity.corpus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/** 
 * 20150130 Frank Adolf 
 *          所需语料处理工作
 */
public class CorpusProgress {

	/**
	 * word collection
	 */
	public Set<String> m_wordVocab = null;
	
	// abrrWord == > initWord1;initWord2;
	public Map<String,Vector<String>> m_allWordDield = null;
	
	/**
	 * construction function of class
	 */
	public CorpusProgress() {
		m_wordVocab = new HashSet<String>();
		m_allWordDield = new HashMap<String,Vector<String>>();
	}
	
	public boolean readFile(String filepath) throws IOException{
		if (filepath.isEmpty()) {
			return false;
		}
		
		FileInputStream fis = new FileInputStream(filepath);
		InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		BufferedReader br = new BufferedReader(isr);
		
		String textLine = "";
		
		while (( textLine = br.readLine()) != null) {
			// 利达;利达社区	            利达社区居委会
			int pos = textLine.indexOf("\t");
			if (pos != -1) {
				
				String word = textLine.substring(0,pos);
				textLine = textLine.substring(pos + 1);
				if(word.isEmpty() == false && textLine.isEmpty() == false && word.length() > 2) {
					String [] wordArr = word.split(";");
					this.m_wordVocab.add(textLine);
					
					for (int i = 0; i < wordArr.length; i++) {
						
						String abbr = wordArr[i];
						if (abbr.length() > 2) {
							this.m_wordVocab.add(abbr);
							if (this.m_allWordDield.containsKey(abbr) == true) {
								
								Vector<String> itAbbr = new Vector<String>();
								itAbbr = this.m_allWordDield.get(abbr);
								int size = itAbbr.size();
								int k = 0;
								boolean tagg = false;
								for(; k < size; k++) {
									if (textLine.equals(itAbbr.get(k))  == true){
										tagg = true;
										break;
									}
								}
								if ((k == size && tagg == false)) {
									itAbbr.add(textLine);
									this.m_allWordDield.put(abbr, itAbbr);
								}
							}
							else {
								Vector<String> itAbbr = new Vector<String>();
								itAbbr.add(textLine);
								this.m_allWordDield.put(abbr, itAbbr);
							}
						}
					}
				}
			}
			else {
				System.out.println("error lines name:" + textLine);
			}
		}

		br.close();
		isr.close();
		br.close();
		
		return true;
	}
	
	/**
	  * output the vocabulary word
	  */
	public boolean outputStreamResult(String outfileName) throws IOException {
	    if (outfileName.isEmpty()) {
	    	return false;
	    }
	    	
    	FileOutputStream fos = new FileOutputStream(new File(outfileName));
    	OutputStreamWriter os = new OutputStreamWriter(fos,"UTF-8");
    	BufferedWriter bw = new BufferedWriter(os);
	    System.out.println(m_wordVocab.size());
	    
    	for( Iterator<String>   iter = m_wordVocab.iterator(); iter.hasNext(); ) {             
	         bw.write(iter.next().toString());
	         bw.write("\n");
	    }
    	
    	bw.close();
    	os.close();
    	fos.close();
    	return true;
    }
	   
	/**
	  * output the vocabulary word
	  */
	public boolean outputAbbrResult(String outfileName) throws IOException {
	    if (outfileName.isEmpty()) {
	    	return false;
	    }
	    	
	   	FileOutputStream fos = new FileOutputStream(new File(outfileName));
	   	OutputStreamWriter os = new OutputStreamWriter(fos,"UTF-8");
	   	BufferedWriter bw = new BufferedWriter(os);
		System.out.println(this.m_allWordDield.size());
		    
	   	for( Iterator<String>   iter = this.m_allWordDield.keySet().iterator(); iter.hasNext(); ) {  
	   		
	   		String abbr = iter.next().toString();
	   		bw.write(abbr + "\t");
	   		Vector<String> tmp = this.m_allWordDield.get(abbr);
	   		for(String val: tmp) {
		         bw.write(val + ";");
	   		}
	        bw.write("\n");
		}
	   	
	   	bw.close();
	   	os.close();
	   	fos.close();
	   	return true;
   }
	/**   
	 * 合并两个文件中的内容
	 * 对两个文件的内容去重
	 * */
	public boolean mergerFile(String filepath,String outfile) throws IOException{
		if (filepath.isEmpty() || outfile.isEmpty() == true) {
			return false;
		}
		
		FileInputStream fis = new FileInputStream(filepath);
		InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		BufferedReader br = new BufferedReader(isr);
		
		String textLine = "";
		System.out.println("begion loading file...");
		while (( textLine = br.readLine()) != null) {
			
			// 主要原因是因为数量较大
			// 正向词典4字词语8000多未审核
			// 贬义负面词语4字词语没有进行处理
			if ( textLine.length() == 3){
			//if (textLine.indexOf(".") != -1) {
				System.out.println(textLine);
			}
		    else {
				this.m_wordVocab.add(textLine);
			}
		}

		br.close();
		isr.close();
		br.close();
		System.out.println("the total number of the vocabluary\t" + this.m_wordVocab.size());
		this.outputStreamResult(outfile);
		return true;
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		CorpusProgress cproc = new CorpusProgress();
		/*cproc.readFile("E:\\my_work\\EntityNamedRecognition\\dict\\abbrev.txt");
		cproc.outputAbbrResult("E:\\my_work\\EntityNamedRecognition\\dict\\abbrev_2.txt");
		cproc.outputStreamResult("E:\\my_work\\EntityNamedRecognition\\dict\\towns.txt");
		*/
		
		//cproc.mergerFile("E:\\my_work\\SentimentAnalysis\\dict\\posi_words.txt", "E:\\my_work\\SentimentAnalysis\\dict\\3.txt");
		cproc.mergerFile("E:\\my_work\\SentimentAnalysis\\dict\\nega_words.txt", "E:\\my_work\\SentimentAnalysis\\dict\\3.txt");
	}
}
