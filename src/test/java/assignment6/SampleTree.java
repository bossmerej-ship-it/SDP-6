package assignment6;

record SampleTree(DirectoryNode root, DirectoryNode branch) {
    static SampleTree create() {
        DirectoryNode root = new DirectoryNode("project");
        DirectoryNode branch = new DirectoryNode("b");
        DirectoryNode nested = new DirectoryNode("a");
        root.addChild(new FileNode("z.txt", 20));
        root.addChild(new FileNode("report.md", 900));
        root.addChild(branch);
        root.addChild(new DirectoryNode("empty"));
        branch.addChild(new FileNode("c.txt", 300));
        branch.addChild(nested);
        nested.addChild(new FileNode("d.txt", 500));
        return new SampleTree(root, branch);
    }
}
