package com.karthik.concurrent.sample;

import com.karthik.concurrent.Task;

public class DivisionTask implements Task<Integer> {
	
	private int a;
	private int b;

	public DivisionTask(int a, int b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public Integer run() throws Exception {
		return a/b;
	}

}
