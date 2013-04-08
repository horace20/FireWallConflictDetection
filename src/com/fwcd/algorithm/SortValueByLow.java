package com.fwcd.algorithm;

import java.util.Comparator;

/**
 * ��Value�������Ȱ�lowֵ����low���ʱ��highֵ����
 * @author horace
 * 
 */
public class SortValueByLow implements Comparator<Value> {
	public int compare(Value v1, Value v2) {
		if (v1.getLow() > v2.getLow()) {
			return 1;
		} else if (v1.getLow() == v2.getLow()) {
			if (v1.getHigh() > v2.getHigh()) {
				return 1;
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}
}
