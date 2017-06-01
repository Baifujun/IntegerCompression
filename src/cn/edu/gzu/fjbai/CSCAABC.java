package cn.edu.gzu.fjbai;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.google.common.math.BigIntegerMath;

public class CSCAABC {

	private ArrayList<Integer> X; //��ѹ����������
	private ArrayList<ArrayList<Integer>> bestDivision; //��ѷֶ����µ����ŷֶη�ʽ
	private double T; //���ŷֶη�ʽ�µ�ƽ��Tֵ
	private int P; //��ѷֶ���
	private ArrayList<Integer> Ts; //ÿ���ֶε�Tֵ
	private ArrayList<Integer> Ms; //ÿ���ֶε�mֵ
	private ArrayList<BigInteger> CSNs; //ÿ���ֶε�CSNֵ
	private ArrayList<Integer> Y; //��CSN��ԭ����
	
	public CSCAABC(ArrayList<Integer> x) throws Exception {
		X = x;
		bestDivision = new ArrayList<ArrayList<Integer>>();
		T = Integer.MAX_VALUE;
		P = Integer.MAX_VALUE;
		Ts = new ArrayList<Integer>();
		Ms = new ArrayList<Integer>();
		CSNs = new ArrayList<BigInteger>();
		getCSNsWithABC(X);
		getXFromCSNsWithABC();
		
	}
	
	private void getCSNsWithABC(ArrayList<Integer> x) {
		X = x;
        /*�������е�W����С�����������������Сֵ*/
		ArrayList<Integer> W = new ArrayList<Integer>();
		for(Iterator<Integer> iter = x.iterator(); iter.hasNext();) {
			W.add(iter.next());
		}
		W.sort(new MyComparator());
		
		int m = W.get(0);//�����в���m
		T = W.get(W.size() - 1) - m + 2;//�����в���T
		int p = 1;//��ѷֶ���
		
		for(int i = 2; i < X.size() - 2; i++) {//�ֶ���i
			
			/*�˹���Ⱥ�㷨�Ż��ֶη�ʽ*/
			ABC abc = new ABC(30, i-1, 8, 5, 500, X);
			ArrayList <Integer> bestFood = abc.getBestFood();
	
			ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>(i);//�����зֶ�
			for(int j = 0; j < i; j++) {
				int k;
				boolean flag = false;
				if(j == 0) {
					k = 0;
				} else {
					k = bestFood.get(j - 1);	
					if((j - 1) == (i - 1 - 1)) 
						flag = true;
				}
				ArrayList<Integer> al = new ArrayList<Integer>();
				
				if(flag == false)
					for( ; k < bestFood.get(j); k++) {
					    al.add(X.get(k));
				    }
				else
					for(int l = bestFood.get(i - 1 - 1); l < X.size(); l++) {
						al.add(X.get(l));
					}
				list.add(al);
			}

			ArrayList<Integer> TList = new ArrayList<Integer>(list.size());
			for(int a = 0; a < list.size(); a++) {
				ArrayList<Integer> V = new ArrayList<Integer>();
				for(Iterator<Integer> iter = list.get(a).iterator(); iter.hasNext();) {
					V.add(iter.next());
				}
				V.sort(new MyComparator());
				TList.add(V.get(V.size() - 1) - V.get(0) + 2);//��������������м���T
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
    
    private void getXFromCSNsWithABC() throws Exception {
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
