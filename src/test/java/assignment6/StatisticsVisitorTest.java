package assignment6;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StatisticsVisitorTest {
    @Test
    void countsEveryFileAndDirectoryIncludingRoot() {
        StatisticsVisitor visitor = new StatisticsVisitor();
        visitor.scan(SampleTree.create().root());
        assertEquals(4, visitor.getFileCount());
        assertEquals(4, visitor.getDirectoryCount());
        assertEquals(3, visitor.getMaxDepth());
    }

    @Test
    void emptyRootIsOneDirectoryAtDepthZero() {
        StatisticsVisitor visitor = new StatisticsVisitor();
        visitor.scan(new DirectoryNode("empty"));
        assertEquals(0, visitor.getFileCount());
        assertEquals(1, visitor.getDirectoryCount());
        assertEquals(0, visitor.getMaxDepth());
    }

    @Test
    void fileRootIsOneFileAtDepthZero() {
        StatisticsVisitor visitor = new StatisticsVisitor();
        visitor.scan(new FileNode("single.txt", 1));
        assertEquals(1, visitor.getFileCount());
        assertEquals(0, visitor.getDirectoryCount());
        assertEquals(0, visitor.getMaxDepth());
    }

    @Test
    void repeatedAndInterleavedScansDoNotKeepOldState() {
        StatisticsVisitor visitor = new StatisticsVisitor();
        DirectoryNode root = SampleTree.create().root();
        visitor.scan(root);
        new TotalSizeVisitor().scan(root);
        new SearchVisitor(name -> true, 0).scan(root);
        visitor.scan(root);
        assertEquals(4, visitor.getFileCount());
        assertEquals(4, visitor.getDirectoryCount());
        assertEquals(3, visitor.getMaxDepth());
        visitor.scan(new DirectoryNode("empty"));
        assertEquals(0, visitor.getFileCount());
        assertEquals(1, visitor.getDirectoryCount());
        assertEquals(0, visitor.getMaxDepth());
    }

    @Test
    void supportsDeepNestingAndCountsEmptyLeafDepth() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode current = root;
        for (int index = 1; index <= 64; index++) {
            DirectoryNode next = new DirectoryNode("level" + index);
            current.addChild(next);
            current = next;
        }
        StatisticsVisitor visitor = new StatisticsVisitor();
        visitor.scan(root);
        assertEquals(0, visitor.getFileCount());
        assertEquals(65, visitor.getDirectoryCount());
        assertEquals(64, visitor.getMaxDepth());
    }
}
