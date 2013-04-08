package com.fwcd.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * ����ǽ�������࣬��Ҫ���ڸ��ݷ���ǽ����ƵĹ����������
 * 
 * @author horace
 * 
 */
public class FirewallDecisionTree {
	private List<Rule> rules;
	private Node rootNode;

	public FirewallDecisionTree() {
		rootNode = new Node();
		rules = new ArrayList<Rule>();
	}

	// ����̨��ӡ���
	public void printResult() {
		// ���ȴ�ӡ��ƥ�伯
		System.out.println("----------����Ϊƥ�伯----------");
		for (Rule r : rules) {
			r.getRSet().printFieldSet();
		}
		System.out.println("----------����Ϊ�ж���----------");
		for (Rule r : rules) {
			for (FieldSet fs : r.getEvalSet()) {
				fs.printFieldSet();
			}
		}
		System.out.println("----------����Ϊ��ͻԴ----------");
		List<String> str_ctype = new ArrayList<String>();
		str_ctype.add("û�г�ͻ");
		str_ctype.add("���ָ���");
		str_ctype.add("��ȫ����");
		str_ctype.add("�����ͻ");
		for (Rule r : rules) {
			System.out.println("����" + r.getRuleNum() + ":\n" + "��ͻ����:"
					+ str_ctype.get(r.getCollisionType()));
			System.out.println("��ͻԴ��");
			for (FieldSet fs : r.getCollisionSrc()) {
				fs.printFieldSet();
			}

		}

	}

	// ���������ͻ
	public void findRedundanceCollision() {
		int rules_size = rules.size();
		List<Rule> rlist_temp;
		Node rn_temp;
		Rule r_temp;
		List<FieldSet> sSet_temp; // ��ʱ��������¼��ͬ��Ϊ����

		for (int i = rules_size - 2; i >= 0; i--) { // ���һ������һ����Ϊ����
			r_temp = rules.get(i);
			if (r_temp.getCollisionType() == Rule.FULL_OVERLAP
					|| r_temp.getCollisionType() == Rule.REDUNDANCE_COLLISION)// �����i��������ȷ������ȫ�������迼��
				continue;

			rlist_temp = new ArrayList<Rule>();
			for (int sub_i = i + 1; sub_i < rules_size; sub_i++) { // ȥ������ȫ���ǵĹ���
				if (rules.get(sub_i).getCollisionType() != Rule.FULL_OVERLAP
						&& rules.get(sub_i).getCollisionType() != Rule.REDUNDANCE_COLLISION) {
					rlist_temp.add(Rule.copyRule(rules.get(sub_i)));
				}
			}
			rn_temp = new Node();

			// �����i+1������n(��ȥ����ȫ���ǵĹ���)�ľ�����
			for (Rule r : rlist_temp) {
				int fieldIndex = 0;
				BuildSubFDT(r, fieldIndex, rn_temp);
			}

			sSet_temp = new ArrayList<FieldSet>();
			Field r_action_field = r_temp.getRSet().getFields().get(
					r_temp.getRSet().getFields().size() - 1);
			for (Rule r : rlist_temp) {
				for (FieldSet fs : r.getEvalSet()) {
					if (r_action_field.isEqual(fs.getFields().get(
							fs.getFields().size() - 1))) {// ������ͬ����Ϊ��
						sSet_temp.add(fs);
					}
				}
			}

			// ���ҳ�ͻԴ
			List<FieldSet> collisionTemp = new ArrayList<FieldSet>();
			for (FieldSet r_efs : r_temp.getEvalSet()) {
				for (int j = 0; j < sSet_temp.size(); j++) {
					FieldSet one_s_set = sSet_temp.get(j);
					FieldSet fset_temp = new FieldSet();
					int size_temp = one_s_set.getFields().size();
					for (int k = 0; k < size_temp - 1; k++) {// ����Action��
						Field f_temp = Field.calcIntersect(r_efs.getFields()
								.get(k), one_s_set.getFields().get(k));
						if (f_temp.getValue().size() != 0) {
							fset_temp.addField(f_temp);
						}
					}

					if (fset_temp.getFields().size() == size_temp - 1) { // ֻ�е����е����н�������Ӹó�ͻԴ
						Field action = new Field();
						action.addValue(Value.copyValue(one_s_set.getFields()
								.get(size_temp - 1).getValue().get(0)));
						fset_temp.addField(action);
						fset_temp.setRuleNum(one_s_set.getRuleNum());
						collisionTemp.add(fset_temp);
					}
				}
			}

			if (collisionTemp.size() != 0) {
				// ȷ���Ƿ�Ϊ�����ͻ
				rlist_temp = new ArrayList<Rule>();
				Rule rule_temp;
				int flag = sSet_temp.size();
				int a = 0;
				for (a = 0; a < flag; a++) {
					rule_temp = new Rule(a + 1);
					sSet_temp.get(a).setRuleNum(a + 1);
					rule_temp.setRSet(sSet_temp.get(a));
					rlist_temp.add(rule_temp);
				}
				for (int b = 0; b < r_temp.getEvalSet().size(); b++) {
					FieldSet fset_temp = FieldSet.copyFieldSet(r_temp
							.getEvalSet().get(b));
					fset_temp.setRuleNum(a + 1);
					rule_temp = new Rule(a + 1);
					rule_temp.setRSet(fset_temp);
					rlist_temp.add(rule_temp);
					a++;
				}

				rn_temp = new Node();
				for (Rule r : rlist_temp) {
					int fieldIndex = 0;
					BuildSubFDT(r, fieldIndex, rn_temp);
				}
				for (; flag < rlist_temp.size(); flag++) {
					if (rlist_temp.get(flag).getEvalSet().size() != 0) {
						break;
					}
				}
				if (flag == rlist_temp.size()) {
					// �������ó�ͻԴ�ͳ�ͻ����
					r_temp.setCollisionType(Rule.REDUNDANCE_COLLISION);
					r_temp.setCollisionSrc(collisionTemp);
				}
			}

		}

	}

