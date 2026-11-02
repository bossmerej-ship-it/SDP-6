package assignment6;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TotalSizeVisitorTest {
    @Test
    void sumsFilesAcrossSeveralLevels() {
        TotalSizeVisitor visitor = new TotalSizeVisitor();
        visitor.scan(SampleTree.create().root());
        assertEquals(1720, visitor.getTotalSize());
    }

    @Test
    void visitsOnlyTheChosenSubtree() {
        TotalSizeVisitor visitor = new TotalSizeVisitor();
        visitor.scan(SampleTree.create().branch());
        assertEquals(800, visitor.getTotalSize());
    }

    @Test
    void supportsFileRootAndSizesAboveIntegerRange() {
        TotalSizeVisitor visitor = new TotalSizeVisitor();
        visitor.scan(new FileNode("large.bin", 5_000_000_000L));
        assertEquals(5_000_000_000L, visitor.getTotalSize());
    }

    @Test
    void emptyDirectoryHasZeroSize() {
        TotalSizeVisitor visitor = new TotalSizeVisitor();
        visitor.scan(new DirectoryNode("empty"));
        assertEquals(0, visitor.getTotalSize());
    }

    @Test
    void repeatedScansReplacePreviousResult() {
        TotalSizeVisitor visitor = new TotalSizeVisitor();
        DirectoryNode root = SampleTree.create().root();
        visitor.scan(root);
        visitor.scan(root);
        assertEquals(1720, visitor.getTotalSize());
        visitor.scan(new FileNode("zero.bin", 0));
        assertEquals(0, visitor.getTotalSize());
    }

    @Test
    void rejectsOverflowAndCanScanAgain() {
        DirectoryNode root = new DirectoryNode("large");
        root.addChild(new FileNode("first.bin", Long.MAX_VALUE));
        root.addChild(new FileNode("second.bin", 1));
        TotalSizeVisitor visitor = new TotalSizeVisitor();
        assertThrows(ArithmeticException.class, () -> visitor.scan(root));
        visitor.scan(new FileNode("small.bin", 7));
        assertEquals(7, visitor.getTotalSize());
    }
}
