/**
 * Copyright (c) 2008 by Theo Nwosu. All Rights Reserved.
 *  
 * Theo Nwosu grants you a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that this copyright notice and license appear on all copies of
 * the software.
 * 
 * This software is provided "AS IS" without a warranty of any kind.
 */
package search.agent;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import search.common.UIDGenerator;
import search.common.filters.ArchiveFilter;
import search.common.filters.Filter;
import search.exceptions.SearchException;
import search.matcher.SearchMatcher;
import search.observers.SearchObserver;
import search.util.Util;
import search.util.UtilFactory;

/**
 * @author Theophine Nwosu
 * 
 */
public abstract class SearchAgent {
  private static final String DIRECTORY_TO_SEARCH_CAN_T_BE_EMPTY = "Directory to search can't be empty";
  private static final String FILE_TO_SEARCH_CAN_T_BE_EMPTY = "File to search can't be empty";
  private static final String SEARCH_FIELD_CAN_T_BE_EMPTY = "Search field can't be empty";
  public static final int SEARCH_AGENT = 1;
  public static final int INSIDE_FILE_SEARCH_AGENT = 2;  
  public static final String FILE_INFO = "INFO:::::";
  protected static final String JAR_EXTENSION = ".jar";
  protected static final String WAR_EXTENSION = ".war";
  protected static final String EAR_EXTENSION = ".ear";
  protected static final String ZIP_EXTENSION = ".zip";  
  protected static final int RESULT = 0;
  protected static final int STATUS = 1;
  protected String compressedFile = null;
  protected Map map = new HashMap();
  protected Map levelMap = new HashMap();  
  protected int level = 0;
  protected boolean CANCEL = false;
  protected String excludePatterns = "";
  protected int matcherType = SearchMatcher.EXACT_MATCHER;
  protected int caseSensitivity = SearchMatcher.IGNORE_CASE;
  private final String TEMP = "temp999";
  private boolean first = true;
  private String hold;
  private List observers = new ArrayList(); 
  private String archivesToSearch = ArchiveFilter.SUPPORTED_ARCHIVE_TYPES;
  private String filesToFind = "*";
  

  public SearchAgent() {
    this(ArchiveFilter.SUPPORTED_ARCHIVE_TYPES, Filter.ALL_FILE_TYPES);
  }

  /**
   * @param archivesToSearch
   * @param filesToFind
   */
  public SearchAgent(String archivesToSearch, String filesToFind) {
    this.archivesToSearch = archivesToSearch;
    this.filesToFind = filesToFind;
  }

  /**
   * This method will list all files in a given jar file that matches a search
   * string
   * 
   * @param file
   * @param searchStr
   * @throws Exception
   */
  public void processFile(File file, String searchStr, String javaHome) throws SearchException {
    javaHome = UtilFactory.getInstance().getUtil().validateJavaHome(javaHome);
    searchStr = validateInput(file, searchStr,FILE_TO_SEARCH_CAN_T_BE_EMPTY,SEARCH_FIELD_CAN_T_BE_EMPTY);
    if(!file.exists() && !isRoot(file)){
      throw new SearchException(file +" does not exist");
    }
    if (file.isDirectory()) {
      processFiles(file, searchStr, javaHome);
      return;
    }
    if (CANCEL) {
      return;
    }
    process(file, searchStr, javaHome);
  }

  /**
   * @param file
   * @param searchStr
   * @param javaHome
   * @throws SearchException
   */
  private void process(File file, String searchStr, String javaHome) throws SearchException {
    String name = file.toString();
    matchFile(name, searchStr);
    processArchive(name, searchStr, javaHome);
  }

  /**
   * @param file
   * @param searchStr
   * @param fileMessage
   * @param searchFieldMessage
   * @return
   * @throws SearchException
   */
  private String validateInput(File file, String searchStr, String fileMessage, String searchFieldMessage)
      throws SearchException {
    if (file == null) {
      throw new SearchException(fileMessage);
    }
    if (searchStr == null || (searchStr = searchStr.trim()).length() < 1) {
      throw new SearchException(searchFieldMessage);
    }
    return searchStr;
  }

