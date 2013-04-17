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
import java.util.ArrayList;
import java.util.List;

public class Option implements IOption {
    private String shortOpt;
    private String longOpt;
    private boolean isMandatory;
    private Integer paramCount;
    private String paramString;
    private String helpString;

    private List<String> values = null;
    private boolean isDefined = false;

    
    
    /*
     * Setters and Getters
     */
    
    /* (non-Javadoc)
     * @see dbtool.schema.exporter.options.IOption#isDefined()
     */
    public boolean isDefined(){
        return this.isDefined;
    }
    /* (non-Javadoc)
     * @see dbtool.schema.exporter.options.IOption#setDefined(boolean)
     */
    public void setDefined(boolean isDefined){
        this.isDefined = isDefined;
    }

    /* (non-Javadoc)
     * @see dbtool.schema.exporter.options.IOption#isSet()
     */
    public boolean isSet(){
        return (values!=null);
    }
    
    
    //==
    
    /* (non-Javadoc)
     * @see dbtool.schema.exporter.options.IOption#getValues()
     */
    public List<String> getValues() {
        return values;
    }
    
    /* (non-Javadoc)
     * @see dbtool.schema.exporter.options.IOption#getValue(int)
     */
    public String getValue(int index){
        if (values==null) {
            return null;
        }
        if ( (index<0) || (index > values.size()) ) {
            return null;
        }
        return values.get(index);
    }
    
    /* (non-Javadoc)
     * @see dbtool.schema.exporter.options.IOption#setValues(java.util.List)
     */
    public void setValues(List<String> values) {
        this.values = values;
        this.isDefined = true;
    }

    /* (non-Javadoc)
     * @see dbtool.schema.exporter.options.IOption#addValue(java.lang.String)
     */
    public void addValue(String value){
        if (values==null) {
            values = new ArrayList<String>();
        }
        this.values.add(value);
        this.isDefined = true;
    }
    
    //==
    
    /* (non-Javadoc)
     * @see dbtool.schema.exporter.options.IOption#getShortOpt()
     */
    public String getShortOpt() {
        return shortOpt;
    }

    /* (non-Javadoc)
     * @see dbtool.schema.exporter.options.IOption#setShortOpt(java.lang.String)
     */
    public void setShortOpt(String shortOpt) {
        this.shortOpt = shortOpt;
    }

    //==
    
    /* (non-Javadoc)
     * @see dbtool.schema.exporter.options.IOption#getLongOpt()
     */
    public String getLongOpt() {
        return longOpt;
    }

    /* (non-Javadoc)
     * @see dbtool.schema.exporter.options.IOption#setLongOpt(java.lang.String)
     */
    public void setLongOpt(String longOpt) {
        this.longOpt = longOpt;
    }

    //==
    
    /* (non-Javadoc)
     * @see dbtool.schema.exporter.options.IOption#isMandatory()
     */
    public boolean isMandatory() {
        return isMandatory;
    }

    /* (non-Javadoc)
     * @see dbtool.schema.exporter.options.IOption#setMandatory(boolean)
     */
    public void setMandatory(boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    //==
    
    /* (non-Javadoc)
     * @see dbtool.schema.exporter.options.IOption#getParamCount()
     */
    public Integer getParamCount() {
        return paramCount;
    }

    /* (non-Javadoc)
     * @see dbtool.schema.exporter.options.IOption#setParamCount(java.lang.Integer)
     */
    public void setParamCount(Integer paramCount) {
        this.paramCount = paramCount;
    }

    //==
    
    /* (non-Javadoc)
     * @see dbtool.schema.exporter.options.IOption#getHelpString()
     */
    public String getHelpString() {
        return helpString;
    }

    /* (non-Javadoc)
     * @see dbtool.schema.exporter.options.IOption#setHelpString(java.lang.String)
     */
    public void setHelpString(String helpString) {
        this.helpString = helpString;
    }
    
    //==
    
    /* (non-Javadoc)
     * @see dbtool.schema.exporter.options.IOption#getParamString()
     */
    public String getParamString() {
        return paramString;
    }
    /* (non-Javadoc)
     * @see dbtool.schema.exporter.options.IOption#setParamString(java.lang.String)
     */
    public void setParamString(String paramString) {
        this.paramString = paramString;
    }
    
    
    /*
     * Constructor
     */
    public Option(String shortOpt, String longOpt, Integer paramCount, boolean isMandatory, String paramString, String helpString){
        this.shortOpt = shortOpt;
        this.longOpt = longOpt;
        this.paramCount = paramCount;
        this.isMandatory = isMandatory;
        this.paramString = paramString;
        this.helpString = helpString;
    }
    

    /* (non-Javadoc)
     * @see dbtool.schema.exporter.options.IOption#matches(java.lang.String)
     */
    public boolean matches(String option) {
        if (option != null) {
            return (option.compareTo("-" + this.shortOpt) == 0)
                    || (option.compareTo("--" + this.longOpt) == 0);
        } else {
            return false;
        }

    }

}
