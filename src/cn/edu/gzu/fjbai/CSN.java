package cn.edu.gzu.fjbai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * @author fjbai
 * 对于大序列会出错，通用性更强的CSNP取而代之
 */
public class CSN {

	private ArrayList<Integer> X ; //待压缩序列
	private long CSN; //序列X的CSN值
	private int M; //参数M
	private int T; //参数T
	private ArrayList<Integer> Y;//从CSN还原序列
	
	public CSN(ArrayList<Integer> x) {
		X = x;
		Y = new ArrayList<Integer>(); 
//		for(int i = 0; i < X.size(); i++) {
//			Y.add(Integer.MAX_VALUE);
//		}
		getCSN();
		getXFromCSN();
	}
	
	private void getCSN() {
		ArrayList<Integer> W = new ArrayList<Integer>();
		for(int j = 0; j < X.size(); j++) {
			W.add(X.get(j));
		}
		W.sort(new MyComparator());
		M = W.get(0);
		T = W.get(W.size()-1) - M + 2;
		
		ArrayList<Long> A = new ArrayList<Long>();//X.size()+1
		A.add(0L);
		for(int i = 0; i < X.size(); i++) {
			A.add(i + 1, T*A.get(i) + X.get(i) - (M - 1)); 
		}
		CSN = A.get(A.size() - 1);
	}
	
	private void getXFromCSN() {
		ArrayList<Integer> Z = new ArrayList<Integer>();
		ArrayList<Long> A = new ArrayList<Long>();
		A.add(CSN);
		int k = 1;
		do {
			A.add(A.get(k - 1) / T);
			Z.add((int)(A.get(k - 1) % T + (M - 1)));
			k++;
		} while(A.get(A.size()-1) != 0);
        Collections.reverse(Z);
        Y = Z;
	}
	
	public ArrayList<Integer> getY() {
		return Y;
	}
	
	public int getM() {
		return M;
	}
	
	public int getT() {
		return T;
	}
	
	public long getSpaceOfCSN() {
		return Math.round((double)(Math.log(CSN)) / (Math.log(2))) + 1 + 2 * Integer.SIZE;
	}
	
	public static void main(String[] args) throws Exception {
		ArrayList<Integer>  x = new ArrayList<Integer>();
		Random random = new Random();
		for(int i=0; i < 10;) {
			int r = random.nextInt(20);
			if(!x.contains(r)) {
				x.add(r);
				i++;
			}
		}
		System.out.println(x);
		CSN csn = new CSN(x);
		System.out.println(x.size());
		System.out.println(csn.getSpaceOfCSN());
		System.out.println(csn.getM());
		System.out.println(csn.getT());
		System.out.println(csn.getY());
	}
}
