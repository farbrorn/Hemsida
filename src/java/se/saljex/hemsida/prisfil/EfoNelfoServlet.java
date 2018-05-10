/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.saljex.hemsida.prisfil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import se.saljex.hemsida.Const;

/**
 *
 * @author ulf
 */
public class EfoNelfoServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		Connection con=Const.getConnection(request);
                String dbuser="";
                if ("no".equals(request.getParameter("country"))) dbuser="sxasfakt."; 

		ByteArrayOutputStream w = new ByteArrayOutputStream();
		try {
			ResultSet rs = con.prepareStatement("select * from " + dbuser + "efonelfofil").executeQuery();
			while (rs.next()) {
	//				w.println(new String(rs.getString(1).getBytes(),"ISO-8859-1"));
				w.write(rs.getString(1).getBytes("ISO-8859-1"));
				w.write("\r\n".getBytes());
				w.flush();

			}
                    ServletOutputStream o = response.getOutputStream();
                    try {
                            response.setContentType("text/plain");
                            response.setHeader("Content-Disposition", "attachment;filename=v4efonelfo.txt");
                            w.writeTo(o);
                    } finally {			
                            o.close();
                    }
		} catch (Exception e) { 
			response.setContentType("text/html");
			response.getWriter().print(e.toString()); 
		}
		
		
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    
    
    /*
    Detta är originalet till SQL-satsen för view efonelfofil. 
    Eftersom den ligger som en view så kan den komma att ändras i databasen utan att uppdateras i denna kommentar.
    
    
create or replace view efonelfofil as
select 
case when prio=-100 then
'VH' || ';' ||
'EFONELFO' || ';' ||
'4.0' || ';' ||
replace(substring(coalesce(fup.regnr,''),1,14),';',':') || ';' ||
replace(substring(coalesce('',''),1,14),';',':') || ';' ||
replace(substring(coalesce('',''),1,10),';',':') || ';' ||
to_char(current_date,'YYYYMMDD') || ';' ||
replace(substring(coalesce('',''),1,8),';',':') || ';' ||
'SEK' || ';' ||
replace(substring(coalesce('',''),1,10),';',':') || ';' || --Avtaleid
replace(substring(coalesce(fup.namn,''),1,35),';',':') || ';' || 
replace(substring(coalesce(fup.adr1,''),1,35),';',':') || ';' || 
replace(substring(coalesce(fup.adr2,''),1,35),';',':') || ';' || 
replace(substring(coalesce(fup.adr3,''),1,4),';',':') || ';' || --postnummer
replace(substring(coalesce(fup.adr3,''),6,35),';',':') || ';' || --ort
'SE' --land
else 

replace(substring(coalesce(posttype,''),1,2),';',':') || ';' ||
replace(substring(coalesce(varemrk,''),1,1),';',':') || ';' ||
replace(substring(coalesce(varenr,''),1,14),';',':') || ';' ||
replace(substring(coalesce(vabetg,''),1,30),';',':') || ';' ||
replace(substring(coalesce(vabetg2,''),1,30),';',':') || ';' ||
replace(substring(coalesce(maleenhet,''),1,1),';',':') || ';' ||
replace(substring(coalesce(prisenhet,''),1,3),';',':') || ';' ||
replace(substring(coalesce(prisenhettxt,''),1,8),';',':') || ';' ||
replace(substring(coalesce(pris,0)::varchar,1,10),';',':') || ';' ||
replace(substring(coalesce(mengde,0)::varchar,1,9),';',':') || ';' ||
replace(substring(coalesce(prisdato,''),1,8),';',':') || ';' ||
replace(substring(coalesce(status,''),1,1),';',':') || ';' ||
replace(substring(coalesce(blokknummer,''),1,6),';',':') || ';' ||
replace(substring(coalesce(rabattgruppe ,''),1,14),';',':') || ';' ||
replace(substring(coalesce(fabrikat ,''),1,10),';',':') || ';' ||
replace(substring(coalesce(type ,''),1,10),';',':') || ';' ||
replace(substring(coalesce(lagerfort ,''),1,1),';',':') || ';' ||
replace(substring(coalesce(salgspakning ,0)::varchar,1,9),';',':') || ';' ||
replace(substring(coalesce(rabatt ,0)::varchar,1,4),';',':') || ';' ||
replace(substring(coalesce(pristype,''),1,1),';',':') 


end
from (
	select 
		0 as prio,
		'VL' as posttype, --VL=Varelinje, PL=pristilbud
		varemrk, -- 0=ukjent, 1=elnr, 2=EAN, 3=producentens nr, 4=NRF
		varenr,
		substring(a.namn,1,30) as vabetg,
		substring(a.namn,31,5) as vabetg2,
		case when upper(a.enhet)='L' then '3' else
				case when upper(a.enhet)='M' then '2' else
					case when upper(a.enhet)='KG' then '4' else
						'1' 
		end end end as maleenhet, --1=stykk, 2=meter, 3=liter, 4=kg
		coalesce (e.ersatt,'EA') as prisenhet,
		coalesce (e.ersattext,'STYKK') as prisenhettxt,
		round(utpris*100) as pris,
		10000 as mengde,
		to_char(prisdatum,'YYYYMMDD') as prisdato,
		'2' as status, --0=uendret, 1=ny vare, 2=Endret, 3=utgår
		null as blokknummer,
		rabkod || case when kod1<>'' and kod1 is not null then '-' || kod1 else '' end as rabattgruppe,
		null as fabrikat,
		null as type,
		case when l.ilager >0 or l.maxlager > 0 then 'J' else 'N' end as lagerfort, --J=Jam N=Nej, Blank=ikke i bruk
		round(forpack*10000) as salgspakning,
		0 as rabatt,
		'B' as pristype -- B=Brutto, N=Netto

	from
		(
			select '0' as varemrk, nummer as varenr, nummer, namn, utpris, enhet, rabkod, kod1, forpack, minsaljpack, utgattdatum, hindraexport, prisdatum from artikel 
			--varemrk 0 = annat
			union all
			--varemrk 4 = nrf
			select '4' as varemrk, rsk as varenr, nummer, namn, utpris, enhet, rabkod, kod1, forpack, minsaljpack, utgattdatum, hindraexport, prisdatum from artikel where rsk is not null and rsk <> '' and length(rsk) = 7

		) a 
		left outer join lager l on l.artnr=a.nummer and l.lagernr=0 
		left outer join 
		(select unnest(array['ST','PAR','SB','KG','M','M2','M3','L']) as original, unnest(array['EA','SET','SET','KGM','MTR','MTK','MTQ','LTR']) as ersatt, unnest(array['STYKK','SETT','SETT','KILOGRAM','METER','M2','M3','LITER']) as ersattext)
		e on e.original = upper(a.enhet)
	where a.utgattdatum is null and a.nummer between '01' and 'X'  and a.hindraexport = 0
	and upper(a.enhet) in ('SB','L','PKT','FRP','ST','RLE','SET','LST','M','TUB','PAR','KG') --Formatet stöder inte yta som enhet. Om feler enhetr tillåts så måste Malenheten och mängd ses över
	
	union all
	
	select -100, null,null,null,null,null,null,null,null,null,null,null,null,null,null, null, null, null, null, null, null
) r
left outer join fuppg fup on 1=1
order by prio, varenr





    
    
    
    
    */
}
