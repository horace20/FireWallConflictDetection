package com.fwcd.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ���������࣬Ϊһ�����䣬�洢����������޺����ޣ����ر���ΪActionʱ�洢Actionֵ
 * @author horace
 *
 */
public class Value implements Action {
	private long low;
	private long high;
	private int action;

	public Value() {
		low = 0;
		high = 0;
		action = NOT_ACTION;
	}

	public Value(long l, long h) {
		this.low = l;
		this.high = h;
		action = NOT_ACTION;
	}

	public Value(int action) {
		this.action = action;
		low = 0;
		high = 0;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public long getLow() {
		return low;
	}

	public void setLow(long low) {
		this.low = low;
	}

	public long getHigh() {
		return high;
	}

	public void setHigh(long high) {
		this.high = high;
	}

	public boolean isAction() {
		if (action == NOT_ACTION) {
			return false;
		} else {
			return true;
		}
	}

	// �ж�����ֵ�Ƿ����
	public boolean isEqual(Value dstValue) {
		if (action == NOT_ACTION) {
			if (low == dstValue.getLow() && high == dstValue.getHigh()) {
				return true;
			}
		} else if (action == dstValue.getAction()) {
			return true;
		}
		return false;
	}

	// �ж����鼯���Ƿ����(Ԫ��һһ��Ӧ��ȼ����)������������������ж���������һ��
	public static boolean isEqual(List<Value> v1, List<Value> v2) {
		if (v1.size() != v2.size()) {
			return false;
		} else {
			for (int i = 0; i < v1.size(); i++) {
				if (!v1.get(i).isEqual(v2.get(i))) {
					return false;
				}
			}
			return true;
		}
	}

	// ����һ��һ֪��Value����Ҫ����Ϊ�µ�Value��ֵ
	public static Value copyValue(Value value) {
		Value new_value = new Value();
		new_value.setLow(value.getLow());
		new_value.setHigh(value.getHigh());
		new_value.setAction(value.getAction());

		return new_value;
	}

	// ����鼯�ϵĽ���
	public static List<Value> calcIntersect(List<Value> valueListA,
			List<Value> valueListB) {
		List<Value> valueList = new ArrayList<Value>();

		for (Value v1 : valueListA) {
			for (Value v2 : valueListB) {
				Value v = Value.calcIntersect(v1, v2);
				if (v != null) {
					valueList.add(v);
				}
			}
		}

		return valueList;
	}

	// ���������ϵĽ���
	public static Value calcIntersect(Value a, Value b) {
		if (a == null || b == null) {
			return null;
		}

		if (a.isAction() || b.isAction()) {
			return null;
		}

		if ((a.getHigh() < b.getLow()) || (b.getHigh() < a.getLow())) {
			return null;
		}

		Value value = new Value();

		if (a.getHigh() > b.getHigh()) {
			value.setHigh(b.getHigh());
			if (a.getLow() > b.getLow()) {
				value.setLow(a.getLow());
			} else {
				value.setLow(b.getLow());
			}
		} else {
			value.setHigh(a.getHigh());
			if (a.getLow() > b.getLow()) {
				value.setLow(a.getLow());
			} else {
				value.setLow(b.getLow());
			}
		}

		return value;
	}

	// ��һ�鼯�ϵĲ���(ֻ�����ڱ��㷨�ж������ж������������ϵĽ�����)
	public static List<Value> calcUnionSet(List<Value> srcVList) {
		List<Value> result = new ArrayList<Value>();
		Collections.sort(srcVList, new SortValueByLow());

		Value vtmp;
		Value vtmp1;
		Value vtmp2;

		vtmp1 = srcVList.get(0);
		result.add(vtmp1);
		for (int i = 1; i < srcVList.size(); i++) {
			vtmp2 = srcVList.get(i);
			if (vtmp2.getLow() == vtmp1.getHigh() + 1) { // ���Բ����ж���֮�䲻���ڽ�����
				vtmp = new Value(vtmp1.getLow(), vtmp2.getHigh());
				result.set(result.size() - 1, vtmp);
			} else {// �����Բ�
				vtmp = new Value(vtmp2.getLow(), vtmp2.getHigh());
				result.add(vtmp);
			}
			vtmp1 = vtmp;
		}

		return result;
	}

	// ���������ϵĲ���
	public static List<Value> calcUnionSet(Value a, Value b) {
		List<Value> values = new ArrayList<Value>();
		Value v1 = Value.copyValue(a);
		Value v2 = Value.copyValue(b);

		if (v1.getLow() > v2.getHigh()) {
			values.add(v2);
			values.add(v1);
		} else if (v2.getLow() > v1.getHigh()) {
			values.add(v1);
			values.add(v2);
		} else if (v1.getLow() < v2.getLow()) {
			if (v1.getHigh() < v2.getHigh()) {
				v1.setHigh(v2.getHigh());
			}
			values.add(v1);
		} else {
			if (v2.getHigh() < v1.getHigh()) {
				v2.setHigh(v1.getHigh());
			}
			values.add(v2);
		}

		return values;
	}

	// ���������ϲ,[param in]minuend:��������subtrahend:����
	public static List<Value> calcDifferenceSet(Value minuend, Value subtrahend) {

		List<Value> values = new ArrayList<Value>();
		Value v_temp = null;
		if (minuend == null) {
			return values;
		} else if (minuend.getLow() > subtrahend.getHigh()
				|| subtrahend.getLow() > minuend.getHigh()) {// ����û�н�������Ϊ����������
			values.add(Value.copyValue(minuend));
		} else if (minuend.getLow() < subtrahend.getLow()) {
			v_temp = new Value();
			v_temp.setLow(minuend.getLow());
			v_temp.setHigh(subtrahend.getLow() - 1);
			values.add(v_temp);
			if (minuend.getHigh() > subtrahend.getHigh()) {
				v_temp = new Value();
				v_temp.setLow(subtrahend.getHigh() + 1);
				v_temp.setHigh(minuend.getHigh());
				values.add(v_temp);
			}
		} else {
			if (minuend.getHigh() > subtrahend.getHigh()) {
				v_temp = new Value();
				v_temp.setLow(subtrahend.getHigh() + 1);
				v_temp.setHigh(minuend.getHigh());
				values.add(v_temp);
			}
		}

		return values;
	}

	// ��һ�鼯������һ�鼯�ϵĲ
	public static List<Value> calcDifferenceSet(List<Value> minuend,
			List<Value> subtrahend) {

		List<Value> values = new ArrayList<Value>();
		List<Value> vlist_temp;
		int sub_size = subtrahend.size();

		if (sub_size == 0) {
			for (Value v : minuend) {
				values.add(Value.copyValue(v));
			}
		} else {
			for (Value v1 : minuend) {
				int sub_vIndex = 0;
				vlist_temp = Value.calcDifferenceSet(v1, subtrahend
						.get(sub_vIndex));
				sub_vIndex++;
				for (; sub_vIndex < sub_size; sub_vIndex++) {
					if (vlist_temp.size() > 1) {
						vlist_temp = Value.calcDifferenceSet(vlist_temp,
								subtrahend.subList(sub_vIndex, sub_size));
					} else if (vlist_temp.size() == 1) {
						vlist_temp = Value.calcDifferenceSet(vlist_temp.get(0),
								subtrahend.get(sub_vIndex));
					}
				}
				values.addAll(vlist_temp);
			}
		}
		return values;
	}

}
