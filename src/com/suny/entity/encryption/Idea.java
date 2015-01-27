package com.suny.entity.encryption;


import java.io.UnsupportedEncodingException;

/**
 * this class is using for encrypt and decrypt word
 * protected our dictionary data
 * @author Frank suny
 * @version 1.0  date 20140801
 */

public class Idea {
	
	/**
	 *  the encrypt key
	 */
	public static final String encryptKey = "sunying";
	
	/**
	 * 
	 * @return encryptKey
	 */
	public String getEncryptKey() {
		return this.encryptKey;
	}
	// encrypt the input data using key
    private byte[] Encrypt(byte[] bytekey, byte[] inputBytes, boolean flag) {
        
    	byte[] encryptCode = new byte[8];

        // 分解子密钥
        int[] key = get_subkey(flag, bytekey);

        // 进行加密操作
        encrypt(key, inputBytes, encryptCode);

        // 返回加密数据
        return encryptCode;
    }
    
    // change bytes data into Integer
    private int bytesToInt(byte[] inBytes, int startPos) {
        return ((inBytes[startPos] << 8) & 0xff00) +
        (inBytes[startPos + 1] & 0xff);
    }
    
    // change Integer data into bytes
    private void intToBytes(int inputInt, byte[] outBytes, int startPos) {
        outBytes[startPos] = (byte) (inputInt >>> 8);
        outBytes[startPos + 1] = (byte) inputInt;
    }

    private int x_multiply_y(int x, int y) {
        if (x == 0) {
            x = 0x10001 - y;
        } else if (y == 0) {
            x = 0x10001 - x;
        } else {
            int tmp = x * y;
            y = tmp & 0xffff;
            x = tmp >>> 16;
            x = (y - x) + ((y < x) ? 1 : 0);
        }

        return x & 0xffff;
    }

    // 进行加密操作
    // the kernel method
    private void encrypt(int[] key, byte[] inbytes, byte[] outbytes) {
        int k = 0;
        int a = bytesToInt(inbytes, 0);
        int b = bytesToInt(inbytes, 2);
        int c = bytesToInt(inbytes, 4);
        int d = bytesToInt(inbytes, 6);

        for (int i = 0; i < 8; i++) {
            a = x_multiply_y(a, key[k++]);
            b += key[k++];
            b &= 0xffff;
            c += key[k++];
            c &= 0xffff;
            d = x_multiply_y(d, key[k++]);

            int tmp1 = b;
            int tmp2 = c;
            c ^= a;
            b ^= d;
            c = x_multiply_y(c, key[k++]);
            b += c;
            b &= 0xffff;
            b = x_multiply_y(b, key[k++]);
            c += b;
            c &= 0xffff;
            a ^= b;
            d ^= c;
            b ^= tmp2;
            c ^= tmp1;
        }

        intToBytes(x_multiply_y(a, key[k++]), outbytes, 0);
        intToBytes(c + key[k++], outbytes, 2);
        intToBytes(b + key[k++], outbytes, 4);
        intToBytes(x_multiply_y(d, key[k]), outbytes, 6);
    }

    // get sub encrypt keys
    private int[] encrypt_subkey(byte[] byteKey) {
        int[] key = new int[52];

        if (byteKey.length < 16) {
            byte[] tmpkey = new byte[16];
            System.arraycopy(byteKey, 0, tmpkey,
                tmpkey.length - byteKey.length, byteKey.length);
            byteKey = tmpkey;
        }

        for (int i = 0; i < 8; i++) {
            key[i] = bytesToInt(byteKey, i * 2);
        }

        for (int j = 8; j < 52; j++) {
            if ((j & 0x7) < 6) {
                key[j] = (((key[j - 7] & 0x7f) << 9) | (key[j - 6] >> 7)) &
                    0xffff;
            } else if ((j & 0x7) == 6) {
                key[j] = (((key[j - 7] & 0x7f) << 9) | (key[j - 14] >> 7)) &
                    0xffff;
            } else {
                key[j] = (((key[j - 15] & 0x7f) << 9) | (key[j - 14] >> 7)) &
                    0xffff;
            }
        }

        return key;
    }

    private int fun_a(int a) {
        if (a < 2) {
            return a;
        }

        int b = 1;
        int c = 0x10001 / a;

        for (int i = 0x10001 % a; i != 1;) {
            int d = a / i;
            a %= i;
            b = (b + (c * d)) & 0xffff;

            if (a == 1) {
                return b;
            }

            d = i / a;
            i %= a;
            c = (c + (b * d)) & 0xffff;
        }

        return (1 - c) & 0xffff;
    }

