package part2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PA2 {
	
	/**
	 * Weighted quick-union (without path compression).
	 * @author http://algs4.cs.princeton.edu/
	 *
	 */
	public class WeightedQuickUnionUF {
	    
		private int[] id;    // id[i] = parent of i
	    
		private int[] sz;    // sz[i] = number of objects in subtree rooted at i
	    
		private int count;   // number of components

	    // Create an empty union find data structure with N isolated sets.
	    public WeightedQuickUnionUF(int N) {
	        count = N;
	        id = new int[N];
	        sz = new int[N];
	        for (int i = 0; i < N; i++) {
	            id[i] = i;
	            sz[i] = 1;
	        }
	    }

	    // Return the number of disjoint sets.
	    public int count() {
	        return count;
	    }

	    // Return component identifier for component containing p
	    public int find(int p) {
	        while (p != id[p])
	            p = id[p];
	        return p;
	    }

	   // Are objects p and q in the same set?
	    public boolean connected(int p, int q) {
	        return find(p) == find(q);
	    }

	  
	   // Replace sets containing p and q with their union.
	    public void union(int p, int q) {
	        int i = find(p);
	        int j = find(q);
	        if (i == j) return;

	        // make smaller root point to larger one
	        if   (sz[i] < sz[j]) { id[i] = j; sz[j] += sz[i]; }
	        else                 { id[j] = i; sz[i] += sz[j]; }
	        count--;
	    }

	}
	
	public class Link implements Comparable<Link> {
		public Link(int a, int b, double c){
			this.a = a;
			this.b = b;
			this.cost = b;
		}
		
		public Link() {
			this.a = this.b = -1;
			this.cost = 0.0;
		}
		
		public int a, b;
		
		public double cost;
		
		@Override
		public int compareTo(Link o) {
			if(o.cost<this.cost) return 1;
			else if(o.cost>this.cost) return -1;
			return 0;
		}
		
		@Override
		public String toString() {
			return a + "," + b + "," + cost;
		}
	}
	
	public double MaxSpacingKClustering(String fileName, int numClusters) throws IOException {

		List<Link> linkList = new ArrayList<Link>();
		Scanner input = new Scanner(new File(fileName));
		int numNodes = input.nextInt();
		int numLinks = (numNodes-1)*numNodes/2;
		for(int i=0;i<numLinks;i++) {
			Link current = new Link();
			current.a = input.nextInt();
			current.b = input.nextInt();
			current.cost = input.nextDouble();
			linkList.add(current);
		}
		input.close();
		Collections.sort(linkList);
		WeightedQuickUnionUF UFObj = new WeightedQuickUnionUF(numNodes+1);
		int K = numNodes;
		for(int i=0;i<linkList.size();i++) {
			Link link = linkList.get(i);
			if(!UFObj.connected(link.a, link.b)) {
				UFObj.union(link.a, link.b);
				K--;
				if(K==numClusters) break;
			}
		}
		double minSpace = Double.MAX_VALUE;
		for(int i=0;i<numLinks;i++) {
			Link link = linkList.get(i);
			if(!UFObj.connected(link.a, link.b)){
				minSpace = Math.min(minSpace, link.cost);
			}
		}
		return minSpace;
	}
	
	public int ImplicitlyMaxSpacingKClustering(String fileName) throws IOException {
		Scanner input = new Scanner(new File(fileName));
		int numNodes = input.nextInt();
		int numBits = input.nextInt();
		List<String> nodeList = new ArrayList<String>();
		Map<String, Integer> labelMap = new HashMap<String, Integer>();
		int pt = 0;
		for(int i=0;i<numNodes;i++) {
			String line = "";
			for(int j=0;j<numBits;j++) line += input.next();
			if(!labelMap.containsKey(line)) {
				nodeList.add(line);
				labelMap.put(line, pt++);
			}
		}
		input.close();
		WeightedQuickUnionUF UFObj = new WeightedQuickUnionUF(pt);
		for(String node : nodeList) {
			int id = labelMap.get(node);
			for(int i=0;i<numBits;i++){
				for(int j=i;j<numBits;j++){
					char[] cList = node.toCharArray();
					cList[i] = (char) (((cList[i]-'0')+1)%2 + '0');
					if(i!=j) cList[j] = (char) (((cList[j]-'0')+1)%2 + '0');
					String tmp = new String(cList);
					if(labelMap.containsKey(tmp)) {
						int cid = labelMap.get(tmp);
						UFObj.union(id, cid);
					}
				}
			}
		}
		return UFObj.count();
	}
	
	
	/**
	 * Main Function
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		PA2 pa2 = new PA2();
		System.out.println(pa2.MaxSpacingKClustering("data/clustering1.txt", 4));
		System.out.println(pa2.ImplicitlyMaxSpacingKClustering("data/clustering_big.txt"));
	}
	
}
