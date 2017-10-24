package com.medline.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.medline.model.Abstract;
import com.medline.repository.AbstractRepository;

public class IndexingService {

	private Analyzer analyzer;
	private AbstractRepository repository;
	private Integer limit = 10_000;
	
	public IndexingService(AbstractRepository repository) {
		this.repository = repository;
	}

	public boolean createIndex(String indexPath, boolean isAppend) {
		try {
			analyzer = new StandardAnalyzer();

			Directory dir = FSDirectory.open(Paths.get(indexPath));
			IndexWriterConfig config = new IndexWriterConfig(analyzer);

			if (!isAppend) {
				config.setOpenMode(OpenMode.CREATE);
			} else {
				config.setOpenMode(OpenMode.CREATE_OR_APPEND);
			}			
			IndexWriter writer = new IndexWriter(dir, config);
			
			Integer totalPages = (int) Math.ceil(27_575_896/limit);
			
			for (int page = 1; page <= 10; page++) {
				System.out.println(String.format("-> Start reading %s records[page %s of %s]", limit, page, totalPages));
				long start = System.currentTimeMillis();
				
				List<Abstract> abstracts = repository.findAllAbstracts(getLimit(), (page - 1) * getLimit());
				
				System.out.println(String.format("-> Finish reading from database in %s seconds", (System.currentTimeMillis() - start) * Math.pow(10, -3)));
				addIndex(writer, abstracts);
				abstracts = null;
			}
			writer.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private void addIndex(IndexWriter write, List<Abstract> abstracts) throws IOException {
		System.out.println(String.format("-> Start indexing %s pmid...", abstracts.size()));
		long start = System.currentTimeMillis();
		for (Abstract abs : abstracts) {
			try{
				Document doc = new Document();
				if(abs.getValue() != null){
					doc.add(new TextField("title", abs.getTitle(), Field.Store.YES));
					doc.add(new TextField("abstract", abs.getValue(), Field.Store.YES));
					doc.add(new TextField("journalTitle", abs.getJournalTitle(), Field.Store.YES));
					doc.add(new StringField("date", abs.getDate(), Field.Store.YES));
					doc.add(new StringField("pmid", abs.getPmid(), Field.Store.YES));
					write.addDocument(doc);
				}
			}catch(Exception ex){
				System.out.println("Exception: " + ex.getMessage() + ", " + abs);
			}
		}
		System.out.println(String.format("-> Finish indexing in %s seconds.\n", (System.currentTimeMillis() - start) * Math.pow(10, -3)));
	}
	
	public boolean readAndIndex(String folder, String indexPath, boolean isAppend) {
		try {
			analyzer = new StandardAnalyzer();

			Directory dir = FSDirectory.open(Paths.get(indexPath));
			IndexWriterConfig config = new IndexWriterConfig(analyzer);

			if (!isAppend) {
				config.setOpenMode(OpenMode.CREATE);
			} else {
				config.setOpenMode(OpenMode.CREATE_OR_APPEND);
			}			
			IndexWriter writer = new IndexWriter(dir, config);
			
			//TODO: read from file
			File files = new File(folder);
			File[] listOfFiles = files.listFiles();

			for (int i = 0; i < listOfFiles.length; i++) {
				String filePath = listOfFiles[i].getAbsolutePath();
				
				System.out.println(String.format("-> Executing file: %s", listOfFiles[i].getAbsolutePath()));
				
				try (BufferedReader b = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"))) {
					String readLine = "";
					while ((readLine = b.readLine()) != null) {
						String[] sTexts = readLine.split("\t", 4);

						//System.out.println(String.format("%s\t%s\t%s\t%s", sTexts[0], sTexts[1], sTexts[2], sTexts[3]));
						
						String pmid = sTexts[0];
						String abstractTextOrder = sTexts[1];
						String sentenceOrder = sTexts[2];
						String sentence = sTexts[3];
						
						//TODO: Indexing 
						Document doc = new Document();
						if (sentence != null) {
							doc.add(new StringField("pmid", pmid, Field.Store.YES));
							doc.add(new StringField("abstractTextOrder", abstractTextOrder, Field.Store.YES));
							doc.add(new StringField("sentenceOrder", sentenceOrder, Field.Store.YES));
							doc.add(new TextField("sentence", sentence, Field.Store.YES));
							writer.addDocument(doc);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			writer.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}
}
