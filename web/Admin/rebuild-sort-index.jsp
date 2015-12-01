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
           
           
    +" update artklase set autosortvikt = 5000/(select max(mx) from t2)*autoantalorderrader; "
           + "update artklase  aak set " 
    + " auto_sokord = ( "
	+" select upper(coalesce(ak.rubrik,'') ||' ' || coalesce(ak.text,'') || ' ' || string_agg(coalesce(a.nummer,'') || ' ' || coalesce(a.refnr,'') || ' ' || coalesce(a.rsk,'') || ' ' || coalesce(a.enummer,'') || ' ' || coalesce(a.bestnr,'') || ' ' || coalesce(a.katnamn,'') || ' ' || coalesce(a.namn,''), ' '))  "
	+" from artklase ak join artklaselank akl on akl.klasid=ak.klasid join artikel a on a.nummer=akl.artnr where ak.klasid = aak.klasid group by ak.klasid  "
	+" ), "
    +" auto_sokartnr = ( "
	+" select upper(string_agg('-' || a2.nummer || '-', ' '))  "
	+" from artklase ak2 join artklaselank akl2 on akl2.klasid=ak2.klasid join artikel a2 on a2.nummer=akl2.artnr where ak2.klasid = aak.klasid group by ak2.klasid ), "
    +" auto_sokrefnr = ( "
	+" select upper(string_agg('-' || coalesce(a3.refnr,'') || '- -' || coalesce(a3.rsk,'') || '- -' || coalesce(a3.enummer,'') || '- -' || coalesce(a3.bestnr,'') || '-', ' '))  "
	+" from artklase ak3 join artklaselank akl3 on akl3.klasid=ak3.klasid join artikel a3 on a3.nummer=akl3.artnr where ak3.klasid = aak.klasid group by ak3.klasid ), "
    +" auto_bildartnr = ( "
	+" select a4.nummer "
	+" from artklase ak4 join artklaselank akl4 on akl4.klasid=ak4.klasid join artikel a4 on a4.nummer=akl4.artnr where ak4.klasid = aak.klasid order by akl4.sortorder, akl4.artnr limit 1); "
           
// Bygg "Andra köpte samtidigt"
+" update artklase set auto_samkopta_klasar=null; "
+" create temp table t on commit drop as ( "
+" with f as ( "
+" select  "
+" f2.ordernr as ordernr, akl.klasid as klasid, count(f2.artnr) as antal "
+" from artklaselank akl  "
+" join faktura2 f2 on akl.artnr=f2.artnr and f2.ordernr<>0 and f2.lev>0 "
+" join faktura1 f1 on f1.faktnr=f2.faktnr and f1.datum > current_date-600 "
+" group by f2.ordernr, akl.klasid "
+" ), o as( "
+" select f.klasid as klasid, ordernr as ordernr from f"
+" ) "
 
+" select klasidf,  string_agg(klasido::varchar,',')::varchar as klasar from ( "
+" select f.klasid as klasidf, o.klasid as klasido, sum(f.antal) as antal, row_number() over (partition by f.klasid order by sum(f.antal) desc ) "
+" from f join o on o.ordernr=f.ordernr and f.klasid<>o.klasid "
+" group by f.klasid, o.klasid"
+" ) q"
+" where row_number <= 4"
+" group by klasidf"
+" order by klasidf"
+" );"

+" update artklase ak set auto_samkopta_klasar = ("
+" select klasar from t where t.klasidf = ak.klasid); ";
           

   
   
   
   con.createStatement().executeUpdate(q);

%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Rebuild  index</title>
    </head>
    <body>
        <h1>Rebuild  index</h1>
    </body>
</html>
