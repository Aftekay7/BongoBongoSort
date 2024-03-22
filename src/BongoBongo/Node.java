package BongoBongo;

/**
    - simple linked list/stack for the (unsorted) values, forms the value-list.
    - head (and tail) of this list is stored by a NodeMeta instance.
 */
public class Node {

    private int value;
    private Node next;

    public Node (int value) {
        this.value = value;
        this.next = null;
    }

    public Node getNext() {
        return next;
    }

    public int getValue() {
        return value;
    }


    public void setNext(Node next) {
        this.next = next;
    }
}
