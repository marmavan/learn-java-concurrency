package com.karthik.concurrent;

import java.util.concurrent.ExecutionException;

public class TaskFuture<T> {
	private Task<T> task;

	private T result;
	
	private ExecutionException exception;
	
	private int status;
	
	public static final int NOT_STARTED=0;
	public static final int COMPLETED=1;
	public static final int FAILED=2;
	
	
	public TaskFuture(Task<T> task) {
		this.task = task;
	}
	
	public T get() throws InterruptedException, ExecutionException {
		synchronized (this) {
			if (getStatus() == NOT_STARTED) {
				System.out.println("Not complete, waiting " + Thread.currentThread().getName());
				this.wait();
				System.out.println("Future execution complete. Notifying " + Thread.currentThread().getName());
				return get();
			} else if (getStatus() == FAILED) {
				System.out.println("Failed");
				throw getException();
			}
			return result;
		}
	}

	public Task<T> getTask() {
		return task;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public ExecutionException getException() {
		return exception;
	}

	public void setException(ExecutionException exception) {
		this.exception = exception;
	}

}
