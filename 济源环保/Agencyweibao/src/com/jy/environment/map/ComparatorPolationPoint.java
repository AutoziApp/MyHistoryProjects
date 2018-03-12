package com.jy.environment.map;

import java.util.Comparator;



public class ComparatorPolationPoint implements Comparator {

	@Override
	public int compare(Object arg0, Object arg1) {
		PolationPoint p=(PolationPoint)arg0;
		PolationPoint p1=(PolationPoint)arg1;
		return p.compare(p1);
	}

}
