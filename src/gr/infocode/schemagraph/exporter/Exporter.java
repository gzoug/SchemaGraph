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
package gr.infocode.schemagraph.exporter;

import org.w3c.dom.Document;
import java.io.File;
import java.io.InputStream;
import gr.infocode.schemagraph.util.Logger;
import gr.infocode.schemagraph.util.XMLUtil;
import gr.infocode.schemagraph.exporter.config.ExporterOptionsEnum;
import gr.infocode.schemagraph.exporter.db.DBUtil;

public class Exporter {

    // The base path for resources
    public static final String RC_EXPORTER_BASE = "gr/infocode/schemagraph/exporter/resources/";

    public static void main(String[] args) {
 	
        try {
            ExporterOptionsEnum.optionHandler().parseArgs(args);
        } catch (Exception e) {
            System.out.println("Couldn't parse arguments\n" + e.getMessage());
            System.out.println(e.getMessage());  
            System.exit(1);
        }
       
        if (ExporterOptionsEnum.OPT_HELP.getOption().isDefined() || 
            (ExporterOptionsEnum.optionHandler().getDefinedCount()==0) )
        {
            System.out.println("\nSchemaGraph - Exporter v1.0\n");
            System.out.println(ExporterOptionsEnum.optionHandler().getHelp());
        	System.exit(0);        	        		
        }

        Logger log = Logger.getInstance();
        if (ExporterOptionsEnum.OPT_LOGFILE.getOption().isDefined()) {
            log.setLogfilename(ExporterOptionsEnum.OPT_LOGFILE.getOption().getValue(0));    
        }        
        
        if (ExporterOptionsEnum.OPT_TEMPLATE.getOption().isDefined()) {
            InputStream cis = ClassLoader.getSystemResourceAsStream(RC_EXPORTER_BASE + "configuration_template.xml");
            if(cis == null) {
                log.exit("Could not build configuration template - resource not found");
            }
            System.out.println(XMLUtil.getDocumentAsString(XMLUtil.parseDocument(cis)));
            System.exit(0);            
        }
        
       /* variables declaration */
        String database = ExporterOptionsEnum.OPT_DATABASE.getOption().getValue(0);
        if (database == null) {
        	log.exit("No database section defined");
        }
        
        //String database = "mssqltest";
        DBUtil db = new DBUtil(database);
        Document doc = db.getMetadataAsDOM();
        String outputfile = ExporterOptionsEnum.OPT_OUTPUT.getOption().getValue(0);
        if (outputfile == null) {
            System.out.println(XMLUtil.getDocumentAsString(doc));
        }
        else
        {
            try {
                File f = new File(outputfile);
                XMLUtil.writeDocument(doc, f);
            } catch (Exception e){
                log.exit("Couldn't create " + outputfile + "\n" + e.getMessage());
            }
        }
    }

}
