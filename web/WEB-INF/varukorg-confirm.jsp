<%-- 
    Document   : varukorg-full
    Created on : 2014-nov-30, 09:47:46
    Author     : Ulf
--%>
<%@page import="se.saljex.hemsida.StartupData"%>
<%@page import="se.saljex.hemsida.Language"%>
<%@page import="se.saljex.hemsida.VarukorgFormHandler"%>
<%@page import="se.saljex.hemsida.ReCaptcha"%>
<%@page import="se.saljex.hemsida.VarukorgArtikel"%>
<%@page import="se.saljex.hemsida.VarukorgProdukt"%>
<%@page import="se.saljex.hemsida.VarukorgRow"%>
<%@page import="se.saljex.hemsida.Varukorg"%>
<%@page import="se.saljex.hemsida.Const"%>
<%@page import="se.saljex.hemsida.SessionData"%>
<% Language lang = StartupData.getLanguage(); %>
<%
    SessionData sd = Const.getSessionData(request);
//    Varukorg vk = sd.getVarukorg(Const.getConnection(request));
    VarukorgFormHandler vkf = (VarukorgFormHandler)request.getAttribute(VarukorgFormHandler.PARAMETER_NAME);
    boolean inkMoms=Const.getSessionData(request).isInkMoms(request);
        boolean isBruttopris = Const.getSessionData(request).isUserInloggad() ? Const.getSessionData(request).isVisaBruttopris(request) : false;
    
%>

<h1>Bekräftelse webborder</h1>

    <% se.saljex.hemsida.User u = Const.getSessionData(request).getInloggadUser();  %>
        <%if (u==null) { %>
            Tack för din order!
            Tänk på att fraktkostnader och expeditionskostnader tillkommer.
        <% } else { %>
            Tack för din order!
        <% } %>
    <table>
    
        <tr><td><%= lang.Kundnummer() %></td><td><%= Const.toHtml(vkf.getKundnr()) %></td></tr>
        <tr><td><%= lang.Foretag() %></td><td><%= Const.toHtml(vkf.getForetag()) %></td></tr>
        <tr><td><%= lang.Kontaktperson() %></td><td><%= Const.toHtml(vkf.getKontaktperson()) %></td></tr>
        <tr><td><%= lang.EPost() %></td><td><%= Const.toHtml(vkf.getEpost()) %></td></tr>
        <% if (!sd.isUserInloggad()) { %>
            <tr><td><%= lang.Adress() %></td><td><%= Const.toHtml(vkf.getAdress()) %></td></tr>
            <tr><td><%= lang.Postnr() %></td><td><%= Const.toHtml(vkf.getPostnr()) %></td></tr>
            <tr><td><%= lang.Ort() %></td><td><%= Const.toHtml(vkf.getOrt()) %></td></tr>
            <tr><td><%= lang.Telefon() %></td><td><%= Const.toHtml(vkf.getTel()) %></td></tr>
            <tr><td><%= lang.Organisationsnummer() %></td><td><%= Const.toHtml(vkf.getOrgnr()) %></td></tr>
        <% } %>
        <tr><td><%= lang.OrderRegistreradPaLager() %></td><td><%= Const.toHtml(Const.getStartupData().getLagerEnhetList().get(vkf.getLagernr()).getNamn()) %></td></tr>
        <tr><td><%= lang.ValtTransportsatt() %></td><td><%= Const.toHtml(sd.getFraktsattBeskrivning(vkf.getFraktsatt())) %></td></tr>

        <tr><td><%= lang.Godsmarke() %></td><td><%= Const.toHtml(vkf.getMarke()) %></td></tr>
        <tr><td><%= lang.Meddelande() %></td><td><%= Const.toHtml(vkf.getMarke()) %></td></tr>
    </table>



<% //VarukorgProdukt vkProdukt; %>
<div class="momsinfo">
    <%= inkMoms ? lang.PriserInklusiveMoms() : lang.PriserExklusiveMoms() %>
</div>
    <table>
        <tr><th></th><th><%= lang.Artnr() %></th><th><%= lang.Benamning() %></th><th><%= lang.Antal() %></th><th><%= lang.Enhet() %></th><th><%= lang.Totalt() %></th></tr>
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
                <td><%= a.getAntal() %></td>
                <td><%= Const.toHtml(Const.getSaljpackFormaeradEnhet(a.getArt())) %></td>
                <td><%= Const.getFormatNumber(a.getArt().getNettoprisVidAntalSaljpack(a.getAntal(),inkMoms,isBruttopris ) * a.getAntal() * a.getArt().getAntalSaljpack(),2) %></td>
            </tr>
       <% } %>
                
       <% } %>
    </table>
    
    
