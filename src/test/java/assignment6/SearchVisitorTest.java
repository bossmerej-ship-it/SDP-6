package assignment6;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SearchVisitorTest {
    @Test
    void combinesFilenameAndInclusiveSizeConditions() {
        SearchVisitor visitor = new SearchVisitor(name -> name.endsWith(".txt"), 300);
        visitor.scan(SampleTree.create().root());
        assertEquals(List.of("b/a/d.txt", "b/c.txt"), visitor.getMatches());
    }

    @Test
    void pathsAreSortedRegardlessOfInsertionOrder() {
        DirectoryNode root = new DirectoryNode("ignored-root-name");
        root.addChild(new FileNode("z.txt", 10));
        root.addChild(new FileNode("a.txt", 10));
        SearchVisitor visitor = new SearchVisitor(name -> true, 0);
        visitor.scan(root);
        assertEquals(List.of("a.txt", "z.txt"), visitor.getMatches());
    }

    @Test
    void pathsAreRelativeToTheChosenSubtree() {
        SearchVisitor visitor = new SearchVisitor(name -> name.startsWith("d"), 0);
        visitor.scan(SampleTree.create().branch());
        assertEquals(List.of("a/d.txt"), visitor.getMatches());
    }

    @Test
    void handlesFileRootIncludingZeroByteThreshold() {
        SearchVisitor visitor = new SearchVisitor(name -> name.equals("zero.txt"), 0);
        visitor.scan(new FileNode("zero.txt", 0));
        assertEquals(List.of("zero.txt"), visitor.getMatches());
    }

    @Test
    void handlesNoMatchAndEmptyDirectory() {
        SearchVisitor visitor = new SearchVisitor(name -> name.endsWith(".png"), 0);
        visitor.scan(SampleTree.create().root());
        assertTrue(visitor.getMatches().isEmpty());
        visitor.scan(new DirectoryNode("empty"));
        assertTrue(visitor.getMatches().isEmpty());
    }

    @Test
    void repeatedScansResetResultsAndReturnedListsAreSnapshots() {
        SearchVisitor visitor = new SearchVisitor(name -> name.endsWith(".txt"), 300);
        DirectoryNode root = SampleTree.create().root();
        visitor.scan(root);
        List<String> firstResult = visitor.getMatches();
        assertThrows(UnsupportedOperationException.class, () -> firstResult.add("bad.txt"));
        visitor.scan(root);
        assertEquals(firstResult, visitor.getMatches());
        visitor.scan(new DirectoryNode("empty"));
        assertTrue(visitor.getMatches().isEmpty());
        assertEquals(List.of("b/a/d.txt", "b/c.txt"), firstResult);
    }

    @Test
    void rejectsInvalidConfiguration() {
        assertThrows(NullPointerException.class, () -> new SearchVisitor(null, 0));
        assertThrows(IllegalArgumentException.class, () -> new SearchVisitor(name -> true, -1));
    }
}
