package com.fwcd.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * �����࣬ʵ�ʹ�����ɼ̳и��������Ƕ�����������չ
 * 
 * @author horace
 * 
 */
public class Rule {
	private int ruleNum; // ������
	private FieldSet rSet; // ƥ�伯
	private List<FieldSet> evalSet; // �ж���(evalSet.size()=0��ʾ�ж���Ϊ��),����Action��Ϊƥ�伯��Action
	private List<FieldSet> mSet; // ���Ǽ�
	private List<FieldSet> collisionSrc; // ��ͻԴ
	private int collisionType; // ��ͻ���ͣ��Զ�����ֵӳ�䣩,-1��ʾ��δ��ó�ͻ����

	public final static int NO_COLLISION = 0; // ľ�г�ͻ
	public final static int PART_OVERLAP = 1; // ���ָ���
	public final static int FULL_OVERLAP = 2; // ��ȫ����
	public final static int REDUNDANCE_COLLISION = 3; // �����ͻ

	public Rule() {
		ruleNum = -1;
		collisionType = NO_COLLISION;
		rSet = new FieldSet();
		evalSet = new ArrayList<FieldSet>();
		setMSet(new ArrayList<FieldSet>());
		collisionSrc = new ArrayList<FieldSet>();
	}

	public Rule(int r_num) {
		ruleNum = r_num;
		collisionType = NO_COLLISION;
		rSet = new FieldSet();
		rSet.setRuleNum(r_num);
		evalSet = new ArrayList<FieldSet>();
		setMSet(new ArrayList<FieldSet>());
		collisionSrc = new ArrayList<FieldSet>();
	}

	// ���һ���ж���
	public void addEvalSet(FieldSet fields) {
		Field f_temp = new Field();
		int action_index = rSet.getFields().size() - 1;
		Value v_temp = Value.copyValue(rSet.getFields().get(action_index)
				.getValue().get(0));
		f_temp.addValue(v_temp);
		fields.addField(f_temp); // Ϊ�ж������Action

		fields.setRuleNum(getRuleNum()); // Ϊ�ж�����ӹ�����
		evalSet.add(fields);
	}

	// ���һ����ͻԴ,��ӵĳ�ͻԴ�б����й�����,�����ț]�Ј��Йz��,ʹ�÷����ĵ������Йz��
	public void addCollisionSrc(FieldSet cFieldSet) {
		collisionSrc.add(cFieldSet);
	}

	// Ϊƥ�伯���һ���ж���
	public void addFieldForRSet(Field f) {
		rSet.addField(f);
	}

	// ʵ�ֱ������һ����ȫ��¡��
	public Rule Clone() {
		Rule dstRule = new Rule(this.ruleNum);
		dstRule.setCollisionType(collisionType);
		Field f_temp;
		FieldSet fs_temp = new FieldSet();
		fs_temp.setRuleNum(ruleNum);
		// ����ƥ�伯
		for (Field f : rSet.getFields()) {
			f_temp = new Field();
			List<Value> vlist_temp = f.getValue();
			for (Value v : vlist_temp) {
				f_temp.addValue(Value.copyValue(v));
			}
			fs_temp.addField(f_temp);
		}
		dstRule.setRSet(fs_temp);

		List<FieldSet> fs_list_temp = new ArrayList<FieldSet>();
		// ���Ƹ��Ǽ�
		for (FieldSet fs : mSet) {
			fs_temp = new FieldSet();
			fs_temp.setRuleNum(fs.getRuleNum());
			for (Field f : fs.getFields()) {
				f_temp = new Field();
				List<Value> vlist_temp = f.getValue();
				for (Value v : vlist_temp) {
					f_temp.addValue(Value.copyValue(v));
				}
				fs_temp.addField(f_temp);
			}
			fs_list_temp.add(fs_temp);
		}
		dstRule.setMSet(fs_list_temp);

		fs_list_temp = new ArrayList<FieldSet>();
		// �����ж���
		for (FieldSet fs : collisionSrc) {
			fs_temp = new FieldSet();
			fs_temp.setRuleNum(fs.getRuleNum());
			for (Field f : fs.getFields()) {
				f_temp = new Field();
				List<Value> vlist_temp = f.getValue();
				for (Value v : vlist_temp) {
					f_temp.addValue(Value.copyValue(v));
				}
				fs_temp.addField(f_temp);
			}
			fs_list_temp.add(fs_temp);
		}
		dstRule.setCollisionSrc(fs_list_temp);

		return dstRule;
	}

	// ����һ����������ֻ���Ǵ�������ƥ�伯����ŵȳ�ʼֵ��
	public static Rule copyRule(Rule srcRule) {
		Rule dstRule = new Rule(srcRule.getRuleNum());
		List<Field> flist_temp = srcRule.getRSet().getFields();
		FieldSet dst_fset = new FieldSet();
		dst_fset.setRuleNum(srcRule.getRuleNum()); // ��ƥ�伯���ù�����
		Field f_temp;

		for (Field f : flist_temp) {
			f_temp = new Field();
			List<Value> vlist_temp = f.getValue();
			for (Value v : vlist_temp) {
				f_temp.addValue(Value.copyValue(v));
			}
			dst_fset.addField(f_temp);
		}
		dstRule.setRSet(dst_fset); // Ϊ�¹������ƥ�伯

		return dstRule;
	}

	public void setRuleNum(int ruleNum) {
		this.ruleNum = ruleNum;
		if (rSet != null) {
			rSet.setRuleNum(ruleNum);
		}
	}

	public int getRuleNum() {
		return ruleNum;
	}

	public void setRSet(FieldSet rSet) {
		rSet.setRuleNum(getRuleNum());
		this.rSet = rSet;
	}

	public FieldSet getRSet() {
		return rSet;
	}

	public List<FieldSet> getEvalSet() {
		return evalSet;
	}

	public void setCollisionSrc(List<FieldSet> collisionSrc) {
		this.collisionSrc = collisionSrc;
	}

	public List<FieldSet> getCollisionSrc() {
		return collisionSrc;
	}

	// ���ó�ͻ����
	public void setCollisionType(int collisionType) {
		this.collisionType = collisionType;
	}

	public int getCollisionType() {
		return collisionType;
	}

	public void setMSet(List<FieldSet> mSet) {
		this.mSet = mSet;
	}

	public List<FieldSet> getMSet() {
		return mSet;
	}
}
