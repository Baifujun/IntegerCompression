package cn.edu.gzu.fjbai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * @author fjbai
 * ��ABC�㷨����Ѱ��һ���������е����ŷֶη�ʽ
 */
public class ABC {

	private int NP;//��Դ����
	private int D;//��Դά��
	private ArrayList<ArrayList<Integer>> Foods;//��Դ����
	private ArrayList<Integer> X;//��ѹ����������
	private ArrayList<Integer> Limit;//��Դδ���¼���
	private ArrayList<Double> P;//����Դ��ѡ��ĸ���
	private ArrayList<Integer> bestFood; //�����Դ
	private ArrayList<Double> fitnessValues;//����Դ��Ӧ��ֵ
	private double bestFitness = Integer.MIN_VALUE;//�����Ӧ��ֵ
	private int LIMIT;//��Դδ������ֵ�����乤������
	private int LB;//��Դ��ά��Сֵ
	private int UB;//��Դ��ά���ֵ
	private int a;//��Դ������Χ���Ʋ���
	private int maxCycle = 2500;
//	private int runtime = 30;

	/**
	 * �˹���Ⱥ���췽�����������ã���ʼ����Դ
	 * @param np ��Դ����
	 * @param d ��Դά������
	 * @param limit ��Դδ������ֵ
	 * @param a ��Դ�ѷ�Χ��������
	 * @param cycle ����䣬����䣬������������
	 * @param x ��ѹ����������
	 */
	public ABC(int np, int d ,int limit, int a, int cycle, ArrayList<Integer> x) {
		NP = np;
		D = d;
		this.a = a;
		maxCycle = cycle;
		Foods = new ArrayList<ArrayList<Integer>>(NP);
		bestFood = new ArrayList<Integer>(D);
		fitnessValues = new ArrayList<Double>(NP);
		X = x;
		LB = 1;
		UB = X.size() - 1;
		LIMIT = limit;
		Limit = new ArrayList<Integer>(NP);
		for(int i =0; i < NP; i++) {
			Limit.add(0);
		}
		P = new ArrayList<Double>(NP);
		for(int i =0; i < NP; i++) {
			P.add(0.0);
		}
		
		for(int i = 0; i < NP; i++) {//��Դ��ʼ��
			ArrayList<Integer> Y = new ArrayList<Integer>(D);
			int r;
			for(int j = 0; j < D; j++) {
				do {
					r = LB + (int) (Math.random() * (UB - LB + 1));//��Դ��ά��ֵ��ʼ��Ϊ[1,UB]֮������ֵ
					if(!Y.contains(r)) {
						Y.add(j, r);
						break;
					}
				} while(true);
			}
			Y.sort(new MyComparator());
			Foods.add(i, Y);
		}
	}
	
