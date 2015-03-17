<%-- 
    Document   : varukorg-full
    Created on : 2014-nov-30, 09:47:46
    Author     : Ulf
--%>
<%@page import="se.saljex.hemsida.VarukorgFormHandler"%>
<%@page import="se.saljex.hemsida.ReCaptcha"%>
<%@page import="se.saljex.hemsida.VarukorgArtikel"%>
<%@page import="se.saljex.hemsida.VarukorgProdukt"%>
<%@page import="se.saljex.hemsida.VarukorgRow"%>
<%@page import="se.saljex.hemsida.Varukorg"%>
<%@page import="se.saljex.hemsida.Const"%>
<%@page import="se.saljex.hemsida.SessionData"%>
<%
    SessionData sd = Const.getSessionData(request);
    boolean inkMoms = sd.isInkMoms();
//    Varukorg vk = sd.getVarukorg(Const.getConnection(request));
    VarukorgFormHandler vkf = (VarukorgFormHandler)request.getAttribute(VarukorgFormHandler.PARAMETER_NAME);
    
%>

<h1>Order fr�n webbutiken</h1>
<% if (Const.getStartupData().isHemsidaTestlage()) { %>
<h1>Detta �r bara en test!! Ingen skarp order!</h1>
<% } %>

    <% se.saljex.hemsida.User u = Const.getSessionData(request).getInloggadUser();  %>
        <%if (u==null) { %>
            <div style="color: red">OBS! Kunden var inte inloggad d� order lades. Kontrollera att det �r en riktig order! * Kan vara bluff! *</div>
        <% } %>
        
    <table>
    
        <tr><td>Kundnummer</td><td><%= Const.toHtml(vkf.getKundnr()) %></td></tr>
        <tr><td>F�retag</td><td><%= Const.toHtml(vkf.getForetag()) %></td></tr>
        <tr><td>Kontaktperson</td><td><%= Const.toHtml(vkf.getKontaktperson()) %></td></tr>
        <tr><td>E-Post</td><td><%= Const.toHtml(vkf.getEpost()) %></td></tr>
        <tr><td>Adress</td><td><%= Const.toHtml(vkf.getAdress()) %></td></tr>
        <tr><td>Postnr</td><td><%= Const.toHtml(vkf.getPostnr()) %></td></tr>
        <tr><td>Ort</td><td><%= Const.toHtml(vkf.getOrt()) %></td></tr>
        <tr><td>Telefon</td><td><%= Const.toHtml(vkf.getTel()) %></td></tr>
        <tr><td>Organisationsnummer</td><td><%= Const.toHtml(vkf.getOrgnr()) %></td></tr>

        <tr><td>Godsm�rke</td><td><%= Const.toHtml(vkf.getMarke()) %></td></tr>
        <tr><td>Meddelande</td><td><%= Const.toHtml(vkf.getMeddelande()) %></td></tr>
    </table>




<% //VarukorgProdukt vkProdukt; %>
<% if (inkMoms) { %>
    Priser inklusive moms.
<% } else { %>
    Priser exklusive moms.
<% } %>

    <table>
        <tr><th></th><th>Art.nr.</th><th>Ben�mning</th><th>Antal</th><th>Enhet</th><th>Nettopris</th><th>Totalpris</th></tr>
    <%// for (int cn = vkf.getRows().size()-1; cn >= 0; cn--) 
           for (VarukorgProdukt vkProdukt : vkf.getRows()) { %>
        <% //vkProdukt = vk.getVarukorgProdukter().get(cn); %>
        <tr>
            <td rowspan="<%= vkProdukt.getVarukorgArtiklar().size()+1 %>"><img src="<%= Const.getArtBildURL(vkProdukt.getVarukorgArtiklar().get(0).getArtnr()) %>"></td>
            <td colspan="5" style="font-weight: bold"><%= Const.toHtml(vkProdukt.getProdukt().getRubrik()) %></td>
        </tr>
        <% for (VarukorgArtikel a : vkProdukt.getVarukorgArtiklar()) { %>
            <tr>
                <td><%= Const.toHtml(a.getArtnr()) %></td>
                <td><%= Const.toHtml(a.getArt().getNamn()) %></td>
                <td><%= a.getAntal()*a.getArt().getAntalSaljpack() %></td>
                <td><%= Const.toHtml(a.getArt().getEnhet()) %></td>
                <td><%= Const.getFormatNumber(a.getArt().getNettoprisVidAntalSaljpack(a.getAntal(), inkMoms),2) %></td>
                <td><%= Const.getFormatNumber(a.getArt().getNettoprisVidAntalSaljpack(a.getAntal(), inkMoms) * a.getAntal() * a.getArt().getAntalSaljpack(),2) %></td>
            </tr>
       <% } %>
                
       <% } %>
    </table>
    
    
