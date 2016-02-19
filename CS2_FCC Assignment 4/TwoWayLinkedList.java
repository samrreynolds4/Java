import java.util.*;

public class TwoWayLinkedList<E> extends java.util.AbstractSequentialList<E>
{
   public class Node<E>
   {
      E element;
      Node<E> next;
      Node<E> previous;
   
      // Sets the element of this Node
      public Node(E e)
      {
         element = e;
      }
   }
   
   private Node<E> head, tail;
   private int size = 0;
   
   // Creates empty list
   public TwoWayLinkedList()
   {
   }
   
   // Creates a list with the given array
   public TwoWayLinkedList(E[] object)
   {
      for (int i = 0; i< object.length - 1; i++)
      {
         if(add(object[i])) size++;
      }
   }
   
   //Returns the head element
   public E getFirst()
   {
      if (size == 0)
         return null;
      return head.element;
   }
   
   //Returns the tail element
   public E getLast()
   {
      if (size == 0)
         return null;
      return tail.element;
   }
   
   //Adds a Node to the front of the list
   public void addFirst(E e)
   {
      Node<E> newNode = new Node<E>(e);
      
      if (tail == null) // If list is empty
         tail = head = newNode;
      else {
             head.previous = newNode;
             newNode.next = head;
             newNode.previous = null;
             head = newNode;
             size++;
           }
   }
   
   // Add a Node to the end of the list
   public void addLast(E e)
   {
      Node<E> newNode = new Node<E>(e);
      
      if (tail == null) // If list is empty
         tail = head = newNode;
      else{
            tail.next = newNode;
            newNode.previous = tail;
            newNode.next = null;
            tail = newNode;
            size++;
         }
   }
   
   //Add a Node at the specified index
   public void add( int index, E e )
   {
      if (index == 0) {addFirst(e);} // add to front if index is 0
      else if (index >= size) { addLast(e); } // if index exceeds the size of list, add to the end of list
      else 
      {
         Node<E> current = head;
         for (int i = 1; i < index; i++)
            current = current.next;
         
         Node<E> temp = current.next;
         current.next = new Node<E>(e);
         (current.next).next = temp;
         (current.next).previous = temp.previous;
         size++;
      }
   }
   
   //Adds a node to the end of a list
   public boolean add( E e )
   {
      if (size == 0) { 
        addFirst(e);
        size++;
        return true;
      }
      else 
      {
         Node<E> newNode = new Node<E>(e);
            
         newNode.previous = tail;
         tail.next = newNode;
         newNode.next = null;
         tail = newNode;
         size++;
         return true;
            
      }
         
   }
   
   // Returns size of list
   @Override
   public int size()
   {
      return size;
   }
   
   //Removes and returns the first element in the list
   public E removeFirst()
   {
      if (size == 0) return null; // If list is empty
      else if (size == 1 ) // If there's only one element in the list
      {
         Node<E> temp = head;
         clear();
         return temp.element;
      }
      else 
      {
         Node<E> temp = head;
         head = temp.next;
         size--;
         return temp.element;
      }
      
   }
   
   //Removes and returns the last element in the list
   public E removeLast()
   {
      if (size == 0) return null; // If list is empty
      if (size == 1) // If list only has 1 element
      {
         Node<E> temp = head;
         clear();
         return temp.element;
      }
      else
      {
         Node<E> temp = tail;
         tail = temp.previous;
         size--;
         return temp.element;
      }
   }
   
   // Removes and returns the element at specefied index
   @Override
   public E remove(int index)
   {
      if (size < 0 || index >= size) return null; // If list is empty or index exceeds size
      else if (index == 0)    return removeFirst(); // If index is 0, return head element
      else if (index == size) return removeLast(); // if index is last element, return tail element
      else 
      {
         Node<E> previous = head;
         
         for (int i = 1; i < index; i++)
            previous = previous.next;
         
         Node<E> current = previous.next;
         previous.next   = current.next;
         previous.previous = current.previous;
         size--;
         return current.element;
      }
   }
   
   // toString method for list
   @Override
   public String toString()
   {
      StringBuilder result = new StringBuilder("[");
      
      Node<E> current = head;
      for (int i = 0; i < size; i++)
      {
         result.append(current.element);
         current = current.next;
         if (current != null)
         {
            result.append(", ");
         } else { 
            result.append("]");
         }
      }
      return result.toString();
   }
   
   // Clears the list
   @Override
   public void clear()
   {
      size = 0;
      head = tail = null;
   }
   