	/**
	 * Ϊÿ����Դ������Ӧ��
	 */
	private void calcFitnesses() {
		for(int i = 0; i < NP; i++) {
			ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>(D + 1);//�����зֶ�
			for(int j = 0; j < D + 1; j++) {
				int k;
				boolean flag = false;
				if(j == 0) {
					k = 0;
				} else {
					k = Foods.get(i).get(j - 1);	
					if((j - 1) == (D - 1)) 
						flag = true;
				}
				ArrayList<Integer> al = new ArrayList<Integer>();
				
				if(flag == false)
					for( ; k < Foods.get(i).get(j); k++) {
					    al.add(X.get(k));
				    }
				else
					for(int l = Foods.get(i).get(D - 1); l < X.size(); l++) {
						al.add(X.get(l));
					}
				list.add(al);
			}

			int t = 0;
			for(int l = 0; l < list.size(); l++) {//�Ը��ֶμ�����Ӧ��ֵ
				ArrayList<Integer> array = new ArrayList<Integer>();
				for(int s = 0; s < list.get(l).size(); s++) {
					array.add(list.get(l).get(s));
				}
				array.sort(new MyComparator());
				
				int m = array.get(0);
				t += array.get(array.size() - 1) - m + 2;
			}
			double t1 = (double)(list.size()) / (double)(t);
            if(fitnessValues.size() != NP)
            	fitnessValues.add(t1);
            else
			    fitnessValues.set(i, t1);
		}

	}
	
	
	/**
	 * Ϊָ����Դ������Ӧ��
	 * @param food ��Դ
	 * @return
	 */
	private Double calcFitness(ArrayList<Integer> food) {
		ArrayList<Integer> F = food;
		Double res;
		
		ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>(D + 1);
		for(int j = 0; j < D + 1; j++) {
			int k;
			boolean flag = false;
			if(j == 0) {
				k = 0;
			} else {
				k = F.get(j - 1);
				if((j - 1) == (D - 1)) 
					flag = true;
			}
			ArrayList<Integer> al = new ArrayList<Integer>();
			
			if(flag == false) {
				for( ; k < F.get(j); k++) {
					al.add(X.get(k));
				}
			}
			else {
				for(int l = F.get(D - 1); l < X.size(); l++) {
					al.add(X.get(l));
				}
			} 
			list.add(al);
		}

		int t = 0;
		for(int l = 0; l < list.size(); l++) {
			ArrayList<Integer> array = new ArrayList<Integer>();
			for(int s = 0; s < list.get(l).size(); s++) {
				array.add(list.get(l).get(s));
			}
			array.sort(new MyComparator());
	
			int m = array.get(0);
			t += array.get(array.size() - 1) - m + 2;
		}
		double t1 = (double)(list.size()) / (double)(t);
		res = t1;
		return res;
	}
	
	
	/**
	 * ������Դ
	 * @param 
	 * @return
	 */
	private void updateFoods() {
		for(int i = 0; i < Foods.size(); i++) {
		
			ArrayList<Integer> al = new ArrayList<Integer>();//��Դֵ
			double f1 = fitnessValues.get(i);//���ԭ��Ӧ��ֵ

			for(int o = 0; o < Foods.get(i).size(); o++) {
				al.add(Foods.get(i).get(o));
			}		
			
			ArrayList<Integer> sList = new ArrayList<Integer>();
			int s;
			do {
				s = (int)(Math.random() * D);//s->[0,D-1]�����ѡ��һά
				if(!sList.contains(s)) {
					sList.add(s);
					
					ArrayList<Integer> qList =  new ArrayList<Integer>();
					for(int q = al.get(s) - a; q <= al.get(s) + a; q++) {
						if((q >= LB) && (q <= UB) && (!al.contains(q))) {
							qList.add(q);			    
						}
					}
					if(qList.size() != 0) {
						int g = (int) Math.random() * qList.size();
						al.set(s, qList.get(g));
						break;
					} 
				}
			} while(true);	
			al.sort(new MyComparator());//�����������Դ
			
			double f2 = calcFitness(al);
			if(f1 < f2) {//�ҵ������ʵ���Դ
				int z = 0;
				for(Iterator<Integer> iter = al.iterator(); iter.hasNext();) {//��Դ����
					Foods.get(i).set(z, iter.next());
					z++;
				}			
				fitnessValues.set(i, f2);//������Ӧ��ֵ
				Limit.set(i, 0);//��Դ���º�������Դδ���¼���
			}
			else {
				Limit.set(i, Limit.get(i) + 1);//δ���¼���
				judgeSolution();//�ж�δ�����Ƿ񵽴�LIMIT		
			}
		}	
	}

	/**
	 * Ϊ����Դ���㱻ѡ�����
	 */
	private void calcProbabilities() {
		double f = 0.0;
		for(int i = 0; i < Foods.size(); i++) {
			f += fitnessValues.get(i);
		}
		
		for(int i = 0; i < Foods.size(); i++) {
			P.set(i, fitnessValues.get(i) / f);
		}
	}
	
