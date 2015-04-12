/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ulf
 */
public class SokResult {
	private ArrayList<ProduktGrund> pl = new ArrayList<>();

	public List<ProduktGrund> getPl() {
		return pl;
	}
	public void add(ProduktGrund produktGrund) {
		pl.add(produktGrund);
	}

	
}
