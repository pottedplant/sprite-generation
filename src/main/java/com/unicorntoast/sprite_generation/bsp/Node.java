package com.unicorntoast.sprite_generation.bsp;

public class Node<T> {

	public Split split;
	public Node<T> first;
	public Node<T> second;

	public T value;

	public static <T> Node<T> leaf(T value) {
		Node<T> n = new Node<>();
		n.value = value;
		return n;
	}

	public static <T> Node<T> empty() {
		return new Node<>();
	}

	public static <T> Node<T> split(Split split,Node<T> first,Node<T> second) {
		Node<T> n = new Node<>();

		n.split = split;
		n.first = first;
		n.second = second;

		return n;
	}

}
