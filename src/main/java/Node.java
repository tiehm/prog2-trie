package main.java;

public class Node {

  private char character;
  private Integer points;
  private final Node[] children = new Node[26];
  private Node parent;
  private boolean isRoot;

  public Node() {}

  public Node(boolean isRoot) {
    this.isRoot = isRoot;
  }

  public Node(char ch, Node parent) {
    this.character = ch;
    this.parent = parent;
    parent.setChild(ch, this);
  }

  private static int calculateArrayPositionByChar(char ch) {
    return ch - 'a';
  }

  private void setChild(char ch, Node child) {
    children[calculateArrayPositionByChar(ch)] = child;
    if (child == null && this.points == null && getChildrenCount() == 0) {
      this.parent.setChild(this.character, null);
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
    if (this.isRoot) {
      return;
    }
    this.points = null;
    cleanup();
  }

  private void cleanup() {
    if (getChildrenCount() != 0) {
      return;
    }
    parent.setChild(this.character, null);
  }

  @Override
  public String toString() {
    if (getChildrenCount() == 0) {
      if (this.isRoot) {
        return "+";
      }
      return getCharacter() + "[" + this.points + "]";
    }
    StringBuilder sb = new StringBuilder();
    for (Node child : this.children) {
      if (child != null) {
        sb.append(child.toString());
      }
    }
    return getCharacter() + "(" + sb.toString() + ")";
  }

  public void setPoints(Integer points) {
    this.points = points;
  }

  public Integer getPoints() {
    return points;
  }

  private int getChildrenCount() {
    int count = 0;
    for (Node child : this.children) {
      if (child != null) {
        count++;
      }
    }
    return count;
  }

  private char getCharacter() {
    if (this.isRoot) {
      return '+';
    }
    return this.character;
  }

}
