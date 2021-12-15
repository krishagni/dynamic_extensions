package edu.common.dynamicextensions.query.cachestore;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LinkedEhCacheMap<K, V> implements Map<K, V> {
	private EhCacheMap<K, DoublyLinkedListNode<K, V>> linkedMap = new EhCacheMap<K, DoublyLinkedListNode<K, V>>();
	
	private DoublyLinkedListNode<K, V> first;
	
	private DoublyLinkedListNode<K, V> last;

	@Override
	public void clear() {
		linkedMap.clear();
		first = last = null;		
	}

	@Override
	public boolean containsKey(Object key) {
		return linkedMap.containsKey(key);
	}

	@Override
	public boolean containsValue(Object arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public V get(Object key) {
		DoublyLinkedListNode<K, V> node = linkedMap.get(key);
		return node != null ? node.getVal() : null;
	}

	@Override
	public boolean isEmpty() {
		return linkedMap.isEmpty();
	}

	@Override
	public Set<K> keySet() {
		return linkedMap.keySet();
	}

	@Override
	public V put(K key, V value) {
		DoublyLinkedListNode<K, V> node = linkedMap.get(key);
		if (node == null) { // first time
			DoublyLinkedListNode<K, V> prev = last;
			
			node = new DoublyLinkedListNode<K, V>(key, value);			
			if (prev != null) {
				node.setPrevKey(prev.getKey());
				prev.setNextKey(key);
			}
			
			if (first == null) {
				first = node;
			}
			
			last = node;
			
			linkedMap.put(key, node);
			return null;
		} 
		
		V prevVal = node.getVal();
		node.setVal(value);
		return prevVal;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}		
	}

	@Override
	public V remove(Object key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		return linkedMap.size();
	}

	@Override
	public Collection<V> values() {
		List<V> values = new ArrayList<V>();
		
		DoublyLinkedListNode<K, V> cur = first;
		while (cur != null) {
			values.add(cur.getVal());
			K nextKey = cur.getNextKey();
			if (nextKey != null) {
				cur = linkedMap.get(nextKey);
			} else {
				cur = null;
			}			
		}
		
		return values;
	}
	
	public void destroy() {
		first = last = null;
		if (linkedMap != null) {
			linkedMap.destroy();
		}
	}
	
	public Iterator<V> iterator() {
		return new Iterator<V>() {
			DoublyLinkedListNode<K, V> nextK = null;
			
			boolean started = false;
			
			{
				ensureNext();
			}
			
			
			@Override
			public boolean hasNext() {
				return nextK != null;
			}

			@Override
			public V next() {
				V val = null;
				if (nextK != null) {
					val = nextK.getVal();
					ensureNext();
				}
								
				return val;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
				
			}
			
			private void ensureNext() {
				if (!started) {
					started = true;
					nextK = first;
				} else if (nextK != null) {
					K nextKey = nextK.getNextKey();
					if (nextKey != null) {
						nextK = linkedMap.get(nextKey);
					} else {
						nextK = null;
					}							
				}				
			}
		};
	}
}
