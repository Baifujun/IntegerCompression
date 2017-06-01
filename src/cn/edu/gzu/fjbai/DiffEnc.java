package cn.edu.gzu.fjbai;

import java.util.ArrayList;

public class DiffEnc {

	public static ArrayList<Integer> diffEnco(ArrayList<Integer> x) {
		ArrayList<Integer> X = x;
		ArrayList<Integer> d = new ArrayList<Integer>(X.size());
		d.add(X.get(0));
		for(int i = 1; i < 	X.size(); i++) {
			d.add(X.get(i) - X.get(i-1));		
		}
		return d;
	}
	
	public static ArrayList<Integer> deCoding(ArrayList<Integer> y) {
		ArrayList<Integer> Y = y;
		for(int i = 0; i < Y.size(); i++) {
			Y.set(i + 1, Y.get(i) + Y.get(i + 1));
		}
		return Y;
	}
}
