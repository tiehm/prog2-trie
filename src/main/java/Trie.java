package main.java;

public class Trie {

  private final Node root = new Node();

  public Trie() {}

  public boolean add(String key, Integer points) {
    boolean alreadyExists = this.points(key) != null;
    if (alreadyExists) {
      return false;
    }
    Node currentRoot = this.root;
    for (char c : key.toCharArray()) {
      Node existingNode = currentRoot.getChild(c);
      if (existingNode != null) {
        currentRoot = existingNode;
        continue;
      }
      currentRoot = new Node(c, currentRoot);
    }
    currentRoot.setPoints(points);
    return true;
  }

  public boolean remove(String key) {
    Node node = root.find(key);
    if (node == null) {
      return false;
    }
    node.remove();
    return true;
  }

  public boolean change(String key, Integer points) {
    Node node = root.find(key);
    if (node == null) {
      return false;
    }
    node.setPoints(points);
    return true;
  }

  public Integer points(String key) {
    Node node = root.find(key);
    if (node == null) {
      return null;
    }
    return node.getPoints();
  }

  @Override
  public String toString() {
    return root.toString();
  }
}
