package sample;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PrimeGeneratorCallableTest {
	public static void main(String[] args) {
		final int numTasks = 10;
		final long range = (long)Math.pow(10, 4);
		/*
		List<Callable<List<Long>>> tasks = new ArrayList<Callable<List<Long>>>();
		for(int i = 0; i < numTasks; i++) {
			final long from = i * range + 1;
	        final long to = (i + 1) * range;
	        tasks.add(new PrimeGeneratorCallable(from, to));
		}
		
		ExecutorService executor = Executors.newFixedThreadPool(numTasks);
	    try {
	        List<Future<List<Long>>> futures = executor.invokeAll(tasks);
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
	    */
		ExecutorService executor = Executors.newFixedThreadPool(numTasks);
	    CompletionService<List<Long>> completion = new ExecutorCompletionService<List<Long>>(executor);
	    for (int i = 0; i < numTasks; i++) {
	        final long from = i * range + 1;
	        final long to = (i + 1) * range;
	        completion.submit(new PrimeGeneratorCallable(from, to));
	    }
	    
	    executor.shutdown();
	    
	    List<Long> totalPrimes = new ArrayList<Long>();
	    for (int i = 0; i < numTasks; i++) {
	        try {
	            Future<List<Long>> future = completion.take();
	            List<Long> primes = future.get();
	            totalPrimes.addAll(primes);
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	            continue;
	        } catch (ExecutionException e) {
	            continue;
	        }
	    }
	    
	    for (Long prime : totalPrimes) {
            System.out.print(prime + ",");
        }
	}
}
