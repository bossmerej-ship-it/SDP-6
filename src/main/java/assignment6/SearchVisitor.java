package assignment6;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

public final class SearchVisitor extends TreeVisitor {
    private final Predicate<String> filenameCriterion;
    private final long minimumSize;
    private final Set<String> matches = new TreeSet<>();

    public SearchVisitor(Predicate<String> filenameCriterion, long minimumSize) {
        this.filenameCriterion = Objects.requireNonNull(filenameCriterion, "Filename criterion cannot be null.");
        if (minimumSize < 0) {
            throw new IllegalArgumentException("Minimum size cannot be negative.");
        }
        this.minimumSize = minimumSize;
    }

    @Override
    protected void reset() {
        matches.clear();
    }

    @Override
    public void visit(FileNode file) {
        if (filenameCriterion.test(file.getName()) && file.getSize() >= minimumSize) {
            String path = getCurrentPath();
            matches.add(path.isEmpty() ? file.getName() : path);
        }
    }

    public List<String> getMatches() {
        return List.copyOf(matches);
    }
}
