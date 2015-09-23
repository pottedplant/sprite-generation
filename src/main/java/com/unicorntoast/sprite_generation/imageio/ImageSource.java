package com.unicorntoast.sprite_generation.imageio;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.unicorntoast.sprite_generation.gen.ImageDef;

public interface ImageSource {

	<T> ImageDef<T> imageDef(T attachment) throws IOException;
	BufferedImage read() throws IOException;

}
