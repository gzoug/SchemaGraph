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

package gr.infocode.schemagraph.exporter.db;

import java.sql.*;
import java.lang.Error;
import org.w3c.dom.*;
import gr.infocode.schemagraph.util.Logger;
import gr.infocode.schemagraph.util.StringUtil;
import gr.infocode.schemagraph.util.XMLUtil;
import gr.infocode.schemagraph.exporter.config.DBConfig;
import gr.infocode.schemagraph.exporter.config.DBConfigEnum;
import gr.infocode.schemagraph.exporter.config.ExporterOptionsEnum;

public class DBUtil {
	private DBConnection con = null;
	private static Logger log = Logger.getInstance();

	public DBUtil(String ConnectionID) {
		con = DBConfig.getInstance().getDBConnection(ConnectionID);
		if ((con==null) || (!con.Connect())) {
		    log.exit("Couldn't connect to database " + ConnectionID);
		}
	}


	// the main method that collects schema information
	private void getMetadataDomFragment(Document doc, Node parentNode,
			DatabaseMetaData meta, String catalogPattern, String schemaPattern, String objectname, String elementname,
			DBObjectsEnum dbObject) {
	    
		ResultSetMetaData rsmeta = null;
		ResultSet rs = null;

		String objname;		
        String escapeString;
        
        try {
            escapeString = meta.getSearchStringEscape();
        } catch (Exception e) {
            escapeString = "";
        }

		if (con.getConfigValue(DBConfigEnum.ESCAPENAMES).equalsIgnoreCase("yes")) {
            objname = objectname.replaceAll("_", StringUtil.repString(escapeString, 4) + "_");
        } else {
            objname = objectname;
        }
        
		try {
			switch (dbObject) {
			case TABLES: // unused here
				log.info("  Retreiving views , user tables and system tables...");
				rs = meta.getTables(catalogPattern, schemaPattern, objectname, null);
				break;
			case TABLE_COLUMNS:
				log.info("  Retreiving table columns for " + objectname);
				rs = meta.getColumns(catalogPattern, schemaPattern, objname, null);
				break;
			case PROCEDURE_COLUMNS:
				log.info("  Retreiving procedure columns for " + objectname);
				rs = meta.getProcedureColumns(catalogPattern, schemaPattern, objname, null);
				break;
			case FUNCTION_COLUMNS:
				log.info("  Retreiving function columns for " + objectname);
				rs = meta.getFunctionColumns(catalogPattern, schemaPattern, objname, null);
				break;
			case FOREIGN_KEYS:
				log.info("  Retreiving foreign keys for " + objectname);
				rs = meta.getImportedKeys(catalogPattern, schemaPattern, objname);
				break;
			case PRIMARY_KEYS:
				log.info("  Retreiving primary keys for " + objectname);
				rs = meta.getPrimaryKeys(catalogPattern, schemaPattern, objname);
				break;
			case INDEXES:
				log.info("  Retreiving indexes for " + objectname);
				rs = meta.getIndexInfo(catalogPattern, schemaPattern, objname, false, false);
				break;

			}
			rsmeta = rs.getMetaData();
			while (rs.next()) {
				Node fragNode = doc.createElement(elementname);
				NamedNodeMap nm = fragNode.getAttributes();
				for (int i = 0; i < rsmeta.getColumnCount(); i++) {
					String colName = rsmeta.getColumnName(i + 1).toLowerCase();
					if (colName.compareTo("remarks") != 0) {
                        Attr attr = doc.createAttribute(colName);
                        String nv = "";
	                    try {                   
    						nv = rs.getString(i + 1);
	                    } catch (Exception e) {
	                        log.error("Could not get column data : column [total count, index, name]=[" + 
	                                rsmeta.getColumnCount() + "," + 
	                                (i+1) + "," +  
	                                colName + 
	                                "]");
	                    }
                        attr.setNodeValue(nv);
                        nm.setNamedItem(attr);
					}
				}
				
				parentNode.appendChild(fragNode);
			}
		} catch (Exception e) {
		    log.error(e.getMessage());
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private Node getObjectNode(Document doc, ResultSetMetaData rsMetadata,
			ResultSet rsObject, String ElementName) {
		Node objectNode = null;
		try {
			objectNode = doc.createElement(ElementName);
			NamedNodeMap tnm = objectNode.getAttributes();
			for (int ti = 0; ti < rsMetadata.getColumnCount(); ti++) {
				String colName = rsMetadata.getColumnName(ti + 1).toLowerCase();
				// stip out comments
				if (colName.compareTo("remarks") != 0) {
					Attr attr = doc.createAttribute(colName);
					attr.setNodeValue(rsObject.getString(ti + 1));
					tnm.setNamedItem(attr);
				}
			}
		} catch (Exception e) {

		}
		return objectNode;
	}

	public Document getMetadataAsDOM() {

		DatabaseMetaData dbMetadata = null;
		ResultSetMetaData rsMetadata = null;
		ResultSet rsObject = null;
		Node objectNode = null;
		String objectname = "";

		String catalogPattern = null;
		if (ExporterOptionsEnum.OPT_CATALOG.getOption().isDefined()) {
		    catalogPattern = ExporterOptionsEnum.OPT_CATALOG.getOption().getValue(0);
		    log.info("Using catalog pattern [" + catalogPattern + "]");
		}
		
		String schemaPattern = null;
        if (ExporterOptionsEnum.OPT_SCHEMA.getOption().isDefined()) {
            schemaPattern = ExporterOptionsEnum.OPT_SCHEMA.getOption().getValue(0);
            log.info("Using schema pattern [" + schemaPattern + "]");
        }

		Document doc = XMLUtil.newDocument();
		Node root = doc.createElement("xmldbschema");
		doc.appendChild(root);

		try {
			dbMetadata = con.getConnection().getMetaData();
			// Get VIEWS/TABLES/SYSTEM_TABLES
			if (!ExporterOptionsEnum.OPT_NOTABLES.getOption().isDefined()) {
    			try {
    				log.info("Retreiving views , user tables and system tables...");
    				rsObject = dbMetadata.getTables(catalogPattern, schemaPattern, null, null);
    				rsMetadata = rsObject.getMetaData();
    				while (rsObject.next()) {
    					objectname = rsObject.getString("TABLE_NAME");
                                            log.info("Retreiving metadata for table: " + objectname);
    				
    					objectNode = getObjectNode(doc, rsMetadata, rsObject, "table");
    					getMetadataDomFragment(doc, objectNode, dbMetadata, catalogPattern, schemaPattern, objectname, "column", DBObjectsEnum.TABLE_COLUMNS);
    					getMetadataDomFragment(doc, objectNode, dbMetadata, catalogPattern, schemaPattern, objectname, "pkey", DBObjectsEnum.PRIMARY_KEYS);
    					getMetadataDomFragment(doc, objectNode, dbMetadata, catalogPattern, schemaPattern, objectname, "fkey", DBObjectsEnum.FOREIGN_KEYS);
    					getMetadataDomFragment(doc, objectNode, dbMetadata, catalogPattern, schemaPattern, objectname, "index", DBObjectsEnum.INDEXES);
    
    					root.appendChild(objectNode);
    				}
    			} catch (Exception e) {
    			    log.error(e.getMessage());
    				// e.printStackTrace();
    			} catch (Error e) {
    			    log.error(e.getMessage());
    			}
			}

            if (!ExporterOptionsEnum.OPT_NOPROCS.getOption().isDefined()) {
    			// Get PROCEDURES
    			try {
    				log.info("Retreiving stored procedures...");
    				rsObject = dbMetadata.getProcedures(catalogPattern, schemaPattern, null);
    				rsMetadata = rsObject.getMetaData();
    				while (rsObject.next()) {
    					objectname = rsObject.getString("PROCEDURE_NAME");
    					objectNode = getObjectNode(doc, rsMetadata, rsObject, "procedure");
    					getMetadataDomFragment(doc, objectNode, dbMetadata, catalogPattern, schemaPattern, objectname, "column", DBObjectsEnum.PROCEDURE_COLUMNS);
    
    					root.appendChild(objectNode);
    				}
    			} catch (Exception e) {
    			    log.error(e.getMessage());
                } catch (Error e) {
                    log.error(e.getMessage());
                }
            }


			// Get FUNCTIONS
            if (!ExporterOptionsEnum.OPT_NOFUNCS.getOption().isDefined()) {
    			try {
    				log.info("Retreiving functions...");
    				rsObject = dbMetadata.getFunctions(catalogPattern, schemaPattern, null);
    				rsMetadata = rsObject.getMetaData();
    				while (rsObject.next()) {
    					objectname = rsObject.getString("FUNCTION_NAME");
    					objectNode = getObjectNode(doc, rsMetadata, rsObject, "function");
    					getMetadataDomFragment(doc, objectNode, dbMetadata, catalogPattern, schemaPattern, objectname, "column", DBObjectsEnum.FUNCTION_COLUMNS);
    
    					root.appendChild(objectNode);
    				}
    			} catch (Exception e) {
    			    log.error(e.getMessage());
    				log.exception(e);
                } catch (Error e) {
                    log.error(e.getMessage());
                    log.exception(e);
                }
            }

            if (!ExporterOptionsEnum.OPT_NOUDFS.getOption().isDefined()) {
    			// Get UDTs
    			try {
    				log.info("Retreiving UDTs...");
    				rsObject = dbMetadata.getUDTs(catalogPattern, schemaPattern, null, null);
    				rsMetadata = rsObject.getMetaData();
    				while (rsObject.next()) {
    					objectname = rsObject.getString("TYPE_NAME");
    					objectNode = getObjectNode(doc, rsMetadata, rsObject, "function");
    
    					root.appendChild(objectNode);
    				}
    			} catch (Exception e) {
    				log.error(e.getMessage());
                } catch (Error e) {
                    log.error(e.getMessage());
                }
            }


		} catch (Exception e) {
		    log.error(e.getMessage());
			e.printStackTrace();
        } catch (Error e) {
            log.error(e.getMessage());
        }


		return doc;

	}

}
