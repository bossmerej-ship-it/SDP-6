package assignment6;

import java.util.Objects;

public abstract class TreeVisitor implements Visitor {
    private String currentPath = "";
    private int currentDepth;

    public final void scan(Element root) {
        Objects.requireNonNull(root, "Root cannot be null.");
        currentPath = "";
        currentDepth = 0;
        reset();
        root.accept(this);
    }

    protected abstract void reset();

    protected final String getCurrentPath() {
        return currentPath;
    }

    protected final int getCurrentDepth() {
        return currentDepth;
    }

    @Override
    public void visit(DirectoryNode directory) {
        for (Node child : directory.getChildren()) {
            visitChild(child);
        }
    }

    private void visitChild(Node child) {
        String previousPath = currentPath;
        currentPath = previousPath.isEmpty() ? child.getName() : previousPath + "/" + child.getName();
        currentDepth++;
        try {
            child.accept(this);
        } finally {
            currentPath = previousPath;
            currentDepth--;
        }
    }
}