	// ���ҳ�ͻԴ(ֻ�ʺϲ��Ҳ��ָ��Ǻ���ȫ�������ֳ�ͻԴ)��������Ӧ�ĳ�ͻ����,������Ӧ�Ĺ�����
	public void findCollisionSrc() {

		calcMSetForRules(); // ����Ϊÿһ��������㸲�Ǽ�
		int rule_index = 0;
		// �ӵڶ�������ʼ���Ѱ�ҳ�ͻԴ(һ�����򲻿��ܷ�����ͻ)
		for (rule_index = 1; rule_index < rules.size(); rule_index++) {
			Rule currentRule = rules.get(rule_index);
			FieldSet current_rset = currentRule.getRSet();
			List<FieldSet> current_mset = currentRule.getMSet();
			for (int i = 0; i < current_mset.size(); i++) {
				FieldSet one_mset = current_mset.get(i);
				FieldSet fset_temp = new FieldSet();
				int size_temp = one_mset.getFields().size();
				for (int j = 0; j < size_temp - 1; j++) {// ����Action��
					Field f_temp = Field.calcIntersect(one_mset.getFields()
							.get(j), current_rset.getFields().get(j));
					if (f_temp.getValue().size() != 0) {
						fset_temp.addField(f_temp);
					}
				}
				// ֻ�е����е����н�������Ӹó�ͻԴ
				if (fset_temp.getFields().size() == size_temp - 1) {
					Field action = new Field();
					action.addValue(Value.copyValue(one_mset.getFields().get(
							size_temp - 1).getValue().get(0)));
					fset_temp.addField(action);
					fset_temp.setRuleNum(one_mset.getRuleNum());
					currentRule.addCollisionSrc(fset_temp);
					currentRule.setCollisionType(Rule.PART_OVERLAP); // ����ƥ�伯�͸��Ǽ��н������ȼٶ�Ϊ���ָ���
				}
			}

			if (currentRule.getEvalSet().size() == 0) { // �ж���Ϊ�գ�˵��Ϊ��ȫ����
				currentRule.setCollisionType(Rule.FULL_OVERLAP);
			}
		}
	}

