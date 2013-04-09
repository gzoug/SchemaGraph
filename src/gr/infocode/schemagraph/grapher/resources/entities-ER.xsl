<?xml version="1.0" encoding="UTF-8"?>
<!--
#
# ER schema renderer
#
# (C) Copyright 2007-2013, George M. Zouganelis (george.zouganelis@gmail.com)
#
#
#
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

<xsl:output method="text"
            version="1.0"
            encoding="UTF-8"
            omit-xml-declaration="yes"
            standalone="no"
            indent="no"
/>


<xsl:template match="/xmldbschema">
<![CDATA[ ]]>
digraph G {
 dpi=300; antialias=on;

 <!-- get tables as nodes -->
 <xsl:for-each select="table[@table_type='TABLE']">
   <xsl:variable name="pkeys_are_fkeys">
   <xsl:for-each select="pkey">
     <xsl:variable name="pkname" select="@column_name"/>
     <xsl:variable name="infk" select="count(parent::node()/fkey[@fkcolumn_name=$pkname])"/>
	 <xsl:if test="$infk=0">0</xsl:if>
   </xsl:for-each>
   </xsl:variable>
    
   "<xsl:value-of select="@table_name"/>"
   <xsl:choose>
      <xsl:when test="$pkeys_are_fkeys=''"> [shape=diamond];
	  </xsl:when>
	  <xsl:otherwise> [shape=box];
	  </xsl:otherwise>
   </xsl:choose>
   
 </xsl:for-each>

 <!-- get foreign key links -->
 <xsl:for-each select="table[@table_type='TABLE']/fkey">
   "<xsl:value-of select="@fktable_name"/>"<![CDATA[ -> ]]>"<xsl:value-of select="@pktable_name"/>";
 </xsl:for-each>
 

}
</xsl:template>


</xsl:stylesheet>