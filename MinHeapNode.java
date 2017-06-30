
/**
 * Created by antramishra on 3/30/17.
 */
public class MinHeapNode {
    String data;
    int freq;  // Frequency of the character
    MinHeapNode left, right; // Left and right child of this node
    MinHeapNode leftChild;
    MinHeapNode nextSibling;
    MinHeapNode prev;

    MinHeapNode(String data,int freq,MinHeapNode left,MinHeapNode right){
        this.data = data;
        this.freq = freq;
        this.left = left;
        this.right = right;
        this.leftChild = null;
        this.nextSibling = null;
        this.prev = null;
    }

    public int compareTo(MinHeapNode node){
        return  this.freq-node.freq;
    }
}
