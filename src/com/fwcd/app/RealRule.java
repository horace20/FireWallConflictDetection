package com.fwcd.app;

import java.util.ArrayList;
import java.util.List;

import com.fwcd.algorithm.*;
import com.fwcd.util.ConvertIP;

/**
 * ʵ�ʹ����࣬��������Դ���㷨���ù����Լ����ߵ�ת��������ʵ�ʹ���ĳ�ͻ��
 * @author horace
 *
 */
public class RealRule {
	private List<String> ruleSrc; // ����Դ
	private List<Rule> algRule; // �㷨���ù���
	private List<Rule> algRuleClone; // �㷨���ù��򼯿���(��Ҫ���������ͻ���ʱ����ԭ�м����)

	private FirewallDecisionTree fwdt_instance;
	private boolean isCoverDetect;
	private boolean isRedundanceDetect;

	public final static String FIELD_SEPARATOR = "\t";

	public RealRule() {
		ruleSrc = new ArrayList<String>();
		algRule = new ArrayList<Rule>();
		algRuleClone = new ArrayList<Rule>();

		fwdt_instance = new FirewallDecisionTree();
		isCoverDetect = false;
		isRedundanceDetect = false;
	}

	// ��һ����ת��ΪString
	public String convertFieldSetToString(FieldSet fs) {
		String result = "";
		result += fs.getRuleNum() + FIELD_SEPARATOR;

		List<Field> flist_temp = fs.getFields();
		List<Value> vlist_temp = flist_temp.get(0).getValue(); // Э����
		result += convertValueToProtocol(vlist_temp.get(0)) + FIELD_SEPARATOR;

		vlist_temp = flist_temp.get(1).getValue(); // ԴIP
		for (int i = 0; i < vlist_temp.size(); i++) {
			result += convertValueToIP(vlist_temp.get(i));
			if (i < vlist_temp.size() - 1) {
				result += ", ";
			}
		}

		result += FIELD_SEPARATOR;
		vlist_temp = flist_temp.get(2).getValue(); // Դ�˿�
		result += convertValueToPort(vlist_temp) + FIELD_SEPARATOR;

		vlist_temp = flist_temp.get(3).getValue(); // Ŀ��IP
		for (int i = 0; i < vlist_temp.size(); i++) {
			result += convertValueToIP(vlist_temp.get(i));
			if (i < vlist_temp.size() - 1) {
				result += ", ";
			}
		}

		result += FIELD_SEPARATOR;
		vlist_temp = flist_temp.get(4).getValue(); // Ŀ�Ķ˿�
		result += convertValueToPort(vlist_temp) + FIELD_SEPARATOR;

		vlist_temp = flist_temp.get(5).getValue(); // Action
		result += convertValueToAction(vlist_temp.get(0));

		return result;
	}

	// �����ͻ���
	public void redundanceCollisionDetect() {
		if (!isCoverDetect) {
			convertSrcRuleToAlgRule(); // ���ȵõ��㷨���ù���

			fwdt_instance.setRules(algRule); // ���ù���
			fwdt_instance.BuildFDT(); // ��������ǽ������
			fwdt_instance.findCollisionSrc(); // ���ҳ�ͻԴ�����ָ��ǣ���ȫ���ǣ�

			isCoverDetect = true;
		}
		if (!isRedundanceDetect) {
			cloneRules();
			fwdt_instance.findRedundanceCollision();
			isRedundanceDetect = true;
		}
		fwdt_instance.printResult(); // ���ڲ���
	}

	// ���ǳ�ͻ���
	public void coverCollisionDetect() {
		if (!isCoverDetect) {
			convertSrcRuleToAlgRule(); // ���ȵõ��㷨���ù���

			fwdt_instance.setRules(algRule); // ���ù���
			fwdt_instance.BuildFDT(); // ��������ǽ������
			fwdt_instance.findCollisionSrc(); // ���ҳ�ͻԴ�����ָ��ǣ���ȫ���ǣ�

			isCoverDetect = true;
		}
		fwdt_instance.printResult(); // ���ڲ���
	}

