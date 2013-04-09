#!/bin/sh

DBTAG=$1
if [ -z "$DBTAG" ]; then echo " 
Syntax: $0 <db id>

Database connection parameters will be located in configuration file
where DB element has an ID of <db id> 
"
   exit
fi

JDBC_LIBS=lib
CLASSPATH=$CLASSPATH:$JDBC_LIBS

java  -cp SchemaGraph.jar gr.infocode.schemagraph.exporter.Exporter -c config.xml -d $DBTAG  -o out/${DBTAG}.xml -l out/${DBTAG}.log
java  -cp SchemaGraph.jar gr.infocode.schemagraph.grapher.Grapher -s out/${DBTAG}.xml -o out

dot -o out/${DBTAG}_detailed.png -Tpng out/${DBTAG}_detailed.dot
dot -o out/${DBTAG}_er.png -Tpng out/${DBTAG}_er.dot
dot -o out/${DBTAG}_plain.png -Tpng out/${DBTAG}_plain.dot








