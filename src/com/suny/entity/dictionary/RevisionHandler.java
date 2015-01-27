package com.suny.entity.dictionary;

/*
 * For classes that should return their source control revision.
 * 
 * @author  suny
 * @version $Revision: 1.1 $
 * @date    20140522
 * */
public interface RevisionHandler {
	
	/**
	 * Returns the revision string.
	 * 
	 * @return		the revision
	 */
    public String getRevision();
}
