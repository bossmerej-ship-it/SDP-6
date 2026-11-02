package assignment6;

public abstract class Node implements Element {
    private final String name;
    private DirectoryNode parent;

    protected Node(String name) {
        if (name == null || name.isBlank() || name.equals(".") || name.equals("..")
                || name.contains("/") || name.contains("\\")) {
            throw new IllegalArgumentException("Name must be a non-empty path segment.");
        }
        this.name = name;
    }

    @Override
    public final String getName() {
        return name;
    }

    final DirectoryNode getParent() {
        return parent;
    }

    final void attachTo(DirectoryNode directory) {
        parent = directory;
    }
}
