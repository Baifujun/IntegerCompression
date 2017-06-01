package cn.edu.gzu.fjbai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class IntegerToString {

	private ArrayList<Integer> X;
	
	public IntegerToString(ArrayList<Integer> x) {
		X = x;
	}
	
	public long getSpace() {
		long count = 0l;
		for(Iterator<Integer> iter = X.iterator(); iter.hasNext();) {
			count += iter.next().toString().length();
		}
		count += X.size() - 1;
		count *= Integer.SIZE;
		return count;
	}
	
	public static void main(String[] args) {
		ArrayList<Integer>  X = new ArrayList<Integer>();
		Random random = new Random();
		for(int i=0; i < 2;) {
			int r = random.nextInt(500);
			if(!X.contains(r)) {
				X.add(r);
				i++;
			}
		}
		System.out.println(X);
		X.sort(new MyComparator());
		IntegerToString its = new IntegerToString(X);
		System.out.println(its.getSpace());
	}
}
