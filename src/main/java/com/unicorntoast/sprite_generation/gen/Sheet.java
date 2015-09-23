package com.unicorntoast.sprite_generation.gen;

import java.util.List;

import com.unicorntoast.sprite_generation.util.Verify;

public class Sheet<T> {

	public final List<Placement<T>> placements;
	public final int width;
	public final int height;

	public Sheet(List<Placement<T>> placements,int width,int height) {
		Verify.verify(width>=0 && height>=0);
		this.width = width;
		this.height = height;
		this.placements = Verify.notNull(placements);
	}

}
