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

import java.util.HashMap;
import java.sql.*;
import java.io.Console;
import gr.infocode.schemagraph.util.Logger;
import gr.infocode.schemagraph.exporter.config.DBConfigEnum;
import gr.infocode.schemagraph.exporter.config.ExporterOptionsEnum;

public class DBConnection {
    private static Logger logger;
    static {
        logger = Logger.getInstance();
    }

    private HashMap<DBConfigEnum, String> data;
    private String _id = null;
    private Connection con = null;

    public String getId() {
        return _id;
    }

    public DBConnection(String id) {
        data = new HashMap<DBConfigEnum, String>();
        _id = id;
    }

    public Connection getConnection() {
        return this.con;
    }

    public String getID() {
        return this._id;
    }

    public void setConfigValue(DBConfigEnum key, String value) {
        data.put(key, value);
    }

    public String getConfigValue(DBConfigEnum key) {
        String s = data.get(key);
        if (s == null) {
            s = "";
        }
        return s;
    }

    public boolean Connect() {
        // Load the ODBC driver.
        try {
            Class.forName(data.get(DBConfigEnum.DRIVERCLASS));
        } catch (Exception e) {
            System.err.println("An error occur while loading the Driver : "
                    + e.getMessage());
            e.printStackTrace();
            return false;
        }

        // Establish the connection to the database.

        try {
            String username = null;
            String password = null;
            if (ExporterOptionsEnum.OPT_INTERACTIVE.getOption().isDefined()) {
                Console cons = System.console();
                if (cons == null) {
                    logger.exit("Can not open console for interactive login");
                }
                username = System.console().readLine("%s", "login:");
                char[] passwd = System.console()
                        .readPassword("%s", "password:");
                if (passwd != null) {
                    password = String.copyValueOf(passwd);
                }
            } else {
                username = data.get(DBConfigEnum.USERNAME);
                password = data.get(DBConfigEnum.PASSWORD);
            }

            con = DriverManager.getConnection(
                    data.get(DBConfigEnum.DRIVERCONNECTOR) + ":"
                            + data.get(DBConfigEnum.DATABASE), username,
                    password);

        } catch (Exception e) {
            System.out.println("Could not establish connection to dbURL: "
                    + e.getMessage());
            return false;
        }
        return true;

    }

}
