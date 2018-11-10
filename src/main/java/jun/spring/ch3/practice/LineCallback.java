package jun.spring.ch3.practice;

public interface LineCallback<T> {
    T doSomethingWithLine(String line, T value);
}
