package com.fwcd.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * ����ǽ�������еı���
 * 
 * @author horace
 * 
 */
public class TreeBranch {
	private Node head; // �ó�����ָ��Ľ�㣬ͷ���
	private Node tail; // �ó����������Ľ�㣬β���
	private List<Value> value; // �ó��ߵ�ֵ

	public TreeBranch() {
		head = null;
		tail = null;
		value = null;
	}

	// ����һ��ȫ�µı�,�����ظñߵ�·��
	public void CreateNewBranch(Rule rule, int fieldIndex, List<Value> value) {
		int rf_length = rule.getRSet().getFields().size(); // ��øù���ƥ�伯�ж������
		this.value = value;
		Node nTemp = new Node();
		setHead(nTemp);
		fieldIndex++;
		while (fieldIndex < (rf_length - 1)) {// ��ֹԽ��,���ⲻ��Ҫȡ�����һ����,��Action��
			Field fTemp = rule.getRSet().getFields().get(fieldIndex);
			TreeBranch bTemp = new TreeBranch();

			bTemp.setTail(nTemp);
			//ȡ�ø���ֵ��һ�ݿ���
			List<Value> vlist = new ArrayList<Value>();
			for(Value v:fTemp.getValue()){
				Value v_temp = Value.copyValue(v);
				vlist.add(v_temp);	
			}
			bTemp.setValue(vlist);
			nTemp = new Node();
			bTemp.setHead(nTemp);

			fieldIndex++;
		}

	}

	// ����һ����֪�ı�
	public TreeBranch copyBranch(TreeBranch branch, Node tailNode) {

		TreeBranch bTemp = new TreeBranch();
		List<Value> vlist_temp = new ArrayList<Value>();
		for(Value v:branch.getValue()){
			vlist_temp.add(Value.copyValue(v));
		}
		bTemp.setValue(vlist_temp);
		bTemp.setTail(tailNode);

		Node nTemp = new Node();
		bTemp.setHead(nTemp);

		int branchIndex = 0;
		while (branchIndex < branch.getHead().getBranch_list().size()) { // ֻҪͷ���ı߼���Ϊ��
			// �����ӱ߼�
			copyBranch(branch.getHead().getBranch_list().get(branchIndex),
					nTemp);
			branchIndex++;
		}
		return bTemp;
	}

	// ��ȡ�ߵ�·��,ֻ�������ӵı�����
	public FieldSet getPath() {
		List<Field> fListTemp = new ArrayList<Field>();
		List<Value> vlist_temp = new ArrayList<Value>();
		Field fTemp;
		Node nTemp = this.tail;
		// ���������ڵ�
		while (!nTemp.isRootNode()) {
			fTemp = new Field();
			vlist_temp = new ArrayList<Value>();
			for(Value v:nTemp.getEnterBranch().getValue()){
				vlist_temp.add(Value.copyValue(v));
			}
			fTemp.setValue(vlist_temp);
			fListTemp.add(fTemp);

			nTemp = nTemp.getEnterBranch().getTail();
		}

		FieldSet fields = new FieldSet();
		int fieldIndex = fListTemp.size() - 1; // �����һ����ʼ����ȡ
		while (fieldIndex >= 0) {
			fields.addField(fListTemp.get(fieldIndex));
			fieldIndex--;
		}// �������ǰ����

		fTemp = new Field();
		vlist_temp = new ArrayList<Value>();
		for(Value v:this.value){
			vlist_temp.add(Value.copyValue(v));
		}
		fTemp.setValue(vlist_temp); // ȡ�ñ��ߵ�ֵ
		fields.addField(fTemp);

		// ���������Ҷ�ӽڵ�
		nTemp = head;
		while (!nTemp.isLeafNode()) {
			fTemp = new Field();
			vlist_temp = new ArrayList<Value>();
			for(Value v:nTemp.getBranch_list().get(0).getValue()){
				vlist_temp.add(Value.copyValue(v));
			}
			fTemp.setValue(vlist_temp);
			fields.addField(fTemp);
			
			nTemp = nTemp.getBranch_list().get(0).getHead();
		}

		return fields;
	}

	public Node getHead() {
		return head;
	}

	public void setHead(Node head) {
		this.head = head;
		head.setEnterBranch(this); // ������ͷ����ͬʱ������������Ϊ�������
	}

	public Node getTail() {
		return tail;
	}

	public void setTail(Node tail) {
		this.tail = tail;
		tail.addBranch(this); // ������Ϊβ����ͬʱ�������߼�����ı��б�
	}

	public List<Value> getValue() {
		return value;
	}

	public void setValue(List<Value> value) {
		this.value = value;
	}

}
