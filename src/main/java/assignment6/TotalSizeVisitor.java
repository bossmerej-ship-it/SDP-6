package assignment6;

public final class TotalSizeVisitor extends TreeVisitor {
    private long totalSize;

    @Override
    protected void reset() {
        totalSize = 0;
    }

    @Override
    public void visit(FileNode file) {
        totalSize = Math.addExact(totalSize, file.getSize());
    }

    public long getTotalSize() {
        return totalSize;
    }
}
