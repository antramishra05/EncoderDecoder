/**
 * Created by antramishra on 4/1/17.
 */

import java.util.NoSuchElementException;


public class FourWayHeap implements MinHeap {

    private int d;	// The number of children each node has.i
    private int heapSize;	// Number of elements in heap
    private MinHeapNode[] array;	// The heap array
    private int cacheLine = 3;

    public FourWayHeap( int capacity) {
        heapSize = 0;
        d = 4;
        array = new MinHeapNode[ capacity + 1 ];
    }

    public FourWayHeap( MinHeapNode [] array, int numKids){
        int i = 0;
        while (array[i] != null)
            i++;
        heapSize = i;

        this.array = array;
        this.d = numKids;
//        buildMinHeap();
    }

    private int parent(int i) {
        i = i-cacheLine;
        int parent =  (i-1)/d + cacheLine;
        return parent;
    }


    private int kthChild(int i, int k) {
        i -= cacheLine;
        int kChildIndex = d*i + k + cacheLine;
        return kChildIndex;
    }

    public void insert( MinHeapNode node ) {
        if(isFull())
            throw new NoSuchElementException("Heap is full");

        // heapify up
        int hole = heapSize + cacheLine;
        heapSize++;
        array[hole] = node;
        heapifyUp(hole);
    }

    public MinHeapNode findMin( ) {
        if( isEmpty( ) )
            return null;
        return array[0+cacheLine];
    }

    @Override
    public int getSize() {
        return heapSize;
    }

    public MinHeapNode extractMin() {
        if(isEmpty())
            return null;

        MinHeapNode minItem = findMin();
        array[0 + cacheLine] = array[heapSize - 1 + cacheLine];
        heapSize--;
        heapifyDown( 0+cacheLine );
        return minItem;
    }

    public MinHeapNode delete( int hole ) {
        if( isEmpty( ) )
            return null;

        MinHeapNode keyItem = array[hole];
        array[ hole ] = array[ heapSize -1 ];
        heapSize--;
        heapifyDown( hole );

        return keyItem;
    }

    public void buildMinHeap(MinHeapNode[] array) {
        this.array= new MinHeapNode[array.length+cacheLine];
        int j = cacheLine;
        for(MinHeapNode node : array){
            this.array[j++] = node;
        }
        this.heapSize = array.length;
        for(int i = heapSize+cacheLine - 1; i >= 0+cacheLine; i--)
            heapifyDown(i);

    }


    public boolean isEmpty( ) {
        return heapSize == 0;
    }


    public boolean isFull( ) {
        return heapSize == array.length;
    }

    private void heapifyDown(int index ) {
		int child;
		MinHeapNode tmp = array[index];

		for(; kthChild(index, 1) < heapSize + cacheLine; index = child ) {
            child = smallestChild(index);

			if( array[child].compareTo(tmp) < 0 )
			    array[index] = array[child];
            else
				break;
        }
	     array[index] = tmp;
    }

    private int smallestChild( int index ) {
        int bestChildYet = kthChild(index, 1);
        int k = 2;
        int candidateChild = kthChild(index, k);
        while ((k <= d) && (candidateChild < heapSize + cacheLine)) {
            if (array[candidateChild].compareTo(array[bestChildYet]) < 0)
                bestChildYet = candidateChild;
            k++;
            candidateChild = kthChild(index, k);
        }
        return bestChildYet;
    }

    private void heapifyUp(int index) {
        MinHeapNode tmp = array[index];

        for (; index > 0+cacheLine && tmp.compareTo( array[parent(index)] ) < 0; index = parent(index) )
            array[index] = array[parent(index)];
        array[index] = tmp;
    }
}
