/**
* Filename:   BPTree.java
* Project:    Food Query
* Authors:  Pooja Sivakumar, Anna Kim, Stella Kim, Weston Nelson
*  
* Semester:   Fall 2018
* Course:     CS400
* 
*/

package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Implementation of a B+ tree to allow efficient access to many different
 * indexes of a large data set. BPTree objects are created for each type of
 * index needed by the program. BPTrees provide an efficient range search as
 * compared to other types of data structures due to the ability to perform
 * log_m N lookups and linear in-order traversals of the data items.
 * 
 * @author sapan (sapan@cs.wisc.edu)
 *
 * @param <K>
 *            key - expect a string that is the type of id for each item
 * @param <V>
 *            value - expect a user-defined type that stores all data for a food
 *            item
 */
public class BPTree<K extends Comparable<K>, V> implements BPTreeADT<K, V> {

	// Root of the tree
	private Node root;

	// Branching factor is the number of children nodes
	// for internal nodes of the tree
	private int branchingFactor;

	/**
	 * Public constructor of BPTree class. It sets up the branching factor
	 * which is specified by user.
	 * 
	 * @param branchingFactor
	 */
	public BPTree(int branchingFactor) {
		if (branchingFactor <= 2) {
			throw new IllegalArgumentException("Illegal branching factor: " + branchingFactor);
		}
		// assigns root as leaf node
		this.root = new LeafNode();
		// assigns branching factor as branching factor given as a parameter
		this.branchingFactor = branchingFactor;

	}

	/*
     * this method inserts the key and value in the appropriate nodes in the tree.
     * key-value pairs with duplicate keys can be inserted into the tree.
     *  
	 * (non-Javadoc)
	 * 
     * @param key to be inserted
     * @param value to be inserted 
     * 
	 * @see BPTreeADT#insert(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void insert(K key, V value) {
		root.insert(key, value);
	}

	/*
	 * 
     * this method gets the values that satisfy the given range 
     * search arguments.
     * The options for value of comparator are "<=", "==", ">=".
     * 
     * Example:
     *     If given key = 2.5 and comparator = ">=":
     *         return all the values with the corresponding 
     *      keys >= 2.5
     *      
     * If key is null or not found, return empty list.
     * If comparator is null, empty, or not according
     * to required form, return empty list.
     * 
	 * (non-Javadoc)
	 * 
	 * 
     * @param key to be searched
     * @param comparator is a string that tells value of comparator
     * @return list of values that are the result of the 
     * 		range search; if nothing found, return empty list
	 * 
	 * 
	 * @see BPTreeADT#rangeSearch(java.lang.Object, java.lang.String)
	 * 
	 */
	@Override
	public List<V> rangeSearch(K key, String comparator) {
		
		// return empty arraylist as a result if comparator is invalid
		if (!comparator.contentEquals(">=") && 
				!comparator.contentEquals("==") && 
				!comparator.contentEquals("<="))
			return new ArrayList<V>();
		
		// return a list of values that are the result of the range search
		return root.rangeSearch(key, comparator);
	}



	/*
	 * 
     * This method returns a string representation for the tree.
     * 
	 * (non-Javadoc)
	 * 
     * @return a string representation of the tree.
     * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		Queue<List<Node>> queue = new LinkedList<List<Node>>();
		queue.add(Arrays.asList(root));
		StringBuilder sb = new StringBuilder();
		while (!queue.isEmpty()) {
			Queue<List<Node>> nextQueue = new LinkedList<List<Node>>();
			while (!queue.isEmpty()) {
				List<Node> nodes = queue.remove();
				sb.append('{');
				Iterator<Node> it = nodes.iterator();
				while (it.hasNext()) {
					Node node = it.next();
					sb.append(node.toString());
					if (it.hasNext())
						sb.append(", ");
					if (node instanceof BPTree.InternalNode)
						nextQueue.add(((InternalNode) node).children);
				}
				sb.append('}');
				if (!queue.isEmpty())
					sb.append(", ");
				else {
					sb.append('\n');
				}
			}
			queue = nextQueue;
		}
		return sb.toString();
	}

	/**
	 * This abstract class represents any type of node in the tree This class is a
	 * super class of the LeafNode and InternalNode types.
	 * 
	 * @author sapan
	 */
	private abstract class Node {

