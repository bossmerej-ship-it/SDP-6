package assignment6;

public interface Element {
    String getName();
    void accept(Visitor visitor);
}
