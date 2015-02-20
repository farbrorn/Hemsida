/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Ulf
 */
public class ImageServer {

private String imgPath;
private String cachePath;

	public ImageServer(String imgPath, String cachePath) {
		this.imgPath=imgPath;
		this.cachePath = cachePath;
	}
	
	
	//resize to fit insida a box boxSize pixels widh and height
	public static BufferedImage resize(BufferedImage orgImg, int type, int boxSize){
		int h = orgImg.getHeight();
		int w = orgImg.getWidth();
		double f;
		int nH=0;
		int nW=0;
		if (h>0 && w>0) {
			f = h/w;
			if (f >= 1.0) {
				nH=boxSize;
				nW=(int)(nH/f);
				if (nW>boxSize) nW=boxSize;
			} else {
				nW=boxSize;
				nH=(int)(f/nW);
				if (nH>boxSize) nH=boxSize;
			}
		}
		return resize(orgImg, type, nW, nH);
	}
	
	private static BufferedImage resize(BufferedImage orgImg, int type, int width, int height){
		BufferedImage newImg = new BufferedImage(width, height, type);
		Graphics2D g = newImg.createGraphics();
		g.drawImage(orgImg, 0, 0, width, height, null);
		g.dispose();
		return newImg;
	}
}
