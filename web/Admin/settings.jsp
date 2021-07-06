<%-- 
    Document   : settings
    Created on : 2020-dec-14, 17:41:46
    Author     : ulf
--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="se.saljex.hemsida.Const"%>
<%@page import="java.sql.Connection"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Inställningar</title>
    </head>
    <body>
        <h1>Inställningar</h1>
        <% 
        Connection con = Const.getConnection(request);
        ResultSet rs;
        String varde;
        PreparedStatement ps;
        %>

        <%
        if ("spara".equals(request.getParameter("ac"))) {
            ps=con.prepareStatement("insert into sxreg as s (id,varde) values (?,?) on conflict  (id) do update set varde=excluded.varde where excluded.id=s.id");
            
            ps.setString(1, "Hemsida-AutosparaOrder");
            ps.setString(2, "true".equals(request.getParameter("autosparaorder")) ? "true" : "false");
            try { ps.executeUpdate(); } catch (Exception e) { %>Fel spara Hemsida-AutosparaOrder <br><%= e.toString() %><%  }

            ps.setString(1, "Hemsida-AutosparaOrderStatus");
            ps.setString(2, "Avvakt".equals(request.getParameter("autosparaorderstatus")) ? "Avvakt" : "");
            try { ps.executeUpdate(); } catch (Exception e) { %>Fel spara Hemsida-AutosparaOrderStatus <br><%= e.toString() %><%  }

            ps.setString(1, "Hemsida-KatalogExcludeGrps");
            ps.setString(2, request.getParameter("katalogexcludegrps"));
            try { ps.executeUpdate(); } catch (Exception e) { %>Fel spara Hemsida-KatalogExcludeGrps<br> <%= e.toString() %><%  }

            ps.setString(1, "Hemsida-Momsmultiplikator");
            ps.setString(2, request.getParameter("momsmultiplikator"));
            try { ps.executeUpdate(); } catch (Exception e) { %>Fel vid spara Hemsida-Momsmultiplikator <br><%= e.toString() %><%  }

            ps.setString(1, "Hemsida-GogleAnalyticsID");
            ps.setString(2, request.getParameter("googleanalyticsid"));
            try { ps.executeUpdate(); } catch (Exception e) { %>Fel vid spara Hemsida-GogleAnalyticsID<br> <%= e.toString() %><%  }
        }
%>
        
        <%
        ps = con.prepareStatement("select varde from sxreg where id=?");
        %>
        <form method="post">
        <table>
            <tr>
                <% ps.setString(1, "Hemsida-AutosparaOrder"); rs = ps.executeQuery(); if (rs.next()) varde=rs.getString(1); else varde=""; %>
                <td>Spara order automatiskt</td>
                <td><input type="checkbox" name="autosparaorder" value="true" <%= "true".equals(varde) ? "checked" : "" %>></td>
            </tr>
            <tr>
                <% ps.setString(1, "Hemsida-AutosparaOrderStatus"); rs = ps.executeQuery(); if (rs.next()) varde=rs.getString(1); else varde=""; %>
                <td>Autosparade order sparas som avvaktande. Aktivera även autospara.</td>
                <td><input type="checkbox" name="autosparaorderstatus" value="Avvakt" <%= "Avvakt".equals(varde) ? "checked" : "" %>></td>
            </tr>
            <tr>
                <% ps.setString(1, "Hemsida-KatalogExcludeGrps"); rs = ps.executeQuery(); if (rs.next()) varde=rs.getString(1); else varde=""; %>
                <td>Exkludera katalogträdsgrupper (ang gruppnummer med , mellan t.ex. 1,2,3)</td>
                <td><input id="a1" type="text" name="katalogexcludegrps" value="<%= varde %>" disabled></td>
            </tr>
            <tr>

                <% ps.setString(1, "Hemsida-Momsmultiplikator"); rs = ps.executeQuery(); if (rs.next()) varde=rs.getString(1); else varde=""; %>
                <td>Momsmultiplikator (anges med . som decimal. T.ex 1.25)</td>
                <td><input id="a2" type="text" name="momsmultiplikator" value="<%= varde %>" disabled></td>
            </tr>
            <tr>

                <% ps.setString(1, "Hemsida-GogleAnalyticsID"); rs = ps.executeQuery(); if (rs.next()) varde=rs.getString(1); else varde=""; %>
                <td>Google Analytics ID</td>
                <td><input type="text" name="googleanalyticsid" value="<%= varde %>"></td>
            </tr>
        </table>
            <input type="hidden" name="ac" value="spara">
            <input type="submit" value="Spara">
    </form>
            <button onclick="document.getElementById('a1').disabled=false; document.getElementById('a2').disabled=false;">Aktivera avancerade fält</button>
            <div>OBS! För att aktivera ändringarna ska måste servern startas om eller inställningarna läsas om: <a href="reload-config.jsp">Reload Config</a></div>
            <div>Ändra inget om du inte är säker på vad du gör. Felaktiga värden gör att servern inte startas.</div>
    </body>
</html>
