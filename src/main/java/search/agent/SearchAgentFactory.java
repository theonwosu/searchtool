/**
 * Copyright (c) 2008 by Theo Nwosu. All Rights Reserved.
 *  
 * Theo Nwosu grants you a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that this copyright notice and license appear on all copies of
 * the software.
 * 
 * This software is provided "AS IS," without a warranty of any kind.
 */
package search.agent;

import search.exceptions.SearchException;
/**
 * @author Theophine Nwosu
 * 
 */
public class SearchAgentFactory {
  private static SearchAgentFactory factory = null;
  
  private SearchAgentFactory(){
  }
  
  public static synchronized SearchAgentFactory getInstance(){
    if(factory == null){
      factory = new SearchAgentFactory();
    }
    return factory;
  }
  
  /**
   * @param type
   * @param archivesToSearch
   * @param filesToFind
   * @return
   * @throws SearchException
   */
  public SearchAgent getSearchAgent(int type,String archivesToSearch, String filesToFind) throws SearchException{
    if(SearchAgent.SEARCH_AGENT == type){
      return new FindFileSearchAgent(archivesToSearch, filesToFind);
    }
    else if(SearchAgent.INSIDE_FILE_SEARCH_AGENT == type){
      return new ContainsTextSearchAgent(archivesToSearch, filesToFind);
    }
    throw new SearchException("Invalid Search Agent Type");
  }
}
