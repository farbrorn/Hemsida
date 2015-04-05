<%-- 
    Document   : rebuild-sort-index
    Created on : 2015-apr-05, 20:19:46
    Author     : Ulf
--%>

<%@page import="se.saljex.hemsida.Const"%>
<%@page import="java.sql.Connection"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
   Connection con = Const.getConnection(request);

   String q = "create temp table t on commit drop as ( "
    +" select akl.klasid as klasid, count(*) as cnt from artklaselank akl "
    +" join faktura2 f2 on f2.artnr=akl.artnr "
    +" join faktura1 f1 on f1.faktnr=f2.faktnr "
    +" where  f1.datum > current_date-300 and f2.lev>0  "
    +" group by akl.klasid); "
    +" update artklase ak set autoantalorderrader = coalesce ( ( select cnt from t where t.klasid=ak.klasid ),0); "
    +" create temp table t2 on commit drop as 	( select max(autoantalorderrader) as mx from artklase); "
    +" update artklase set autosortvikt = 5000/(select max(mx) from t2)*autoantalorderrader; ";
   
   
   
   con.createStatement().executeUpdate(q);
   
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Rebuild sort index</title>
    </head>
    <body>
        <h1>ebuild sort index</h1>
    </body>
</html>
