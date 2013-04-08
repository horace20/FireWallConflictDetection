package com.fwcd.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * �ж����࣬������ƥ�伯���ж��������Ǽ�����ͻԴ�е�һ���򼯣����һ��������ΪAction
 * @author horace
 *
 */
public class FieldSet {
	private int ruleNum;
	private List<Field> fields;

	public FieldSet() {
		setRuleNum(-1);
		fields = new ArrayList<Field>();
	}

	public void printFieldSet() {
		List<String> str_action = new ArrayList<String>();
		str_action.add(Action.NOT_ACTION, "Not Action");
		str_action.add(Action.ACCEPT, "Accept");
		str_action.add(Action.DENY, "Deny");

		System.out.print("<" + ruleNum + ">");
		int i = 0;
		for (i = 0; i < fields.size() - 1; i++) {
			System.out.print("^F" + i);
			for (Value v : fields.get(i).getValue()) {
				System.out.print("[" + v.getLow() + "," + v.getHigh() + "]");
			}
		}
		System.out.println("->"
				+ str_action.get(fields.get(i).getValue().get(0).getAction()));

	}

	// �������򼯵Ĳ
	public static List<FieldSet> calcDifferenceSet(List<FieldSet> m,
			List<FieldSet> s) {
		List<FieldSet> result = new ArrayList<FieldSet>();
		List<FieldSet> fs_list_temp;
		int s_size = s.size();

		if (s_size == 0) {
			for (FieldSet fs : m) {
				result.add(FieldSet.copyFieldSet(fs));
			}
		} else {
			for (FieldSet fs : m) {
				int s_index = 0;
				fs_list_temp = FieldSet.calcDifferenceSet(fs, s.get(s_index));
				s_index++;
				if (fs_list_temp.size() != 0) {// �����ȥ��һ���򼯺󷵻�ֵ�Ѿ�Ϊ�գ������ټ��������
					for (; s_index < s_size; s_index++) {
						if (fs_list_temp.size() > 1) {
							fs_list_temp = FieldSet.calcDifferenceSet(
									fs_list_temp, s.subList(s_index, s_size));
						} else if (fs_list_temp.size() == 1) {
							fs_list_temp = FieldSet.calcDifferenceSet(
									fs_list_temp.get(0), s.get(s_index));
						} else {
							break;
						}
					}
				}
				result.addAll(fs_list_temp);
			}
		}

		return result;
	}

	// ��������Ĳ
	public static List<FieldSet> calcDifferenceSet(FieldSet m, FieldSet s) {
		List<FieldSet> result = new ArrayList<FieldSet>();
		FieldSet fs_temp;
		Field f_temp;
		List<Field> m_flist = m.getFields();
		List<Field> s_flist = s.getFields();
		int m_size = m_flist.size();
		int s_size = s_flist.size();

		if (m_size == s_size) { // ֻ�е������򼯵�ά����ͬ�ſ�����
			for (int i = 0; i < m_size - 1; i++) { // �������е�һ���򲢱���������Ϊ�����֮һ������Action������
				fs_temp = FieldSet.copyFieldSet(m);
				f_temp = Field
						.calcDifferenceSet(m_flist.get(i), s_flist.get(i));
				if (f_temp.getValue().size() != 0) { // ��ǰ������Ĳ��Ϊ��
					fs_temp.getFields().set(i, f_temp);
					result.add(fs_temp);
				}
			}
		}
		return result;
	}

	// ����һ����
	public static FieldSet copyFieldSet(FieldSet src) {
		FieldSet result = new FieldSet();
		Field f_temp;
		for (Field f : src.getFields()) {
			f_temp = new Field();
			for (Value v : f.getValue()) {
				f_temp.addValue(Value.copyValue(v));
			}
			result.addField(f_temp);
		}
		return result;
	}

	public void addField(Field field) {
		fields.add(field);
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setRuleNum(int ruleNum) {
		this.ruleNum = ruleNum;
	}

	public int getRuleNum() {
		return ruleNum;
	}

}
