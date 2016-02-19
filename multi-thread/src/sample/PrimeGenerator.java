package sample;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PrimeGenerator implements Runnable {

	private final long from;
	
	private final long to;
	
	public PrimeGenerator(long from, long to) {
		this.from = from;
		this.to = to;
	}
	
	@Override
	public void run() {
		for(long i = from; i <= to; i++) {
			if(isPrime(i)) {
				System.out.print(i + ", ");
			}
		}
	}
	
	private boolean isPrime(long number) {
		for(long i = 2; i < number - 1; i++) {
			if(number % i == 0) {
				return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		final int numTasks = 10;
		ExecutorService executor = Executors.newFixedThreadPool(numTasks);
		final long range = (long)Math.pow(10, 4);
		for(int i = 0; i < numTasks; i++) {
			final long from = i * range + 1;
			final long to = (i + 1) * range;
			executor.execute(new PrimeGenerator(from, to));
		}
		executor.shutdown();
	}
}

