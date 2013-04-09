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

package gr.infocode.schemagraph.grapher.config;

import gr.infocode.schemagraph.lib.options.IOption;
import gr.infocode.schemagraph.lib.options.IOptionsEnum;
import gr.infocode.schemagraph.lib.options.Option;
import gr.infocode.schemagraph.lib.options.Options;

public enum GrapherOptionsEnum implements IOptionsEnum {
           OPT_SCHEMA("s",  "schema",   1, false, "<file>",            "xml schema file to read (generated using Exporter)"), 
           OPT_OUTPUT("o",  "output",   1, false, "<folder>",           "output folder to store results"), 
          OPT_LOGFILE("l",  "logfile",  1, false, "<file>",             "logfile to write"),
         OPT_LOGLEVEL("v",  "loglevel", 1, false, "<int>",              "log level"),
             OPT_HELP("h",  "help",     0, false, "",                   "Print this help"); 

    private static Options<GrapherOptionsEnum> options;
    static {
        options = new Options<GrapherOptionsEnum>(GrapherOptionsEnum.values());
    }
    
    private IOption option;

    // Contructor
    GrapherOptionsEnum(String shortOpt, String longOpt, Integer paramCount, boolean isMandatory, String paramString, String helpString) {
        option = new Option(shortOpt, longOpt, paramCount, isMandatory, paramString, helpString);
    }

    // interface implementation
    public IOption getOption() {
        return option;
    }
    
    // local getter
    public static Options<GrapherOptionsEnum> optionHandler() {
        return options;
    }
    
  
}



