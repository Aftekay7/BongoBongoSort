/*
    - elements of the container-array.
    - stores the head and tail of the value-list
    - stores meta-information (max, min, count) of the value-stack
 */
public class NodeMeta {
    private int count = 0;          //size of the stack
    private Node head = null;       //minimum of the stack
    private Node tail = null;       //maximum of the stack


    /*
        - inserts a value into the stack
        - stack has form of [minimum, (count - 2) unsorted values, maximum]
    */
    public void addValue (int value) {
        //no values where added yet
        if (head == null) {

            head = new Node(value);
            tail = head;

            } else {
                Node newNode = new Node(value);

                //value < minimum -> minimum = value
                if(value < head.getValue()) {
                    newNode.setNext(head);
                    head = newNode;

                   // value >= maximum -> maximum = value (>=, so order of values are not switched if equal. Its unimportant here, might be interesting while optimizing)
                } else if (value >= tail.getValue()) {
                    tail.setNext(newNode);
                    tail = newNode;
                    //value is inserted after the minimum
                } else {
                    newNode.setNext(head.getNext());
                    head.setNext(newNode);
                }
        }
        count++;
    }

    public int getCount() {
        return count;
    }
    public Node getHead() {
        return head;
    }
    public Node getTail() {
        return tail;
    }
}
