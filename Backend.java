import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.zip.DataFormatException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Backend implements BackendInterface{
  private ArrayList<String> Genres;
  private int size;
  private HashTableMap<String, MovieInterface> bigTable;
  private ArrayList<String> inList;
  private ArrayList<String> keys;
  private ArrayList<Integer> Ratings;
  private ArrayList<String> allGenres;
  //uses a file reader vs a path
  public Backend(FileReader FileRead){
    List<MovieInterface> BigList;
    MovieDataReaderInterface read = new MovieDataReader();
    try {
    BigList = read.readDataSet(FileRead);
    } catch (IOException IOException){ System.out.println("File format error");
    return;}
    catch (DataFormatException DataException) {
      System.out.println("File format error");
      return;
    }
    bigTable = new HashTableMap<String, MovieInterface>(BigList.size()*2);
    keys = new ArrayList<String>(BigList.size());
    for (int i=0; i<BigList.size(); i++) {
      MovieInterface current = BigList.get(i);
      String key = "";
      for (int a=0; a<current.getGenres().size(); a++) key.concat(current.getGenres().get(a));
      key.concat(current.getTitle());
      key.concat(current.getDirector());
      bigTable.put(key, current);
      keys.add(key);
      Iterator<String> itGenre = current.getGenres().iterator();
      String cGenre;
      allGenres = new ArrayList<String>(10);
      int gcount=0;
      while (itGenre.hasNext()) {
        cGenre = itGenre.next();
        if (!allGenres.contains(cGenre)) {
          allGenres.add(cGenre); 
          gcount++;}
        }
        }
      /*System.out.println("loaded ratings:");
      System.out.println(BigList.get(0).getAvgVote());
      System.out.println(BigList.get(1).getAvgVote());
      System.out.println(BigList.get(2).getAvgVote());
      */
      inList = new ArrayList<String>(BigList.size());
      Genres = new ArrayList<String>(10);
      Ratings = new ArrayList<Integer>(11);
      size = 0;
      }
  
  
  //just uses a path
  public Backend(String FilePath) throws FileNotFoundException {
    List<MovieInterface> BigList;
    FileReader file = new FileReader(FilePath);
    MovieDataReader read = new MovieDataReader();
    try {
      ///System.out.println("Right before readdataset");
      BigList = read.readDataSet(file);
      } catch (IOException IOException){ System.out.println("File format error");
      return;}
      catch (DataFormatException DataException) {
        System.out.println("File format error");
        return;
      }/*
    System.out.println("Succesfully loaded in file, film count:");
    System.out.println(BigList.size());
    */
    bigTable = new HashTableMap<String, MovieInterface>(BigList.size()*2);
    keys = new ArrayList<String>(BigList.size());
    for (int i=0; i<BigList.size(); i++) {
      MovieInterface current = BigList.get(i);
      String key = current.getDirector();
      for (int a=0; a<current.getGenres().size(); a++) key= key+current.getGenres().get(a);
      key=key+current.getTitle();
      bigTable.put(key, current);
      keys.add(key);
      Iterator<String> itGenre = current.getGenres().iterator();
      String cGenre;
      allGenres = new ArrayList<String>(10);
      int gcount=0;
      while (itGenre.hasNext()) {
        cGenre = itGenre.next();
        if (!allGenres.contains(cGenre)) {
          allGenres.add(cGenre); 
          gcount++;}
        if (gcount+2 == allGenres.size()) {
          ArrayList<String> newAllGenres = new ArrayList<String>((gcount+2)*2);
          Iterator<String> genit2 = allGenres.iterator();
          while (genit2.hasNext()) newAllGenres.add(genit2.next());
          allGenres = newAllGenres;
        }
        }
      }
      inList = new ArrayList<String>(BigList.size());
      Genres = new ArrayList<String>(10);
      Ratings = new ArrayList<Integer>(11);
      size = 0;
    
  }
  private void sweep() {
    MovieInterface currentFilm;
    boolean validGenre =false;
    for (int i=0; i<bigTable.size(); i++) {
      currentFilm = bigTable.get(keys.get(i));
      Iterator<String> genreIt  = Genres.iterator();
      while (genreIt.hasNext()) {
        if (Genres.contains(genreIt.next())) validGenre=true;
      }
      if (validGenre) {
        validGenre=false;
        if (Ratings.contains((Integer) currentFilm.getAvgVote().intValue())) {
          if (!inList.contains(keys.get(i))) {
            inList.add(keys.get(i));
            size++;
          }
      }
      
    }
  }
  }
  private void doubleGenre() {
    ArrayList<String> newList = new ArrayList<String>(Genres.size()*2);
    for (int i=0;i<Genres.size(); i++) {
      newList.set(i, Genres.get(i));
    }
    Genres = newList;
  }
  public void addGenre(String genre) {
    if (Genres.contains(genre)) return;
    Genres.add(genre);
    sweep();
  }
  public void addAvgRating(String rating) {
    int intRating = Integer.parseInt(rating);
    if (Ratings.contains(intRating)) return;
    Ratings.add(intRating);
    sweep();
  }
      
  private void cleanup() {
    Iterator<String> itFilms = inList.iterator();
    int index = 0;
    MovieInterface current;
    boolean genreMatch = false;
    Iterator<String> itgenre;
    while (itFilms.hasNext()) {
      current = bigTable.get(itFilms.next());
      itgenre = current.getGenres().iterator();
      while (itFilms.hasNext()) if (Genres.contains(itFilms.next())) genreMatch=true;
      if (genreMatch & Ratings.contains((Integer)current.getAvgVote().intValue())) {
        genreMatch=false;
      }
      else {
        inList.remove(index);
        size--;
      }
      index++;
    }
  }
  public void removeGenre(String genre) {
    if (Genres.contains(genre)) {
      Genres.remove(genre);
      cleanup();
    }
  }
  public void removeAvgRating(String rating) {
    Integer Intrating = Integer.parseInt(rating);
    if (Ratings.contains(Intrating)) {
      Ratings.remove(Intrating);
      cleanup();
    }
  }
  public List<String> getGenres() {
    return Genres;
  }
  public List<String> getAvgRatings() {
    List<String> Final = new ArrayList<String>(Genres.size());
    Iterator<String> it = Genres.iterator();
    while (it.hasNext()) {
      Final.add(it.next());
    }
    return Final;
  }
  public int getNumberOfMovies() {
    return size;
    
  }
  public List<MovieInterface> getThreeMovies(int startingIndex) {
    ///System.out.println(keys.toString());
    List<MovieInterface> workingM = new ArrayList<MovieInterface>();
    Iterator<String> keyIt = keys.iterator();
    while (keyIt.hasNext()){
      workingM.add(bigTable.get(keyIt.next()));
      
    }
    workingM.sort(null);
    Collections.reverse(workingM);
    ArrayList<MovieInterface> finalM = new ArrayList<MovieInterface>(3);
    for (int i=startingIndex; i<startingIndex+3 | i<workingM.size(); i++) {
      finalM.add(workingM.get(i));
    }
    Collections.reverse(finalM);
    ///finalM.forEach(member -> System.out.println(member.getAvgVote()));
    return finalM;
  }
  public List<String> getAllGenres() {
    return allGenres;
  }
  
  
}