		// List of keys
		List<K> keys;

		/**
		 * Package constructor of Node class.
		 */
		Node() {
			// creates arraylist that can contain list of keys in a node
			this.keys = new ArrayList<K>();
		}

		/**
		 * Inserts key and value in the appropriate leaf node and balances the tree if
		 * required by splitting
		 * 
		 * @param key
		 * @param value
		 */
		abstract void insert(K key, V value);

		/**
		 * Gets the first leaf key of the tree
		 * 
		 * @return key
		 */
		abstract K getFirstLeafKey();

		/**
		 * Gets the new sibling created after splitting the node
		 * 
		 * @return Node
		 */
		abstract Node split();

		/*
		 * 
		 * this method gets the values that satisfy the given range 
		 * search arguments.
		 * The options for value of comparator are "<=", "==", ">=".
		 * 
		 * (non-Javadoc)
		 * 
		 * @param key to be searched
		 * @param comparator is a string that tells value of comparator
		 * @return list of values that are the result of the 
		 * 		range search; if nothing found, return empty list
		 * 
		 * @see BPTree#rangeSearch(java.lang.Object, java.lang.String)
		 */
		abstract List<V> rangeSearch(K key, String comparator);

		/**
		 * 
		 * This method checks if a node or a list (of keys or values) contains
		 * more than (branching factor - 1) elements in it. If so, the method
		 * returns true which indicates overflow. If there is (branching factor - 1)
		 * or less elements in the list, the method returns false to indicate
		 * no overflow.
		 * 
		 * @return boolean value that returns true if overflow is caught 
		 * 			or false if overflow is not caught
		 */
		abstract boolean isOverflow();

		/**
		 * 
		 * This method returns a string representation for the tree.
		 * 
		 * @return string representation of the tree
		 */
		public String toString() {
			
			// return a list of keys
			return keys.toString();
		}

	} // End of abstract class Node

	/**
	 * This class represents an internal node of the tree. This class is a concrete
	 * sub class of the abstract Node class and provides implementation of the
	 * operations required for internal (non-leaf) nodes.
	 * 
	 * @author sapan
	 */
	private class InternalNode extends Node {

		// List of children nodes
		List<Node> children;

		/**
		 * Package constructor of InternalNode class
		 */
		InternalNode() {
			// call constructor of Node 
			super();
			
			// creates array list of children for the node 
			this.children = new ArrayList<Node>();
		}

		/**
		 * 
		 * This method gets the first leaf key of the tree
		 * 
		 * (non-Javadoc)
		 * 
		 * @return key located at the first leaf 
		 * 
		 * @see BPTree.Node#getFirstLeafKey()
		 */
		K getFirstLeafKey() {
			
			// return key at index of 0
			return children.get(0).getFirstLeafKey();
		}

		/**
		 * 
		 * This method checks if number of children of a node is greater than
		 * number of branching factor. If so, the method returns true 
		 * which indicates overflow. If number of children of a node is equal
		 * or less than branching factor, the method returns false to indicate
		 * that overflow doesn't occur.
		 * 
		 * (non-Javadoc)
		 * 
		 * @return boolean value that returns true if overflow is caught 
		 * 			or false if overflow is not caught
		 * 
		 * @see BPTree.Node#isOverflow()
		 */
		boolean isOverflow() {
			
			// check the size of a children list
			// return true if int value of size is larger than branching factor.
			return children.size() > branchingFactor;
		}

		/**
		 * 
		 * this method inserts the key and value in the appropriate nodes in the tree.
		 * key-value pairs with duplicate keys can be inserted into the tree.
		 * 
		 * After inserting the key and value in the tree, check for overflow within the tree.
		 * 
		 * (non-Javadoc)
		 * 
		 * @param key to be inserted
		 * @param value to be inserted 
		 * 
		 * @see BPTree.Node#insert(java.lang.Comparable, java.lang.Object)
		 */
		void insert(K key, V value) {
			
			// get child node using given key
			Node child = getChild(key);
			
			// insert key and value into child's keys list and values list
			child.insert(key, value);
			
			// check for overflow within child
			if (child.isOverflow()) {
				
				// if there is an overflow
				// rearrange the tree structure by splitting the node
				Node sibling = child.split();
				
				// promote the first key and add as child
				insertChild(sibling.getFirstLeafKey(), sibling);
			}
			
			// check for overflow for root node
			if (root.isOverflow()) {
				
				// if there is an overflow
				// rearrange the tree structure by splitting the node
				Node sibling = split();
				
				InternalNode newRoot = new InternalNode();
				// promote the first key
				newRoot.keys.add(sibling.getFirstLeafKey());
				// add children for new root node
				newRoot.children.add(this);
				newRoot.children.add(sibling);
				// assign new root 
				root = newRoot;
			}
		}