	// ���㷨���ù��򼯿���
	private void cloneRules() {
		for (Rule src_r : algRule) {
			algRuleClone.add(src_r.Clone());
		}
	}

	// ��ԭ����ת��Ϊ�㷨���õĹ���
	private void convertSrcRuleToAlgRule() {
		String str_temp[];
		Rule r_temp;
		Field f_temp;
		for (String str : ruleSrc) {
			str_temp = str.split(FIELD_SEPARATOR);
			r_temp = new Rule(Integer.parseInt(str_temp[0].trim())); // ������
			f_temp = new Field();
			f_temp.addValue(convertProToValue(str_temp[1].trim())); // Э����
			r_temp.addFieldForRSet(f_temp);

			f_temp = new Field();
			f_temp.addValue(convertIPToValue(str_temp[2].trim())); // ԴIP
			r_temp.addFieldForRSet(f_temp);

			f_temp = new Field();
			f_temp.setValue(convertPortToValue(str_temp[3].trim())); // Դ�˿�
			r_temp.addFieldForRSet(f_temp);

			f_temp = new Field();
			f_temp.addValue(convertIPToValue(str_temp[4].trim())); // Ŀ��IP
			r_temp.addFieldForRSet(f_temp);

			f_temp = new Field();
			f_temp.setValue(convertPortToValue(str_temp[5].trim())); // Ŀ�Ķ˿�
			r_temp.addFieldForRSet(f_temp);

			f_temp = new Field();
			f_temp.addValue(convertActionToValue(str_temp[6].trim())); // Action
			r_temp.addFieldForRSet(f_temp);

			algRule.add(r_temp);
		}
	}

	// ��Valueת��ΪString Action
	private String convertValueToAction(Value v) {
		String result = "";
		switch (v.getAction()) {
		case Action.ACCEPT:
			result += "accept";
			break;
		case Action.DENY:
			result += "deny";
			break;
		}
		return result;
	}

	// ��String Actionת��ΪValue
	private Value convertActionToValue(String action) {
		Value result = new Value();
		if (action.equals("accept")) {
			result.setAction(Action.ACCEPT);
		} else if (action.equals("deny")) {
			result.setAction(Action.DENY);
		}
		return result;
	}

	// ��Valueת��Ϊ�˿�
	private String convertValueToPort(List<Value> vlist) {
		String result = "";
		Value v_temp;
		long l_temp, h_temp;
		for (int i = 0; i < vlist.size(); i++) {
			v_temp = vlist.get(i);
			l_temp = v_temp.getLow();
			h_temp = v_temp.getHigh();
			if (l_temp == h_temp) {
				result += String.valueOf(l_temp);
			} else { // ��һ���˿ڶ�
				result += "[" + String.valueOf(l_temp) + ","
						+ String.valueOf(h_temp) + "]";
			}
			if (i < (vlist.size() - 1)) {
				result += ",";
			}
		}

		return result;
	}

	// ���˿�ת��ΪValue��ʽ
	private List<Value> convertPortToValue(String port) {
		List<Value> result = new ArrayList<Value>();
		Value v_temp = new Value();
		String str_temp[] = port.split(",");
		int iTemp = 0;
		if (port.equals("ANY")) {
			v_temp.setLow(0x0);
			v_temp.setHigh(0xFFFF);
			result.add(v_temp);
		} else if (str_temp[0].substring(0, 1).equals("[")) { // �����һ�ζ˿�
			v_temp.setLow(Integer.valueOf(str_temp[0].substring(1, str_temp[0]
					.length()), 10));
			v_temp.setHigh(Integer.parseInt(str_temp[1].substring(0,
					str_temp[1].length() - 1)));
			result.add(v_temp);
		} else {
			if (str_temp.length == 1) { // ֻ��һ���˿�
				iTemp = Integer.valueOf(str_temp[0], 10);
				v_temp.setLow(iTemp);
				v_temp.setHigh(iTemp);
				result.add(v_temp);
			} else { // �����˿�
				iTemp = Integer.valueOf(str_temp[0], 10);
				v_temp.setLow(iTemp);
				v_temp.setHigh(iTemp);
				result.add(v_temp);

				iTemp = Integer.valueOf(str_temp[1], 10);
				v_temp = new Value();
				v_temp.setLow(iTemp);
				v_temp.setHigh(iTemp);
				result.add(v_temp);
			}
		}
		return result;
	}

