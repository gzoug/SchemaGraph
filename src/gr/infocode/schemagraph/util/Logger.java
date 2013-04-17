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

import java.io.File;
import java.io.PrintWriter;
import java.util.Date;

public class Logger {
    private final static Logger logger;  
    private String logfilename = null;
    private PrintWriter logfile = null; 
    
    static {
        logger = new Logger();
    }
    
    // declare the default constructor as private
    // force access through getInstance() method
    private Logger() { }
    
    
    public static Logger getInstance() {
        return logger;
    }

    private void log(String message) {
        Date d = new Date();
        String logentry = d.toString() + " " + message + "\n";
        if (logfile!=null) {
            logfile.write(logentry);
            logfile.flush();
        } else {
            System.err.println(message);           
        }
    }
    
    public void warn(String message){
    	log("WARNING: " + message);
    }
    
    public void info(String message){
    	log("INFO: " + message);
    }
    
    public void error(String message){
    	log("ERROR: " + message);
    }

    public void exception(Exception e){
        log("EXCEPTION: " + e.getMessage());
        if (e.getStackTrace()!=null){
            for (StackTraceElement ste : e.getStackTrace()){
                log("EXCEPTION:   " + ste.toString());
            }
        }
    }
    
    public void exception(Error e){
        log("EXCEPTION: " + e.getMessage());
        if (e.getStackTrace()!=null){
            for (StackTraceElement ste : e.getStackTrace()){
                log("EXCEPTION:   " + ste.toString());
            }
        }
    }
    
    
    public void exit(String message) {
        log("EXIT: " + message);
        System.err.println(message);
        System.exit(1);
    }
    

    public String getLogfilename() {
        return logfilename;
    }


    public void setLogfilename(String logfilename) {
        File f;
        if (logfile!=null) {
            try {
              logfile.close();
            } catch (Exception e) {
                System.err.format("Couldn't close previously opened logfile [%s]\n",logfilename);
                return;
            }
        }
        try {
            f = new File(logfilename);
            logfile = new PrintWriter(f);
        } catch (Exception e) {
            System.err.format("Couldn't create logfile [%s]\n",logfilename);                
        }      
        this.logfilename = logfilename;        
    }

    
   
   
}
