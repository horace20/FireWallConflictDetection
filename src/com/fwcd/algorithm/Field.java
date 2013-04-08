package com.fwcd.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * �ж�����
 * 
 * @author horace
 * 
 */
public class Field {
	private List<Value> value;

	public Field() {
		value = new ArrayList<Value>();
	}

	// �ж��������Ƿ���ȣ�����ֻ�򵥵��ж���ֵ�Ƿ����
	public boolean isEqual(Field dstField) {
		List<Value> vlist_temp = dstField.getValue();
		if (value.size() != vlist_temp.size()) {
			return false;
		} else {
			for (int vIndex = 0; vIndex < value.size(); vIndex++) {
				if (!value.get(vIndex).isEqual(vlist_temp.get(vIndex))) {
					return false;
				}
			}
		}
		return true;
	}

	// ��������Ĳ
	public static Field calcDifferenceSet(Field m, Field s) {
		Field result = new Field();
		result.getValue().addAll(
				Value.calcDifferenceSet(m.getValue(), s.getValue()));
		return result;
	}

	// ��������Ľ���
	public static Field calcIntersect(Field a, Field b) {
		Field result = new Field();
		List<Value> vlist_temp = Value
				.calcIntersect(a.getValue(), b.getValue());
		if (vlist_temp.size() != 0) {
			result.setValue(vlist_temp);
		}
		return result;
	}

	public List<Value> getValue() {
		return value;
	}

	public void addValue(Value v) {
		value.add(v);
	}

	public void setValue(List<Value> value) {
		this.value = value;
	}

}
