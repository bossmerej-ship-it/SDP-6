package assignment6;

public final class StatisticsVisitor extends TreeVisitor {
    private int fileCount;
    private int directoryCount;
    private int maxDepth;

    @Override
    protected void reset() {
        fileCount = 0;
        directoryCount = 0;
        maxDepth = 0;
    }

    @Override
    public void visit(FileNode file) {
        fileCount++;
        maxDepth = Math.max(maxDepth, getCurrentDepth());
    }

    @Override
    public void visit(DirectoryNode directory) {
        directoryCount++;
        maxDepth = Math.max(maxDepth, getCurrentDepth());
        super.visit(directory);
    }

    public int getFileCount() {
        return fileCount;
    }

    public int getDirectoryCount() {
        return directoryCount;
    }

    public int getMaxDepth() {
        return maxDepth;
    }
}
