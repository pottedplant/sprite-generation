package com.unicorntoast.sprite_generation.imageio;

import java.io.IOException;
import java.io.InputStream;

@FunctionalInterface
public interface StreamSource {
	
	InputStream open() throws IOException;
	
}