  /**
   * This method will recursively search a directory and list all jar files that
   * contain a file matching a given search string
   * 
   * @param directory
   * @param searchStr
   * @throws SearchException
   */
  public final void processFiles(File directory, String searchStr, String javaHome) throws SearchException {

    javaHome = UtilFactory.getInstance().getUtil().validateJavaHome(javaHome);
    searchStr = validateInput(directory, searchStr,DIRECTORY_TO_SEARCH_CAN_T_BE_EMPTY,SEARCH_FIELD_CAN_T_BE_EMPTY);
    if (!directory.isDirectory()) {
      processFile(directory, searchStr, javaHome);
      return;
    }
    File[] files = directory.listFiles();
    // check for null (when a directory is not readable)
    for (int i = 0; files != null && i < files.length; i++) {
      if(CANCEL){
        return;
      }
      if (files[i].isDirectory()) {
        processFiles(files[i], searchStr, javaHome);
      }
      else {
        process(files[i], searchStr, javaHome);
      }
    }
  }
  
  public boolean isNestedArchive(List contents){
    String temp;
    for (int i = 0; contents != null && i < contents.size(); i++) {
      temp = (String)contents.get(i);
      if (temp.toLowerCase().endsWith(JAR_EXTENSION) || temp.toLowerCase().endsWith(ZIP_EXTENSION) || temp.toLowerCase().endsWith(EAR_EXTENSION) || temp.toLowerCase().endsWith(WAR_EXTENSION)){
        return true;
      }
    }
    return false;
  }
  
  /**
   * @param filePath
   * @param searchStr
   * @return
   * @throws SearchException
   */
  abstract protected void matchFile(String filePath, String searchStr) throws SearchException;  
  
  /**
   * @param name
   * @param searchStr
   * @param javaHome
   * @throws SearchException
   */
  abstract protected void processArchive(String name, String searchStr, String javaHome) throws SearchException;  

  /**
   * @param file
   * @return
   */
  protected boolean isDirectory(String file) {
    if (file != null && (file.endsWith("/") || file.endsWith("\\"))) {
      return true;
    }
    return false;
  }

  /**
   * Utility method to format path to file.
   * 
   * @param pathToFile
   * @return
   */
  private String formatPath(String pathToFile) {
    if (pathToFile.trim().length() > 0) {
      String fileSeparator = File.separator;
      if (fileSeparator.equals("\\")) {
        pathToFile = pathToFile.replaceAll("/", "\\\\");
      }
      return File.separator + pathToFile;
    }
    return pathToFile.trim();
  }

  /**
   * This method will display the result of a search operation. It is used when
   * the jar file is not contained in an archive, for example, inside a zip -
   * that is when the value of the compressedFile variable is null. It just
   * appends the full package name to the full path of the jar file
   * 
   * @param jarName
   * @param pathToFile
   */
  protected String getPathToFile2(String jarName, String pathToFile) {
    String fileSeparator = File.separator;
    StringBuffer fullPathToFile = new StringBuffer();
    if (fileSeparator.equals("\\")) {
      pathToFile = pathToFile.replaceAll("/", "\\\\");
    }
    fullPathToFile.append(jarName).append(File.separator).append(pathToFile);
    return fullPathToFile.toString();
  }

  /**
   * This method will display the result of a search operation. It uses a hash
   * map implementation of a link list to get the full archive path containing
   * the search file. It then appends the path to the file if for example the
   * file is contained in a package by a call to formatPath(pathToFile)
   * otherwist it appends an empty string
   * 
   * @param name
   * @param pathToFile
   */
  protected String getPathToFile(String name, String pathToFile) {
    String value = (String) map.get(name);
    String temp = null;
    StringBuffer buffer = new StringBuffer();
    StringBuffer buffer2 = new StringBuffer();
    while (value != null) {
      if (map.get(value) == null) {
        temp = getRelativePathContainingSearchStr(name);
        buffer.append(value).append(buffer2).append(temp).append(formatPath(pathToFile));
      }
      else {
        buffer2.insert(0, getRelativePathContainingSearchStr(value));
      }
      value = (String) map.get(value);
    }
    return buffer.toString();
  }

  /**
   * Get relative path containing search string, disregard the temporary
   * directory that was created to extract the files
   * 
   * @param name
   * @return
   */
  private String getRelativePathContainingSearchStr(String name) {
    int len = TEMP.length();
    int index = name.indexOf(TEMP);
    // index = index + len;
    if (index < 0 || (index = index + len) >= name.length()) {
      return "";
    }
    // loop until end of temporay directory substring, return the remaining
    // directory path
    while (name.charAt(index) != '/' && name.charAt(index) != '\\') {
      index++;
      if (index >= name.length()) {
        return "";
      }
    }
    String value = name.substring(index);
    return value;
  }

