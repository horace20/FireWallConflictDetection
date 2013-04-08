package com.fwcd.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * ���ڲ��Ի����㷨ʵ��
 * @author horace
 *
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		List<Rule> rulSet = new ArrayList<Rule>();
		Field f_temp;
		// ���<1>�Ź���
		Rule r = new Rule();
		r.setRuleNum(1);
		f_temp = new Field();
		f_temp.addValue(new Value(20, 50));
		r.addFieldForRSet(f_temp);

		f_temp = new Field();
		f_temp.addValue(new Value(20, 40));
		r.addFieldForRSet(f_temp);

		f_temp = new Field();
		f_temp.addValue(new Value(Action.ACCEPT));
		r.addFieldForRSet(f_temp);
		rulSet.add(r);

		// ���<2>�Ź���
		r = new Rule();
		r.setRuleNum(2);
		f_temp = new Field();
		f_temp.addValue(new Value(10, 40));
		r.addFieldForRSet(f_temp);

		f_temp = new Field();
		f_temp.addValue(new Value(0, 30));
		r.addFieldForRSet(f_temp);

		f_temp = new Field();
		f_temp.addValue(new Value(Action.DENY));
		r.addFieldForRSet(f_temp);
		rulSet.add(r);

		// ���<3>�Ź���
		r = new Rule();
		r.setRuleNum(3);
		f_temp = new Field();
		f_temp.addValue(new Value(20, 40));
		r.addFieldForRSet(f_temp);

		f_temp = new Field();
		f_temp.addValue(new Value(10, 40));
		r.addFieldForRSet(f_temp);

		f_temp = new Field();
		f_temp.addValue(new Value(Action.ACCEPT));
		r.addFieldForRSet(f_temp);
		rulSet.add(r);

		// ���<4>�Ź���
		r = new Rule();
		r.setRuleNum(4);
		f_temp = new Field();
		f_temp.addValue(new Value(0, 20));
		r.addFieldForRSet(f_temp);

		f_temp = new Field();
		f_temp.addValue(new Value(0, 50));
		r.addFieldForRSet(f_temp);

		f_temp = new Field();
		f_temp.addValue(new Value(Action.DENY));
		r.addFieldForRSet(f_temp);
		rulSet.add(r);

		// ���<5>�Ź���
		r = new Rule();
		r.setRuleNum(5);
		f_temp = new Field();
		f_temp.addValue(new Value(10, 50));
		r.addFieldForRSet(f_temp);

		f_temp = new Field();
		f_temp.addValue(new Value(0, 50));
		r.addFieldForRSet(f_temp);

		f_temp = new Field();
		f_temp.addValue(new Value(Action.DENY));
		r.addFieldForRSet(f_temp);
		rulSet.add(r);

		FirewallDecisionTree fdt_test = new FirewallDecisionTree();
		fdt_test.setRules(rulSet);
		fdt_test.BuildFDT();
		fdt_test.findCollisionSrc();
		fdt_test.findRedundanceCollision();
		fdt_test.printResult();

	}

}
