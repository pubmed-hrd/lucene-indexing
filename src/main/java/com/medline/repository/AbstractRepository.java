package com.medline.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.medline.model.Abstract;
import com.medline.utility.DbUtil;

public class AbstractRepository {
	
	private String pwd;
	
	public AbstractRepository() {
		
	}
	
	public AbstractRepository(String pwd) {
		this.pwd = pwd;
	}
	
    public List<Abstract> findAllAbstracts(Integer limit, Integer offset){
        List<Abstract> abstracts = new ArrayList<>();

        try(Connection con = DbUtil.getConnection(pwd)){
            String sql =  "SELECT"
		            		+ "	A .pmid,"
		            		+ "	A .art_arttitle as title,"
		            		+ "	A .art_journal_title as journal_title,"
		            		+ "	AT.value as abstract,"
		            		+ "	to_char(P.date, 'YYYYMMDD') as publish_date"
	            		+ "	FROM"
		            		+ "	pmid_to_date P"
		            		+ "	INNER  JOIN medcit_art_abstract_abstracttext AT ON AT.pmid = P.pmid"
		            		+ "	INNER  JOIN medcit A ON A.pmid = P.pmid"
		            		+ "	ORDER BY P.pmid"
		            		+ "	LIMIT ? OFFSET ?";
            
            PreparedStatement p = con.prepareStatement(sql);
            p.setInt(1, limit);
            p.setInt(2, offset);

            ResultSet rs = p.executeQuery();
            while(rs.next()){
                Abstract a = new Abstract(rs.getString("pmid"), rs.getString("title"), rs.getString("abstract"), rs.getString("journal_title"), rs.getString("publish_date"));
                abstracts.add(a);
            }
        }catch (Exception ex){
            System.out.println(ex);
        }
        return abstracts;
    }
    
}
