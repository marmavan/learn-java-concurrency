package com.karthik.concurrent;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class SynchronizedBlockingPollingQueue<E> implements Queue<E>{
	
	private boolean stopped = false;
	
	private LinkedList<E> list = new LinkedList<E>();
	
	public SynchronizedBlockingPollingQueue() {
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		return list.iterator();
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}

	@Override
	public boolean remove(Object o) {
		return list.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return list.addAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}

	@Override
	public void clear() {
		list.clear();
	}

	@Override
	public boolean add(E e) {
		boolean result;
		synchronized(this) {
			result = list.add(e);
			this.notify();
		}
		return result;
	}

	@Override
	public boolean offer(E e) {
		synchronized(this) {
			boolean result = list.offer(e);
			this.notify();
			return result;
		}
	}

	@Override
	public E remove() {
		synchronized (this) {
			E poll = list.remove();
			if (poll == null) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return list.remove();
		}
	}

	@Override
	public E poll() {
		if (stopped) {
			return null;
		}
		synchronized (this) {
			E poll = list.poll();
			if (poll == null) {
				try {
					System.out.println("Waiting - queue empty " + Thread.currentThread().getName());
					this.wait();
					System.out.println("Notified " + Thread.currentThread().getName());
					poll = list.poll();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} 
			return poll==null?poll():poll;
		}
	}

	@Override
	public E element() {
		synchronized (this) {
			return list.element();
		}
	}

	@Override
	public E peek() {
		synchronized (this) {
			return list.peek();
		}
	}
	
	public void stop () {
		synchronized (this) {
			this.stopped = true;
			this.notifyAll();
		}
	}

}
