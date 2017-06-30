import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by antramishra on 3/29/17.
 */

public class encoder {
    String inputFileName;
    HashMap<String,Integer> freqTable;
    HashMap<String,String> codeTable;
    MinHeap minHeap;
    PrintWriter codeTableWriter;
    static boolean debug = false;

    encoder(String fileName){
        this.inputFileName = fileName;
        freqTable = new HashMap<>();
        codeTable = new HashMap<>();
    }

    public void buildFrequencyTable(){
        try (Stream<String> stream = Files.lines(Paths.get(inputFileName))) {
            stream.forEach(number -> {
                if(!number.trim().equals("")){
                    if (freqTable.containsKey(number)){
                        freqTable.put(number,freqTable.get(number)+1);
                    }else {
                        freqTable.put(number,1);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
/*
        for(Map.Entry<String,Integer> entry : freqTable.entrySet()){
            Integer number = entry.getValue();
            String key = entry.getKey();
            System.out.println(key+" "+number);
        }
*/

    }

    MinHeapNode buildHuffmanTree(){
        MinHeapNode left, right, root;

        while (minHeap.getSize() > 1) {
            left = minHeap.extractMin();
            right = minHeap.extractMin();
            root = new MinHeapNode("#",left.freq+right.freq,left,right);
            minHeap.insert(root);
        }
        return minHeap.extractMin();
    }

    void printCodes(MinHeapNode root, int arr[], int top) {
        if (root.left != null) {
            arr[top] = 0;
            printCodes(root.left, arr, top + 1);
        }

        if (root.right != null) {
            arr[top] = 1;
            printCodes(root.right, arr, top + 1);
        }

        if (root.left == null && root.right == null) {
            StringBuilder code = new StringBuilder();
            for (int i = 0; i < top; ++i) {
                code.append(arr[i]);
            }
            codeTable.put(root.data, code.toString());
            codeTableWriter.println(root.data + " " + code.toString());
        }
    }

    public void encodeInputFile(){
        try {
            FileOutputStream os = new FileOutputStream(Constants.encodedFileName);
            BufferedOutputStream fos = new BufferedOutputStream(os);
            BufferedReader bf = new BufferedReader(new FileReader(inputFileName));
            String number;
            StringBuilder encodeString = new StringBuilder();

            while ((number = bf.readLine()) != null && !number.trim().equals("")){
                encodeString.append(codeTable.get(number));
                while(encodeString.length() >= 8){
                    String byteString = encodeString.toString().substring(0,8);
                    Integer parsedByte = Integer.parseInt(byteString, 2);
                    byte b = parsedByte.byteValue();
                    fos.write(b);
                    encodeString.delete(0,8);
                }
            }
            fos.close();
            bf.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setMinHeap(MinHeap minHeap) {
        this.minHeap = minHeap;
    }

    void encodeUsingBinaryHeap(MinHeapNode[] minHeapNodes) throws IOException{
        BinaryHeap minHeap = new BinaryHeap(minHeapNodes.length);
        minHeap.buildMinHeap(minHeapNodes);
        setMinHeap(minHeap);
    }

    void encodeUsingPairingHeap(MinHeapNode[] minHeapNodes) throws IOException{
        PairingHeap minHeap = new PairingHeap();
        minHeap.buildMinHeap(minHeapNodes);
        setMinHeap(minHeap);
    }

    void encodeUsingFourWayHeap(MinHeapNode[] minHeapNodes) throws IOException{
        FourWayHeap minHeap = new FourWayHeap(0);
        minHeap.buildMinHeap(minHeapNodes);
        setMinHeap(minHeap);
    }

    static void printDebugMessage(String msg){
        if(debug)
            System.out.println(msg);
    }

    public static void main(String[] args) throws IOException{
        String fileName = args[0];
//        calculateFastestHeap(fileName);
//        System.exit(0);

        long startTime = System.currentTimeMillis();
        encoder e = new encoder(fileName);
        e.buildFrequencyTable();
        e.codeTableWriter = new PrintWriter(Constants.codeFileName, "UTF-8");

        int i=0;
        int size = e.freqTable.entrySet().size();
        MinHeapNode[] minHeapNodes = new MinHeapNode[size];
        for(Map.Entry<String,Integer> entry : e.freqTable.entrySet()){
            String data = entry.getKey();
            int freq = entry.getValue();
            minHeapNodes[i++] = new MinHeapNode(data,freq,null,null);
        }

        printDebugMessage("Time to build freqTable(millisecond):"+String.valueOf(System.currentTimeMillis() - startTime));

        e.encodeUsingBinaryHeap(minHeapNodes);
//        e.encodeUsingPairingHeap(minHeapNodes);
//        e.encodeUsingFourWayHeap(minHeapNodes);

        long startTime1 = System.currentTimeMillis();
        MinHeapNode huffman = e.buildHuffmanTree();
        printDebugMessage("Time to buildHuffman tree(millisecond):"+String.valueOf(System.currentTimeMillis() - startTime1));

        long startTime2 = System.currentTimeMillis();
        e.printCodes(huffman, new int[10000], 0);
        printDebugMessage("Time to print codes(millisecond):"+String.valueOf(System.currentTimeMillis() - startTime2));

        long startTime3 = System.currentTimeMillis();
        e.encodeInputFile();
        printDebugMessage("Time to encode file(millisecond):"+String.valueOf(System.currentTimeMillis() - startTime3));
        e.codeTableWriter.close();

        printDebugMessage("Total time taken(millisecond)"+String.valueOf(System.currentTimeMillis() - startTime));
    }


    static void calculateFastestHeap(String inputFileName) throws IOException {
        encoder e = new encoder(inputFileName);
        e.buildFrequencyTable();


        long startTime1 = System.currentTimeMillis();
        for(int j=0; j<4;j++) {
            int i=0;
            int size = e.freqTable.entrySet().size();
            MinHeapNode[] minHeapNodes = new MinHeapNode[size];
            for(Map.Entry<String,Integer> entry : e.freqTable.entrySet()){
                String data = entry.getKey();
                int freq = entry.getValue();
                minHeapNodes[i++] = new MinHeapNode(data,freq,null,null);
            }
            e.encodeUsingBinaryHeap(minHeapNodes);
            e.buildHuffmanTree();
        }
        printDebugMessage("Time using binary heap(millisecond):"+String.valueOf((System.currentTimeMillis() - startTime1)/10));

        long startTime2 = System.currentTimeMillis();
        for(int j=0; j<10;j++) {
            int i=0;
            int size = e.freqTable.entrySet().size();
            MinHeapNode[] minHeapNodes = new MinHeapNode[size];
            for(Map.Entry<String,Integer> entry : e.freqTable.entrySet()){
                String data = entry.getKey();
                int freq = entry.getValue();
                minHeapNodes[i++] = new MinHeapNode(data,freq,null,null);
            }
            e.encodeUsingPairingHeap(minHeapNodes);
            e.buildHuffmanTree();
        }
        printDebugMessage("Time using pairing heap(millisecond):"+String.valueOf((System.currentTimeMillis() - startTime2)/10));

        long startTime3 = System.currentTimeMillis();
        for(int j=0; j<10;j++) {
            int i=0;
            int size = e.freqTable.entrySet().size();
            MinHeapNode[] minHeapNodes = new MinHeapNode[size];
            for(Map.Entry<String,Integer> entry : e.freqTable.entrySet()){
                String data = entry.getKey();
                int freq = entry.getValue();
                minHeapNodes[i++] = new MinHeapNode(data,freq,null,null);
            }
            e.encodeUsingFourWayHeap(minHeapNodes);
            e.buildHuffmanTree();
        }
        printDebugMessage("Time using 4-way heap(millisecond):"+String.valueOf((System.currentTimeMillis() - startTime3)/10));
    }
}