    private int fun_b(int b) {
        return (0 - b) & 0xffff;
    }

    // get the decrypt sub keys 
    private int[] uncrypt_subkey(int[] key) {
        int dec = 52;
        int asc = 0;
        int[] unkey = new int[52];
        int aa = fun_a(key[asc++]);
        int bb = fun_b(key[asc++]);
        int cc = fun_b(key[asc++]);
        int dd = fun_a(key[asc++]);
        unkey[--dec] = dd;
        unkey[--dec] = cc;
        unkey[--dec] = bb;
        unkey[--dec] = aa;

        for (int k1 = 1; k1 < 8; k1++) {
            aa = key[asc++];
            bb = key[asc++];
            unkey[--dec] = bb;
            unkey[--dec] = aa;
            aa = fun_a(key[asc++]);
            bb = fun_b(key[asc++]);
            cc = fun_b(key[asc++]);
            dd = fun_a(key[asc++]);
            unkey[--dec] = dd;
            unkey[--dec] = bb;
            unkey[--dec] = cc;
            unkey[--dec] = aa;
        }

        aa = key[asc++];
        bb = key[asc++];
        unkey[--dec] = bb;
        unkey[--dec] = aa;
        aa = fun_a(key[asc++]);
        bb = fun_b(key[asc++]);
        cc = fun_b(key[asc++]);
        dd = fun_a(key[asc]);
        unkey[--dec] = dd;
        unkey[--dec] = cc;
        unkey[--dec] = bb;
        unkey[--dec] = aa;

        return unkey;
    }

    // 分解子密钥
    private int[] get_subkey(boolean flag, byte[] bytekey) {
        if (flag) {
            return encrypt_subkey(bytekey);
        } else {
            return uncrypt_subkey(encrypt_subkey(bytekey));
        }
    }

    private byte[] ByteDataFormat(byte[] data, int unit) {
        int len = data.length;
        int padlen = unit - (len % unit);
        int newlen = len + padlen;
        byte[] newdata = new byte[newlen];
        System.arraycopy(data, 0, newdata, 0, len);

        for (int i = len; i < newlen; i++)
            newdata[i] = (byte) padlen;

        return newdata;
    }

    public byte[] IdeaEncrypt(byte[] idea_key, byte[] idea_data, boolean flag) {
        byte[] format_key = ByteDataFormat(idea_key, 16);
        byte[] format_data = ByteDataFormat(idea_data, 8);

        int datalen = format_data.length;
        int unitcount = datalen / 8;
        byte[] result_data = new byte[datalen];

        for (int i = 0; i < unitcount; i++) {
            byte[] tmpkey = new byte[16];
            byte[] tmpdata = new byte[8];
            System.arraycopy(format_key, 0, tmpkey, 0, 16);
            System.arraycopy(format_data, i * 8, tmpdata, 0, 8);

            byte[] tmpresult = Encrypt(tmpkey, tmpdata, flag);
            System.arraycopy(tmpresult, 0, result_data, i * 8, 8);
        }

        return result_data;
    }
    
  
    public static void main(String[] args) throws UnsupportedEncodingException {
        
    	Idea idea = new Idea();
    	String key = idea.encryptKey;
        
    	String data = "中华人名共和国";
    	//String data = "360公司";
    	data = data.length() + "\t" + data;
    	System.out.println(data);
        byte[] bytekey = key.getBytes();
        byte[] bytedata = data.getBytes();

        byte[] encryptdata = idea.IdeaEncrypt(bytekey, bytedata, true);
        byte[] decryptdata = idea.IdeaEncrypt(bytekey, encryptdata, false);

        System.out.println("--------------------------------");

        for (int i = 0; i < bytedata.length; i++) {
            System.out.print(" " + bytedata[i] + " ");
        }

        System.out.println("");

        for (int i = 0; i < encryptdata.length; i++) {
            System.out.print(" " + encryptdata[i] + " ");
        }

        System.out.println("");

        for (int i = 0; i < decryptdata.length; i++) {
            System.out.print(" " + decryptdata[i] + " ");
        }

        System.out.println("");
        System.out.println("--------------------------------");
        
        String recString = new String(decryptdata);
		
		int pos = recString.indexOf("\t");
		if (pos != -1) {
			int len = Integer.parseInt(recString.substring(0,pos));
			recString = recString.substring(pos + 1);
			String word = recString.substring(0,len);
			System.out.println("the initinal string is:" + word);
		}
    }
}