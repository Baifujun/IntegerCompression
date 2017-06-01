package cn.edu.gzu.fjbai;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.google.common.math.BigIntegerMath;

public class CSNP {

	private ArrayList<Integer> X; //待压缩序列
	private BigInteger CSN; //序列X的CSN值
	private int M; //参数M
	private int T; //参数T
	private ArrayList<Integer> Y;//从CSN还原序列
	
	public CSNP(ArrayList<Integer> x) {
		X = x;
		getCSN();
		getXFromCSN();
	}
	
	public BigInteger getCSN() {
		
		ArrayList<Integer> W = new ArrayList<Integer>();
		for(int j = 0; j < X.size(); j++) {
			W.add(X.get(j));
		}
		W.sort(new MyComparator());
		M = W.get(0);
		T = W.get(W.size()-1) - M + 2;
	
		ArrayList<BigInteger> A = new ArrayList<BigInteger>();//X.size()+1
		A.add(new BigInteger("0"));
		for(int i = 0; i < X.size(); i++) {
			//A.add(i + 1, T*A.get(i) + X.get(i) - (m - 1)); 
			A.add(i + 1, A.get(i).multiply(new BigInteger( String.valueOf(T) ) )
					.add(new BigInteger(String.valueOf(X.get(i))))
					.subtract(new BigInteger(String.valueOf(M - 1)))
					);
		}
		CSN = A.get(A.size() - 1);
		return CSN;
	}
	
	private void getXFromCSN() {
		ArrayList<Integer> Z = new ArrayList<Integer>();
		ArrayList<BigInteger> A = new ArrayList<BigInteger>();
		A.add(CSN);
		int k = 1;
		do {
			//A.add(k, A.get(k - 1) / T);
			A.add(k, A.get(k - 1).divide(new BigInteger(String.valueOf(T))));
			//X.add(k - 1, (int)(A.get(k - 1) % T + (m - 1)));
			Z.add(k - 1, A.get(k - 1).remainder(new BigInteger(String.valueOf(T)))
					.add(new BigInteger(String.valueOf(M - 1)))
					.intValue()
					);
			k++;
		} while(!A.get(A.size()-1).equals(new BigInteger("0")));
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
	
	public int getSpaceOfCSN() {
		return BigIntegerMath.log2(CSN, RoundingMode.HALF_EVEN) + 2 * Integer.SIZE;
	}
	
	public static void main(String[] args) throws Exception {
		ArrayList<Integer>  x = new ArrayList<Integer>();
		Random random = new Random();
		for(int i=0; i < 10000;) {
			int r = random.nextInt(50000);
			if(!x.contains(r)) {
				x.add(r);
				i++;
			}
		}
		System.out.println(x);
		CSNP csn = new CSNP(x);
		System.out.println(csn.getSpaceOfCSN());
		System.out.println(x.size());
		System.out.println(csn.getY());
	}
}
