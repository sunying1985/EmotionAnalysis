package com.suny.entity.encryption;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

public class EncryptDictionary {

	/**
	 * idea encrypt handle
	 */
	public Idea idea = null;
	
	/**
	 * dictionary word, only used for contain word
	 */
	public Set<String> wordVocab = null;
	
	/**
	 * the construction
	 */
	public EncryptDictionary() {
		this.idea = new Idea();
		wordVocab = new HashSet<String>();
	}
	
    /*
     *  @param chars  the input string 
     *  @return the bytes array
     */
	private byte[] getBytes( char [] chars) {
    	
	   Charset cs = Charset.forName("UTF-8");
	   CharBuffer cb = CharBuffer.allocate(chars.length);
	   cb.put(chars);
	   cb.flip();
	   ByteBuffer bb = cs.encode(cb);
	  
	   return bb.array();
    }
    
	/*
     *  @param bytes  the input string 
     *  @return the char array
     */
    private char[] getChars(byte[] bytes) {
    	
	   Charset cs = Charset.forName("UTF-8");
	   ByteBuffer bb = ByteBuffer.allocate(bytes.length);
	   bb.put(bytes);
	   bb.flip();
	   CharBuffer cb = cs.decode(bb);
	  
	   return cb.array();
    }

    /*
     * get word into encrypt format
     * @param inWordFile      the word file we can see exmp: E:\\a\\xxx.txt
     * @return                true if success, otherwise is false
     * this maybe return tow file, the index file and the eccrypt data file
     */
    public boolean encryptWordFile( String inWordFile) {
    	if (inWordFile.isEmpty()) {
    		return false;
    	}

		try {
			File file = new File(inWordFile); 
			long len = file.length();
			//System.out.println("the file length is:" + len + " types!");
			// only store every one item length
			int []posArray = new int[(int) (len/4)];
	        // array tag: begin position and word length
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
			BufferedReader br = new BufferedReader(isr);
			
			String indexFile = inWordFile;
			int delFileNamePos = indexFile.lastIndexOf(".");
			String dictFile = indexFile.substring(0,delFileNamePos) + ".dict";
			indexFile = indexFile.substring(0,delFileNamePos) + ".idx";
			
			FileOutputStream fos = new FileOutputStream(indexFile);	
			OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF-8");
			BufferedWriter bw = new BufferedWriter(osw);
			
			String textLine = "";
			String allText = "";
			int countNumber = 0;
			
			while (( textLine = br.readLine()) != null) {
				if(textLine.startsWith("#") == false) {
					int wordLen = textLine.length();
					posArray[countNumber] = wordLen;
					countNumber++;
					if (countNumber%100 == 0) {
						System.out.println("running lines number\t" + countNumber);
					}
					allText += textLine;
				}
			}
			// put the dictionary into index file 
			for (int i = 0; i < (int) (len/4); i++) {
		    	if (posArray[i] != 0) {
		    		String writeValue = String.valueOf(posArray[i]);
		    		bw.write(writeValue + "\n");
		    	}
		    	else {
		    		break;
		    	}
		    }
			// close file
			br.close();
			isr.close();
			fis.close();	
			bw.close();
			osw.close();
			fos.close();
			
			// out the encrypt data into file
			FileOutputStream fosWord = new FileOutputStream(dictFile);	
			String key = this.idea.getEncryptKey();	
	    	byte[] bytekey = key.getBytes();
	    	
			// get the encrypt data
			byte[] bytedata = allText.getBytes();
		    byte[] encryptdata = idea.IdeaEncrypt(bytekey, bytedata, true);
		    
		    fosWord.write(encryptdata, 0, encryptdata.length);
 
		    fosWord.close();	
		    System.out.println("encrypt file " + inWordFile +" is finished!");
		    
		} catch (IOException o) {
			o.printStackTrace();
		}
    	
    	return true;
    }
    
