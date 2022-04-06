import java.util.Random;

class Main {
	public static void main(String[] args) {
		
		int[] arr = new int[200000000];
		Random random = new Random();
		
		for (int r = 0; r < arr.length; r++) {
			arr[r] = random.nextInt(10) + 1;
			//System.out.println("calculating...");
		}
		
		long start = System.currentTimeMillis();
		start = System.currentTimeMillis();
		System.out.println("Single Sum: " + Concurrency.Sum(arr));
		System.out.println("Single Thread: " + (System.currentTimeMillis() - start) + " milliseconds");
		System.out.println("Parallel Thread: " + Concurrency.SumParallel(arr));
		System.out.println("Parallel thread: " + (System.currentTimeMillis() - start) + " milliseconds");
	}
}

public class Concurrency extends Thread {
	
	int[] rand;
	int low;
	int high;
	int partial;
	//int total;

	public Concurrency(int[] list, int low, int high) {
		this.rand = list;
		this.low = Math.min(low, list.length);
		this.high = Math.min(high, list.length);
	}

	public void Run() {
		partial = Sum(rand, low, high);
	}

	public static int Sum(int[] rand) {
		return Sum(rand, 0, rand.length);
	}

	public static int Sum(int[] rand, int low, int high) {
		int total = 0;
		for (int i = low; i < high; i++) {
			total += rand[i];
		}
		return total;
	}
	
	public int SumPartial() {
		return partial;
	}

	public static int SumParallel(int[] rslt) {
		return ParallelSum(rslt, Runtime.getRuntime().availableProcessors());
	}

	public static int ParallelSum(int[] rand, int thread) {
		
		int size = (int) Math.ceil(rand.length*1.0/thread);
		int total = 0;
		int calc = 0;
		
		Concurrency[] sums = new Concurrency[thread];
		for (int n = 0; n < thread; n++) {
			sums[n] = new Concurrency(rand, n * size, (n + 1) * size);
			sums[n].start();
		}
		try {
			for (Concurrency sum : sums) {
				sum.join();
			}
		} catch (InterruptedException e) {
			return calc;
		}
		for (Concurrency sum : sums) {
			total += sum.SumPartial();
		}
		return total;
	}
}