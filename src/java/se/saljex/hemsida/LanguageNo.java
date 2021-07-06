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
public class LanguageNo extends Language {
private Locale locale = new Locale("no","NO");
@Override public String getLanguage() { return "no"; }
@Override public String getCountry() { return "no"; }
@Override public Locale getLocale() {return locale; }
@Override public String translateString(String s) {
switch(s) {
    case "Tillfälligt slut": return TillfalligtSlut();
    case "Fåtal kvar": return FatalKvar();
    case "Beställningsvara": return Bestallningsvara();
    case "Finns i lager": return FinnsILager();
    default: return s;
}
}

    
@Override public String TillKassan() { return "Til kassen"; }
@Override public String LoggaIn() { return "Logg inn"; }
@Override public String Inloggad() { return "Logget inn"; }
@Override public String Avdelningar() { return "Avdelinger"; }
@Override public String Sok() { return "Søk"; }
@Override public String Gast() { return "Gjest"; }
@Override public String Lager() { return "Lager"; }
@Override public String Tel() { return "Tel"; }
@Override public String EPost() { return "E-post"; }
@Override public String Priser() { return "Priser"; }
@Override public String InklMoms() { return "Inkl. mva"; }
@Override public String ExklMoms() { return "Ekskl. mva"; }
@Override public String Avtalspris() { return "Prisavtale"; }
@Override public String Varukorg() { return "Handlekurv"; }
@Override public String OkaAntal() { return "Øke antall"; }
@Override public String MinskaAntal() { return "Redusere antall"; }
@Override public String TaBortProdukt() { return "Slett produkt"; }
@Override public String Anvandarnamn() { return "Brukernavn"; }
@Override public String Losen() { return "Passord"; }
@Override public String GlomtLosenord() { return "Glemt passord"; }
@Override public String SkickaNyttLosenord() { return "Send nytt passord"; }
@Override public String Artikel() { return "Artikkel"; }
@Override public String Antal() { return "Antall"; }
@Override public String Enhet() { return "Enhet"; }
@Override public String Pris() { return "Pris"; }
@Override public String Totalt() { return "Total"; }
@Override public String ILager() { return "I Lager"; }
@Override public String Ordertotal() { return "Ordretotalen"; }
@Override public String PriserInklusiveMoms() { return "Prisen inkludert merverdiavgift"; }
@Override public String PriserExklusiveMoms() { return "Prisen ekskludert merverdiavgift"; }
@Override public String ValtLager() { return "Valgt lager"; }
@Override public String Transportsatt() { return "Transportmåte"; }
@Override public String Kundnummer() { return "Kundenummer"; }
@Override public String Foretag() { return "Bedrift"; }
@Override public String Kontaktperson() { return "Kontaktperson"; }
@Override public String Adress() { return "Adresse"; }
@Override public String Postnr() { return "Postkode"; }
@Override public String Ort() { return "Sted"; }
@Override public String Telefon() { return "Telefon"; }
@Override public String Organisationsnummer() { return "Organisasjonsnummer"; }
@Override public String Godsmarke() { return "Merking"; }
@Override public String Meddelande() { return "Melding"; }
@Override public String SkickaOrder() { return "Send ordre"; }
@Override public String FinnsILager() { return "Finnes i lager"; }
@Override public String AllaVarianter() { return "Alle variasjoner"; }
@Override public String Listpris() { return "Listepris"; }
@Override public String PreliminaraLagersaldonFor() { return "Preliminære lagersaldon før"; }
@Override public String Specifikationer() { return "Spesifikasjoner"; }
@Override public String ForegaendeSida() { return "<< Forrige side"; }
@Override public String Index() { return "Indeks"; }
@Override public String NastaSida() { return "Neste side >>"; }
@Override public String Bestallningsvara() { return "Bestillingsvare"; }
@Override public String PrisFran() { return "Pris fra"; }
@Override public String RelateradeProdukter() { return "Relaterte produkter"; }
@Override public String Kop() { return "Kjøp"; }
@Override public String ViAnvanderCookiesForOkadAnvandarupplevelse() { return "Vi bruker cookies før økt brukeropplevelse"; }
@Override public String Artnr() { return "Artnr"; }
@Override public String FinnsPaLagerI() { return "Finnes på lager i"; }
@Override public String Slutsald() { return "Utsolgt"; }
@Override public String LoggaInOmDuVillFaTillgangTillMittKonto() { return "Logg inn hvis du vil få tilgang till Min Konto"; }
@Override public String Valkommen() { return "Velkommen"; }
@Override public String NyttLosenord() { return "Nytt passord"; }
@Override public String LoginGlomtLosenHTML() { return "<p> Nytt passord er sent til den e-postadress som er registrert på din konto. </p><p> Hvis du ikke mottat noen E-post må du sjekke at du brukt riktig brukernavn og at mailen ikke havnet i søppelposten.</p>"; }
@Override public String DuArInloggad() { return "Du er logget in"; }
@Override public String LoggaUt() { return "Logg ut"; }
@Override public String PageNotFoundHTML() { return "Beklager, vi finner ikke siden"; }
@Override public String ValutaNamn() { return "kr"; }
@Override public String RSK() { return "ID"; }
@Override public String LiknandeProdukter() { return "Lignende produkter"; }
@Override public String ProduktEjHittadHTML() { return "<h1> Produkten du søkte finnes ikke.</h1><h3>Kan noen av disse produkter brukes?</h3>"; }
@Override public String BekraftelseWebborder() { return "Bekreftelse webordre"; }
@Override public String BekräftelseWebborderTextHTML() { return "Takk før din ordre!"; }
@Override public String BekräftelseWebborderTextMedExpAvgiftHTML() { return "Takk før din ordre!<br>Husk at alle fraktkostnader og ekspedisjongebyr kommer i tillegg"; }
@Override public String OrderRegistreradPaLager() { return "Ordre registrert på lager"; }
@Override public String ValtTransportsatt() { return "Valgt transportmåte"; }
@Override public String Benamning() { return "Benevning"; }
@Override public String SkrymmefraktTillkommerPaDennaArtikel() { return "Uhåndterlig-frakt vil bli lagt til på denne artikkelen."; }
@Override public String SarskildFraktkostnadTillkommerPaDennaArtikel() { return "Spesiell fraktkostned kommer i tilegg på denne produkt."; }
@Override public String ObserveraAttDuHar() { return "Noter at du har"; }
@Override public String ILangtidsforfallnaFakturorSomMasteBetalasInnanLeveransKanSke() { return "i langtidsforallne fakturaer som må betales før leveranse kan skje."; }
@Override public String ObserveraAttDinKreditgrans() { return "Noter at din kreditgrense"; }
@Override public String KrArOverskridenBetalningMasteSkeInnanLeverans() { return "er overskredet. Betaling må skje før leveranse."; }
@Override public String Turbil() { return "Turbil"; }
@Override public String Mandagar() { return "Mandager"; }
@Override public String Tisdagar() { return "Tirsdager"; }
@Override public String Onsdagar() { return "Onsdager"; }
@Override public String Torsdagar() { return "Torsdager"; }
@Override public String Fredagar() { return "Fredager"; }
@Override public String SkickasMedLampligSpeditor() { return "Sendes med lempet speditør"; }
@Override public String VarukorgSaveErrorHTML() { return "Oops...vi kunne ikke sende ordern, og vi vet ikke hvorfor. Prøv igjen senere!"; }
@Override public String TillfalligtSlut() { return "Tilfeldig slut"; }
@Override public String FatalKvar() { return "Få igjen"; }
@Override public String Rekommenderat() { return "Anbefallt"; }
@Override public String Fran() { return "Fra"; }
@Override public String fran() { return "fra"; }
@Override public String Orderbekraftelse() { return "Orderbekreftelse"; }
@Override public String webborder() { return "webbordre"; }
@Override public String TomVarukorg() { return "Tøm handlekurven"; }
@Override public String BearbetarOrder() { return "Behandler bestilling..."; }
@Override public String LaggListanIVarukorgen() { return "Legg listen i handlekurven"; }
@Override public String MinaFavoriter() { return "Mine favoritter"; }


}