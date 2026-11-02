package assignment6;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ElementTest {
    @Test
    void fileUsesFileOverload() {
        RecordingVisitor visitor = new RecordingVisitor();
        Element file = new FileNode("note.txt", 12);
        file.accept(visitor);
        assertEquals("file", visitor.called);
        assertSame(file, visitor.element);
    }

    @Test
    void directoryUsesDirectoryOverloadWithoutTraversing() {
        DirectoryNode directory = new DirectoryNode("docs");
        directory.addChild(new FileNode("note.txt", 12));
        RecordingVisitor visitor = new RecordingVisitor();
        directory.accept(visitor);
        assertEquals("directory", visitor.called);
        assertSame(directory, visitor.element);
    }

    @Test
    void rejectsNegativeFileSize() {
        assertThrows(IllegalArgumentException.class, () -> new FileNode("bad.txt", -1));
    }

    @Test
    void acceptsZeroByteFile() {
        assertEquals(0, new FileNode("empty.txt", 0).getSize());
    }

    @Test
    void rejectsInvalidNames() {
        for (String name : new String[] {null, "", "  ", ".", "..", "a/b", "a\\b"}) {
            assertThrows(IllegalArgumentException.class, () -> new DirectoryNode(name));
            assertThrows(IllegalArgumentException.class, () -> new FileNode(name, 1));
        }
    }

    @Test
    void rejectsDuplicateNamesAcrossElementTypesWithoutAttachingChild() {
        DirectoryNode root = new DirectoryNode("root");
        root.addChild(new FileNode("shared", 10));
        DirectoryNode duplicate = new DirectoryNode("shared");
        assertThrows(IllegalArgumentException.class, () -> root.addChild(duplicate));
        assertEquals(1, root.getChildren().size());
        DirectoryNode other = new DirectoryNode("other");
        assertDoesNotThrow(() -> other.addChild(duplicate));
    }

    @Test
    void rejectsMultipleParentsWithoutChangingEitherDirectory() {
        DirectoryNode first = new DirectoryNode("first");
        DirectoryNode second = new DirectoryNode("second");
        FileNode file = new FileNode("data.bin", 30);
        first.addChild(file);
        assertThrows(IllegalArgumentException.class, () -> second.addChild(file));
        assertThrows(IllegalArgumentException.class, () -> first.addChild(file));
        assertEquals(List.of(file), first.getChildren());
        assertTrue(second.getChildren().isEmpty());
    }

    @Test
    void rejectsSelfCycle() {
        DirectoryNode root = new DirectoryNode("root");
        assertThrows(IllegalArgumentException.class, () -> root.addChild(root));
        assertTrue(root.getChildren().isEmpty());
    }

    @Test
    void rejectsIndirectCycleWhileKeepingValidNesting() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode middle = new DirectoryNode("middle");
        DirectoryNode leaf = new DirectoryNode("leaf");
        root.addChild(middle);
        middle.addChild(leaf);
        assertThrows(IllegalArgumentException.class, () -> leaf.addChild(root));
        assertEquals(List.of(middle), root.getChildren());
        assertEquals(List.of(leaf), middle.getChildren());
        assertTrue(leaf.getChildren().isEmpty());
    }

    @Test
    void childrenAreReadOnlySnapshots() {
        DirectoryNode root = new DirectoryNode("root");
        List<Node> snapshot = root.getChildren();
        assertThrows(UnsupportedOperationException.class,
                () -> snapshot.add(new FileNode("outside.txt", 1)));
        root.addChild(new FileNode("inside.txt", 2));
        assertTrue(snapshot.isEmpty());
        assertEquals(1, root.getChildren().size());
    }

    @Test
    void permitsSameNameInDifferentDirectories() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode first = new DirectoryNode("first");
        DirectoryNode second = new DirectoryNode("second");
        first.addChild(new FileNode("note.txt", 10));
        second.addChild(new FileNode("note.txt", 20));
        root.addChild(first);
        root.addChild(second);
        assertEquals(2, root.getChildren().size());
    }

    @Test
    void rejectsNullChild() {
        DirectoryNode root = new DirectoryNode("root");
        assertThrows(NullPointerException.class, () -> root.addChild(null));
        assertTrue(root.getChildren().isEmpty());
    }

    private static class RecordingVisitor implements Visitor {
        private String called;
        private Element element;

        @Override
        public void visit(FileNode file) {
            called = "file";
            element = file;
        }

        @Override
        public void visit(DirectoryNode directory) {
            called = "directory";
            element = directory;
        }
    }
}
