package cn.edu.gzu.fjbai;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.google.common.math.BigIntegerMath;

public class CSCA {

	private ArrayList<Integer> X; //待压缩整数序列
	private ArrayList<ArrayList<Integer>> bestDivision; //最佳分段数下的最优分段方式
	private double T; //最优分段方式下的平均T值
	private int P; //最佳分段数
	private ArrayList<Integer> Ts; //每个分段的T值
	private ArrayList<Integer> Ms; //每个分段的m值
	private ArrayList<BigInteger> CSNs; //每个分段的CSN值
	private ArrayList<Integer> Y; //从CSN还原序列
	
	public CSCA(ArrayList<Integer> x) throws Exception {
		X = x;
		bestDivision = new ArrayList<ArrayList<Integer>>();
		T = Integer.MAX_VALUE;
		P = Integer.MAX_VALUE;
		Ts = new ArrayList<Integer>();
		Ms = new ArrayList<Integer>();
		CSNs = new ArrayList<BigInteger>();
		getCSNs(X);
		getXFromCSNs();
		
	}
	
	private void getCSNs(ArrayList<Integer> x) {
		X = x;
        /*拷贝序列到W并按小到大排序，搜索最大最小值*/
		ArrayList<Integer> W = new ArrayList<Integer>();
		for(Iterator<Integer> iter = x.iterator(); iter.hasNext();) {
			W.add(iter.next());
		}
		W.sort(new MyComparator());
		
		int m = W.get(0);//长序列参数m
		T = W.get(W.size() - 1) - m + 2;//长序列参数T
		int p = 1;//最佳分段数
		
		for(int i = 2; i < X.size() - 2; i++) {//分段数i
			ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>(i);//长序列分段
			ArrayList<Integer> blockSize= new ArrayList<Integer>(i);
			for(int j = 0 ;j < i; j++) {//对于每一段j
				if(j < (X.size() % i)) {
					blockSize.add(X.size()/i + 1);
				} else {
					blockSize.add(X.size()/i);
				}	
				ArrayList<Integer> Z = new ArrayList<Integer>(blockSize.get(j));//每一小段
				int k = 0;
				for(int q = 0; q < j; q++)
					k += blockSize.get(q);
				for(int l = 0; l < blockSize.get(j); l++)
				    Z.add(X.get(k + l));
				list.add(Z);
			}
			
			//System.out.println(list);
			
			ArrayList<Integer> TList = new ArrayList<Integer>(list.size());
			for(int a = 0; a < list.size(); a++) {
				ArrayList<Integer> V = new ArrayList<Integer>();
				for(Iterator<Integer> iter = list.get(a).iterator(); iter.hasNext();) {
					V.add(iter.next());
				}
				V.sort(new MyComparator());
				TList.add(V.get(V.size() - 1) - V.get(0) + 2);//对于已排序的序列计算T
				//V.clear();
			}
			int t1 = 0;
			for(Iterator<Integer> iter = TList.iterator(); iter.hasNext(); ) {
				t1 += iter.next();
			}
			double t = (double)(t1) / list.size();
			if(t < T) {
				T = t;
				p = i;
				bestDivision = list;
			}
		}
		P = p;	
		
		for(int i = 0; i < bestDivision.size(); i++) {
			ArrayList<Integer> temp = new ArrayList<Integer>();
			for(int j = 0; j < bestDivision.get(i).size(); j++) {
				temp.add(bestDivision.get(i).get(j));
			}		
			temp.sort(new MyComparator());
			Ms.add(temp.get(0));
			Ts.add(temp.get(temp.size() - 1) - temp.get(0) + 2);
			CSNP csn = new CSNP(bestDivision.get(i));	
			CSNs.add(csn.getCSN());	
		}
	}
	
	public ArrayList<ArrayList<Integer>> getBestDivision() {
		return this.bestDivision;
	}
	
	public double getAvgT() {
		return this.T;
	}
	
	public ArrayList<Integer> getTs() {
		return this.Ts;
	}
	
    public ArrayList<Integer> getMs() {
		return this.Ms;
	}
	
    public int getP() {
    	return P;
    }
    
    public ArrayList<Integer> getY() {
    	return Y;
    }
    
    private void getXFromCSNs() throws Exception {
    	ArrayList<Integer> x = new ArrayList<Integer>();
    	for(int i = 0; i < bestDivision.size(); i++) {
			ArrayList<Integer> temp = bestDivision.get(i);
			CSNP csn = new CSNP(temp);
			if(!x.addAll(csn.getY()))
				throw new Exception();
		}
    	Y = x;
    }
    
    public long getSpaceOfCSNs() {
    	int s = 0;
    	for(int i = 0; i < CSNs.size(); i++) {
    		s += BigIntegerMath.log2(CSNs.get(i), RoundingMode.HALF_EVEN) + 2 * Integer.SIZE;
    	}
    	return s;
    }
    
	public static void main(String[] args) throws Exception {
		ArrayList<Integer>  x = new ArrayList<Integer>();
		Random random = new Random();
		for(int i=0; i < 20;) {
			int r = random.nextInt(30);
			if(!x.contains(r)) {
				x.add(r);
				i++;
			}
		}
		System.out.println(x);
		CSCAABC cscaabc = new CSCAABC(x);
		System.out.println(cscaabc.getBestDivision());
		System.out.println(cscaabc.getSpaceOfCSNs());
		System.out.println(cscaabc.getY());
	}
}
