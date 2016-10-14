// An implementation of an ordered structure, using lists
// (c) 1998, 2001 duane a. bailey

package ppciarravano.algoexplorer.test;

import java.util.Iterator;
import java.util.Comparator;

/**
 * A class that implements a collection of values that are kept in order.
 * Base values must be comparable.  Unlike Lists there is no notion of head
 * or tail.
 * <P>
 * Example Usage:
 * <P>
 * To determine the effect of the original Starwars&trade; movie on the careers
 * of its stars, we could place ComparableAssociations between each star's
 * name and the number of movies they have been in since Starwars&trade;
 * into an ordered vector and print our the results, as follows:
 * <pre>
 * public static void main(String[] argv){
 *      //instantiate an ordered vector
 *      OrderedList<ComparableAssociation<Integer,String>> v = new {@link #OrderedList<ComparableAssociation<Integer,String>>()};
 *      
 *      //add the cast members of the original star wars along with
 *      //the number of films in which the have subsequently appeared
 *      v.{@link #add(Object) add(new ComparableAssociation<Integer,String>(new Integer(12),"Sir Alec Guiness"))};
 *      v.{@link #add(Object) add(new ComparableAssociation<Integer,String>(new Integer(24),"Carrie Fisher"))};
 *      v.{@link #add(Object) add(new ComparableAssociation<Integer,String>(new Integer(28),"Harrison Ford"))}; 
 *      v.{@link #add(Object) add(new ComparableAssociation<Integer,String>(new Integer(28),"Mark Hamill"))};
 *
 *      //print out the results
 *      for(Iterator<ComparableAssociation<Integer,String>> i = v.{@link #iterator()}; i.hasNext();){
 *          ComparableAssociation<Integer,String> actor = i.next();
 *          System.out.println(actor.getValue() + " has been in " + 
 *                             actor.getKey() + " movies since Star Wars"); 
 *      }
 *   }
 * </pre>
 * @see structure.Vector
 *
 * @version $Id: OrderedList.java 3 2013-06-18 23:13:50Z ppciarravano@gmail.com $
 * @author, 2001 duane a. bailey
 */
