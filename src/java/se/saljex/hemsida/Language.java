/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.saljex.hemsida;

import java.util.Locale;

/**
 *
 * @author ulf
 */
public class Language {
    
    
private Locale locale = new Locale("sv","SE");
public String getLanguage() { return "sv"; }
public String getCountry() { return "se"; }
public Locale getLocale() {return locale; }
public String translateString(String s) {return s; }
    
    
public String TillKassan() { return "Till kassan"; }
public String LoggaIn() { return "Logga In"; }
public String Inloggad() { return "Inloggad"; }
public String Avdelningar() { return "Avdelningar"; }
public String Sok() { return "Sök"; }
public String Gast() { return "Gäst"; }
public String Lager() { return "Lager"; }
public String Tel() { return "Tel"; }
public String EPost() { return "E-post"; }
public String Priser() { return "Priser"; }
public String InklMoms() { return "Inkl. moms"; }
public String ExklMoms() { return "Exkl. moms"; }
public String Avtalspris() { return "Avtalspris"; }
public String Varukorg() { return "Varukorg"; }
public String OkaAntal() { return "Öka antal"; }
public String MinskaAntal() { return "Minska antal"; }
public String TaBortProdukt() { return "Ta bort produkt"; }
public String Anvandarnamn() { return "Användarnamn"; }
public String Losen() { return "Lösen"; }
public String GlomtLosenord() { return "Glömt lösenord"; }
public String SkickaNyttLosenord() { return "Skicka nytt lösenord"; }
public String Artikel() { return "Artikel"; }
public String Antal() { return "Antal"; }
public String Enhet() { return "Enhet"; }
public String Pris() { return "Pris"; }
public String Totalt() { return "Totalt"; }
public String ILager() { return "I Lager"; }
public String Ordertotal() { return "Ordertotal"; }
public String PriserInklusiveMoms() { return "Priser inklusive moms"; }
public String PriserExklusiveMoms() { return "Priser exklusive moms"; }
public String ValtLager() { return "Valt lager"; }
public String Transportsatt() { return "Transportsätt"; }
public String Kundnummer() { return "Kundnummer"; }
public String Foretag() { return "Företag"; }
public String Kontaktperson() { return "Kontaktperson"; }
public String Adress() { return "Adress"; }
public String Postnr() { return "Postnr"; }
public String Ort() { return "Ort"; }
public String Telefon() { return "Telefon"; }
public String Organisationsnummer() { return "Organisationsnummer"; }
public String Godsmarke() { return "Godsmärke"; }
public String Meddelande() { return "Meddelande"; }
public String SkickaOrder() { return "Skicka order"; }
public String FinnsILager() { return "Finns i lager"; }
public String AllaVarianter() { return "Alla varianter"; }
public String Listpris() { return "Listpris"; }
public String PreliminaraLagersaldonFor() { return "Preliminära lagersaldon för"; }
public String Specifikationer() { return "Specifikationer"; }
public String ForegaendeSida() { return "<< Föregående sida"; }
public String Index() { return "Index"; }
public String NastaSida() { return "Nästa sida >>"; }
public String Bestallningsvara() { return "Beställningsvara"; }
public String PrisFran() { return "Pris från"; }
public String RelateradeProdukter() { return "Relaterade produkter"; }
public String Kop() { return "Köp"; }
public String ViAnvanderCookiesForOkadAnvandarupplevelse() { return "Vi använder cookies för ökad användarupplevelse."; }
public String Artnr() { return "Artnr"; }
public String FinnsPaLagerI() { return "Finns på lager i"; }
public String Slutsald() { return "Slutsåld"; }
public String LoggaInOmDuVillFaTillgangTillMittKonto() { return "Logga in om du vill få tillgång till Mitt Konto."; }
public String Valkommen() { return "Välkommen"; }
public String NyttLosenord() { return "Nytt lösenord"; }
public String LoginGlomtLosenHTML() { return "<p>Nytt lösenord är skickat till den e-postadress som är registrerad på ditt konto. </p><p>Om du inte får någon e-post kontrollerar du att du angivit rätt användarnamn och att mailet inte har hamnat i din skräppost.</p>"; }
public String DuArInloggad() { return "Du är inloggad"; }
public String LoggaUt() { return "Logga ut"; }
public String PageNotFoundHTML() { return "Tyvärr kan vi inte hitta sidan..."; }
public String ValutaNamn() { return "kr"; }
public String RSK() { return "RSK"; }
public String LiknandeProdukter() { return "Liknande produkter"; }
public String ProduktEjHittadHTML() { return "<h1>Produkten du sökte finns inte.</h1><h3>Kanske någon av dessa produkter passar?</h3>"; }
public String BekraftelseWebborder() { return "Bekräftelse webborder"; }
public String BekräftelseWebborderTextHTML() { return "Tack för din order!"; }
public String BekräftelseWebborderTextMedExpAvgiftHTML() { return "Tack för din order!<br>Tänk på att fraktkostnader och expeditionskostnader tillkommer."; }
public String OrderRegistreradPaLager() { return "Order registrerad på lager"; }
public String ValtTransportsatt() { return "Valt transportsätt"; }
public String Benamning() { return "Benämning"; }
public String SkrymmefraktTillkommerPaDennaArtikel() { return "Skrymmefrakt tillkommer på denna artikel."; }
public String SarskildFraktkostnadTillkommerPaDennaArtikel() { return "Särskild fraktkostnad tillkommer på denna artikel."; }
public String ObserveraAttDuHar() { return "Observera att du har"; }
public String ILangtidsforfallnaFakturorSomMasteBetalasInnanLeveransKanSke() { return "i långtidsförfallna fakturor som måste betalas innan leverans kan ske."; }
public String ObserveraAttDinKreditgrans() { return "Observera att din kreditgräns ("; }
public String KrArOverskridenBetalningMasteSkeInnanLeverans() { return "kr) är överskriden. Betalning måste ske innan leverans."; }
public String Turbil() { return "Turbil"; }
public String Mandagar() { return "Måndagar"; }
public String Tisdagar() { return "Tisdagar"; }
public String Onsdagar() { return "Onsdagar"; }
public String Torsdagar() { return "Torsdagar"; }
public String Fredagar() { return "Fredagar"; }
public String SkickasMedLampligSpeditor() { return "Skickas med lämplig speditör"; }
public String VarukorgSaveErrorHTML() { return "<b>Hoppsan... det gick inte att skicka ordern, och vi vet inte varför. Prova igen!</b>"; }
public String TillfalligtSlut() { return "Tillfälligt slut"; }
public String FatalKvar() { return "Fåtal kvar"; }
public String Rekommenderat() { return "Rekommenderat"; }
public String Fran() { return "Från"; }
public String fran() { return "från"; }
public String Orderbekraftelse() { return "Orderbekräftelse"; }
public String webborder() { return "webborder"; }
public String TomVarukorg() { return "Töm varukorgen"; }
public String BearbetarOrder() { return "Bearbetar order..."; }

}