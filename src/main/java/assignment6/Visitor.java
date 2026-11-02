package assignment6;

public interface Visitor {
    void visit(FileNode file);
    void visit(DirectoryNode directory);
}