		/**
		 * Helper method to find where to insert a new node with given key and value
		 * pair.
		 * 
		 * @param key to find
		 * @return Node
		 */
		Node getChild(K key) {
			
			// find the place for given key within keys array list
			int place = Collections.binarySearch(keys, key);
			
			// int variable for actual index for child
			int index;
			
			// if key already exist in the keys list
			if (place >= 0) {
				// add by one to found place
				index = place + 1;
			}
			else {
				
				// convert the place to actual index by 
				// multiplying it by (-1) and subtracting by 1
				index = -place - 1;
			}
			
			// return child node
			return children.get(index);
		}

		/**
		 * Helper method for insert when the internal node needs to split.
		 * 
		 * @param key to find
		 * @param child
		 *            a Node
		 */
		void insertChild(K key, Node child) {
			
			// find the place for parameter key
			int place = Collections.binarySearch(keys, key);
			
			// int variable for actual index
			int index;
			

			// if key already exists in keys list
			if (place >= 0) {
			
				// add by 1 to found place
				index = place + 1;
			}
			else {
				// multiplying (-1) to insertion point and subtracting by 1
				// it will convert found place to actual index of the list
				index = -place - 1;
			}

			// add key to correct index of the keys list
			keys.add(index, key);
			// add child node to children list
			children.add(index + 1, child);
		}

		/**
		 * 
		 * This method gets the new sibling created after splitting the node.
		 * 
		 * (non-Javadoc)
		 * 
		 * @return Node of sibling
		 * 
		 * @see BPTree.Node#split()
		 */
		Node split() {

			// finding mid index of keys list and assigning it as start index
			int fromIndex = (keys.size()+1) / 2;
			// last index of keys list is end index
			int toIndex = keys.size();
			
			// new internal node for sibling
			InternalNode sibling = new InternalNode();
			// add keys which are from last half of keys list into sibling's keys list
			sibling.keys.addAll(keys.subList(fromIndex, toIndex));
			// add children to sibling's children list
			sibling.children.addAll(children.subList(fromIndex, toIndex + 1));

			// remove keys that have inserted to sibling's keys list
			keys.subList(fromIndex - 1, toIndex).clear();
			// remove children that have inserted to sibling's children list
			children.subList(fromIndex, toIndex + 1).clear();

			
			// return newly created sibling node
			return sibling;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * this method gets the values that satisfy the given range 
		 * search arguments.
		 * The options for value of comparator are "<=", "==", ">=".
		 * 
		 * @param key to be searched
		 * @param comparator is a string that tells value of comparator
		 * @return list of values that are the result of the 
		 * 		range search; if nothing found, return empty list
		 * 
		 * @see BPTree.Node#rangeSearch(java.lang.Comparable, java.lang.String)
		 */
		List<V> rangeSearch(K key, String comparator) {
			
			// search through children and return list of values as a result
			return getChild(key).rangeSearch(key, comparator);
		}

	} // End of class InternalNode

	/**
	 * This class represents a leaf node of the tree. This class is a concrete sub
	 * class of the abstract Node class and provides implementation of the
	 * operations that required for leaf nodes.
	 * 
	 * @author sapan
	 */
	private class LeafNode extends Node {

		// List of values
		List<V> values;

		// Reference to the next leaf node
		LeafNode next;

		// Reference to the previous leaf node
		LeafNode previous;

		/**
		 * Package constructor of LeafNode class
		 */
		LeafNode() {
			// calls constructor of Node class
			super();
			// creates new array list to store values in the node
			values = new ArrayList<V>();
		}

		/**
		 * 
		 * This method gets the first leaf key of the tree
		 * 
		 * (non-Javadoc)
		 * 
		 * @return key at index 0 in keys list
		 * @see BPTree.Node#getFirstLeafKey()
		 */
		K getFirstLeafKey() {
			
			// return key located at the first index of keys list of the node
			return keys.get(0);
		}

