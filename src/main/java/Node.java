package main.java;

public class Node {

  private char character;
  private Integer points;
  private final Node[] children = new Node[26];
  private Node parent;

  private boolean isRoot() {
    return parent == null;
  }

  public Node() {}

  public Node(char ch, Node parent) {
    character = ch;
    this.parent = parent;
    parent.setChild(ch, this);
  }

  private static int calculateArrayPositionByChar(char ch) {
    return ch - 'a';
  }

  private void setChild(char ch, Node child) {
    children[calculateArrayPositionByChar(ch)] = child;
    if (child == null && points == null && getChildrenCount() == 0) {
      parent.setChild(character, null);
    }
  }

  public Node getChild(char ch) {
    return children[calculateArrayPositionByChar(ch)];
  }

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

  public void remove() {
    if (isRoot()) {
      return;
    }
    points = null;
    cleanup();
  }

  private void cleanup() {
    if (getChildrenCount() != 0) {
      return;
    }
    parent.setChild(character, null);
  }

  @Override
  public String toString() {
    if (getChildrenCount() == 0) {
      if (isRoot()) {
        return "+";
      }
      return getCharacter() + "[" + points + "]";
    }
    StringBuilder sb = new StringBuilder();
    for (Node child : children) {
      if (child != null) {
        sb.append(child.toString());
      }
    }
    return getCharacter() + "(" + sb + ")";
  }

  public void setPoints(Integer points) {
    this.points = points;
  }

  public Integer getPoints() {
    return points;
  }

  private int getChildrenCount() {
    int count = 0;
    for (Node child : children) {
      if (child != null) {
        count++;
      }
    }
    return count;
  }

  private char getCharacter() {
    if (isRoot()) {
      return '+';
    }
    return character;
  }

}
