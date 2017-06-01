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
		System.out.println("��ѹ������XΪ��\n" + X + "\n\n");
		
		System.out.println("----��ѹ��----");
		System.out.println("����X�ַ��洢��ռ�ռ�Ϊ��" + new IntegerToString(X).getSpace());
		System.out.println("����X���ֳ��洢��ռ�ռ�Ϊ��" + Integer.SIZE * X.size() + "\n\n");
		
		System.out.println("----CSNѹ��----");
		CSNP csn = new CSNP(X);
		System.out.println("����X�Ĳ���mΪ��"+csn.getM());
		System.out.println("����X�Ĳ���TΪ��"+ csn.getT());
		System.out.println("����X��CSNֵΪ��"+ csn.getCSN());
		System.out.println("����Xѹ������ռ�ռ�Ϊ��"+ csn.getSpaceOfCSN());
		System.out.println("ѹ����Ϊ��" + (double) (Integer.SIZE * X.size() - csn.getSpaceOfCSN()) / Integer.SIZE + "\n\n");
		
		System.out.println("----���CSNѹ��----");
		ArrayList<Integer>  dX = DiffEnc.diffEnco(X);
		System.out.println("X�Ĳ������dXΪ��\n" + dX);
		CSNP dcsn = new CSNP(dX);
		System.out.println("����dX�Ĳ���mΪ��"+dcsn.getM());
		System.out.println("����dX�Ĳ���TΪ��"+ dcsn.getT());
		System.out.println("����dX��CSNֵΪ��"+ dcsn.getCSN());
		System.out.println("����dXѹ������ռ�ռ�Ϊ��"+ dcsn.getSpaceOfCSN());
		System.out.println("ѹ����Ϊ��" + (double) (Integer.SIZE * X.size() - dcsn.getSpaceOfCSN()) / Integer.SIZE + "\n\n");
		
		System.out.println("----CSCAѹ��----");
		CSCA csca = new CSCA(X);
		System.out.println("����Xѹ������ռ�ռ�Ϊ��"+ csca.getSpaceOfCSNs());
		System.out.println("ѹ����Ϊ��" + (double) (Integer.SIZE * X.size() - csca.getSpaceOfCSNs()) / Integer.SIZE + "\n\n");
		
		System.out.println("----���CSCAѹ��----");
		CSCA dcsca = new CSCA(dX);
		System.out.println("����dXѹ������ռ�ռ�Ϊ��"+ dcsca.getSpaceOfCSNs());
		System.out.println("ѹ����Ϊ��" + (double) (Integer.SIZE * X.size() - dcsca.getSpaceOfCSNs()) / Integer.SIZE + "\n\n");
		
		System.out.println("----CSCAABCѹ��----");
		CSCAABC cscaabc = new CSCAABC(X);
		System.out.println("����Xѹ������ռ�ռ�Ϊ��"+ cscaabc.getSpaceOfCSNs());
		System.out.println("ѹ����Ϊ��" + (double) (Integer.SIZE * X.size() - cscaabc.getSpaceOfCSNs()) / Integer.SIZE + "\n\n");
		
		System.out.println("----CSCAABC���ѹ��----");
		CSCAABC dcscaabc = new CSCAABC(X);
		System.out.println("����dXѹ������ռ�ռ�Ϊ��"+ dcscaabc.getSpaceOfCSNs());
		System.out.println("ѹ����Ϊ��" + (double) (Integer.SIZE * X.size() - dcscaabc.getSpaceOfCSNs()) / Integer.SIZE + "\n\n");
		
		System.out.println("------------------------------end--------------------------------");
	}
}