		/**
		 * 
		 * 
		 * This method checks if a node or a list (of keys or values) contains
		 * more than (branching factor - 1) elements in it. If so, the method
		 * returns true which indicates overflow. If there is (branching factor - 1)
		 * or less elements in the list, the method returns false to indicate
		 * no overflow.
		 * 
		 * (non-Javadoc)
		 * 
		 * @return boolean value that returns true if overflow is caught 
		 * 			or false if overflow is not caught
		 *
		 * @see BPTree.Node#isOverflow()
		 */
		boolean isOverflow() {
			
			// check the size of a list for values 
			// (since values list and keys list have same size, 
			// it does not matter which list is used to check the size)
			// return true if int value of size is larger than branching factor - 1.
			return values.size() > branchingFactor - 1;
		}

		/**
		 * 
		 * this method inserts the key and value in the appropriate node in the tree.
		 * key-value pairs with duplicate keys can be inserted into the tree.
		 * 
		 * After inserting the key and value in the tree, check for overflow within the tree.
		 * 
		 * (non-Javadoc)
		 * 
		 * @see BPTree.Node#insert(Comparable, Object)
		 */
		void insert(K key, V value) {
			
			// The id of a food item is unique, while the name can be a duplicate of some
			// other food's name
			
			// find place for the given key to be inserted in keys list
			int place = Collections.binarySearch(keys, key);
			
			// actual index
			int index;
			
			// if key already exists in the keys list
			if (place >= 0) {
				// convert the place to actual index
				index = place +1;
			}
			// if key does not exist in the keys list
			else {
				// convert the place to actual index by
				// multiplying place by (-1) and subtracting by 1
				index = -place - 1;
			}

			
			// add key into the correct index of list
			keys.add(index, key);
			
			// add value into the correct index of list
			values.add(index, value);

			// check for overflow within the tree
			if (root.isOverflow()) {
				
				// store last half of the node
				Node sibling = split();
				// node for new root
				InternalNode newRoot = new InternalNode();
				// get key for new root
				newRoot.keys.add(sibling.getFirstLeafKey());
				// add children to new root
				newRoot.children.add(this);
				newRoot.children.add(sibling);
			
				// assign new root
				root = newRoot;
			}
		}

		/**
		 * 
		 * This method gets the new sibling created after splitting the node.
		 * (non-Javadoc)
		 * 
		 * @return Node of sibling
		 * @see BPTree.Node#split()
		 */
		Node split() {
			
			// node for new sibling
			LeafNode sibling = new LeafNode();
			
			// find mid index of keys list for start index
			int fromIndex = (keys.size() + 1) / 2;
			// last index of list is end index
			int toIndex = keys.size();
			
			// add keys and values in the last half of lists 
			sibling.keys.addAll(keys.subList(fromIndex, toIndex));
			sibling.values.addAll(values.subList(fromIndex, toIndex));

			// remove keys and values added to sibling
			keys.subList(fromIndex, toIndex).clear();
			values.subList(fromIndex, toIndex).clear();
			
			// if there is node after the current node
			if (this.next != null) {
				// make next node of current node to point sibling node
				this.next.previous = sibling;
			}
			
			// make sibling node to point at next node of current node
			sibling.next = next;
			// make current node to point at sibling as next node
			this.next = sibling;
			// make sibling node to point at current node as previous node
			sibling.previous = this;
			// return newly created sibling node
			return sibling;
		}

