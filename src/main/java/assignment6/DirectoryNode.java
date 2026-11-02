package assignment6;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class DirectoryNode extends Node {
    private final List<Node> children = new ArrayList<>();

    public DirectoryNode(String name) {
        super(name);
    }

    public void addChild(Node child) {
        Objects.requireNonNull(child, "Child cannot be null.");
        for (Node ancestor = this; ancestor != null; ancestor = ancestor.getParent()) {
            if (ancestor == child) {
                throw new IllegalArgumentException("A directory cannot contain itself or an ancestor.");
            }
        }
        if (child.getParent() != null) {
            throw new IllegalArgumentException("Child already belongs to a directory.");
        }
        for (Node existing : children) {
            if (existing.getName().equals(child.getName())) {
                throw new IllegalArgumentException("Sibling names must be unique.");
            }
        }
        children.add(child);
        child.attachTo(this);
    }

    public List<Node> getChildren() {
        return List.copyOf(children);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
