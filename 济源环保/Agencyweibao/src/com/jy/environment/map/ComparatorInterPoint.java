package com.jy.environment.map;

import java.util.Comparator;

public class ComparatorInterPoint implements Comparator {

	@Override
	public int compare(Object lhs, Object rhs) {
		InterPoint p=(InterPoint)lhs;
		InterPoint p1=(InterPoint)rhs;
		return p.compare(p1);
	}

}
