import assignment6.DirectoryNode;
import assignment6.FileNode;
import assignment6.SearchVisitor;
import assignment6.StatisticsVisitor;
import assignment6.TotalSizeVisitor;

public class Main {
    public static void main(String[] args) {
        if (args.length != 0 && args.length != 2) {
            System.err.println("Usage: java -jar target/visitor-pattern-1.0.jar [suffix minimumBytes]");
            return;
        }
        try {
            String suffix = args.length == 2 ? args[0] : ".txt";
            long minimumSize = args.length == 2 ? Long.parseLong(args[1]) : 200;
            SearchVisitor search = new SearchVisitor(name -> name.endsWith(suffix), minimumSize);
            DirectoryNode root = createFileSystem();
            TotalSizeVisitor size = new TotalSizeVisitor();
            StatisticsVisitor statistics = new StatisticsVisitor();
            size.scan(root);
            search.scan(root);
            statistics.scan(root);

            System.out.println("Root: " + root.getName());
            System.out.println("Total size: " + size.getTotalSize() + " bytes");
            System.out.println("Search: suffix=" + suffix + ", minimum=" + minimumSize + " bytes");
            search.getMatches().forEach(System.out::println);
            System.out.println("Files: " + statistics.getFileCount());
            System.out.println("Directories: " + statistics.getDirectoryCount());
            System.out.println("Maximum depth: " + statistics.getMaxDepth());
        } catch (IllegalArgumentException exception) {
            System.err.println("Error: " + exception.getMessage());
        }
    }

    private static DirectoryNode createFileSystem() {
        DirectoryNode root = new DirectoryNode("workspace");
        DirectoryNode assets = new DirectoryNode("assets");
        DirectoryNode drafts = new DirectoryNode("drafts");
        DirectoryNode docs = new DirectoryNode("docs");
        root.addChild(new FileNode("notes.txt", 120));
        root.addChild(assets);
        root.addChild(docs);
        root.addChild(new DirectoryNode("empty"));
        root.addChild(new FileNode("zero.bin", 0));
        assets.addChild(new FileNode("logo.png", 800));
        assets.addChild(drafts);
        drafts.addChild(new FileNode("outline.txt", 240));
        drafts.addChild(new FileNode("todo.txt", 80));
        docs.addChild(new FileNode("guide.txt", 400));
        docs.addChild(new FileNode("readme.md", 160));
        return root;
    }
}
