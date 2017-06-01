package cn.edu.gzu.fjbai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * @author fjbai
 * 本ABC算法用于寻找一个整数序列的最优分段方式
 */
public class ABC {

	private int NP;//蜜源数量
	private int D;//蜜源维度
	private ArrayList<ArrayList<Integer>> Foods;//蜜源编码
	private ArrayList<Integer> X;//带压缩整数序列
	private ArrayList<Integer> Limit;//蜜源未更新计数
	private ArrayList<Double> P;//各蜜源被选择的概率
	private ArrayList<Integer> bestFood; //最佳蜜源
	private ArrayList<Double> fitnessValues;//各蜜源适应度值
	private double bestFitness = Integer.MIN_VALUE;//最大适应度值
	private int LIMIT;//蜜源未更新阈值，侦查蜂工作条件
	private int LB;//蜜源各维最小值
	private int UB;//蜜源各维最大值
	private int a;//蜜源搜索范围控制参数
	private int maxCycle = 2500;
//	private int runtime = 30;

	/**
	 * 人工蜂群构造方法，参数设置，初始化蜜源
	 * @param np 蜜源数量
	 * @param d 蜜源维度数量
	 * @param limit 蜜源未更新阈值
	 * @param a 蜜源搜范围控制因子
	 * @param cycle 引领蜂，跟随蜂，针擦蜂迭代次数
	 * @param x 带压缩整形数据
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
		
		for(int i = 0; i < NP; i++) {//蜜源初始化
			ArrayList<Integer> Y = new ArrayList<Integer>(D);
			int r;
			for(int j = 0; j < D; j++) {
				do {
					r = LB + (int) (Math.random() * (UB - LB + 1));//蜜源各维的值初始化为[1,UB]之间的随机值
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
	 * 为每个蜜源计算适应度
	 */
	private void calcFitnesses() {
		for(int i = 0; i < NP; i++) {
			ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>(D + 1);//长序列分段
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
			for(int l = 0; l < list.size(); l++) {//对各分段计算适应度值
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
	 * 为指定蜜源计算适应度
	 * @param food 蜜源
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
	 * 更新蜜源
	 * @param 
	 * @return
	 */
	private void updateFoods() {
		for(int i = 0; i < Foods.size(); i++) {
		
			ArrayList<Integer> al = new ArrayList<Integer>();//蜜源值
			double f1 = fitnessValues.get(i);//获得原适应度值

			for(int o = 0; o < Foods.get(i).size(); o++) {
				al.add(Foods.get(i).get(o));
			}		
			
			ArrayList<Integer> sList = new ArrayList<Integer>();
			int s;
			do {
				s = (int)(Math.random() * D);//s->[0,D-1]，随机选择一维
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
			al.sort(new MyComparator());//已排序的新蜜源
			
			double f2 = calcFitness(al);
			if(f1 < f2) {//找到更优质的蜜源
				int z = 0;
				for(Iterator<Integer> iter = al.iterator(); iter.hasNext();) {//蜜源更新
					Foods.get(i).set(z, iter.next());
					z++;
				}			
				fitnessValues.set(i, f2);//更新适应度值
				Limit.set(i, 0);//蜜源更新后，重置蜜源未更新计数
			}
			else {
				Limit.set(i, Limit.get(i) + 1);//未更新计数
				judgeSolution();//判断未更新是否到达LIMIT		
			}
		}	
	}

	/**
	 * 为各蜜源计算被选择概率
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
	 * 跟随蜂轮盘赌选择蜜源，搜索蜜源，更新蜜源
	 */
	private void RouletteDice() {
		for(int i = 0; i < NP; i++) {
			double p = 0.0;
			double r = Math.random();
			for(int j = 0; j < P.size(); j++) {
				p += P.get(j);
				if(r < p){ //轮盘赌选择蜜源		
					ArrayList<Integer> al = new ArrayList<Integer>();//蜜源值
					for(int o = 0; o < D; o++) {
						al.add(Foods.get(j).get(o));
					}		
		
					ArrayList<Integer> sList = new ArrayList<Integer>();
					int s;
					do {
						s = (int)(Math.random() * D);//s->[0,D-1]，随机选择一维
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
					al.sort(new MyComparator());//已排序的新蜜源
					
					double f2 = calcFitness(al);//新蜜源的适应度值
					double f1 = P.get(j);//calcFitness(Foods.get(j));
					if(f1 < f2) {//找到更优的蜜源，并更新蜜源					
						int temp;//更新蜜源
						for(int u = 0; u < al.size(); u++) {
							temp = al.get(u);
							Foods.get(j).set(u, temp);
						}						
					    fitnessValues.set(j, f2);//更新适应度
					    Limit.set(j, 0);//蜜源更新后，重置未更新计数
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
	 * 判断蜜源未更新次数是否达到阈值，若达到则随机选择一个蜜源替换原有蜜源
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
	 * 找出最佳蜜源（最优解）
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
	 * ABC算法迭代运行过程
	 */
	private void run() {
		for(int i = 0; i < maxCycle; i++) {
			calcFitnesses();//引领蜂计算各蜜源的适应度值
			updateFoods();//引领蜂搜索更新蜜源
			calcProbabilities();//跟随蜂计算选择蜜源概率
			RouletteDice();//跟随蜂轮盘选择蜜源并搜索和更新蜜源
		}
	}
	
	/**
	 * ABC算法唯一运行入口
	 */
	public ArrayList<Integer> getBestFood() {
		run();
		return this.bestFood;
	}
	
	/**
	 * ABC算法测试
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
