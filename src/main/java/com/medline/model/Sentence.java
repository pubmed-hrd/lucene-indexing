package com.medline.model;

public class Sentence {
	private String sentence;
	private String pmid;
	private String sentenceOrder;
	private String abstractTextOrder;
	
	public Sentence(String sentence, String pmid, String sentenceOrder, String abstractTextOrder) {
		this.sentence = sentence;
		this.pmid = pmid;
		this.sentenceOrder = sentenceOrder;
		this.abstractTextOrder = abstractTextOrder;
	}
	
	public String getSentence() {
		return sentence;
	}
	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
	public String getPmid() {
		return pmid;
	}
	public void setPmid(String pmid) {
		this.pmid = pmid;
	}
	public String getSentenceOrder() {
		return sentenceOrder;
	}
	public void setSentenceOrder(String sentenceOrder) {
		this.sentenceOrder = sentenceOrder;
	}
	public String getAbstractTextOrder() {
		return abstractTextOrder;
	}
	public void setAbstractTextOrder(String abstractTextOrder) {
		this.abstractTextOrder = abstractTextOrder;
	}
	@Override
	public String toString() {
		return String.format("[pmid: %s, sentenceOrder: %s, sentence: %s, abstractTextOrder: %s]", pmid, sentenceOrder, sentence, abstractTextOrder);
	}
	
}
