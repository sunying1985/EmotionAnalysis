package com.suny.entity.text.trans;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 判断文本的繁简，并根据需求进行文本的繁简体转换
 * 输入法还不
 * @author Frank suny
 *
 */
public class TextTradTransSimple {

	/**  traditional character and simple character */
	private Map<Character, Character> trad_simp_dict = null;
	
	public TextTradTransSimple() {
		trad_simp_dict = new HashMap<Character, Character>();
		
	}
	/** 
	 * using the class must to be initialization the trad_simple dictionary
	 *  */
	public boolean initDictExternal() {
		DictionaryLoad loadHandle =  new DictionaryLoad();
		try {
			if (false == loadHandle.loadTradSimpCharPairsDictionary() ) {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.trad_simp_dict = loadHandle.tradSimpDict;
		return true;
	}
	/**
	 *  loading the dictionary word from file 
	 */
	private boolean  initDict(String infile) {
		
		if (infile.isEmpty() == true || infile.equals("") == true) {
			return false;
		}
		try {
			FileInputStream fis = new FileInputStream(infile);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
			BufferedReader br = new BufferedReader(isr);
	
			String line = "";
			
			while (( line = br.readLine()) != null) {
				if (line.startsWith("#") == false) {
					String [] arrs = line.split("	");
					Character trad = arrs[0].charAt(0);
					Character simp = arrs[1].charAt(0);
					this.trad_simp_dict.put(trad, simp);
				}			
			}
			
			br.close();
			isr.close();
			fis.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println("the trad and simple dictionary size\t" + this.trad_simp_dict.size());
		
		return true;
	}
	/**
	 * judage the text content is trad or simple
	 * @param text   the input paper text
	 * @return false: simple === true  traditional
	 */
	private boolean isTraditionalText(String text) {
		if (text.isEmpty() == true) {
			return false;
		}
		
		int [] dectPoint = this.randomNumber(text);
		
		if (dectPoint == null) {
			return false;
		}
		
		for (int i = 0; i < dectPoint.length; i++) {
			Character tmpCh = text.charAt(i);
			if (this.trad_simp_dict.containsKey(tmpCh) == true) {
				return true;
			}
		}
		
		return false;
	}
	/** 
	 * Generate the random index number using text length
	 * @return  the index of the test character
	 * */
	private int [] randomNumber(String text) {
		
		if (text.isEmpty() == true || text.equals("") == true) {
			return null;
		}
		Random random = new Random();
		
		int textSize = text.length();
        // 选取文本长度5%的数量作为文本测试的检测点
		int detectNum = (int) (textSize*0.05);
		int [] dectArray = new int[detectNum];
		
		for (int i = 0; i < detectNum; i++) {
			dectArray[i] = random.nextInt(textSize);
		}
        
        return dectArray;
	}
	/**
	 * change the traditional text to simple text
	 * @param text  the paper content
	 * @return  the transform result
	 */
	public String transformText(String  text) {
		
		if (text.isEmpty() == true) {
			return "";
		}
		
		String retText = new String();
		
		StringBuffer tempText = new StringBuffer();
		
		//String retText = "";
		if (false == this.isTraditionalText(text)) {
			retText = text;
		}
		else {
			int curSize = text.length();
			
			for (int j = 0; j < curSize; j++) {
				if (this.trad_simp_dict.containsKey(text.charAt(j)) == true) {
					Character ch = this.trad_simp_dict.get(text.charAt(j));
					tempText.append(ch);
				}
				else {
					tempText.append(text.charAt(j));
				}
			}
			retText = tempText.toString();
		}
		
		return retText;
	}
	
	/** 
	 * get the paper text from file 
	 *  */
	public String loadFile(String infile) {
		if (infile.isEmpty() == true) {
			return "";
		}
		String allText = "";
		try {
			FileInputStream fis = new FileInputStream(infile);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
			BufferedReader br = new BufferedReader(isr);
	
			String line = "";
			
			while (( line = br.readLine()) != null) {
				allText += line;			
			}
			
			br.close();
			isr.close();
			fis.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return allText;
	}
	public static void main(String[] args) {
		
		TextTradTransSimple tts = new TextTradTransSimple();
		if (true == tts.initDict("F:\\NLPCorpusResource\\繁简对照\\tradDiffSimp.txt") ) {
			System.out.println("loading dictionary is successful!");
		}
		
		//String text = "毗邻北京的河北省首当其冲，十月底开始，河北重点控制区域的881个工地开始停工，会议周期间，更有2000多家企业停产，1700多处工地停工，京津冀区三地首次进行了机动车的同步单双号限行；此外，天津、山东、山西等省市也都对污染企业停工停产。 11月10日，出席北京ＡＰＥＣ领导人非正式会议的中国国家主席习近平表示，为了让远方的客人感到舒适一些，他每天早晨起来的第一件事就是看看北京的空气质量如何，希望雾霾小一些，他说：“好在人努力、天帮忙，这几天北京空气质量总体好多了，不过，我也担心我这个话说早了，但愿明天的天气也还好，这是有关地方和部门共同努力的结果，来之不易。” 那么，ＡＰＥＣ蓝背后的原因到底是什么，到底是归功于严格的减排措施还是来自北方的冷空气？对此，公众环境研究中心主任马军表示：“气象条件只是外因，内因还是我们整个排放量还是非常大，从整个区域来看，排放量最大的是燃煤以及工业的废气的排放，它的消耗量远远大于供暖的用煤消耗，这也解释了为什么在供暖季之外，我们经常会遭遇严重的雾霾天。ＡＰＥＣ蓝实际上再次确认，蓝天是可以找回来的，我们完全可以控制排放的产生。” 在许多人看来，ＡＰＥＣ空气质量保障是验证各项大气污染治理措施的绝好试验，为未来尽快改善空气质量提供了难能可贵的经验和借鉴。北京市环境保护科学研究院院长宋强表示：“整个ＡＰＥＣ保障措施是非常成功的，也证明我们大气污染防治行动计划以及北京市清洁空气行动计划所提出的治理的大气污染技术路线和我们相关的政策要求，它的方向是正确的。” ";
		//String text = "每日交收通知款項須如常繳付，於訊號發出的一小時或以上之前已發出的任何即日額外按金通知，仍須於該通知發出後一小時内繳付。期貨結算所將盡力處理於當天截止時間(上午11時)之前已獲接納的現金交付及歸還要求以及於共同抵押品管理系統與中央結算系統之間抵押品證券的轉移指示，但須視乎相關銀行、共同抵押品管理系統及中央結算系統能否提供適當服務。當天將被視為交收股票的交收日，而交收程序會如常進行。";
		String text = tts.loadFile("F:\\NLPCorpusResource\\繁简对照\\testCor.txt");
		long startTime=System.currentTimeMillis();   //获取开始时间
		/*
		for (int i = 0; i < 10000; i++) {
			if (tts.isTraditionalText(text) == true) {
				//System.out.println("traditional");
				String retText = tts.transformText(text);
				//System.out.println(text);
				//System.out.println(retText);
			}
			else {
				;//System.out.println("the text is chinese simple content!");
			}
		}
		*/
		if (tts.isTraditionalText(text) == true) {
			System.out.println("traditional");
			String retText = tts.transformText(text);
			System.out.println(text);
			System.out.println(retText);
		}
		else {
			System.out.println("the text is chinese simple content!");
		}
		long endTime=System.currentTimeMillis(); //获取结束时间

		System.out.println("程序运行时间： "+(endTime - startTime)+"ms");
		
	}
}
