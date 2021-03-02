// --== CS400 File Header Information ==--
// Name: Carter Lindstrom
// Email: cjlindstrom@wisc.edu
// Team: BF Red Team
// TA: Brianna Cochran
// Lecturer: Gary Dahl
// Notes to Grader: N/A

//imports for the HashTableMap class
import java.util.NoSuchElementException;
import java.util.LinkedList;

// HashTableMap class for my hash table implementation
// implements MapADT and of type <KeyType, ValueType>

public class HashTableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType>{
  
  //instance fields for HashTableMap
  
  private LinkedList<KVPair<KeyType, ValueType>>[] pairArray; //instance array of LinkedLists of type KVPair
  private int size; //int for the size of pairArray
  private int hashKey; //int for hashCode values
  
  /*
   * Class to represent a Key and Value pairing
   */
  @SuppressWarnings("hiding")
  class KVPair<KeyType, ValueType>{
    private KeyType key;
    private ValueType value;
    
    public KVPair(KeyType key, ValueType value) {
      this.key = key;
      this.value = value;
    }
  }
  
  
  /*
   * Constructor with capacity as parameter
   * Initializes an empty hash table.
   */
  @SuppressWarnings("unchecked")
  public HashTableMap(int capacity) {
    this.size = 0;
    this.pairArray = new LinkedList[capacity];
  }
  
  /*
   * Constructor with no parameters
   * Initializes a hash table with a capacity of 10
   */
  @SuppressWarnings("unchecked")
  public HashTableMap() {
    this.pairArray = new LinkedList[10];
    this.size = 0;
  }
  
  /*
   * put method puts a key and value into the hash table
   * returns boolean false if the key is already in the table or
   *          if key is null
   * @param key KeyType key value
   * @param value ValueType value
   * @return boolean true if successfully added
   */
  @Override
  public boolean put(KeyType key, ValueType value) {
    //check if key is null and return false
    if (key == null) {
      return false;
    }
    
    //get hashCode value for this key
    hashKey = Math.abs(key.hashCode()) % pairArray.length;
    KVPair<KeyType, ValueType> newPair = new KVPair<KeyType, ValueType>(key, value);
    
    //check to see if there is already a LinkedList as this hashKey value
    if (pairArray[hashKey] == null) {
      //create new LinkedList
      pairArray[hashKey] = new LinkedList<KVPair<KeyType, ValueType>>();
    }
    else{
      //If a linkedList already exists at this hashCode, check
      // for duplicate keys in the LinkedList. If there is a duplicate
      // key, return false and do not add key
      for (int i = 0; i < pairArray[hashKey].size(); i++) {
        if (pairArray[hashKey].get(i).key.equals(key)) {
          return false;
        }
      }
    }
    
    // Add this KVPair to the hash table and increment size
    pairArray[hashKey].add(newPair);
    size++;
    
    // checks to see if pairArray needs resizing by being at 
    // or above 85% full. No changes are made to the hash table 
    // if no resizing is needed
    grow();
    
    // return true after adding the KVPair
    return true;
  }
  
  /*
   * private helper method to account for resizing
   * 
   * No return type or parameters
   * This method checks the capacity of pairArray by
   * dividing size by the capacity of the array
   * If the array is 85% full or more, this method 
   * sets pairArray to a new array of double capacity and
   *  rehashes each key in the new pairArray.
   */
  @SuppressWarnings("unchecked")
  private void grow() {
    
    //check if array is at 85% capacity or more
    if((double)size / (double)pairArray.length >= 0.85) {
      // set pairArray to a new array of double capacity
      LinkedList<KVPair<KeyType, ValueType>>[] oldArray = pairArray;
      int newCapacity = oldArray.length * 2;
      pairArray = new LinkedList[newCapacity];
      
      //Rehash each element into pairArray 
      for(int i = 0; i < size; i++) {
        if(oldArray[i] != null) {
          for(int j = 0; j < oldArray[i].size(); j++) {
            hashKey = Math.abs(oldArray[i].get(j).key.hashCode()) % pairArray.length;
            if(pairArray[hashKey] == null) {
              pairArray[hashKey] = new LinkedList<KVPair<KeyType, ValueType>>();
              pairArray[hashKey].add(oldArray[i].get(j));
            }
            else {
              pairArray[hashKey].add(oldArray[i].get(j));
            }
          }
        }
      }
    }
  }
  
  /*
   * get method returns the ValueType of the value
   * paired with key parameter
   * 
   * @param KeyType key 
   * @return ValueType matched with key parameter
   */
  @Override
  public ValueType get(KeyType key) throws NoSuchElementException {
    
    //Throw NoSuchElementException if key is null
    if (key == null) {
      throw new NoSuchElementException("No key Exists");
    }
    
    //iterate through hash table and return the value at
    // key
    for (int i = 0; i < pairArray.length; i++) {
      if(pairArray[i] != null) {
        for(int j = 0; j < pairArray[i].size(); j++) {
          if(pairArray[i].get(j).key.equals(key)){
            return pairArray[i].get(j).value;
          }
        }
      }
    }
    // If key was not found in the hash table, throw a 
    // NoSuchElementException 
    throw new NoSuchElementException("No key Exists");

}

  /*
   * size method returns the current size of the hash table
   * 
   * @return int size of pairArray
   */
  @Override
  public int size() {
    return size;
  }

  /*
   * containsKey method returns a boolean true if the hash
   * table contains the key parameter, false otherwise
   * 
   * @param KeyType key - key to be looked for
   * @return boolean true if key is in hash table
   */
  @Override
  public boolean containsKey(KeyType key) {
    
    //return false if key is null
    if (key == null) {
      return false;
    }
  
    //iterate through pairArray and check for key at each
    //element in the array
    for (int i = 0; i < pairArray.length; i++) {
      if(pairArray[i] != null) {
        for(int j = 0; j < pairArray[i].size(); j++) {
          if(pairArray[i].get(j).key.equals(key)){
            return true;
          }
        }
      }
    }
    //return false if key is not found
    return false;
  }

  /*
   * remove method removes an element from the hash table at 
   * the parameter key and returns value of that KVPair or null
   * if the KVPair is not in the hash table
   * 
   * @param KeyType key - key of KVPair to be removed
   * @return ValueType of KVPair that is removed
   */
  @Override
  public ValueType remove(KeyType key) {
    //declare and initialize ValueType object to be returned
    ValueType removed = null;
    
    //return null if key is null
    if (key == null) {
      return null;
    }
    
    //iterate through the hash table and find the KVPair to be removed
    for (int i = 0; i < pairArray.length; i++) {
      if(pairArray[i] != null) {
        for(int j = 0; j < pairArray[i].size(); j++) {
          if(pairArray[i].get(j).key.equals(key)){
            //set ValueType removed to the ValueType of the KVPair to be removed
            removed = pairArray[i].get(j).value;
            //remove the KVPair from the hash table
            pairArray[i].remove(pairArray[i].get(j));
            //decrement the size of the hash table
            size--;
            return removed;
          }
        }
      }
    }
    //return null if the KVPair was not found
    return null;
  }

  /*
   * clear method to empty the hash table
   */
  @SuppressWarnings("unchecked")
  @Override
  public void clear() {
    //set pairArray to a new empty array of LinkedLists
    pairArray = new LinkedList[pairArray.length];
    size = 0;
  }}
