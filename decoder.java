import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Created by antramishra on 3/29/17.
 */

public class decoder {
    private String encodedFile,codeTableFile;
    MinHeapNode root = null;
    BufferedWriter decodeWriter;
    FileInputStream in = null;

    decoder(String encodedFile,String codeTableFile){
        this.encodedFile = encodedFile;
        this.codeTableFile = codeTableFile;
    }

    int readByteFromFile() throws IOException{
        int c;
        if ((c = in.read()) != -1) {
            return c;
        }
        return -1;
    }

    void decodeFile() throws IOException{
        in = new FileInputStream(this.encodedFile);
        MinHeapNode curNode = root;
        int b = readByteFromFile();
        while (b != -1) {
            int i = 0;
            for( ; i < 8 ; i++){
                int mask = (1 << (7 - i));
                int bit = b & mask;
                if(bit != 0){
                    curNode = curNode.right;
                }else{
                    curNode = curNode.left;
                }
                if(curNode.left == null && curNode.right == null){
                    // print to decoded file
                    printToDecodedFile(curNode.data);
                   curNode =  root;
                }
            }
            b = readByteFromFile();
        }
    }

    void constructDecodeTree() throws IOException{
        try (Stream<String> stream = Files.lines(Paths.get(this.codeTableFile))) {
            stream.forEach(line -> {
                String data = line.split(" ")[0];
                String code = line.split(" ")[1];
                root = insertInHuffmanTree(root,code,0,data);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    MinHeapNode insertInHuffmanTree(MinHeapNode node,String code, int index, String data){
        if(node == null){
            node = new MinHeapNode(data,0,null,null);
            if(index == code.length()){
                node.data = data;
                return node;
            }
        }

        if(code.charAt(index) == '0'){
            node.left = insertInHuffmanTree(node.left,code,index+1,data);
        }else {
            node.right = insertInHuffmanTree(node.right,code,index+1,data);
        }
        return node;
    }

    void printToDecodedFile(String data) throws IOException{
        decodeWriter.write(data);
        decodeWriter.newLine();
    }

    public static void main(String[] args) throws IOException {
        String encodedFile = args[0];
        String codeTableFile = args[1];

        Long startTime = System.currentTimeMillis();
        decoder d = new decoder(encodedFile,codeTableFile);
        d.decodeWriter = new BufferedWriter(new FileWriter(Constants.decodedFile));
        d.constructDecodeTree();
        d.decodeFile();
        d.decodeWriter.close();
//        System.out.println(System.currentTimeMillis()-startTime+" ms to decode file.");
    }
}