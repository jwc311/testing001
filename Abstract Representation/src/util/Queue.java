package util;

class Node{
    Node Next;
    Object Info;
    public Node(Object x, Node Next){
        this.Info = x;
        this.Next = Next;
    }
}
public class Queue{
    Node NodeHead;
    Node NodeEnd;
    Long length;    
    public void push(Object x) {
        Node New;
        New = new Node(x, null);
        if (NodeHead == null) {
            NodeHead = New;
        } else {
            NodeEnd.Next = New;
        }
        NodeEnd = New;
        length ++;
    }
    public void pop() throws IllegalArgumentException{
        if(NodeHead == null) throw new IllegalArgumentException();
        NodeHead = NodeHead.Next;
        length --;
    } 
    public Object head() throws IllegalArgumentException {
        if (NodeHead == null) {
            throw new IllegalArgumentException();
        } else {
            return NodeHead.Info;
        }
    }
    public Long size(){
        return length;
    }
    public Queue() {
    // Return Empty Queue
        NodeHead = null;
        NodeEnd = null;
        length = 0L;
    }
}
