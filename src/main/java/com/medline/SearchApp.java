package com.medline;

import java.util.List;
import java.util.Scanner;

import com.medline.model.Sentence;
import com.medline.service.SearchingService;

public class SearchApp {
	public static void main(String[] args) {
		
		String indexPath = args[0];
		SearchingService s = new SearchingService(indexPath);
		Scanner sc = new Scanner(System.in);
		while(true){
			
			System.out.print("\n->Enter Search Query: ");
			String query = sc.nextLine();
			if(query.equals("exit"))
				break;
			
			System.out.println(String.format("-> Start searching for: %s", query));
			long start = System.currentTimeMillis();
			List<Sentence> sentences = s.search(query);
			System.out.println(String.format("-> Searching finish in %s seconds.\n", (System.currentTimeMillis() - start) * Math.pow(10, -3)));
			
			System.out.println("-> Results: ");
			for(Sentence sen: sentences){
				System.out.println("\t" + sen);
			}
		}
		sc.close();
		System.out.println("Program Terminated!!!");
		
	}
}
