<%-- 
    Document   : varukorg-full
    Created on : 2014-nov-30, 09:47:46
    Author     : Ulf
--%>
<%@page import="se.saljex.hemsida.Language"%>
<%@page import="se.saljex.hemsida.VeckodagarSet"%>
<%@page import="se.saljex.hemsida.StartupData"%>
<%@page import="se.saljex.hemsida.LagerEnhet"%>
<%@page import="java.sql.SQLException"%>
<%@page import="se.saljex.hemsida.SQLHandler"%>
<%@page import="se.saljex.hemsida.Kund"%>
<%@page import="se.saljex.sxlibrary.SXUtil"%>
<%@page import="se.saljex.hemsida.LagerSaldo"%>
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
    boolean inkMoms=Const.getSessionData(request).isInkMoms(request);
        boolean isBruttopris = Const.getSessionData(request).isUserInloggad() ? Const.getSessionData(request).isVisaBruttopris(request) : false;

    VeckodagarSet turbilDagar = SQLHandler.getTurbilsdagar(Const.getConnection(request), sd.getInloggadKundnr(), sd.getLagerNr());
    String fraktsatt = sd.getFraktsatt(request, turbilDagar!=null && turbilDagar.isAnyDay() );
    
    
 //   Varukorg vk = sd.getVarukorg(Const.getConnection(request));
    VarukorgFormHandler vkf = (VarukorgFormHandler)request.getAttribute(VarukorgFormHandler.PARAMETER_NAME);
    long rowCn = Const.getInitData(request).getNewUniktID();
    String formName="vk-form-" + rowCn;
    rowCn = Const.getInitData(request).getNewUniktID();

    Double orderSumma = 0.0;
    Kund kund = null;
    if (sd.getInloggadUser()!=null) {
        try {
            kund = SQLHandler.getKund(Const.getConnection(request),sd.getInloggadKundnr());
        }catch (SQLException e) { e.printStackTrace(); }
    }
    
  LagerEnhet le = sd.getLager();
    
