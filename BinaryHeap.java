/**
 * Created by antramishra on 3/29/17.
 */
public class BinaryHeap implements MinHeap {
    MinHeapNode[] array;
    int heapSize = 0;

    BinaryHeap(int capacity){
        this.heapSize = 0;
        this.array = new MinHeapNode[capacity];
    }


    public MinHeapNode extractMin(){
        MinHeapNode minNode = array[0];
        array[0] = array[heapSize -1];
        heapSize -= 1;
        minHeapify(0);
        return minNode;
    }

    public void insert(MinHeapNode node){
        heapSize += 1;
        int index = heapSize -1;
        while (index >= 0 && node.freq < array[(index-1)/2].freq){
            array[index] = array[(index-1)/2];
            index = (index-1)/2;
        }
        array[index] = node;
    }

    private void minHeapify(int index){
        int smallest = index;
        int left = 2 * index + 1;
        int right = 2 * index + 2;

        if (left < heapSize && array[left].freq < array[smallest].freq)
            smallest = left;

        if (right < heapSize && array[right].freq < array[smallest].freq)
            smallest = right;

        if (smallest != index) {
            MinHeapNode tmp = array[index];
            array[index] = array[smallest];
            array[smallest] = tmp;
            minHeapify(smallest);
        }
    }


    public void buildMinHeap(MinHeapNode[] minHeapNodes) {
        this.array = minHeapNodes;
        heapSize = minHeapNodes.length;
        int n = heapSize - 1;
        for (int i = (n - 1) / 2; i >= 0; --i)
            minHeapify(i);
    }

    public int getSize(){
        return heapSize;
    }

}
