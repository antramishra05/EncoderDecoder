/**
 * Created by antramishra on 3/30/17.
 */
public interface MinHeap {
    int getSize();
    MinHeapNode extractMin();
    void insert(MinHeapNode node);
}
