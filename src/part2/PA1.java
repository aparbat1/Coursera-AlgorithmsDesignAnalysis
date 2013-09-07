package part2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Programming Assignment 1 (Algorithms: Design and Analysis, Part 2)
 * 
 * 	1. weighted sum of completion times
 * 	2. optimal weighted sum of completion times
 * 	3. simple Prim for MST
 * 	
 * @author purlin
 *
 */
public class PA1 {
	
	/**
	 * Nested class for job objects
	 * @author purlin
	 *
	 */
	public class Job {
		public int weight, length;
		public double sortValue;
	}
	
	/**
	 * Read job list
	 * @param fileName: File Name
	 * @return job list
	 * @throws IOException
	 */
	protected List<Job> readListWithNumber(String fileName) throws IOException{
		Scanner input = new Scanner(new File(fileName));
		int numJobs = input.nextInt();
		List<Job> jobs = new ArrayList<Job>();
		for(int i=0;i<numJobs;i++) {
			Job job = new Job();
			job.weight = input.nextInt();
			job.length = input.nextInt();
			jobs.add(job);
		}
		input.close();
		return jobs;
	}
	
	/**
	 * Comparator for sorting jobs
	 * @author purlin
	 *
	 */
	public class ComparatorJob implements Comparator<Job>{
		@Override
		public int compare(Job a, Job b) {
			if(b.sortValue<a.sortValue) return -1;
			else if(b.sortValue==a.sortValue && b.weight<a.weight) return -1;
			return 1;
		}
	}
	
	/**
	 * Problem 1 
	 * @param fileName: File Name
	 * @return weighted sum of completion times
	 * @throws IOException
	 */
	public long computeCompletionTimes(String fileName) throws IOException {
		List<Job> jobs = readListWithNumber(fileName);
		for(int i=0;i<jobs.size();i++) {
			Job job = jobs.get(i);
			job.sortValue = job.weight - job.length;
			jobs.set(i, job);
		}
		Collections.sort(jobs, new ComparatorJob());
		long weightedSum = 0, accTime = 0;
		for(Job job : jobs){
			accTime += job.length;
			weightedSum += accTime * job.weight;
			//System.out.println(job.weight + " " + job.length +" " +job.sortValue + " " + weightedSum);
		}
		return weightedSum;
	}
	
	/**
	 * Problem 2
	 * @param fileName: File Name
	 * @return optimal weighted sum of completion times
	 * @throws IOException
	 */
	public long computeCompletionOptTimes(String fileName) throws IOException {
		List<Job> jobs = readListWithNumber(fileName);
		for(int i=0;i<jobs.size();i++) {
			Job job = jobs.get(i);
			job.sortValue = ((double)job.weight)/job.length;
			jobs.set(i, job);
		}
		Collections.sort(jobs, new ComparatorJob());
		long weightedSum = 0, accTime = 0;
		for(Job job : jobs){
			accTime += job.length;
			weightedSum += accTime * job.weight;
		}
		return weightedSum;
	}
	
	/**
	 * simple Prim for MST
	 * @param fileName
	 * @return weight of MST
	 * @throws IOException
	 */
	public long simplePrim(String fileName) throws IOException{
		Scanner input = new Scanner(new File(fileName));
		int numUsers = input.nextInt();
		int numEdges = input.nextInt();
		int[][] mat = new int[numUsers+1][numUsers+1];
		for(int i=0;i<numUsers+1;i++)
			for(int j=0;j<numUsers+1;j++)
				mat[i][j] = Integer.MAX_VALUE;
		for(int i=0;i<numEdges;i++){
			int s = input.nextInt();
			int t = input.nextInt();
			int c = input.nextInt();
			mat[s][t] = mat[t][s] = c;
		}
		input.close();
		
		boolean[] nodeFlag = new boolean[numUsers+1];
		List<Integer> nodeList = new LinkedList<Integer>();
		nodeFlag[1] = true; nodeList.add(1);
		long mst = 0;
		for(int i=1;i<numUsers;i++){
			int edge = Integer.MAX_VALUE, nodeID = -1;
			for(int n : nodeList) {
				for(int u=1;u<=numUsers;u++)
					if((!nodeFlag[u]) && mat[n][u]<edge) {
						edge = mat[n][u]; nodeID = u;
					}
			}
			//System.out.println(edge+" "+nodeID);
			mst += edge; nodeFlag[nodeID] = true;
			nodeList.add(nodeID);
		}
		return mst;
	}
	
	/**
	 * Main Function
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		PA1 obj = new PA1();
		System.out.println(obj.computeCompletionTimes("data/jobs.txt"));
		System.out.println(obj.computeCompletionOptTimes("data/jobs.txt"));
		System.out.println(obj.simplePrim("data/edges.txt"));
	}

}