  /**
   * @param name
   * @return
   */
  protected String getName(String name) {
    File file = new File(name);
    return file.getName();
  }

  /**
   * @param name
   * @param javaHome
   * @param searchStr
   * @throws SearchException
   */
  protected void processZip(String name, String javaHome, String searchStr) throws SearchException {
    String tempDirectory = TEMP + UIDGenerator.getUniqueLong();
    if (first) {
      first = false;
      hold = tempDirectory;
      compressedFile = name;
      map.put(name, null);
    }
    else {
      map.put(name, levelMap.get(level + ""));
    }

    new File(tempDirectory).mkdir();
    Util util = UtilFactory.getInstance().getUtil();
    String executable = util.generateExecutable(javaHome, name, tempDirectory);
    if (executable != null) {
      level++;
      levelMap.put(level + "", name);
      util.execute(executable);
      File temp2 = new File(System.getProperty("user.dir") + "/" + tempDirectory);
      processFiles(temp2, searchStr, javaHome);
      level--;
      File temp1 = new File(executable);
      temp1.delete();
      cleanUP(temp2);
    }
    // Do not set to null until all contents have been processed
    if (tempDirectory.equals(hold)) {
      compressedFile = null;
      first = true;
      levelMap.clear();
      map.clear();
    }
  }
  
  /**
   * This method is to used to verify a root directory where the root directory is CD ROM
   * This is necessary because file exists() method for CD ROM returns false
   * @param file
   * @return
   */
  private boolean isRoot(File file){
    File [] roots = File.listRoots();
    for( int i = 0; roots != null && file != null && i < roots.length; i++){
      if(file.toString().equalsIgnoreCase(roots[i].toString())){
        return true;
      }
    }
    return false;
  }

  /**
   * @param directory
   */
  private void cleanUP(File directory) {
    File[] files = directory.listFiles();
    for (int i = 0; i < files.length; i++) {
      if (files[i].isDirectory()) {
        cleanUP(files[i]);
      }
      files[i].delete();
    }
    directory.delete();
  }

  /**
   * This method will return a list of files in a given directory
   * 
   * @param directory
   * @return
   * @throws SearchException
   */
  public File[] listFiles(File directory) throws SearchException {
    if (directory == null) {
      throw new SearchException(
          "directory parameter passed to listFiles(File directory) method is null");
    }
    if (!directory.isDirectory()) {
      throw new SearchException(
          "File parameter passed to listFiles(File directory) method is not a directory");
    }
    File[] files = directory.listFiles();
    for (int i = 0; i < files.length; i++) {
      System.out.println(files[i]);
    }
    return files;
  }

  /**
   * This method will return a list of files matching a given filter in a given
   * directory
   * 
   * @param directory
   * @param filter
   * @return
   * @throws SearchException
   */
  public ArrayList listFiles(File directory, FileFilter filter) throws SearchException {
    if (directory == null || filter == null) {
      throw new SearchException(
          "directory or filter parameter passed to listFiles(File directory, FileFilter filter) method is null");
    }
    if (!directory.isDirectory()) {
      throw new SearchException(
          "File parameter passed to listFiles(File directory, FileFilter filter) method is not a directory");
    }
    ArrayList list = new ArrayList();
    File[] files = directory.listFiles(filter);
    String fullpath = null;
    for (int i = 0; i < files.length; i++) {
      fullpath = files[i].getAbsolutePath();
      fullpath = fullpath.replaceAll("\\\\", "/");
      list.add(fullpath);
    }
    return list;
  }

  /**
   * This method will list all files in a given jar file that matches a search
   * string
   *  
   * @param file
   * @param searchStr
   * @param javaHome
   * @throws SearchException
   */
  public void processFile(String file, String searchStr, String javaHome) throws SearchException {

    javaHome = UtilFactory.getInstance().getUtil().validateJavaHome(javaHome);
    if (file == null || (file = file.trim()).length() < 1) {
      throw new SearchException("Search file can't be empty");
    }
    File fileObject = new File(file);
    processFile(fileObject, searchStr, javaHome);
  }

  /**
   * This method will recursively search a directory and list all jar files that
   * contain a file matching a given search string
   * 
   * @param dir
   * @param searchStr
   * @param matchType
   * @throws SearchException
   */
  public void processFiles(String dir, String searchStr, String javaHome) throws SearchException {
    javaHome = UtilFactory.getInstance().getUtil().validateJavaHome(javaHome);
    if (dir == null || (dir = dir.trim()).length() < 1) {
      throw new SearchException("Search directory can't be empty");
    }
    File directory = new File(dir);
    processFiles(directory, searchStr, javaHome);
  }

