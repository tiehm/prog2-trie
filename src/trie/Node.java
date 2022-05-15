package trie;

/**
 * Node is the representation of a single node with an assigned character
 * which either holds a points value itself or is just the link to another
 * child node or both.
 */
public class Node {

  /*
   The array of all possible children with the letters a-z
   (26 different characters), might be empty.
  */
  private final Node[] children = new Node[26];
  // The character which this node represents (lowercase a-z range).
  private char character;
  /*
   The points value which this node holds (optional as some nodes are
   just connection nodes).
  */
  private Integer points;
  private Node parent;

  /**
   * Naked constructor to initialize an empty node.
   */
  public Node() {
  }

  /**
   * Initializes a new node with the assigned letter and parent given.
   *
   * @param ch     The letter which is assigned to this node, value between
   *               a-z.
   * @param parent The parent node which is assigned to this node.
   */
  public Node(char ch, Node parent) {
    character = ch;
    this.parent = parent;
    // Set this node as the child of the given parent element.
    parent.setChild(ch, this);
  }

  /**
   * Calculates the array index based by the given character, a = 0 ... z = 25.
   *
   * @param ch The character for which to calculate the array index for.
   * @return The array index for this character.
   */
  private static int calculateArrayIndexByChar(char ch) {
    return ch - 'a';
  }

  /**
   * Returns true if the parent element of this node is the global root
   * element.
   *
   * @return Returns true if this node is the global root, otherwise always
   * false.
   */
  private boolean isGlobalRoot() {
    return parent == null;
  }

  /**
   * Sets the given node with the given character as a child element for the
   * given character
   *
   * @param ch    The character which the child element represents.
   * @param child The child node.
   */
  private void setChild(char ch, Node child) {
    // Set the new child element to the index of the character.
    children[calculateArrayIndexByChar(ch)] = child;

    /*
     If child == null we are removing a child from the children of this node.
     In the case of that this node is just a link to the child which is getting
     deleted, we need to check if we could also delete this node as it might not
     be used anymore. A node is not useful anymore if it has not points
     assigned (points == null), that is has no children to which it links to
     (!hasChildren()) and that the node is not the global root as that can
     not be removed. If all conditions match, this node is deleted by
     calling its parent to delete this node from its children.
    */
    if (child == null && points == null && !hasChildren() && !isGlobalRoot()) {
      parent.setChild(character, null);
    }
  }

  /**
   * Gets a child from this nodes children by the given character.
   *
   * @param ch The character und which the child node is saved.
   * @return The child node or null if it does not exist.
   */
  public Node getChild(char ch) {
    return children[calculateArrayIndexByChar(ch)];
  }

  /**
   * Finds the last node from the children nodes by a key, so the node with last
   * letter in the key (for "abc" it would be "c").
   *
   * @param key The key for which to look for.
   * @return The found node which holds the value for the last letter in the key
   * or null if it does not exist.
   */
  public Node find(String key) {
    Node firstChild = getChild(key.charAt(0));
    if (firstChild == null) {
      return null;
    }
    if (key.length() == 1) {
      return firstChild;
    }
    return firstChild.find(key.substring(1));
  }

  /**
   * Removes this node if it is not the global root and if Node#cleanup()
   * succeeds.
   *
   * @see Node#cleanup()
   */
  public void remove() {
    if (isGlobalRoot()) {
      return;
    }
    points = null;
    cleanup();
  }

  /**
   * Deletes this node by setting it null on the parent if this node has no more
   * children itself and is practically useless.
   */
  private void cleanup() {
    if (hasChildren()) {
      return;
    }
    parent.setChild(character, null);
  }

  /**
   * Stringifies the internal trie for readability. A node is shown as its
   * character and in parentheses all children nodes and/or in brackets the
   * points assigned to the node. A trie which just holds the data "abc" = 10
   * would be represented as "+(a(b(c[10)))" where each layer open new
   * parentheses. Values on the same layer look like "(a(b)(c))" or with values
   * like "(a(b[10])(c[10]))".
   *
   * @return The string representation of the trie beginning from this node on.
   */
  @Override
  public String toString() {
    if (isGlobalRoot() && !hasChildren()) {
      /*
       If the root just exists without any children, we return just the
       character of the root.
      */
      return String.valueOf(getCharacter());
    }

    /*
     characterStringRepresentation holds the current character of this node and
     if applicable the associated points in the format "character[points]".
    */
    String characterStringRepresentation = String.valueOf(this.getCharacter());

    if (hasPoints()) {
      /*
       There are points associated with ths node, we have to add the points
       associated with the character.
      */
      characterStringRepresentation += "[" + points + "]";
    }

    /*
     As there are no children on this node we can just return the character
     with the possibly associated points.
    */
    if (!this.hasChildren()) {
      return characterStringRepresentation;
    }

    StringBuilder nodeStringBuilder = new StringBuilder();
    // Loop over all children and append their string representation.
    for (Node child : children) {
      if (child != null) {
        nodeStringBuilder.append(child);
      }
    }

    return characterStringRepresentation + "(" + nodeStringBuilder + ")";
  }

  /**
   * Gets the points value of this node.
   *
   * @return The points this node has assigned to it, might be null.
   */
  public Integer getPoints() {
    return points;
  }

  /**
   * Sets the given points to this node.
   *
   * @param points The value of points to assign to this node.
   */
  public void setPoints(Integer points) {
    this.points = points;
  }

  /**
   * Weather the node has any points assigned or not.
   *
   * @return Weather the node has any points assigned or not.
   */
  private boolean hasPoints() {
    return points != null;
  }

  /**
   * Get the amount of non-null children which are assigned to this node.
   *
   * @return The count of non-null children.
   */
  private int getChildrenCount() {
    int count = 0;
    for (Node child : children) {
      if (child != null) {
        count++;
      }
    }
    return count;
  }

  /**
   * Weather the node has any children or not.
   *
   * @return Weather the node has any children or not.
   */
  private boolean hasChildren() {
    return this.getChildrenCount() != 0;
  }

  /**
   * Gets the character of this node which is the assigned character or "+" for
   * the global root element.
   *
   * @return The character by which this node should be represented.
   */
  private char getCharacter() {
    if (isGlobalRoot()) {
      return '+';
    }
    return character;
  }

}
