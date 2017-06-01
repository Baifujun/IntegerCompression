package cn.edu.gzu.fjbai;

import java.util.Comparator;

public class MyComparator implements Comparator<Object> {

	@Override
	public int compare(Object arg0, Object arg1) {
		Integer t1 = (Integer)arg0;
		Integer t2 = (Integer)arg1;
		if(t1 > t2)
			return 1;
		else if(t1 < t2)
			return -1;
		else return 0;
	}

}
