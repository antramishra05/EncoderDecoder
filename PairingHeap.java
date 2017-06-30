//class PairNode extends MinHeapNode{
//    PairNode leftChild;
//    PairNode nextSibling;
//    PairNode prev;
//
//    public PairNode(String data, int freq, MinHeapNode left, MinHeapNode right, PairNode leftChild, PairNode nextSibling, PairNode prev) {
//        super(data, freq, left, right);
//        this.leftChild = leftChild;
//        this.nextSibling = nextSibling;
//        this.prev = prev;
//    }
//}

public class PairingHeap implements MinHeap{
    private MinHeapNode root;
    private MinHeapNode[] treeArray;
    int heapSize;

    public PairingHeap(){
        root = null;
        this.heapSize = 0;
        this.treeArray = new MinHeapNode[5];
    }

    public boolean isEmpty(){
        return root == null;
    }

    public void insert(MinHeapNode node){
        heapSize++;
        if (root == null)
            root = node;
        else
            root = compareAndLink(root,node);
    }

    private MinHeapNode compareAndLink(MinHeapNode first, MinHeapNode second) {
        if (second == null)
            return first;

        if (second.freq < first.freq) {
            second.prev = first.prev;
            first.prev = second;
            first.nextSibling = second.leftChild;
            if (first.nextSibling != null)
                first.nextSibling.prev = first;
            second.leftChild = first;
            return second;
        }
        else {
            second.prev = first;
            first.nextSibling = second.nextSibling;
            if (first.nextSibling != null)
                first.nextSibling.prev = first;
            second.nextSibling = first.leftChild;
            if (second.nextSibling != null)
                second.nextSibling.prev = second;
            first.leftChild = second;
            return first;
        }
    }

    private MinHeapNode combineSiblings(MinHeapNode firstSibling){
        if( firstSibling.nextSibling == null )
            return firstSibling;
        int numSiblings = 0;
        for ( ; firstSibling != null; numSiblings++) {
            treeArray = doubleIfFull( treeArray, numSiblings );
            treeArray[ numSiblings ] = firstSibling;
            firstSibling.prev.nextSibling = null;
            firstSibling = firstSibling.nextSibling;
        }
        treeArray = doubleIfFull( treeArray, numSiblings );
        treeArray[ numSiblings ] = null;
        int i = 0;
        for ( ; i + 1 < numSiblings; i += 2)
            treeArray[ i ] = compareAndLink(treeArray[i], treeArray[i + 1]);
        int j = i - 2;
        if (j == numSiblings - 3)
            treeArray[ j ] = compareAndLink( treeArray[ j ], treeArray[ j + 2 ] );
        for ( ; j >= 2; j -= 2)
            treeArray[j - 2] = compareAndLink(treeArray[j-2], treeArray[j]);
        return treeArray[0];
    }

    private MinHeapNode[] doubleIfFull(MinHeapNode[] array, int index){
        if (index == array.length) {
            MinHeapNode[] oldArray = array;
            array = new MinHeapNode[index * 2];
            for( int i = 0; i < index; i++ )
                array[i] = oldArray[i];
        }
        return array;
    }

    public MinHeapNode extractMin(){
        if (isEmpty())
            return null;

        heapSize--;
        MinHeapNode minNode = root;
        if (root.leftChild == null)
            root = null;
        else
            root = combineSiblings(root.leftChild);
        return minNode;
    }

    public MinHeapNode buildMinHeap(MinHeapNode[] array){
//        this.treeArray = new PairNode[array.length];
        for (MinHeapNode p : array) {
            insert(p);
        }
        return root;
    }

    public int getSize(){
        return heapSize;
    }

}
 