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

package gr.infocode.schemagraph.exporter.config;

import gr.infocode.schemagraph.lib.options.*;

public enum ExporterOptionsEnum implements IOptionsEnum {
    OPT_CONFIGURATION("c",  "config",   1, false, "<file>",            "configuration file to use"), 
         OPT_DATABASE("d",  "database", 1, false, "<dbsection>",       "named database section to use"), 
          OPT_CATALOG(null, "catalog",  1, false, "<catalog pattern>", "catalog pattern"), 
           OPT_SCHEMA(null, "schema",   1, false, "<schema pattern>",  "schema pattern"), 
         OPT_NOTABLES(null, "no-tables",0, false, "",                  "do not export table metadata"), 
          OPT_NOPROCS(null, "no-procs", 0, false, "",                  "do not export stored-procedures metadata"), 
          OPT_NOFUNCS(null, "no-funcs", 0, false, "",                  "do not export functions metadata"), 
           OPT_NOUDFS(null, "no-udfs",  0, false, "",                  "do not export UDFs metadata"), 
      OPT_INTERACTIVE("i",  null,       0, false, "",                   "interactive login"),
           OPT_OUTPUT("o",  "output",   1, false, "<file>",             "output file to write results"), 
          OPT_LOGFILE("l",  "logfile",  1, false, "<file>",             "logfile to write"),
         OPT_LOGLEVEL("v",  "loglevel", 1, false, "<int>",              "log level"),
         OPT_TEMPLATE("t",  "template", 0, false, "",                   "export a configuration template"),
             OPT_HELP("h",  "help",     0, false, "",                   "Print this help"); 

    private static Options<ExporterOptionsEnum> options;
    static {
        options = new Options<ExporterOptionsEnum>(ExporterOptionsEnum.values());
    }
    
    private IOption option;

    // Contructor
    ExporterOptionsEnum(String shortOpt, String longOpt, Integer paramCount, boolean isMandatory, String paramString, String helpString) {
        option = new Option(shortOpt, longOpt, paramCount, isMandatory, paramString, helpString);
    }

    // interface implementation
    public IOption getOption() {
        return option;
    }
    
    // local getter
    public static Options<ExporterOptionsEnum> optionHandler() {
        return options;
    }
    
  
}



