/*
 * Decompiled with CFR 0_129.
 */
package me.skorrloregaming.impl.extreme;

import java.util.ArrayList;

public class ExtremeSwearData {
	private ArrayList<Character> swearWordsChar0 = new ArrayList();
	private ArrayList<Character> swearWordsChar1 = new ArrayList();
	private ArrayList<Character> swearWordsChar2 = new ArrayList();
	private ArrayList<Character> swearWordsChar3 = new ArrayList();
	private ArrayList<Character> swearWordsChar4 = new ArrayList();
	private ArrayList<Character> swearWordsChar5 = new ArrayList();
	private ArrayList<Character> swearWordsChar6 = new ArrayList();

	public ArrayList<Character> get(int length) {
		if (length == 0) {
			return this.swearWordsChar0;
		}
		if (length == 1) {
			return this.swearWordsChar1;
		}
		if (length == 2) {
			return this.swearWordsChar2;
		}
		if (length == 3) {
			return this.swearWordsChar3;
		}
		if (length == 4) {
			return this.swearWordsChar4;
		}
		if (length == 5) {
			return this.swearWordsChar5;
		}
		if (length == 6) {
			return this.swearWordsChar6;
		}
		return new ArrayList<Character>();
	}

	public void fill(String swearWord) {
		char[] chars = swearWord.toCharArray();
		int i = 0;
		while (i < chars.length) {
			this.get(i).add(Character.valueOf(chars[i]));
			++i;
		}
	}
}

