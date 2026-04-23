package org.geoazul.view.utils;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class ImageResize {
		
	public void resize(String origemFileGoogleF, String destinationFileGoogleF, int IMG_WIDTH, int IMG_HEIGHT ){
		try{
			BufferedImage originalImage = ImageIO.read(new File(origemFileGoogleF));
			int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
			BufferedImage resizeImagePng = resizeImage(originalImage, type,  IMG_WIDTH,  IMG_HEIGHT);
			ImageIO.write(resizeImagePng, "png", new File(destinationFileGoogleF)); 
		}catch(IOException e){
			e.printStackTrace();
		}
    }
	
    private static BufferedImage resizeImage(BufferedImage originalImage, int type, int IMG_WIDTH, int IMG_HEIGHT){
    	BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
    	Graphics2D g = resizedImage.createGraphics();
    		g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
    		g.dispose();
    	return resizedImage;
    }
	
    @SuppressWarnings("unused")
	private static BufferedImage resizeImageWithHint(BufferedImage originalImage, int type, int IMG_WIDTH, int IMG_HEIGHT){
    	BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
    	Graphics2D g = resizedImage.createGraphics();
    		g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
    		g.dispose();	
    		g.setComposite(AlphaComposite.Src);
    		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,	RenderingHints.VALUE_ANTIALIAS_ON);
		return resizedImage;
    }	    
}
