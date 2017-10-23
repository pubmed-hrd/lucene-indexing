package com.medline.service;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;

public class SearchingService {
	private IndexReader reader;
	private IndexSearcher searcher;
	private Analyzer analyzer;
	private QueryParser parser;
	
	private String indexPath;
	
	public SearchingService(String indexPath){
		this.indexPath = indexPath;
	}
	
	public void search(String searchquery){
		
		try {
			reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
			searcher = new IndexSearcher(reader);
			analyzer = new StandardAnalyzer();
			//parser = new QueryParser("title", analyzer);
			parser = new MultiFieldQueryParser(new String[]{"title","date"}, analyzer);
			Query query;
			try {
				query = parser.parse(searchquery);
				int hitsPerPage = 10;
				TopScoreDocCollector collector= TopScoreDocCollector.create(hitsPerPage);
				searcher.search(query, collector);
				ScoreDoc[] hits = collector.topDocs().scoreDocs;
				
				System.out.println("Total results: "+ collector.getTotalHits());
				
				for(int i=0; i<hits.length;i++){
					Document doc= searcher.doc(hits[i].doc);
					System.out.println(i + ", Title: "+ doc.get("title") +", Date: "+ doc.get("date"));
				}
				
				reader.close();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
