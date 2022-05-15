package trie;

/**
 * Trie is the general representation of the whole data structure. It has one
 * root Node element which can not be deleted.
 */
public class Trie {

  /**
   * The trie always has final root element which never undergoes any changes
   * but adding and removing children.
   */
  private final Node root = new Node();

  /**
   * Constructs an empty Trie.
   */
  public Trie() {
  }

  /**
   * @param key    The key for which to store the points value.
   * @param points The value of points to associate with the given key.
   * @return Returns false in the case that the key already exists as we do not
   * want to overwrite points, otherwise always returns true.
   */
  public boolean add(String key, Integer points) {
    /*
     We check if an entry with the given key already exists and return false
     if it does. Using the points method as a key might exist just as a
     node to point to another node without any value associated, and we
     only disallow keys which have points set to them not keys which
     have the nodes setup already but no value to it.
    */
    boolean alreadyExists = this.points(key) != null;
    if (alreadyExists) {
      return false;
    }

    /*
     At start the lastNode is always the global root from which all nodes
     start, it will store the currently last node which points to the name.
     So for key "abc" lastNode is the root node at the beginning, then the
     "a" node, then "b", then "c". The last node (here "c") will then
     have the points assigned.
    */
    Node lastNode = root;
    for (char c : key.toCharArray()) {
      /*
       If the lastNode has a child with the needed character, we set the
       child as the lastNode and continue.
      */
      Node existingNode = lastNode.getChild(c);
      if (existingNode != null) {
        lastNode = existingNode;
        continue;
      }

      // If no child node with such character exists we create it here.
      lastNode = new Node(c, lastNode);
    }

    // Set the points to the lastNode created/received.
    lastNode.setPoints(points);
    return true;
  }

  /**
   * Removes the key nodes from the trie as long as no other nodes are affected.
   * For a detailed documentation on how the removal works, see the
   * Node#remove() method.
   *
   * @param key The key (name) to remove from the trie.
   * @return Returns false if the key does not exist, otherwise always
   * returns true.
   * @see Node#remove()
   */
  public boolean remove(String key) {
    Node node = root.find(key);
    if (node == null) {
      return false;
    }
    if (node.getPoints() == null) {
      return false;
    }
    node.remove();
    return true;
  }

  /**
   * Changes the points value of a specific key to the given points.
   *
   * @param key    The key for which to change the points for.
   * @param points Zhe new points to assign.
   * @return Returns false if the key does not exist, otherwise always
   * returns true.
   */
  public boolean change(String key, Integer points) {
    Node node = root.find(key);
    if (node == null) {
      return false;
    }

    if (node.getPoints() == null) {
      return false;
    }

    node.setPoints(points);
    return true;
  }

  /**
   * Gets the points associated with a key.
   *
   * @param key The key for which to get the points from.
   * @return Returns the points if there are any assigned, otherwise returns
   * null.
   */
  public Integer points(String key) {
    Node node = root.find(key);
    if (node == null) {
      return null;
    }
    return node.getPoints();
  }

  /**
   * Shows the trie as a string representation where an empty trie is "+".
   *
   * @return The trie in string representation based of the root element.
   * @see Node#toString()
   */
  @Override
  public String toString() {
    return root.toString();
  }
}
