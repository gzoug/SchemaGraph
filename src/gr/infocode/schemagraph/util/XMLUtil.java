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

import java.io.FileOutputStream;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.InputStream;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class XMLUtil {
    private static Logger logger;
    static {
        logger = Logger.getInstance();
    }
    
    public static boolean writeDocument(Document d, File f) {
        try {
            
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
    
            StreamResult result = new StreamResult(new FileOutputStream(f));
            
            DOMSource source = new DOMSource(d);
            transformer.transform(source, result);
            
            return true;
        } catch (TransformerConfigurationException tce) {
            logger.error("Cannot write document " + tce.getMessage());
        } catch (TransformerException te) {
            logger.error("Cannot write document " + te.getMessage());
        } catch (IOException ioe) {
            logger.error("Cannot create file " + ioe.getMessage());
        }
        
        return false;
    }
    
    /**
     * Parse an XML Document from a specified InputStream
     */
    public static Document parseDocument(InputStream is) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document d = db.parse(is);
            return d;
        } catch (SAXException saxe) {
            logger.error("cannot parse document - " + saxe.toString());
        } catch (IOException ioe) {
            logger.error("cannot read data - " + ioe.toString());
        } catch (ParserConfigurationException pce) {
            logger.error("cannot configure DOM parser - " + pce.toString());
        }
        return null;
    }
    
    public static Document newDocument() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            return db.newDocument();
        } catch (ParserConfigurationException pce) {
            logger.error("Cannot create new Document (DOM) " + pce.toString());   
            return null;
        }
    }

    public static String getDocumentAsString(Document d) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(d);
            transformer.transform(source,result );
            return sw.toString();
            
        } catch (TransformerConfigurationException tce) {
            logger.error("cannot write document " + tce.getMessage());
        } catch (TransformerException te) {
            logger.error("cannot write document " + te.getMessage());
        }
        return null;
    }    
    
    // ====================
    // Transformers
    // ====================
    private static void doTransform(InputStream xml, InputStream xsl, OutputStream out){
        try {            
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer(new StreamSource(xsl));
            transformer.transform(new StreamSource(xml), new StreamResult(out));
        } catch (Exception e) {
            logger.exception(e);
        }
    }

    
    // method overides
    public static void transformDocument(InputStream xml, InputStream xsl, OutputStream out){
        try {
            doTransform(xml, xsl, out);
        } catch (Exception e) {
            logger.exception(e);
        }           
    }

    /** 
     * Returns a NodeList using XPath Queries 
     */
    private static NodeList executeQuery(Document doc, String xpath) {
       try {
         XPathFactory factory = XPathFactory.newInstance();
         XPathExpression expr = factory.newXPath().compile(xpath);  
         Object result = expr.evaluate(doc, XPathConstants.NODESET);

         return  (NodeList) result;
       } catch (XPathExpressionException e ){
          logger.error("XPATH error " + e.getMessage());
          return null;
       }
    }

    /**
     * Select a node text value using XQuery
     */
    public static String selectNodeValue(Document doc, String xpath) {
        NodeList n = executeQuery(doc, xpath);    
        if (n != null) {
            if ( n.getLength() != 0 ) {  
              return n.item(0).getTextContent();
            }       
        }
        return null;
    }

    /**
     * Select the text value of multiple nodes using XQuery
     */
    public static ArrayList<String> SelectNodeValues(Document doc, String xpath){
        ArrayList<String> v = new ArrayList<String>();
        NodeList n = executeQuery(doc, xpath);
        if (n != null) {
            int len = n.getLength(); 
            for (int i = 0; i < len; i++){
               v.add(n.item(i).getTextContent());
            }        
        }
        return v;
    }
}