    /*
     * get word from index file and decrypt format file
     * @param encryptFile  the file path and name but not file suffix(.dict or idx)
     *                     example:E:\\my_work\\DataMining\\data\\regiondata\\newRegionRpt
     * @return true if success
     */
    public boolean decryptWordFile(String encryptFile) {
    	
    	if (encryptFile.isEmpty()) {
    		return false;
    	}
    	
    	try {
    		String indexFile = encryptFile + ".idx";
    		String dictFile = encryptFile + ".dict";
    		
    		File file = new File(indexFile); 
    		if (file.exists() == false) {
    			System.out.println("please check your dictionary, there is no " + encryptFile + ".idx file!");
    			return false;
    		}
			long len = file.length();
			//System.out.println("the index file length is:" + len + " types!");
			// only store every one item length
			int []posArray = new int[(int) len];
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
			BufferedReader br = new BufferedReader(isr);
			
            int countNumber = 0;
            String line = "";
            
            while (( line = br.readLine()) != null) {
            	
        		int index = Integer.parseInt(line);
        		posArray[countNumber] = index;
        		countNumber++;	
            }
            
            int temp = 0;
            File fileDict = new File(dictFile);
            long dictLen = fileDict.length();
            byte [] readBuffer = new byte[(int)dictLen];
            FileInputStream fisDict = new FileInputStream(fileDict);
            
            while(true){
                temp = fisDict.read(readBuffer, 0, readBuffer.length);
                if(temp == -1){
                    break;
                } 
            }

            fisDict.close();
            br.close();
            isr.close();
			fis.close();
			
			// only for debug
			/*
            FileOutputStream fos = new FileOutputStream(encryptFile + "r.txt");
			OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF-8");
			BufferedWriter bw = new BufferedWriter(osw);
			*/
			String key = this.idea.getEncryptKey();
	    	byte[] bytekey = key.getBytes();
            byte[] data = idea.IdeaEncrypt(bytekey, readBuffer, false);
            String wordStr = new String(data);
            
            int start = 0;
            for (int i = 0; i < countNumber && posArray[i] != 0; i++) {
            	
            	String retWord = wordStr.substring(start, start + posArray[i]);
            	start = start + posArray[i];
            	//bw.write(retWord + "\n");
            	//System.out.println(retWord);
            	wordVocab.add(retWord);
            }
            /*
			bw.close();
			osw.close();
			fos.close();
			*/
            System.out.println("load file " + encryptFile +" is finished!");
    	}	
    	catch (IOException e) {
    		e.printStackTrace();
    	}
        
    	return true;
    }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EncryptDictionary ed = new EncryptDictionary();
		// 1.
		ed.encryptWordFile("E:\\my_work\\EntityNamedRecognition\\dict\\towns.txt");
		//ed.encryptWordFile("E:\\my_work\\EntityNamedRecognition\\dict\\abbrev.txt");
		//ed.encryptWordFile("E:\\my_work\\RegionMining\\dict\\foreign_region.txt");
		//ed.encryptWordFile("E:\\my_work\\EntityNamedRecognition\\dict\\towns_lev.txt");
		//ed.encryptWordFile("E:\\my_work\\RegionMining\\dict\\regions_idf.txt");
		//ed.encryptWordFile("E:\\my_work\\RegionMining\\dict\\trad_simp.txt");
		//ed.decryptWordFile("E:\\towns");
		
		// 2.
		// 20140822 name mining dictionary encrypt
		//ed.encryptWordFile("E:\\my_work\\EntityNamedRecognition\\dict\\certain_name.txt");
		//ed.encryptWordFile("E:\\my_work\\NameMining\\corpus\\non_name.txt");
		//ed.encryptWordFile("E:\\my_work\\NameMining\\corpus\\char_struc_pro.txt");
		// 3.
		//ed.encryptWordFile("E:\\my_work\\NameMining\\corpus\\ngram.txt");
		//ed.encryptWordFile("E:\\my_work\\EntityNamedRecognition\\dict\\import_org.txt");
		//ed.encryptWordFile("E:\\my_work\\EntityNamedRecognition\\dict\\not_suffix_dict.txt");
		// 四级以上的地域名称，组织机构名识别专用，更改地域识别的情况无需修改
		//ed.encryptWordFile("E:\\my_work\\EntityNamedRecognition\\dict\\towns_org.txt");
		// 4
		//ed.encryptWordFile("E:\\my_work\\CarBrandMining\\dict\\car_ambiguous_del.txt");
		//ed.encryptWordFile("E:\\my_work\\CarBrandMining\\dict\\car_brand_name.txt");
		//ed.encryptWordFile("E:\\my_work\\CarBrandMining\\dict\\family_name_fre.txt");
		//ed.encryptWordFile("E:\\my_work\\CarBrandMining\\dict\\trad_simp.txt");
	}
}