	// ��ÿһ������ĸ��Ǽ����洢����Ӧ������(���д洢�Ľ���ǰ������ж�����һ�����ã���˸��Ǽ������޸�)
	private void calcMSetForRules() {
		int rule_index = 0;
		for (rule_index = 1; rule_index < rules.size(); rule_index++) {// �ӵڶ�������ʼ����һ������û�и��Ǽ�
			Rule currentRule = rules.get(rule_index);
			Rule preRule = rules.get(rule_index - 1);

			List<FieldSet> fs_temp = new ArrayList<FieldSet>();
			fs_temp.addAll(preRule.getMSet()); // ���M[i-1]
			fs_temp.addAll(preRule.getEvalSet()); // ���E[i-1]
			currentRule.setMSet(fs_temp);
		}
	}

	// ���ݵ�ǰ���й�����������FDT
	public void BuildFDT() {
		for (Rule rule : rules) {
			int fieldIndex = 0;
			BuildSubFDT(rule, fieldIndex, rootNode);
		}
	}

	// ������FDT�����в�����rule��һ�����˹���fieldIndex�ǹ���ƥ�伯�ж���������node��FDT���еĽڵ�
	private void BuildSubFDT(Rule rule, int fieldIndex, Node node) {

		if (fieldIndex < rule.getRSet().getFields().size()) {// ��ֹԽ��
			List<Value> vlist_temp;
			Field field = rule.getRSet().getFields().get(fieldIndex);
			// ���������Ҷ�ӽڵ�
			if (!field.getValue().get(0).isAction()) {
				// ����һ������
				if ((vlist_temp = Value.calcDifferenceSet(field.getValue(),
						node.getUnionSet())).size() != 0) {
					TreeBranch new_branch = new TreeBranch();
					new_branch.CreateNewBranch(rule, fieldIndex, vlist_temp);
					new_branch.setTail(node); // ���±߼��뵽node�ļ�������
					FieldSet eset_temp = new_branch.getPath();
					eset_temp.setRuleNum(rule.getRuleNum()); // Ϊ�ж������ù�����
					rule.addEvalSet(eset_temp);
				}

				List<TreeBranch> bList = node.getBranch_list();
				for (int i = 0; i < bList.size(); i++) {
					TreeBranch branch = bList.get(i);
					vlist_temp = Value.calcIntersect(branch.getValue(), field
							.getValue());
					// ����Ϊ�ջ��߱ߵĽڵ�ΪҶ�ӽڵ�
					if (vlist_temp.size() == 0 || branch.getHead().isLeafNode()) {
						continue;
					}

					// rule��field��ֵ��ȫ������ĳ��֧filed��ֵ
					if (Value.isEqual(branch.getValue(), vlist_temp)) {

						BuildSubFDT(rule, fieldIndex + 1, branch.getHead());// ����rule����һ���ж���

					} else {
						TreeBranch new_branch = new TreeBranch();
						new_branch = new_branch.copyBranch(branch, branch
								.getTail());
						new_branch.setValue(Value.calcIntersect(branch
								.getValue(), field.getValue()));
						branch.setValue(Value.calcDifferenceSet(branch
								.getValue(), field.getValue()));
						BuildSubFDT(rule, fieldIndex + 1, new_branch.getHead());
					}
				}
			}
		}
	}

	public List<Rule> getRules() {
		return rules;
	}

	public void setRules(List<Rule> rule) {
		this.rules = rule;
	}

	public void addRule(Rule r) {
		rules.add(r);
	}

	public Node getRootNode() {
		return rootNode;
	}

	public void setRootNode(Node rootNode) {
		this.rootNode = rootNode;
	}
}
