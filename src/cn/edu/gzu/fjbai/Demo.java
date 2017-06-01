package cn.edu.gzu.fjbai;


import java.util.ArrayList;
import java.util.Random;


public class Demo {

	public static void main(String[] args) throws Exception {
		System.out.println("------------------------------start------------------------------");

		ArrayList<Integer>  X = new ArrayList<Integer>();
		Random random = new Random();
		for(int i=0; i < 100;) {
			int r = random.nextInt(200);
			if(!X.contains(r)) {
				X.add(r);
				i++;
			}
		}
		X.sort(new MyComparator());
		System.out.println("待压缩序列X为：\n" + X + "\n\n");
		
		System.out.println("----无压缩----");
		System.out.println("序列X字符存储所占空间为：" + new IntegerToString(X).getSpace());
		System.out.println("序列X单字长存储所占空间为：" + Integer.SIZE * X.size() + "\n\n");
		
		System.out.println("----CSN压缩----");
		CSNP csn = new CSNP(X);
		System.out.println("序列X的参数m为："+csn.getM());
		System.out.println("序列X的参数T为："+ csn.getT());
		System.out.println("序列X的CSN值为："+ csn.getCSN());
		System.out.println("序列X压缩后所占空间为："+ csn.getSpaceOfCSN());
		System.out.println("压缩率为：" + (double) (Integer.SIZE * X.size() - csn.getSpaceOfCSN()) / Integer.SIZE + "\n\n");
		
		System.out.println("----差分CSN压缩----");
		ArrayList<Integer>  dX = DiffEnc.diffEnco(X);
		System.out.println("X的差分序列dX为：\n" + dX);
		CSNP dcsn = new CSNP(dX);
		System.out.println("序列dX的参数m为："+dcsn.getM());
		System.out.println("序列dX的参数T为："+ dcsn.getT());
		System.out.println("序列dX的CSN值为："+ dcsn.getCSN());
		System.out.println("序列dX压缩后所占空间为："+ dcsn.getSpaceOfCSN());
		System.out.println("压缩率为：" + (double) (Integer.SIZE * X.size() - dcsn.getSpaceOfCSN()) / Integer.SIZE + "\n\n");
		
		System.out.println("----CSCA压缩----");
		CSCA csca = new CSCA(X);
		System.out.println("序列X压缩后所占空间为："+ csca.getSpaceOfCSNs());
		System.out.println("压缩率为：" + (double) (Integer.SIZE * X.size() - csca.getSpaceOfCSNs()) / Integer.SIZE + "\n\n");
		
		System.out.println("----差分CSCA压缩----");
		CSCA dcsca = new CSCA(dX);
		System.out.println("序列dX压缩后所占空间为："+ dcsca.getSpaceOfCSNs());
		System.out.println("压缩率为：" + (double) (Integer.SIZE * X.size() - dcsca.getSpaceOfCSNs()) / Integer.SIZE + "\n\n");
		
		System.out.println("----CSCAABC压缩----");
		CSCAABC cscaabc = new CSCAABC(X);
		System.out.println("序列X压缩后所占空间为："+ cscaabc.getSpaceOfCSNs());
		System.out.println("压缩率为：" + (double) (Integer.SIZE * X.size() - cscaabc.getSpaceOfCSNs()) / Integer.SIZE + "\n\n");
		
		System.out.println("----CSCAABC差分压缩----");
		CSCAABC dcscaabc = new CSCAABC(X);
		System.out.println("序列dX压缩后所占空间为："+ dcscaabc.getSpaceOfCSNs());
		System.out.println("压缩率为：" + (double) (Integer.SIZE * X.size() - dcscaabc.getSpaceOfCSNs()) / Integer.SIZE + "\n\n");
		
		System.out.println("------------------------------end--------------------------------");
	}
}