	/**
	 * ��������̶�ѡ����Դ��������Դ��������Դ
	 */
	private void RouletteDice() {
		for(int i = 0; i < NP; i++) {
			double p = 0.0;
			double r = Math.random();
			for(int j = 0; j < P.size(); j++) {
				p += P.get(j);
				if(r < p){ //���̶�ѡ����Դ		
					ArrayList<Integer> al = new ArrayList<Integer>();//��Դֵ
					for(int o = 0; o < D; o++) {
						al.add(Foods.get(j).get(o));
					}		
		
					ArrayList<Integer> sList = new ArrayList<Integer>();
					int s;
					do {
						s = (int)(Math.random() * D);//s->[0,D-1]�����ѡ��һά
						if(!sList.contains(s)) {
							sList.add(s);
							
							ArrayList<Integer> qList =  new ArrayList<Integer>();
							for(int q = al.get(s) - a; q <= al.get(s) + a; q++) {
								if((q >= LB) && (q <= UB) && (!al.contains(q))) {
									qList.add(q);			    
								}
							}
							if(qList.size() != 0) {
								int g = (int) Math.random() * qList.size();
								al.set(s, qList.get(g));
								break;
							} 
						}
					} while(true);	
					al.sort(new MyComparator());//�����������Դ
					
					double f2 = calcFitness(al);//����Դ����Ӧ��ֵ
					double f1 = P.get(j);//calcFitness(Foods.get(j));
					if(f1 < f2) {//�ҵ����ŵ���Դ����������Դ					
						int temp;//������Դ
						for(int u = 0; u < al.size(); u++) {
							temp = al.get(u);
							Foods.get(j).set(u, temp);
						}						
					    fitnessValues.set(j, f2);//������Ӧ��
					    Limit.set(j, 0);//��Դ���º�����δ���¼���
					}
					else {
					    Limit.set(j, Limit.get(j) + 1);			    
					    judgeSolution();
					}
					break;
				}			
			}
		}
	}
	
	/**
	 * �ж���Դδ���´����Ƿ�ﵽ��ֵ�����ﵽ�����ѡ��һ����Դ�滻ԭ����Դ
	 */
	private void judgeSolution() {
		findOutBest();
		for(int i = 0; i < Limit.size(); i++) {
			if(Limit.get(i) >= LIMIT) {
				ArrayList<Integer> Y = new ArrayList<Integer>(D);
				for(int j = 0; j < D; j++) {
					int r;
					do {
						r = LB + (int) (Math.random() * (UB - LB + 1));
						if(!Y.contains(r)) {
							Y.add(j, r);
							break;
						}
					} while(true);
				}
				Y.sort(new MyComparator());
				for(int k = 0; k < Y.size(); k++) {
					Foods.get(i).set(k, Y.get(k));
				}	
				fitnessValues.set(i, calcFitness(Y));
				Limit.set(i, 0);
			}
		}
	}
	
	/**
	 * �ҳ������Դ�����Ž⣩
	 * */
	private void findOutBest() {
		for(int i = 0; i < fitnessValues.size(); i++) {
			if(fitnessValues.get(i) > bestFitness) {
				bestFitness = fitnessValues.get(i);
				
				if(bestFood.size() == 0)
					bestFood = Foods.get(i);
				else {
					for(int j = 0 ;j < Foods.get(i).size(); j++) {
					bestFood.set(j, Foods.get(i).get(j));
				}
				}	
			}
		}
	}
	
	/**
	 * ABC�㷨�������й���
	 */
	private void run() {
		for(int i = 0; i < maxCycle; i++) {
			calcFitnesses();//�����������Դ����Ӧ��ֵ
			updateFoods();//���������������Դ
			calcProbabilities();//��������ѡ����Դ����
			RouletteDice();//���������ѡ����Դ�������͸�����Դ
		}
	}
	
	/**
	 * ABC�㷨Ψһ�������
	 */
	public ArrayList<Integer> getBestFood() {
		run();
		return this.bestFood;
	}
	
	/**
	 * ABC�㷨����
	 */
	public static void main(String[] args) {
		ArrayList<Integer>  x = new ArrayList<Integer>();
		Random random = new Random();
		for(int i=0; i < 10;) {
			int r = random.nextInt(50);
			if(!x.contains(r)) {
				x.add(r);
				i++;
			}
		}
		ABC abc = new ABC(3, 3, 4, 2, 40, x);
		System.out.println(abc.getBestFood());
	}
}