%>
<% VarukorgProdukt vkProdukt; %>
<h1><%= lang.Varukorg() %></h1>
<form method="post" id="<%= formName %>" action="?">
    <div class="vkf-header-row">
        <div class="vkf-img"></div>
        <div class="vkf-artnr"><%= lang.Artikel() %></div>
        <div class="vkf-variant"></div>
        <div class="vkf-change"><%= lang.Antal() %></div>
        <div class="vkf-enh"><%= lang.Enhet() %></div>        
        <div class="vkf-pris"><%= lang.Pris() %></div>
        <div class="vkf-pris"><%= lang.Totalt() %></div>
        <div class="vkf-lagerstatus"><%= lang.Lager() %></div>

    </div>
    <% int vkrcn = -1; %>
    <% for (int cn = vkf.getRows().size()-1; cn >= 0; cn--) { %>
        <% vkProdukt = vkf.getRows().get(cn); %>
                    <div class="vkf-row">
                        <div class="vk-img vkf-img">
                            
                            <img src="<%= Const.getArtBildURL(vkProdukt.getVarukorgArtiklar().get(0).getArtnr()) %>">
                        </div>
                        <div class="vkf-col">
                            <div class="vkf-kl">
                                <div class="vkf-kl-namn"><%= Const.toHtml(vkProdukt.getProdukt().getRubrik()) %></div>

                            </div>
                                <% for (VarukorgArtikel a : vkProdukt.getVarukorgArtiklar()) { %>
                                    <% vkrcn++; %>
                                <div class="vkf-art">
                                    <div class="vkf-artnr"><%= Const.toHtml(a.getArt().getArtnr()) %></div>
                                    <div class="vkf-variant"><%= Const.toHtml(a.getArt().getKatNamn()) %></div>
                                    <div class="vkf-change">
                                        <input size="4" name="<%= VarukorgFormHandler.FORMNAME_ANTAL + vkrcn %>" id="vkf-antal-<%= vkrcn %>" value="<%= a.getHtmlFormErrorMessage()==null ? a.getAntal() : a.getHtmlFormValue() %>"/>
                                        <%= Const.toHtml(a.getHtmlFormErrorMessage()) %>
                                    </div>
                                        <div class="vkf-enh">
                                            <%= a.getArt().getAntalSaljpack().equals(1.0) ? a.getFormatEnhet() : "x " + Const.getAnpassade2Decimaler(a.getArt().getAntalSaljpack()) + a.getFormatEnhet() %> 
                                        </div>
                                        <div class="vkf-pris"><%= Const.getAnpassatPrisFormat(a.getArt().getNettoprisVidAntalSaljpack(a.getAntal(), inkMoms, isBruttopris)) %></div>
                                        <div class="vkf-pris"><%= Const.getAnpassatPrisFormat(a.getArt().getNettoprisVidAntalSaljpack(a.getAntal(), inkMoms, isBruttopris) * a.getAntal() * a.getArt().getAntalSaljpack()) %></div>
                                        <% LagerSaldo ls = a.getSQLLookupLagerSaldo(Const.getConnection(request), sd.getLagerNr()); %>
                                        <div class="vkf-lagerstatus"><%= ls.getTillgangliga() >= a.getAntal()*a.getArt().getAntalSaljpack() ? "OK" : "Saknas " + SXUtil.getFormatNumber(Math.floor((a.getAntal()*a.getArt().getAntalSaljpack() - ls.getTillgangliga())/a.getArt().getAntalSaljpackForDivision()),0)  %></div>
                                        <input type="hidden" name="vkf-aid-<%= vkrcn %>" value="<%= a.getArtnr() %>">
                                        <input type="hidden" name="vkf-kid-<%= vkrcn %>" value="<%= vkProdukt.getProdukt().getKlasid() %>">
                                </div>
                                <% if (a.getArt().getLevVillkor() > 0 && kund!=null && !kund.getMottagarfrakt()) { 
                                     if (a.getArt().getLevVillkor() == 1) {// 1=skrymme som kan g� fritt turbil 
                                     if(!Const.FRAKTSATT_TURBIL.equals(fraktsatt) && !Const.FRAKTSATT_HAMT.equals(fraktsatt) ) { // distrikt 0 �r ospecat, t.ex. h�mt, distrikt 1 = turbil 
                                %>
                                            <div class="vkf-notbeskrivning"><%= lang.SkrymmefraktTillkommerPaDennaArtikel() %></div>
                                      <% 
                                      }
                                     } else if (a.getArt().getLevVillkor() == 2) { // 2=fritt tillverkarens lager %>
                                            <div class="vkf-notbeskrivning"><%= lang.SarskildFraktkostnadTillkommerPaDennaArtikel() %> </div>
                                    <% } 
                                 }  
                                 orderSumma += a.getArt().getNettoprisVidAntalSaljpack(a.getAntal(), inkMoms, isBruttopris) * a.getAntal() * a.getArt().getAntalSaljpack(); 
                             } 
                                    %>         
                        </div>
                          
                    </div>
    <% } %>
    
    <div class="vkf-summa-row">
        <div class="vkf-summa-text"><%= lang.Ordertotal() %></div>
        <div class="vkf-summa-belopp"><%= SXUtil.getFormatNumber(orderSumma) %></div>
    </div>

    <% if (kund!= null && kund.isKreditgransUppnadd()) { %>
        <% if(kund.getReskontraForfall30() + kund.getReskontraKreditEjForfallen30() > 0.0) { %> 
            <div class="kreditvarning"><%= lang.ObserveraAttDuHar() %> <%= SXUtil.getFormatNumber(kund.getReskontraForfall30()) %> <%= lang.ILangtidsforfallnaFakturorSomMasteBetalasInnanLeveransKanSke() %></div> 
        <% } else { %>
            <div class="kreditvarning"><%= lang.ObserveraAttDinKreditgrans() %><%= SXUtil.getFormatNumber(kund.getKgrans()) %> <%= lang.KrArOverskridenBetalningMasteSkeInnanLeverans() %></div> 
        <% } %>
    <% } %>
    <div class="momsinfo">

    <% if (inkMoms) { %>
        <%= lang.PriserInklusiveMoms() %>
    <% } else { %>
        <%= lang.PriserExklusiveMoms() %>
    <% } %>
    </div>
    
    <table>
        <% se.saljex.hemsida.User u = Const.getSessionData(request).getInloggadUser();  %>
        <tr><td><%= lang.ValtLager() %></td><td>
                <select name="<%= VarukorgFormHandler.FORMNAME_LAGERNR %>" id="lagerselector1" onchange="setLager(document.getElementById('lagerselector1'))">
                    <option value="<%= le.getLagernr() %>"><%= Const.toHtml(le.getNamn()) %></option>
                    <% 
                    StartupData sData = Const.getStartupData();
                    java.util.Map<Integer, LagerEnhet> ll = sData.getLagerEnhetList();
                    for (java.util.Map.Entry<Integer, LagerEnhet> lle : ll.entrySet()) {
                        if (!lle.getKey().equals(le.getLagernr())) {
                    %><option value="<%= lle.getKey()%>"><%= Const.toHtml(lle.getValue().getNamn()) %></option>
                        <% }

                    }
                    %>
                </select>   


        </td></tr>

        <tr><td><%= lang.Transportsatt() %></td><td>                
                <select name="<%= VarukorgFormHandler.FORMNAME_FRAKTSATT %>" id="transportselector1" onchange="setTransportsatt(document.getElementById('transportselector1'))">
                    <% if (turbilDagar!=null && turbilDagar.isAnyDay()) { %>
                        <option value="<%= Const.FRAKTSATT_TURBIL %>">
                            <%= lang.Turbil() %> <%= (turbilDagar.isMandag() ?  lang.Mandagar()+" " : "") + (turbilDagar.isTisdag()? lang.Tisdagar()+" " : "") + (turbilDagar.isOnsdag()? lang.Onsdagar()+" " : "") + (turbilDagar.isTorsdag()? lang.Torsdagar()+" " : "") + (turbilDagar.isFredag()? lang.Fredagar()+" " : "") %>
                        </option>
                    <% } %>
                    <option value="<%= Const.FRAKTSATT_HAMT %>" <%= Const.FRAKTSATT_HAMT.equals(fraktsatt) ? " selected=\"selected\"" : "" %>>H�mtas</option>
                    <option  value="<%= Const.FRAKTSATT_SKICKA %>"<%= Const.FRAKTSATT_SKICKA.equals(fraktsatt) ? " selected=\"selected\"" : "" %>><%= lang.SkickasMedLampligSpeditor() %></option>
                </select>
        </td></tr>
        <tr><td><%= lang.Kundnummer() %></td><td><input name="<%= VarukorgFormHandler.FORMNAME_KUNDNR %>" value="<%= Const.toHtml(vkf.getKundnr()) %>" <%= u==null ? "" : "disabled" %>></td></tr>
        <tr><td><%= lang.Foretag() %></td><td><input name="<%= VarukorgFormHandler.FORMNAME_FORETAG %>" value="<%= Const.toHtml(vkf.getForetag()) %>" <%= u==null ? "" : "disabled" %>></td></tr>
        <tr><td><%= lang.Kontaktperson() %></td><td><input name="<%= VarukorgFormHandler.FORMNAME_KONTAKTPERSON %>" value="<%= Const.toHtml(vkf.getKontaktperson()) %>"></td></tr>
        <tr><td><%= lang.EPost() %></td><td><input name="<%= VarukorgFormHandler.FORMNAME_EPOST %>" value="<%= Const.toHtml(vkf.getEpost()) %>"><%= Const.toHtml(vkf.getEpostErrorMsg()) %></td></tr>
        <%if (u==null) { %>
            <tr><td><%= lang.Adress() %></td><td><input name="<%= VarukorgFormHandler.FORMNAME_ADRESS %>" value="<%= Const.toHtml(vkf.getAdress()) %>"></td></tr>
            <tr><td><%= lang.Postnr() %></td><td><input name="<%= VarukorgFormHandler.FORMNAME_POSTNR %>" value="<%= Const.toHtml(vkf.getPostnr()) %>"></td></tr>
            <tr><td><%= lang.Ort() %></td><td><input name="<%= VarukorgFormHandler.FORMNAME_ORT %>" value="<%= Const.toHtml(vkf.getOrt()) %>"></td></tr>
            <tr><td><%= lang.Telefon() %></td><td><input name="<%= VarukorgFormHandler.FORMNAME_TEL %>" value="<%= Const.toHtml(vkf.getTel()) %>"></td></tr>
            <tr><td><%= lang.Organisationsnummer() %></td><td><input name="<%= VarukorgFormHandler.FORMNAME_ORGNR %>" value="<%= Const.toHtml(vkf.getOrgnr()) %>"></td></tr>
        <% } %>
        <tr><td><%= lang.Godsmarke() %></td><td><input name="<%= VarukorgFormHandler.FORMNAME_MARKE %>" value="<%= Const.toHtml(vkf.getMarke()) %>"></td></tr>
        <tr><td><%= lang.Meddelande() %></td><td><textarea name="<%= VarukorgFormHandler.FORMNAME_MEDDELANDE %>" ><%= Const.toHtml(vkf.getMeddelande()) %></textarea></td></tr>
        
        
    </table>
    <input type="hidden" name="g" value="c">
    <input type="hidden" name="<%= Const.PARAM_VARUKORG_AC %>" value="<%= Const.PARAM_VARUKORG_AC_SAVEORDER %>">
    <% String recaptchaSiteKey = null; %>
    <% if (u==null) { %>
        <% recaptchaSiteKey = Const.getStartupData().getReCaptchaSiteKey();
            if (recaptchaSiteKey!=null) { %>
            <script>
                var recaptchaonload = function() {
                    grecaptcha.render('g-recaptcha', {
                        'sitekey' : '<%= recaptchaSiteKey %>'
                    });
                };
            </script>
                <script src='https://www.google.com/recaptcha/api.js?onload=recaptchaonload&render=explicit'></script>
                <div>
                <div id="g-recaptcha" data-sitekey="<%= recaptchaSiteKey %>"></div>
                <%= Const.toHtml(vkf.getRecaptchaErrorMsg()) %>
                </div>
         <% } %>
     <% } %>
     <input type="button" value="<%= lang.SkickaOrder() %>" onclick="this.disabled=true; AJAXPost('<%= formName %>');<%= recaptchaSiteKey!=null ? "recaptchaonload();" :"" %>   " onmousedown="this.value='dddd'; this.value='<%= lang.BearbetarOrder() %>';">
     <a href="/varukorg?<%= Const.PARAM_VARUKORG_AC %>=<%= Const.PARAM_VARUKORG_AC_EMPTY %>"> <%= lang.TomVarukorg()%> </a>
    
</form>
    
     <%
     if (u==null) {
        ReCaptcha.check(Const.getStartupData().getReCaptchaSecretKey(), request.getParameter("g-recaptcha-response"));
     }
   %>