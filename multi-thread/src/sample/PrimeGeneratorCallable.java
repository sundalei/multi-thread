package sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PrimeGeneratorCallable implements Callable<List<Long>> {
	private final long from;

	private final long to;

	public PrimeGeneratorCallable(long from, long to) {
		this.from = from;
		this.to = to;
	}
	
	@Override
	public List<Long> call() throws Exception {
		List<Long> primes = new ArrayList<Long>();
		for(long i = from; i <= to; i++) {
			if(isPrime(i)) {
				primes.add(i);
			}
		}
		return primes;
	}

	private boolean isPrime(long number) {
		for (long i = 2; i < number - 1; i++) {
			if (number % i == 0) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		final int numTasks = 10;
		ExecutorService executor = Executors.newFixedThreadPool(numTasks);
		final long range = (long)Math.pow(10, 4);
		List<Future<List<Long>>> futures = new ArrayList<Future<List<Long>>>();
		for(int i = 0; i < numTasks; i++) {
			final long from = i * range + 1;
			final long to = (i + 1) * range;
			Future<List<Long>> future = executor.submit(new PrimeGeneratorCallable(from, to));
			futures.add(future);
		}
		
		executor.shutdown();
		
		List<Long> totalPrimes = new ArrayList<Long>();
        for (Future<List<Long>> future : futures) {
            try {
                List<Long> primes = future.get();
                totalPrimes.addAll(primes);
            } catch (InterruptedException e) {
                System.err.println("Cannot get result:" + e + ", continue");
                Thread.currentThread().interrupt();
                continue;
            } catch (ExecutionException e) {
                System.err.println("Cannot get result:" + e + ", continue");
                continue;
            }
        }
        Collections.sort(totalPrimes);
        for (Long prime : totalPrimes) {
            System.out.print(prime + ",");
        }
	}
}
