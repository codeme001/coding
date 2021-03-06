package com.niyue.coding.careercup.dynamiclist;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

// http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/6-b14/java/util/ArrayList.java
public class DynamicList<E> implements List<E> {
	private Object[] values = new Object[16];
	
	private int size = 0;
	
	@Override
	public boolean add(E e) {
		ensureCapacity();
		values[size] = e;
		size++;
		return true;
	}
	
	private void ensureCapacity() {
		ensureCapacity(size + 1);
	}
	
	private void ensureCapacity(int minCapacity) {
		if(minCapacity > values.length) {
			Object[] originalValues = values;
			int newCapacity = Math.max(values.length * 2, minCapacity);
			values = Arrays.copyOf(originalValues, newCapacity);
		}
	}

	@Override
	public void add(int index, E element) {
		ensureCapacity();
		System.arraycopy(values, index, values, index + 1, size - index);
		values[index] = element;
		size++;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		for(E e : c) {
			add(e);
		}
		return true;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		ensureCapacity(size + c.size());
		System.arraycopy(values, index, values, index + c.size(), size - index);
		Iterator<? extends E> iter = c.iterator();
		for(int i = index; i < c.size(); i++) {
			values[i] = iter.next();
		}
		size += c.size();
		return true;
	}

	@Override
	public void clear() {
		for(int i = 0; i < size; i++) {
			values[i] = null;
		}
		size = 0;
	}

	@Override
	public boolean contains(Object o) {
		boolean contains = false;
		for(Object v : values) {
			if(o == null && v == null || v != null && v.equals(o)) {
				contains = true;
				break;
			}
		}
		return contains;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		boolean containsAll = true;
		for(Object co : c) {
			if(!contains(co)) {
				containsAll = false;
				break;
			}
		}
		return containsAll;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E get(int index) {
		if(index < size) {
			return (E) values[index];
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public int indexOf(Object o) {
		for(int i = 0; i < size; i++) {
			// null should be treated differently
			if(o == null && values[i] == null || o != null && o.equals(values[i])) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public Iterator<E> iterator() {
		return new DynamicListIterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		for(int i = size - 1; i >= 0; i--) {
			Object v = get(i);
			if(o == null && v == null || v != null && v.equals(o)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public ListIterator<E> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object o) {
		boolean found = false;
		for(int i = 0; i < size; i++) {
			Object v = values[i];
			if(v == null && o == null || v != null && v.equals(o)) {
				values[i] = null;
				System.arraycopy(values, i + 1, values, i, size - i - 1);
				size--;
				found = true;
				break;
			}
		}
		return found;
	}

	@Override
	public E remove(int index) {
		if(index >= 0 && index < size) {
			@SuppressWarnings("unchecked")
			E v = (E) values[index];
			values[index] = null;
			System.arraycopy(values, index + 1, values, index, size - index - 1);
			size--;
			return v;
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		int removed = 0;
		int dest = 0;
		for(int current = 0; current < size; current++) {
			Object v = values[current];
			if(!c.contains(v)) {
				values[dest] = values[current];
				dest++;
			} else {
				removed++;
			}
		}
		size -= removed;
		for(int i = size; i < size + removed; i++) {
			values[i] = null;
		}
		return removed > 0;
	}

	@Override
	// this can be implemented similarly with removeAll method
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public E set(int index, E element) {
		if(index < size) {
			@SuppressWarnings("unchecked")
			E oldValue = (E) values[index];
			values[index] = element;
			return oldValue;
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	// subList is another beast in the dynamic list implementation
	public List<E> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object[] toArray() {
		return Arrays.copyOf(values, size);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a) {
		if(a.length < size) {
			return (T[]) Arrays.copyOf(values, size, a.getClass());
		} else {
			System.arraycopy(values, 0, a, 0, size);
			// ?
			if(a.length > size) {
				a[size] = null; 
			}
			return a;
		}
	}
	
	private class DynamicListIterator implements Iterator<E> {
		private int nextCursor = 0;
		
		@Override
		public boolean hasNext() {
			return nextCursor < size;
		}

		@Override
		public E next() {
			// TODO: modification check
			E v = get(nextCursor);
			nextCursor++;
			return v;
		}

		@Override
		public void remove() {
			values[nextCursor - 1] = null; // for GC
			System.arraycopy(values, nextCursor, values, nextCursor - 1, size - nextCursor);
			size--;
		}
	}
}