  /**
   * Use this method to process all directories in a computer system
   * 
   * @param searchStr
   * @param javaHome
   * @throws SearchException
   */
  public void processFiles(String searchStr, String javaHome) throws SearchException {
    javaHome = UtilFactory.getInstance().getUtil().validateJavaHome(javaHome);
    File[] directoryRoots = File.listRoots();
    for (int i = 0; directoryRoots != null && i < directoryRoots.length; i++) {
      processFiles(directoryRoots[i], searchStr, javaHome);
    }
  }

  /**
   * @param observer
   */
  public void add(SearchObserver observer) {
    observers.add(observer);
  }

  public void cancel() {
    CANCEL = true;
  }

  /**
   * @param archivesToSearch
   * @param filesToFind
   * @param excludePatterns
   * @param matcherType
   * @param caseSensitivity
   */
  public void reset(String archivesToSearch, String filesToFind, String excludePatterns, int matcherType, int caseSensitivity) {
    this.archivesToSearch = archivesToSearch;
    this.filesToFind = filesToFind;
    this.excludePatterns = excludePatterns;
    this.matcherType = matcherType;
    this.caseSensitivity = caseSensitivity;
    this.CANCEL = false;
  }

  /**
   * @param value
   * @param type
   */
  protected void notifyObservers(String value, int type) {
    for(int i = 0; i < observers.size(); i++){
      SearchObserver observer = (SearchObserver) observers.get(i);
      if(type == RESULT){
        observer.updateResult(value);
      }
      else if(type == STATUS){
        value = "".equals(getRelativePathContainingSearchStr(value)) ? value : getRelativePathContainingSearchStr(value);
        observer.updateStatus("Scanning : " + value);
      }
    }
  }

  public String getArchivesToSearch() {
    return archivesToSearch;
  }

  public void setArchivesToSearch(String archivesToSearch) {
    this.archivesToSearch = archivesToSearch;
  }

  public String getFilesToFind() {
    return filesToFind;
  }

  public void setFilesToFind(String filesToFind) {
    this.filesToFind = filesToFind;
  }
  

  public String getExcludePatterns() {
    return excludePatterns;
  }

  public void setExcludePatterns(String excludePatterns) {
    this.excludePatterns = excludePatterns;
  }

  public int getMatcherType() {
    return matcherType;
  }

  public void setMatcherType(int matcherType) {
    this.matcherType = matcherType;
  }

  public int getCaseSensitivity() {
    return caseSensitivity;
  }

  public void setCaseSensitivity(int caseSensitivity) {
    this.caseSensitivity = caseSensitivity;
  }

  /**
   * @param args
   * @throws SearchException
   */
  public static void main(String[] args) throws SearchException {
    SearchAgent helper = new FindFileSearchAgent();
    System.out.println("Starting search...");
    // File directoryO = new File("C:/bea/weblogic81/server/lib");
    String searchStr = "oracle.security.jazn.login.module.db.DBTableOraDataSourceLoginModule"
        + ".class";
    String directory = "C:/theo-OC4J.10.1.3.1.0";
    String javaHome = "C:/bea/8.1.6/bea/jdk142_11";
    // java_home = "C:/j2sdk1.4.2_08";

    directory = "C:/bea/8.1.6";
    // directory = "C:/theo/jboss/4.2.2.GA";
    // directory = "D:/theo/jboss/JBossApplicationServer/binary";
    directory = "C:/delete/hold/delete/WebContent.war";
    searchStr = "gov.nih.ipf.beantier.lookupservice.LookupEntry";
    searchStr = "UsernamePasswordLoginModule.class";
    searchStr = "test.jsp";
    searchStr = "Action.class";
    // searchStr = "DisplayTreeTag.class";
    // searchStr = "FileElement.class";
    // searchStr = "orion-web.xml";
    // searchStr = "HttpServletRequest.class";
    // helper.listJar(directory,searchStr, java_home);
    // helper.listFileJar(directory,searchStr, java_home);
    helper.setMatcherType(SearchMatcher.EXACT_MATCHER);
    helper.setCaseSensitivity(SearchMatcher.CASE_SENSITIVE);
    helper.processFiles(directory, searchStr, javaHome);
    helper.processFile(directory, searchStr, javaHome);
    System.out.println("Ending search!");
  }  
}
