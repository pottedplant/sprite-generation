package com.unicorntoast.sprite_generation.gen;

import com.unicorntoast.sprite_generation.util.Verify;

public class Placement<T> {

	public final ImageDef<T> image;
	public final int x;
	public final int y;

	public Placement(ImageDef<T> image,int x,int y) {
		Verify.verify(x>=0 && y>=0);
		this.image = Verify.notNull(image);
		this.x = x;
		this.y = y;
	}

}
