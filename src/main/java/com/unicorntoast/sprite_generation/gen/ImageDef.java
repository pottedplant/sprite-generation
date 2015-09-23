package com.unicorntoast.sprite_generation.gen;

import com.unicorntoast.sprite_generation.util.Verify;

public class ImageDef<T> {
	
	public final int width;
	public final int height;
	public final T attachment;
	
	public ImageDef(int width,int height,T attachment) {
		Verify.verify(height>0 && height>0);
		this.width = width;
		this.height = height;
		this.attachment = attachment;
	}
	
	public static <T> ImageDef<T> create(int width,int height,T attachment) {
		return new ImageDef<>(width,height,attachment);
	}
	
}
