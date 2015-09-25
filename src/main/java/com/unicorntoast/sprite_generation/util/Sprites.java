package com.unicorntoast.sprite_generation.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

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

	public static byte[] pngByteArray(BufferedImage image) throws IOException {
		Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByMIMEType("image/png");
		try {
			ImageWriter imageWriter = imageWriters.next();
			try {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				try(ImageOutputStream imageStream = new MemoryCacheImageOutputStream(stream)) {
					imageWriter.setOutput(imageStream);
					imageWriter.write(image);
				}
				return stream.toByteArray();
			} finally {
				imageWriter.dispose();
			}
		} finally {
			while(imageWriters.hasNext())
				imageWriters.next().dispose();
		}
	}

	@FunctionalInterface
	public static interface ImageProvider<T> {
		Image image(ImageDef<T> source) throws IOException;
	}

}
