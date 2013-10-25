import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

	
public class Scheduler {
	
	public static void main(String[] args){
		try {
			Scheduler s = new Scheduler(Integer.parseInt(args[0]), args[1]);
		} catch (NumberFormatException e) {
			
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		
	}
	
		PriorityQueue<IntegerItem> jobs = new PriorityQueue<IntegerItem>(new JobComparator());
		PriorityQueue<ProcessorItem> processors = new PriorityQueue<ProcessorItem>(new ProcessorComparator());
		ArrayList<Integer> parseJobs = new ArrayList<Integer>();
		
	public Scheduler(int numberOfProcessors, String fileName) throws FileNotFoundException{
		Scanner sc = new Scanner(new FileReader(fileName));
		int count = 0;
		int[] procTrack = new int[numberOfProcessors];
		String[] endOut = new String[numberOfProcessors];
		boolean canRun = true;
		
		while(sc.hasNext()){
			parseJobs.add(sc.nextInt());
			count++;
		}
		sc.close();
			
		if(numberOfProcessors > count){
			numberOfProcessors = count;
			System.out.println("The number Of Processors exceeds the number of Jobs, " +
					"only " + numberOfProcessors + " processors will be used");
		}
		
		if(numberOfProcessors == 0 && count > 0){ //Incase someone tries something funny
			System.out.println("No Processors, cannot complete any jobs!");
			System.out.println("Shutting down ...");
			canRun = false; // shutdown, dont run if there are 0 processors and jobs > 0
		}
		
		IntegerItem [] heapArray = new IntegerItem[parseJobs.size()];
		
		
		for(int m = 0; m<heapArray.length; m++){	
			heapArray[m] = new IntegerItem(parseJobs.get(m));
			jobs.pqInsert(heapArray[m]);				
		}
		
		if(canRun){	
			//Initialize Processors to the queue, with biggest jobs first
			for(int i = 0; i < numberOfProcessors; i++){
				int job = getJob(jobs); //yo dawg...
				procTrack[i] += job; // adds the work to our processor tracking array
				endOut[i] = ("Processor " + ( i + 1 ) + " Tasks: " + job);
				processors.pqInsert(new ProcessorItem(i, procTrack[i])); //assigns what work the proc must do
				count--; // decrease the amount of jobs needed to be done
			}
	
			//Assign the remaining job to processors in LPT	
			while(count > 0){
				int busy = getProc(processors); 
				int job = getJob(jobs); //yo dawg....
				endOut[busy] += (" "+job); 
				procTrack[busy] += job; // adds the biggest job to our Processor tracking array
				processors.pqInsert(new ProcessorItem(busy, procTrack[busy])); //inserting the new job into the proc queue
				count--;
			
			}	
			System.out.println("A simple machine scheduling problem by xx: \nUsing the LPT algorithm on " + numberOfProcessors
					+ " Processors" );
			longest(procTrack);
			outputAtEnd(endOut);
		}
	}
	private int getJob(PriorityQueue<IntegerItem> job) {return job.pqDelete().getKey();}

	private int getProc(PriorityQueue<ProcessorItem> proc) { return proc.pqDelete().getProcessor();	}
	
	private void outputAtEnd(String[] a){
		for(int i = 0; i < a.length; i++){
			if(a[i] != null){
				System.out.println(a[i]);
			}
			
		}
	}
	
	private void longest(int[] b){
		int largest = 0;
		for(int i = 0; i < b.length; i++){
			if(b[i] > largest){
				largest = b[i];
			}
		}
		System.out.println("The time at which the last task completes is: " + largest);
	}
	
}
	

	
	

	
		
		
	
	
	
	
