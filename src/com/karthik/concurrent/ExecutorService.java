package com.karthik.concurrent;

import java.util.LinkedList;

public class ExecutorService {

	private SynchronizedBlockingPollingQueue<TaskFuture> queue= new SynchronizedBlockingPollingQueue<TaskFuture>();
	
	private LinkedList<ExecutorThread> threadPool = new LinkedList<ExecutorThread>();
	
	private int executorState;
	
	public static final int Running = 0;
	
	public static final int Shutdown = 1;
	
	public ExecutorService(int threadCount) {
		int index =0;
		while (index < threadCount) {
			ExecutorThread executorThread = new ExecutorThread(this);
			threadPool.add(executorThread);
			executorThread.start();
			index++;
		}
		System.out.println("Executor service ready for serving");
	}
	
	public <T> TaskFuture<T> submit(Task<T> task) throws RejectedTaskException {
		TaskFuture<T> taskFuture = new TaskFuture<T>(task);
		if (getExecutorState() == Running) {
			getQueue().add(taskFuture);
			System.out.println("Task submitted");
		} else {
			System.out.println("Task rejected, service already shutdown");
			throw new RejectedTaskException();
		}
		return taskFuture;
	}
	
	public void shutDown() {
		this.executorState = 1;
		queue.stop();
		System.out.println("Executor service shutdown");
	}

	public int getExecutorState() {
		return executorState;
	}

	public SynchronizedBlockingPollingQueue<TaskFuture> getQueue() {
		return queue;
	}

}
