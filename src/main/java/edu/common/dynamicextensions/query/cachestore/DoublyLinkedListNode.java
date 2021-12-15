package edu.common.dynamicextensions.query.cachestore;

import java.io.Serializable;

class DoublyLinkedListNode<K, V> implements Serializable {

	private static final long serialVersionUID = 2843296712974419876L;
	
	private K prevKey;
	
	private K nextKey;
	
	private K key;
	
	private V val;
	
	public DoublyLinkedListNode(K key, V val) {
		this.key = key;
		this.val = val;
	}

	public K getPrevKey() {
		return prevKey;
	}

	public void setPrevKey(K prevKey) {
		this.prevKey = prevKey;
	}

	public K getNextKey() {
		return nextKey;
	}

	public void setNextKey(K nextKey) {
		this.nextKey = nextKey;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getVal() {
		return val;
	}

	public void setVal(V val) {
		this.val = val;
	}
}

