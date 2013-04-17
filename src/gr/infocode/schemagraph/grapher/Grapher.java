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

package gr.infocode.schemagraph.grapher;

import java.io.File;
import gr.infocode.schemagraph.grapher.config.GrapherOptionsEnum;
import gr.infocode.schemagraph.util.Logger;

public class Grapher {
    
    // The base path for resources
    public static final String RC_GRAPHER_BASE = "gr/infocode/schemagraph/grapher/resources/";
    
    public static void main(String[] args) {
        GraphVizBuilder gvb = GraphVizBuilder.getDefaultInstance();
        
        try {
            GrapherOptionsEnum.optionHandler().parseArgs(args);
        } catch (Exception e) {
            System.out.println("Couldn't parse arguments\n" + e.getMessage());
            System.out.println(e.getMessage());
            System.exit(1);
        }

        if (GrapherOptionsEnum.OPT_HELP.getOption().isDefined()
                || (GrapherOptionsEnum.optionHandler().getDefinedCount() == 0)) {
            System.out.println("\nSchemaGraph - Grapher v1.0\n");
            System.out.println(GrapherOptionsEnum.optionHandler().getHelp());
            System.exit(0);
        }

        Logger log = Logger.getInstance();
        if (GrapherOptionsEnum.OPT_LOGFILE.getOption().isDefined()) {
            log.setLogfilename(GrapherOptionsEnum.OPT_LOGFILE.getOption().getValue(0));
        }
        
        if (GrapherOptionsEnum.OPT_SCHEMA.getOption().getValue(0)==null) {
            log.exit("Input schema not defined");
        }
        File inputSchema = null;
        inputSchema = new File(GrapherOptionsEnum.OPT_SCHEMA.getOption().getValue(0));
        if ((inputSchema==null) || (! inputSchema.exists()) ) {
            log.exit("Could not open input schema");
        }
        
        String outputFolder = "";
        if (GrapherOptionsEnum.OPT_OUTPUT.getOption().isDefined()){
            File f = new File(GrapherOptionsEnum.OPT_OUTPUT.getOption().getValue(0));
            if ((f==null) || (!f.isDirectory()) ) {
                log.exit("Output folder does not exist or it is not a directory");
            }
            try {
                outputFolder = f.getCanonicalPath();
                if (!outputFolder.endsWith(String.valueOf(File.separatorChar))) { 
                    outputFolder += File.separatorChar; 
                }
            } catch (Exception e) {
                log.exception(e);
                log.exit("Could not convert output folder into a canonical path");
            }
        }
        gvb.process(inputSchema, outputFolder);
     
    }

}
