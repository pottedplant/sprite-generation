package com.unicorntoast.sprite_generation.bsp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.unicorntoast.sprite_generation.gen.ImageDef;
import com.unicorntoast.sprite_generation.gen.Placement;
import com.unicorntoast.sprite_generation.gen.Sheet;

public class BspPlacer<T> {

	// state

	private final int width;
	private final int height;
	private final int margin;
	private final List<Node<ImageDef<T>>> sheets = new ArrayList<>();

	// impl

	public BspPlacer(int width,int height,int margin) {
		this.width = width;
		this.height = height;
		this.margin = margin;
	}

	public static <T> BspPlacer<T> create(int width,int height,int margin) {
		return new BspPlacer<>(width,height,margin);
	}
	
	public List<ImageDef<T>> push(List<ImageDef<T>> images) {
		Collections.sort(images,(a,b)->Integer.compare(b.width*b.height,a.width*a.height));
		
		List<ImageDef<T>> rejected = new ArrayList<>();
		for(ImageDef<T> image:images)
			if( !push(image) )
				rejected.add(image);
		
		return rejected;
	}

	public boolean push(ImageDef<T> image) {
		for(Node<ImageDef<T>> sheet:sheets)
			if( push(sheet,image,width,height,true,true) )
				return true;

		Node<ImageDef<T>> sheet = Node.empty();
		if( !push(sheet,image,width,height,true,true) )
			return false;

		sheets.add(sheet);
		return true;
	}

	public List<Sheet<T>> sheets() {
		ArrayList<Sheet<T>> sheets = new ArrayList<>(this.sheets.size());

		for(Node<ImageDef<T>> s:this.sheets)
			sheets.add(toSheet(s));

		return sheets;
	}

	// pimpl

	private Sheet<T> toSheet(Node<ImageDef<T>> r) {
		List<Placement<T>> placements = new ArrayList<>();
		collect(placements,r,0,0);
		
		int width=0;
		int height=0;
		
		for(Placement<T> p:placements) {
			width = Math.max(width,p.x+p.image.width);
			height = Math.max(height,p.y+p.image.height);
		}
		
		return new Sheet<>(placements,width,height);
	}

	private void collect(List<Placement<T>> placements,Node<ImageDef<T>> n,int x,int y) {
		if( n.value!=null ) {
			placements.add(new Placement<>(n.value,x,y));
			return;
		}

		if( n.first!=null )
			collect(placements,n.first,x,y);

		if( n.second!=null ) {
			int cx = x;
			int cy = y;

			switch(n.split.type ) {
			case vertical: cx += n.split.offset; break;
			case horizontal: cy += n.split.offset; break;
			}

			collect(placements,n.second,cx,cy);
		}
	}

	private boolean push(Node<ImageDef<T>> n,ImageDef<T> i,int w,int h,boolean xEnd,boolean yEnd) {
		int rw = i.width + (xEnd ? 0 : margin);
		int rh = i.height + (yEnd ? 0 : margin);
		
		if( rw>w || rh>h || n.value!=null )
			return false;

		if( n.split==null ) {
			attach(n,i,i.width+margin,i.height+margin);
			return true;
		}

		switch(n.split.type) {

		case horizontal:

			if( push(n.first,i,w,n.split.offset,xEnd,false) )
				return true;

			return push(n.second,i,w,h-n.split.offset,xEnd,yEnd);

		case vertical:

			if( push(n.first,i,n.split.offset,h,false,yEnd) )
				return true;

			return push(n.second,i,w-n.split.offset,h,xEnd,yEnd);

		default: throw new IllegalStateException();
		}
	}

	private void attach(Node<ImageDef<T>> n,ImageDef<T> i,int w,int h) {
		Split split = new Split(Split.Type.horizontal,h);

		Node<ImageDef<T>> first = Node.split(Split.of(Split.Type.vertical,w),Node.leaf(i),Node.empty());
		Node<ImageDef<T>> second = Node.empty();

		n.split = split;
		n.first = first;
		n.second = second;
	}

}
