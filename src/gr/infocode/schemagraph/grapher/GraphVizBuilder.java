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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import  java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import gr.infocode.schemagraph.util.Logger;
import gr.infocode.schemagraph.util.XMLUtil;
import  org.w3c.dom.Document;

public class GraphVizBuilder {
    private static final Logger log;
    private static final GraphVizBuilder instance;
    
    static{        
        log = Logger.getInstance();
        instance = new GraphVizBuilder();
    }
    public GraphVizBuilder(){}
    
    public static GraphVizBuilder getDefaultInstance() {
        return instance;
    }

    public void process(File input, String outputPath){
        InputStream config_is = null; 
        Document config_xml = null;
        
        
        config_is = ClassLoader.getSystemResourceAsStream( Grapher.RC_GRAPHER_BASE + "transformers.xml");
        if (config_is == null) {
            log.exit("Could not load default transformers configuration");
        }      
        config_xml = XMLUtil.parseDocument(config_is);
        
        
        ArrayList<String> transformerIDs = XMLUtil.SelectNodeValues(config_xml, "/schemaGraphers//transformer/@id");
        for (String id : transformerIDs) {
            InputStream xslt = null;
            InputStream schema_is = null;
            log.info("Processing transformer id " + id);
            String transXSLT = XMLUtil.selectNodeValue(config_xml, String.format("//transformer[@id='%s']/@resource",id));
            String transEXT = XMLUtil.selectNodeValue(config_xml, String.format("//transformer[@id='%s']/@result-extension",id));
            try {
                xslt = ClassLoader.getSystemResourceAsStream( Grapher.RC_GRAPHER_BASE + transXSLT);
            } catch (Exception e){
                log.exception(e);
                log.warn("Could not read transformer id " + id);
                continue;
            }
            try {
                schema_is = new FileInputStream(input);            
            } catch (Exception e) {
                log.exception(e);
                log.exit("Could not load input schema");
            }
            try {
                String fout = outputPath + input.getName().replaceFirst(".xml$", "") + "_" + id + "." + transEXT;
                OutputStream os = new FileOutputStream(new File(fout));                
                XMLUtil.transformDocument(schema_is, xslt, os);                      
                os.flush();
                os.close();
                xslt.close();
                schema_is.close();
            } catch (Exception e){
                log.error("Could not transform or create transformation output");
                log.exception(e);
                continue;
            }
        }
        
        // finalize open objects
        try {
            config_is.close();
        } catch (Exception e) {
            log.error("Could not finalize open objects");
            log.exception(e);
        }
                
    }
    
}