   // returns true or false if the list contains a specified element
   @Override
   public boolean contains(Object e)
   {
      if (head.element == e) return true; // if head.element is e, then return head element
      else if (tail.element == e) return true; // if tail.element is e, then return tail element
      else
      {
         Node<E> previous = head;
         
         for (int i = 0; i < size - 1; i++)
         {
            previous = previous.next;
            
            if (previous.element == e)
               return true;
               
         }
         
         return false;
      }
   }
   
   // Returns the element at the specified index
   public E get(int index)
   {
      if (index < 0 || index > size) return null;  // If index is not a valid number or index exceeds size
      else if (index == size) return getLast(); // if index is last element, return tail element
      else if (index == 0) return getFirst();   // // if index is first element, return head element

      else
      {
         Node<E> previous = head;
         
         for (int i = 1; i < index; i++)
            previous = previous.next;
         
         return (previous.next).element;
      }
   }
   
   // Gets the index of the specified object
   @Override
   public int indexOf(Object e)
   {
      if (head.element == e) return 0; // if object is head element, return 0
      else if (tail.element == e) return size - 1; // if object is tail element, return size
      else
      {
         Node<E> previous = head;
         
         for (int i = 1; i < size - 1; i++)
         {
            if (previous.element == e)
               return i;
               
            previous = previous.next;
            
         }
         
         return -1;
         
      }
   }
   
   // Returns the last index of a specified object
   @Override
   public int lastIndexOf(Object e)
   {
      if( tail.element == e) return size - 1; // if the object is equal to the tail element, return size - 1
      else
      {
         Node<E> previous = tail;
               
         for (int i = size; i > 0; i--)
         {
            if (previous.element == e)
               return i;
            
            previous = previous.previous;
            
         }
         return -1;
      }
   }
   
   // Sets the element of the given index to the specified element
   public E set(int index, E e)
   {
      
      if (index == 0)
      {
         E temp = head.element;
         head.element = e;

         return temp;
      }
      else if (index == size - 1)
      {
         E temp = tail.element;
         tail.element = e;
         return temp;
      }
      else 
      {
         Node<E> previous = head;
         
         for (int i = 0; i < index; i++)
            previous = previous.next;
         
         E temp = previous.element;
         previous.element = e;
         return temp;
         
      }
   }
   
   //List iterator
   @Override
   public java.util.ListIterator<E> listIterator()
   {
      return new LinkedListIterator();
   }
   
   //Creates a new iterator starting at the specified index
   @Override
   public java.util.ListIterator<E> listIterator(int index)
   {
       if (index == 0) return new LinkedListIterator();
       else
       {
          Node<E> current = head;
            
          for (int i = 0; i < index; i++)
             current = current.next;
            
          return new LinkedListIterator(current);
       }
    }
   
   // Linked list Iterator
   private class LinkedListIterator
      implements java.util.ListIterator<E> 
   {
      private Node<E> current = head;
      
      public LinkedListIterator(){} // Default constructor
      
      public LinkedListIterator(Node<E> node) //Setting an index to the iterator
      {
         current = node;
      }
      
      public boolean hasNext() // if the iterator has a next Node
      {
         return (current.next != null);
      }
      
      public boolean hasPrevious() // if the iterator has a previous Node
      {
         return (current.previous != null);
      }
      
      public E next() // returns the next Node
      {
         E e = current.element;
         current = current.next;
         return e;
      }
      
      public E previous() // returns previous Node
      {
         E e = current.element;
         current = current.previous;
         return e;
      }
      
      public void remove() // Removed the current Node from list
      {
         if (current != null)
         {
            (current.previous).next = current.next;
            (current.next).previous = current.previous;
            current = current.next;
         }
      } 
      
      public void add(E e) // Adds an element to the list
      {
         Node<E> newNode = new Node<E>(e);
         
         head.previous = newNode; 
         newNode.next = head;
         newNode.previous = null;
         head = newNode; 
      }
      
      public void set(E e) // Sets the current element to the specified element
      {
         current.element = e;
      }
      
      @Override
      public int previousIndex() // Returns the previous index of the current Node
      {
         if( current.previous == head) return 0;
         
         Node<E> temp = head;
         
         for (int i = 0; i < size - 1; i++)
         {   
            temp = temp.next;
            
            if (temp == current.previous)
               return i;
         }
         
         return -1;
       }
       
      @Override
      public int nextIndex() // Returns the next index of the current Node
      {
         if( current.next == tail) return size - 1;
         Node<E> temp = head;
         
         for (int i = 0; i < size - 1; i++)
         {   
            temp = temp.next;
            
            if (temp == current.next)
               return i;
         }
         
         return -1;
       }
   }
}

