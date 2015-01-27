package com.suny.entity.text.trans;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.suny.entity.dictionary.SystemParas;
import com.suny.entity.encryption.EncryptDictionary;

public class DictionaryLoad {

	
	/** the traditional and simple character dictionary  */
	public Map<Character, Character> tradSimpDict = null;
	
	/**
	 * the faimly name dictionary name
	 */
	public Set<String> faimlyNameDict = null;
	
	public DictionaryLoad() {
		//  traditional and suny add 20141127
		this.tradSimpDict = new HashMap<Character, Character>();
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
		//System.out.println("the text transform dictionary size is: \t" + this.tradSimpDict.size());
		return true;
	}
	
	/**
	 * loading the family name dictionary
	 * @return  true if success otherwise is false
	 * @throws IOException
	 */
	public boolean loadFamilyNameDictionary() throws IOException  {
		
		String familyNameDictPath = SystemParas.family_name_fre_dict;
		if (familyNameDictPath.isEmpty()) {
			System.err.println("ConfigInfoError" + "logging family name  dictionary is error!");
			return false;
		}
		
		EncryptDictionary ed = new EncryptDictionary();
		if (true == ed.decryptWordFile(familyNameDictPath)) {
			
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
	 
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
