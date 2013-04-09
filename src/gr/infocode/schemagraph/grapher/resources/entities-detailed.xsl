<?xml version="1.0" encoding="UTF-8" ?>
<!--
#
# detailed schema renderer
#
# (C) Copyright 2007-2013, George M. Zouganelis (george.zouganelis@gmail.com)
#
#
# 1. xml tr entities-detailed.xsl schema.xml > detailed.dot
# 2. dot -o l.png -Tpng detailed.dot
-->

<!DOCTYPE xsl:stylesheet [
<!ENTITY copy   "&#169;">
<!ENTITY nbsp   "&#160;">
<!ENTITY laquo  "&#171;">
<!ENTITY raquo  "&#187;">
<!ENTITY sect   "&#167;">
]>

<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xi="http://www.w3.org/2001/XInclude"
                exclude-result-prefixes="xi"
                >

<xsl:output method="xml"
            version="1.0"
            encoding="UTF-8"
            omit-xml-declaration="yes"
            standalone="no"
            indent="no"
/>


<xsl:template match="/xmldbschema">
<![CDATA[ ]]>
digraph dbschema {
 dpi=300; antialias=on;
 node [shape=plaintext fontsize=10 fontname=arial charset=utf8];
 graph [
    bgcolor="#ffffff"
	label="" 
 ];
 <!-- get tables -->
 <xsl:for-each select="table[@table_type='TABLE']">
   "<xsl:value-of select="@table_name"/>" [label=<xsl:text disable-output-escaping="yes">&lt;</xsl:text>
   <table border="1" cellborder="0" cellspacing="0">
   <tr><td colspan="4" border="0" bgcolor="#f0f0f0"><xsl:value-of select="@table_name"/></td></tr>
   <xsl:for-each select="column">
     <tr>
	   <td align="right">
	     <xsl:variable name="col" select="@column_name"/>
		 <xsl:variable name="fkcount" select="count(parent::node()/fkey[@fkcolumn_name=$col])"/>
		 <xsl:variable name="pkcount" select="count(parent::node()/pkey[@column_name=$col])"/>
         <table border="0" cellborder="0" cellspacing="0">
	       <tr>
			  <td>
			    <xsl:choose>
				   <xsl:when test="$fkcount &gt; 0"><img src="icons/fk.png"/></xsl:when>
				   <xsl:otherwise><img src="icons/spacer.png"/></xsl:otherwise>
				</xsl:choose>
			  </td>
			  <td>
			    <xsl:choose>
				   <xsl:when test="$pkcount &gt; 0"><img src="icons/pk.png"/></xsl:when>
				   <xsl:otherwise><img src="icons/spacer.png"/></xsl:otherwise>
				</xsl:choose>
			  </td>
		   </tr>
		 </table>
	   </td>
	   <td align="left" port="{@column_name}"><xsl:value-of select="@column_name"/></td>
	   <td align="left">
	      <xsl:value-of select="@type_name"/>
          <xsl:choose>
		    <xsl:when test="@char_octet_length!=0">(<xsl:value-of select="@char_octet_length"/>)</xsl:when>
			<xsl:otherwise></xsl:otherwise>
		  </xsl:choose>
	   </td>
	   <td align="left">
	     <xsl:choose>
		    <xsl:when test="@nullable=0">NN</xsl:when>
			<xsl:otherwise><img src="icons/spacer.png"/></xsl:otherwise>
		 </xsl:choose>
	   </td>
	 </tr>
   </xsl:for-each>
   
   </table>
    <xsl:text disable-output-escaping="yes">&gt;</xsl:text>];
 </xsl:for-each>

 <!-- get foreign key links -->
 <xsl:for-each select="table[@table_type='TABLE']/fkey">
   "<xsl:value-of select="@fktable_name"/>":"<xsl:value-of select="@fkcolumn_name"/>" <xsl:text disable-output-escaping="yes">-&gt; </xsl:text>"<xsl:value-of select="@pktable_name"/>":"<xsl:value-of select="@pkcolumn_name"/>";
 </xsl:for-each>
 
 

}
</xsl:template>


</xsl:stylesheet>