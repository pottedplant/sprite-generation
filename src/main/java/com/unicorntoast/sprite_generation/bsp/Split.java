package com.unicorntoast.sprite_generation.bsp;

import com.unicorntoast.sprite_generation.util.Verify;

public class Split {

	public enum Type { horizontal,vertical, };

	public final Type type;
	public final int offset;

	public Split(Type type,int offset) {
		this.type = Verify.notNull(type);
		this.offset = offset;
	}

	public static Split of(Type type,int offset) {
		return new Split(type,offset);
	}


}
