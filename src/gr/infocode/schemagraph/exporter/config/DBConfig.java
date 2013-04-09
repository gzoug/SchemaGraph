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

import java.util.HashMap;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import gr.infocode.schemagraph.util.Logger;
import gr.infocode.schemagraph.util.XMLUtil;
import gr.infocode.schemagraph.exporter.db.DBConnection;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;

public class DBConfig {
    private static final DBConfig defaultInstance;
    private static Logger log = null;
    private String configurationFile = null; // default configuration
    private HashMap<String, DBConnection> DBConnections;

    static {
        log = Logger.getInstance();
        String configFile = ExporterOptionsEnum.OPT_CONFIGURATION.getOption()
                .getValue(0);
        if (configFile == null) {
            log.exit("No configuration file was defined");
        }
        defaultInstance = new DBConfig(configFile);
    }

    private DBConfig(String configFile) {
        this.configurationFile = new String(configFile);
        Initialize();
    }

    private void Initialize() {
        DBConnections = new HashMap<String, DBConnection>();
        InputStream cis = null;

        // read the XML file and populate the files attribute

        try {
            cis = new FileInputStream(configurationFile);
        } catch (IOException e) {
            log.exit("Error reading configuration file " + configurationFile);
        }

        if (cis == null) {
            log.exit("Could not find configuration file " + configurationFile);
        }
        Document doc = XMLUtil.parseDocument(cis);
        if (doc == null) {
            log.exit("Cannot parse configuration file " + configurationFile);
        }

        NodeList n;
        // get DBConnections
        n = doc.getElementsByTagName("db");
        for (int i = 0; i < n.getLength(); i++) {
            Node dbNode = n.item(i);
            NamedNodeMap dbAttribs = dbNode.getAttributes();
            String dbID = dbAttribs.getNamedItem("id").getNodeValue();
            DBConnection dbConnection = new DBConnection(dbID);
            dbConnection.setConfigValue(
                    DBConfigEnum.DRIVERCLASS,
                    XMLUtil.selectNodeValue(doc, "//db[@id='" + dbID
                            + "']/driver/class/text()"));
            dbConnection.setConfigValue(
                    DBConfigEnum.DRIVERCONNECTOR,
                    XMLUtil.selectNodeValue(doc, "//db[@id='" + dbID
                            + "']/driver/connector/text()"));
            dbConnection.setConfigValue(
                    DBConfigEnum.DATABASE,
                    XMLUtil.selectNodeValue(doc, "//db[@id='" + dbID
                            + "']/database/text()"));
            dbConnection.setConfigValue(
                    DBConfigEnum.USERNAME,
                    XMLUtil.selectNodeValue(doc, "//db[@id='" + dbID
                            + "']/username/text()"));
            dbConnection.setConfigValue(
                    DBConfigEnum.PASSWORD,
                    XMLUtil.selectNodeValue(doc, "//db[@id='" + dbID
                            + "']/password/text()"));
            dbConnection.setConfigValue(
                    DBConfigEnum.ESCAPENAMES,
                    XMLUtil.selectNodeValue(doc, "//db[@id='" + dbID
                            + "']/@escapenames"));
            DBConnections.put(dbID, dbConnection);
        }
    }

    public DBConnection getDBConnection(String id) {
        if (DBConnections.containsKey(id)) {
            return DBConnections.get(id);
        }
        return null;
    }

    public static DBConfig getInstance() {
        return defaultInstance;
    }
}
