package se.misc;

//ToDo: implement observer pattern in class with multi subscribe.
//      overload add/remove operations
@FunctionalInterface
public interface Action<T1> {
    void invoke(T1 arg1);
}
