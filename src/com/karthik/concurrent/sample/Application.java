package com.karthik.concurrent.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.karthik.concurrent.ExecutorService;
import com.karthik.concurrent.RejectedTaskException;
import com.karthik.concurrent.TaskFuture;

public class Application {
	public static void main(String[] args) throws RejectedTaskException {
		int a[] = {1,2,3,4,5,6,7,8,9,10};
		int b[] = {1,2,3,4,5,6,7,8,9,0};
		List<TaskFuture<Integer>> list = new ArrayList<TaskFuture<Integer>>();
		ExecutorService service = new ExecutorService(5);
		for(int i =0; i<10; i++) {
			DivisionTask task = new DivisionTask(a[i], b[i]);
			TaskFuture<Integer> future = service.submit(task);
			list.add(future);
		}
		for (int i =9; i>=0; i--) {
			try {
				TaskFuture<Integer> future = list.get(i);
				Integer result = future.get();
				System.out.println("result: " + result);
			} catch (InterruptedException | ExecutionException e) {
				System.out.println(e.getMessage());
			}
		}
		service.shutDown();
		try {
			service.submit(new DivisionTask(a[5], b[0]));
		} catch (RejectedTaskException ex) {
			System.out.println("TaskSubmission failed");
		}
	}
}
