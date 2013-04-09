/*
Project: SchemaGraph

Copyright (c) 2007-2013, George Zouganelis (george.zouganelis@gmail.com)
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

   1. Redistributions of source code must retain the above copyright notice,
      this list of conditions and the following disclaimer.

   2. Redistributions in binary form must reproduce the above copyright notice,
      this list of conditions and the following disclaimer in the documentation
      and/or other materials provided with the distribution.
   
   3. Neither the name of George Zouganelis nor the names of contributors may 
      be used to endorse or promote products derived from this software without 
      specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE GEORGE ZOUGANELIS AND CONTRIBUTORS ``AS IS'' 
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
IN NO EVENT SHALL GEORGE ZOUGANELIS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package gr.infocode.schemagraph.util;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtil {
	private static Logger logger;
	static {
		logger = Logger.getInstance();
	}

    /**
     * parse a string into a Date object, based on the format parameter
     */
    public static Date parseDateString(String inDate, String ... formats) {
        Date d = null;
        for (int i=0; i<formats.length; i++){
            String format = formats[i];
            try {
                // Logger.getInstance().log(" Parsing date " + inDate + " using format " + formats[i]);
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                ParsePosition pos = new ParsePosition(0);
                d =  sdf.parse(inDate, pos);
                if ( d != null) {
                    break; 
                } else {
                    logger.warn("Cannot parse date " + inDate + " using format " + formats[i]);
                    continue;
                }
            } catch (NullPointerException pe) {
                logger.error("Cannot parse date " + inDate + " using format " + formats[i]);
                continue;
            }
        }
        return d;
    }

    public static String repString(String template, int count){
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<count;i++){
            sb.append(template);
        }
        return sb.toString();
    }
    
    public static String purifyString(String s){
        return s.replaceAll("\\s", " ");        
    }
    
    public static String generateMD5(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            md.reset();
            md.update(text.getBytes());

            StringBuffer hex = new StringBuffer();
            byte digest[] = md.digest();
            String tmpStr;
            for (int i=0; i<digest.length; i++){
               tmpStr = Integer.toHexString(0xFF & digest[i]);
               if (tmpStr.length()==1) {hex.append('0');} 
               hex.append(tmpStr);
            }
            return hex.toString();

        } catch (NoSuchAlgorithmException nsae) {   
        	logger.error("Cannot initialize md5 algorithm");
            return "FAILED: md5";
        }
    }
}