public class OrderedList<E extends Comparable<E>>
    //extends AbstractStructure<E> implements OrderedStructure<E>
{
    /**
     * Pointer to the smallest element, maintained as a singly linked list
     */
    protected Node<E> data; // smallest value
    /**
     * Number of elements in list
     */
    protected int count;        // number of values in list
    /**
     * The ordereding used to arange the values
     */
    protected Comparator<? super E> ordering;   // the comparison function

    /**
     * Construct an empty ordered list
     *
     * @post constructs an empty ordered list
     */
    public OrderedList()
    {
        this(new NaturalComparator<E>());
    }

    /**
     * Construct an empty ordered list with alternative ordering
     *
     * @param ordering the Comparator to be used in comparison
     * @post constructs an empty ordered list ordered by ordering
     */
    public OrderedList(Comparator<? super E> ordering)
    {
        this.ordering = ordering;
        clear();
    }

    /**
     * Remove all the elements from the ordered list
     *
     * @post the ordered list is empty
     */
    public void clear()
    {
        data = null;
        count = 0;
    }
    
    /**
     * Add a value to the ordered list, keeping values in order
     *
     * @pre value is non-null
     * @post value is added to the list, leaving it in order
     * 
     * @param value The value to be added to the list
     */
    public void add(E value)
    {
        Node<E> previous = null; // element to adjust
        Node<E> finger = data;   // target element
        // search for the correct location
        while ((finger != null) &&
               ordering.compare(finger.value(),value) < 0)
        {
            previous = finger;
            finger = finger.next();
        }
        // spot is found, insert
        if (previous == null) // check for insert at top
        {
            data = new Node<E>(value,data);
        } else {
        	Node<E> newNode = new Node<E>(value,previous.next());
            previous.setNext(newNode);
        }
        count++;
    }

    /**
     * Determine if the ordered list contains a value
     *
     * @pre value is a non-null comparable object
     * @post returns true iff contains value
     * 
     * @param value The value sought in the list
     * @return The actual value found, or null, if not
     */
    public boolean contains(E value)
    {
        Node<E> finger = data; // target
        // search down list until we fall off or find bigger value
        while ((finger != null) &&
               ordering.compare(finger.value(),value) < 0)
        {
            finger = finger.next();
        }
        return finger != null && value.equals(finger.value());
    }

    /**
     * Remove a value from the ordered list.  At most one value
     * is removed.
     *
     * @pre value is non-null
     * @post an instance of value is removed, if in list
     * 
     * @param value The value to be removed
     * @return The actual value removed from the list
     */
    public E remove(E value)
    {
        Node<E> previous = null; // element to adjust
        Node<E> finger = data;   // target element
        // search for value or fall off list
        while ((finger != null) &&
               ordering.compare(finger.value(),value) < 0)
        {
            previous = finger;
            finger = finger.next();
        }
        // did we find it?
        if ((finger != null) && value.equals(finger.value())) {
            // yes, remove it
            if (previous == null)  // at top? 
            {
                data = finger.next();
            } else {
                previous.setNext(finger.next());
            }
            count--;
            // return value
            return finger.value();
        }
        // return nonvalue
        return null;
    }

    /**
     * Determine the number of elements in the list
     *
     * @pre returns the number of elements in the ordered list
     * 
     * @return The number of elements in the list
     */
    public int size()
    {
        return count;
    }

    /**
     * Determine if the list is empty
     *
     * @post returns true iff the size is non-zero
     * 
     * @return True if the ordered list is empty
     */
    public boolean isEmpty()
    {
        return size() == 0;
    }
    /**
     * Construct an iterator for traversing elements of ordered list
     * in ascending order
     *
     * @post returns an iterator over ordered list
     * 
     * @return An iterator traversing elements in ascending order
     */
//	public Iterator<E> iterator()
//	{
//		return new SinglyLinkedListIterator<E>(data);
//	}

    /**
     * Generate string representation of the ordered list
     *
     * @post returns string representation of list
     * 
     * @return String representing ordered list
     */
//    public String toString()
//    {
//        StringBuffer s = new StringBuffer();
//        s.append("<OrderedList:");
//        Iterator si = iterator();
//        while (si.hasNext())
//        {
//            s.append(" "+si.next());
//        }
//        s.append(">");
//        return s.toString();
//    }
    
    public static void main(String[] args)
	{
    	OrderedList orderedList = new OrderedList<Integer>();
    	
    	int numNodes = InputUtil.getIntegerFromImput("Num. Nodes", 0, 100);
	
		for (int i = 0; i < numNodes; i++)
		{
			int value = InputUtil.getIntegerFromImput("Node Value", 0, 100);
			
			orderedList.add(value);
			System.out.println(value);
		}
	}
    
}

class NaturalComparator<E extends Comparable<E>> implements Comparator<E>
{
	/**
	 * Compare two values, a and b. Simply calls the default compareTo method
	 * for a on b.
	 * 
	 * @param a
	 *            object performing the compare
	 * @param b
	 *            the object being compared
	 * @pre a, b non-null, and b is same type as a
	 * @post returns value <, ==, > 0 if a <, ==, > b
	 * @return value <, ==, > 0 if a <, ==, > b using a.compareTo
	 */
	public int compare(E a, E b)
	{
		return a.compareTo(b);
	}

	/**
	 * Returns true if the other object is a NaturalComparator.
	 * 
	 * @param b
	 *            a possible NaturalComparator
	 * @post returns true if b is a NaturalComparator
	 * @return true if b is a NaturalComparator
	 */
	public boolean equals(Object b)
	{
		return (b != null) && (b instanceof NaturalComparator);
	}
}
