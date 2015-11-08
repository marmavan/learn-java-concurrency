package com.karthik.concurrent;

public interface Task<T> {

	public T run() throws Exception;
	
	
}
