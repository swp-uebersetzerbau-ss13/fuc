package swp_compiler_ss13.fuc.parser.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;


public final class It<T> implements Iterator<T>, Iterable<T> {
   
   private final Iterator<T> iterator; // first option: iterator, used for collections
   private final T[] array; // second option: array
   private int currentIndex = -1;
   
   
   /**
    * Creates a new {@link It} for the given {@link Collection}.
    * If null is given, a valid iterator with no elements is returned.
    */
   public It(Collection<T> collection) {
      if (collection != null)
         this.iterator = collection.iterator();
      else
         this.iterator = new LinkedList<T>().iterator();
      this.array = null;
   }
   
   
   /**
    * Creates a new {@link It} for the given array.
    * If null is given, a valid iterator with no elements is returned.
    */
   public It(T[] array) {
      if (array != null) {
         this.array = array;
         this.iterator = null;
      } else {
         this.array = null;
         this.iterator = new LinkedList<T>().iterator();
      }
   }
   
   
   public boolean hasNext() {
      if (iterator != null)
         return iterator.hasNext();
      else
         return currentIndex + 1 < array.length;
   }
   
   
   public T next() throws NoSuchElementException {
      currentIndex++;
      if (iterator != null)
         return iterator.next();
      else
         return array[currentIndex];
   }
   
   
   public void remove() {
      throw new UnsupportedOperationException();
   }
   
   
   public Iterator<T> iterator() {
      return this;
   }
   
   
   public int getIndex() {
      return currentIndex;
   }
}
