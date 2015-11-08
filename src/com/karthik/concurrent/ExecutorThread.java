package com.karthik.concurrent;

import java.util.concurrent.ExecutionException;

public class ExecutorThread extends Thread{
	
	private ExecutorService executorService;
	
	public ExecutorThread(ExecutorService executorService) {
		this.executorService = executorService;
	}

	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName() + " starting up");
		for (;;) {
			if (executorService.getQueue().size() == 0 && executorService.getExecutorState() == executorService.Shutdown) {
				System.out.println(Thread.currentThread().getName() + "  exiting");
				return;
			} else {
				TaskFuture taskFuture = executorService.getQueue().poll();
				if(taskFuture != null) {
					synchronized (taskFuture) {
						try {
							System.out.println("Task to be executed  "+ Thread.currentThread().getName());
							Object result = taskFuture.getTask().run();
							taskFuture.setResult(result);
							taskFuture.setStatus(taskFuture.COMPLETED);
							taskFuture.notify();
						} catch (Exception e) {
							taskFuture.setException(new ExecutionException(e));
							taskFuture.setStatus(taskFuture.FAILED);
							taskFuture.notify();
						} finally {
							System.out.println("Execution of task complete "+ Thread.currentThread().getName());
						}
					}
				}
			}
		}
	}
}
