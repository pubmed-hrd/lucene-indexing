package com.medline;

import com.medline.repository.AbstractRepository;
import com.medline.service.IndexingService;

public class App {
	public static void main(String[] args) {
		Integer limit = 0;
		boolean isAppend = false;
		String pwd = null, indexPath = null, filePath = null;

		for (int i = 0; i < args.length; i++) {
			if ("-limit".equals(args[i])) {
				limit = Integer.parseInt(args[i + 1]);
				i++;
			} else if ("-indexPath".equals(args[i])) {
				indexPath = args[i + 1];
				i++;
			} else if ("-isAppend".equals(args[i])) {
				isAppend = true;
				i++;
			} else if ("-pwd".equals(args[i])) {
				pwd = args[i + 1] + "!@";
				i++;
			} else if ("-filePath".equals(args[i])) {
				filePath = args[i + 1];
				i++;
			} 
		}

		System.out.println(String.format("filePath:%s, limit:%s, pwd:%s, isAppend:%s, indexPath:%s", filePath, limit, pwd, isAppend, indexPath));

		IndexingService idx = new IndexingService(new AbstractRepository());
		idx.readAndIndex(filePath, indexPath, isAppend);
		
	}
}
