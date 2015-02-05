package com.suny.entity.emotioncal;

import java.util.HashSet;
import java.util.Set;

import com.suny.entity.dictionary.RevisionHandler;

/**
 * 
 * @author Frank Adolf 
 * @date   20150204
 */

public class LevelWords implements RevisionHandler{

	/**  
	 * level and degree words
	 * */
	public Set<String> superLevel = null;
	
	public Set<String> extremelyLevel = null;
	
	public Set<String> veryLevel = null;
	
	public Set<String> fairlyLevel = null;
	
	public Set<String> slightlyLevel = null;
	
	public Set<String>  oweLevel = null;
	
	/** 
	 * the deny words 
	 */
	public Set<String> denyWords = null;
	
	public Set<String> doubleDenyWords = null;
	
	/**
	 * the perception and think word
	 */
	public Set<String> perceptionWords = null;
	
	public String getRevision() {
		return "The Level words 1.0.0$";
	}
	
	public LevelWords() {
		this.superLevel = new HashSet<String>();
		this.extremelyLevel = new HashSet<String>();
		this.veryLevel = new HashSet<String>();
		this.fairlyLevel = new HashSet<String>();
		this.slightlyLevel = new HashSet<String>();
		this.oweLevel = new HashSet<String>();
		
		this.denyWords = new HashSet<String>();
		this.doubleDenyWords = new HashSet<String>();
		this.perceptionWords = new HashSet<String>();
	}
}