		/**
		 * (non-Javadoc)
		 * This method gets the values that satisfy the given range 
		 * search arguments.
		 * The options for value of comparator are "<=", "==", ">=".
		 * 
		 * @param key to be searched
		 * @param comparator is a string that tells value of comparator
		 * @return list of values that are the result of the 
		 * 		range search; if nothing found, return empty list
		 * 
		 * @see BPTree.Node#rangeSearch(Comparable, String)
		 */
		List<V> rangeSearch(K key, String comparator) {
			// list for storing values within the range
			List<V> result = new ArrayList<V>();

			// if parameter key is null
			if (key == null) {
				// return empty list
				return result;
			}
			
			// greater or equal to
			if (comparator.equals(">=")) {
				LeafNode curr = this;
				LeafNode prevNode = curr.previous;
				LeafNode nextNode = curr;
				//if the node doesn't have a previous, break out of the loop
				while (prevNode != null) {
					for (int i = prevNode.keys.size() - 1; i >= 0; i--) {
						 //if the previous node's key list contains the passed argument, 
						 //then we can keep searching for more
						if (prevNode.keys.get(i).compareTo(key) >= 0) {
							//add that key's value to the filtered list
							result.add(0, prevNode.values.get(i));
						} else {
							//there are no more keys that match
							//the passed argument in the previous nodes. 
							//We can now check the next nodes.
							break;
						}
					}
					//we can check the next previous node
					prevNode = prevNode.previous;
				}
				// add all values on the right side since they are greater than given key
				while (nextNode != null) {
					for (int i = 0; i < nextNode.keys.size(); i++) {
						if (nextNode.keys.get(i).compareTo(key) == 0) {
							
							result.add(0, nextNode.values.get(i));
						}
						// if it is greater add on the right side to keep ascending order
						if (nextNode.keys.get(i).compareTo(key) > 0) {
							result.add(result.size(), nextNode.values.get(i));
						}
					}
					//check the next nodes 
					nextNode = nextNode.next;
				}
			} else if (comparator.equals("==")) {
				LeafNode node = this;
				LeafNode nextNode = node.next;
				while (nextNode != null) {
					for (int i = 0; i < nextNode.values.size(); i++) {
						if (nextNode.keys.get(i).compareTo(key) == 0) {
							result.add(0, nextNode.values.get(i));
						} else {
							break;
						}
					}
					nextNode = nextNode.next;
				}
				while (node != null) {
					for (int i = node.values.size()-1; i >=0 ; i--) {
						if (node.keys.get(i).compareTo(key) == 0) {
							result.add(0, node.values.get(i));
						} else {
							break;
						}
					}
					node = node.previous;
				}
			}	

			else if (comparator.equals("<=")) {
				LeafNode node = this;
				LeafNode prevNode = node.previous;

				//break from the loop if the node has no previous nodes
				while (prevNode != null) {
					// add values to result list
					result.addAll(prevNode.values);
					prevNode = prevNode.previous;
				}

				while (node != null) {
					// check values of that node from the right-most value
					// as the values are sorted in order, if we check from the
					// left-most child, it would break the loop although there
					// is a greater value at the right-most
					for (int i = 0; i < node.keys.size(); i++) {
						if (node.keys.get(i).compareTo(key) <= 0) {
							result.add(0, node.values.get(i));
						} else {
							//once we encounter a key > than the passed argument, break out of the loop
							break;
						}
					}
					node = node.next;
				}

				// list for storing reversed order of result
				List<V> tempList = new ArrayList<V>();
				// index of temp list
				for (int i = result.size() - 1; i >= 0; i--) {
					// add element from the element at last index to the index of 0
					tempList.add(tempList.size(), result.get(i));
				}
				
				// assign reversed order as a result
				result = tempList;

			}
			
			// return result list
			return result;
		}

	} // End of class LeafNode
	


	/**
	 * Contains a basic test scenario for a BPTree instance. It shows a simple
	 * example of the use of this class and its related types.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// create empty BPTree with branching factor of 3
		BPTree<Double, Double> bpTree = new BPTree<>(3);

		// create a pseudo random number generator
		Random rnd1 = new Random();

		// some value to add to the BPTree
		Double[] dd = { 0.0d, 0.5d, 0.2d, 0.8d, 0.1d};

		// build an ArrayList of those value and add to BPTree also
		// allows for comparing the contents of the ArrayList
		// against the contents and functionality of the BPTree
		// does not ensure BPTree is implemented correctly
		// just that it functions as a data structure with
		// insert, rangeSearch, and toString() working.
		List<Double> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Double j = dd[rnd1.nextInt(4)];
			list.add(j);
			bpTree.insert(j, j);
			System.out.println(j);
			System.out.println("\n\nTree structure:\n" + bpTree.toString());
		}
		List<Double> filteredValues = bpTree.rangeSearch(0.5d, ">=");
		System.out.println("Filtered values: " + filteredValues.toString());
		
	}

} // End of class BPTree