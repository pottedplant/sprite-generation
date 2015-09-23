package com.unicorntoast.sprite_generation.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import com.unicorntoast.sprite_generation.gen.ImageDef;
import com.unicorntoast.sprite_generation.gen.Placement;
import com.unicorntoast.sprite_generation.gen.Sheet;

public abstract class Sprites {
	
	public static <T> BufferedImage create(Sheet<T> sheet,ImageProvider<T> imageProvider) throws IOException {
		BufferedImage image = new BufferedImage(sheet.width,sheet.height,BufferedImage.TYPE_4BYTE_ABGR);
		fill(image,sheet.placements,imageProvider);
		return image;
	}
	
	public static <T> void fill(BufferedImage dst,List<Placement<T>> placements,ImageProvider<T> imageProvider) throws IOException {
		Verify.notNull(dst);
		Verify.notNull(imageProvider);
		
		Graphics2D graphics = dst.createGraphics();
		try {
			for(Placement<T> placement:placements) {
				Image src = imageProvider.image(placement.image);
				graphics.drawImage(src,placement.x,placement.y,null);
			}
		} finally {
			graphics.dispose();
		}
	}
	
	@FunctionalInterface
	public static interface ImageProvider<T> {
		Image image(ImageDef<T> source) throws IOException;
	}

}
