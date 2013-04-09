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

package gr.infocode.schemagraph.lib.options;

import gr.infocode.schemagraph.util.Logger;
import gr.infocode.schemagraph.util.StringUtil;

public class Options<T>{

    private T[] enums;

    public Options(T[] enums)  {
        this.enums = enums;        
    }

    public void parseArgs(String[] appArgs) throws Exception {
         Logger log = Logger.getInstance();
         int totalArgs = appArgs.length;
         int i = 0;
         while (i < totalArgs) {
             String arg = appArgs[i];
             Integer expectedParams = 0;
             IOptionsEnum opt = getMatch(arg);
             if (opt != null) {
                 expectedParams = opt.getOption().getParamCount();
             }             
             if (opt == null) {
                 log.exit("Unknown argument " + arg);
             } else {        
                 opt.getOption().setDefined(true);
                 while (expectedParams>0) {
                    if (++i < totalArgs) {
                       String argValue = appArgs[i];
                       opt.getOption().addValue(argValue);
                    } else {
                       log.exit("Missing value for argument " + arg);
                    }
                    expectedParams--;
                  }             
             }
             i++;
         }
     }

    public int getDefinedCount() {         
         int defCount = 0;
         for (IOptionsEnum opt : (IOptionsEnum[])this.enums) {
             if (opt.getOption().isDefined()) {
                 defCount++;
             }
         }
         return defCount;
     }

    public IOptionsEnum getMatch(String argument) {
         for (IOptionsEnum opt : (IOptionsEnum[])this.enums) {
            if (opt.getOption().matches(argument)) {
                return opt;
            }
         }
         return null;
     }

    
    public String getHelp() {
        
         StringBuffer helpString = new StringBuffer();
                  
         for (IOptionsEnum opt : (IOptionsEnum[])this.enums) {
             StringBuffer hs = new StringBuffer();
             if (opt.getOption().getShortOpt()!=null) { 
                 hs.append("-" + opt.getOption().getShortOpt());
             }
             if (opt.getOption().getLongOpt()!=null) { 
                 if (opt.getOption().getShortOpt()!=null) { hs.append(" | "); }
                 hs.append("--" + opt.getOption().getLongOpt()); 
             }
             hs.append(" ");
             hs.append(opt.getOption().getParamString());
             hs.append(opt.getOption().isMandatory() ? " (*)" : "   "  );
             hs.append(StringUtil.repString(" ", 40-hs.length()));
             hs.append(": ");
             hs.append(opt.getOption().getHelpString());
             hs.append("\n");
             
             helpString.append(hs.toString());
         }        
         if (helpString.length()>0) {
             helpString.append("\n(*) : Mandatory parameters\n\n");
         }
         return helpString.toString();
     }
    

}
