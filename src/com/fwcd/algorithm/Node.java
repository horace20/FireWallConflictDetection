package com.fwcd.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * ����ǽ�������еĽڵ���,�����б�Ϊ�յ�ʱ���ʾҶ�ӽڵ�
 * 
 * @author horace
 * 
 */
public class Node {
	private List<TreeBranch> branch_list;
	private List<Value> unionSet;
	private TreeBranch enterBranch; // ������ıߣ���������ĳ�ߵ�·����ͷ���û�����

	public Node() {
		branch_list = new ArrayList<TreeBranch>();
		unionSet = null;
		enterBranch = null;
	}

	public List<TreeBranch> getBranch_list() {
		return branch_list;
	}

	public void setBranch_list(List<TreeBranch> branch_list) {
		this.branch_list = branch_list;
	}

	// ȡ�õ�ǰ�ڵ����з�ֵ֧�Ĳ���,���ڽڵ������һ�����ߵ�ֵû�н���,�ɼ򵥵�ȡ�����ֵ���ϼ���
	public List<Value> getUnionSet() {
		unionSet = new ArrayList<Value>();
		for (TreeBranch branch : branch_list) {
			unionSet.addAll(branch.getValue());
		}
		return unionSet;
	}

	public void addBranch(TreeBranch branch) {
		branch_list.add(branch);
	}

	// �жϸýڵ��Ƿ�Ϊ���ڵ�
	public boolean isRootNode() {
		if (enterBranch == null) {
			return true;
		}
		return false;
	}

	// �жϸýڵ��Ƿ�ΪҶ�ӽڵ�
	public boolean isLeafNode() {
		if (branch_list.size() == 0) {
			return true;
		}
		return false;
	}

	public void setEnterBranch(TreeBranch enterBranch) {
		this.enterBranch = enterBranch;
	}

	public TreeBranch getEnterBranch() {
		return enterBranch;
	}
}
