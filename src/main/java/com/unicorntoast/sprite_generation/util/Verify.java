package com.unicorntoast.sprite_generation.util;

public abstract class Verify {

	public static <T> T notNull(T value) {
		if( value==null ) throw new NullPointerException();
		return value;
	}

	public static void verify(boolean value) {
		if( !value ) throw new IllegalArgumentException();
	}

}
