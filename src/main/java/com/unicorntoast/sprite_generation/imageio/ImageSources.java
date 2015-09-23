package com.unicorntoast.sprite_generation.imageio;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;

import com.unicorntoast.sprite_generation.gen.ImageDef;
import com.unicorntoast.sprite_generation.util.Verify;

public abstract class ImageSources {
	
	// impl
	
	public static ImageSource create(String filename,StreamSource streamSource) {
		String[] parts = Verify.notNull(filename).split("\\.");
		Verify.verify(parts.length>0);
		
		return createBySuffix(parts[parts.length-1],streamSource);
	}
	
	public static ImageSource createBySuffix(String suffix,StreamSource streamSource) {
		ImageReaders imageReaders = ()->ImageIO.getImageReadersBySuffix(Verify.notNull(suffix));
		return create(imageReaders,streamSource);
	}
	
	public static ImageSource create(ImageReaders imageReaders,StreamSource streamSource) {
		Verify.notNull(imageReaders);
		Verify.notNull(streamSource);
		return new ImageSource() {
			
			@Override
			public BufferedImage read() throws IOException {
				return ImageSources.read(imageReaders,streamSource);
			}
			
			@Override
			public <T> ImageDef<T> imageDef(T attachment) throws IOException {
				return ImageSources.imageDef(imageReaders,streamSource,attachment);
			}
			
		};
	}

	public static <T> ImageDef<T> imageDef(ImageReaders imageReaders,StreamSource streamSource,T attachment) throws IOException {
		return with(imageReaders,streamSource,(reader,iis)->{
			try {
				reader.setInput(iis,true,true);
				
				int index = reader.getMinIndex();
				int width = reader.getWidth(index);
				int height = reader.getHeight(index);
				
				return ImageDef.create(width,height,attachment);
			} catch(IOException e) {
				return null;
			}
		});
	}

	public static BufferedImage read(ImageReaders imageReaders,StreamSource streamSource) throws IOException {
		return with(imageReaders,streamSource,(reader,iis)->{
			try {
				reader.setInput(iis,true,true);
				return reader.read(reader.getMinIndex());
			} catch(IOException e) {
				return null;
			}
		});
	}
	
	public static <T> T with(ImageReaders imageReaders,StreamSource streamSource,WithReaderCallback<T> cb) throws IOException {
		Iterator<ImageReader> readers = imageReaders.iterator();
		try {
			
			while( readers.hasNext() ) {
				ImageReader reader = readers.next();
				try {
					try(InputStream stream = streamSource.open()) {
						try(MemoryCacheImageInputStream iis = new MemoryCacheImageInputStream(stream)) {
							T result = cb.with(reader,iis);
							if( result!=null ) return result;
						}
					}
				} finally {
					reader.dispose();
				}
			}
			
		} finally {
			while( readers.hasNext() )
				readers.next().dispose();
		}
		
		return null;
	}
	
	// structs & api
	
	@FunctionalInterface
	public static interface ImageReaders {
		Iterator<ImageReader> iterator();
	}
	
	@FunctionalInterface
	public static interface WithReaderCallback<T> {
		T with(ImageReader reader,ImageInputStream stream) throws IOException;
	}
	

}