	public boolean isCoverDetect() {
		return isCoverDetect;
	}

	public boolean isRedundanceDetect() {
		return isRedundanceDetect;
	}

	// ��Valueת��ΪЭ��
	private String convertValueToProtocol(Value v) {
		String result = "";
		if (v.getLow() != v.getHigh()) {
			result = "ANY";
		} else {
			switch ((int) v.getLow()) {
			case Protocol.TCP:
				result = "TCP";
				break;
			case Protocol.UDP:
				result = "UDP";
				break;
			case Protocol.ICMP:
				result = "ICMP";
				break;
			}
		}
		return result;
	}

	// ��Э��ת��ΪValue����ʽ
	private Value convertProToValue(String protocol) {
		Value result = new Value();
		if (protocol.equals("ANY")) {
			result.setLow(0x0);
			result.setHigh(0xFFFF);
		} else if (protocol.equals("TCP")) {
			result.setLow(Protocol.TCP);
			result.setHigh(Protocol.TCP);
		} else if (protocol.equals("UDP")) {
			result.setLow(Protocol.UDP);
			result.setHigh(Protocol.UDP);
		} else if (protocol.equals("ICMP")) {
			result.setLow(Protocol.ICMP);
			result.setHigh(Protocol.ICMP);
		}
		return result;
	}

	// ��Valueת��ΪIP����ʽ
	private String convertValueToIP(Value v) {
		String result = "";
		long l_temp = v.getLow();
		long h_temp = v.getHigh();
		if (l_temp == h_temp) { // ����IP
			result = ConvertIP.longToIP(h_temp);
		} else { // IP��
			result += ConvertIP.longToIP(l_temp);
			result += "--" + ConvertIP.longToIP(h_temp);
		}
		return result;
	}

	// ��Ipת��ΪValue����ʽ
	private Value convertIPToValue(String ipAndMask) {
		Value result = new Value();
		if (ipAndMask.equals("ANY")) {
			result.setLow(0x0);
			result.setHigh(ConvertIP.ipToLong("255.255.255.255"));
		} else {
			String[] ipAdd = ipAndMask.split("/");
			long IP_Low = ConvertIP.ipToLong(ipAdd[0]);
			if (ipAdd.length == 2) {
				int netMask = Integer.valueOf(ipAdd[1].trim());
				if (netMask < 0 || netMask > 31) {
					throw new IllegalArgumentException(
							"invalid ipAndMask with: " + ipAndMask);
				}
				IP_Low = IP_Low & (0xFFFFFFFF << (32 - netMask)) + 1;
				long IP_High = IP_Low + (0xFFFFFFFF >>> netMask) - 1;
				result.setLow(IP_Low);
				result.setHigh(IP_High);
			} else {
				result.setLow(IP_Low);
				result.setHigh(IP_Low);
			}
		}
		return result;
	}

	public List<String> getRuleSrc() {
		return ruleSrc;
	}

	// ���һ��ԭ����
	public void addRuleSrc(String src) {
		if (src != null && !src.equals("")) {
			ruleSrc.add(src);
		}
	}

	public void setRuleSrc(List<String> ruleSrc) {
		this.ruleSrc = ruleSrc;
	}

	public List<Rule> getAlgRule() {
		return algRule;
	}

	public void setAlgRule(List<Rule> algRule) {
		this.algRule = algRule;
	}

	public List<Rule> getAlgRuleClone() {
		return algRuleClone;
	}

	public void setAlgRuleClone(List<Rule> algRuleClone) {
		this.algRuleClone = algRuleClone;
	}

}
