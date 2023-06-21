
import java.io.FileOutputStream;
import java.io.IOException;

public class HuffmanEncoder {
    protected String inputFileName = "WarAndPeace.txt";
    protected String outputFileName = "./WarAndPeace-compressed.bin";
    protected String codesFileName = "./WarAndPeace-codes.txt";
    protected BookReader book;
    //protected MyOrderedList<FrequencyNode> frequencies;
    protected MyHashTable<String, Integer> frequenciesHash;
    protected HuffmanNode huffmanTree;
    //protected MyOrderedList<CodeNode> codes;
    protected MyHashTable<String, String> codesHash;
    protected boolean wordCodes;
    protected byte[] encodedText;



    public HuffmanEncoder() throws IOException {
        book = new BookReader("WarAndPeace.txt");
        //frequencies = new MyOrderedList<>();
        frequenciesHash = new MyHashTable<>(37768);
        huffmanTree = new HuffmanNode();
        //codes = new MyOrderedList<>();
        codesHash = new MyHashTable<>(32768);
        encodedText = new byte[0];
        countFrequency();
        buildTree();
        encode();
        writeFiles();
    }

    protected void countFrequency() {
        System.out.println("\nCounting Frequencies...");
        long startTime = System.currentTimeMillis();
        book.wordsAndSeparators.current = book.wordsAndSeparators.first;
        int temp;

        while (book.wordsAndSeparators.current != null) {
            if(!frequenciesHash.keys.contains(book.wordsAndSeparators.current.item)){
                frequenciesHash.put(book.wordsAndSeparators.current.item, 1);
            } else {
                temp = frequenciesHash.get(book.wordsAndSeparators.current.item);
                temp++;
                frequenciesHash.put(book.wordsAndSeparators.current.item, temp);
            }
            book.wordsAndSeparators.next();
        }

        long stopTime = System.currentTimeMillis();
        System.out.println("Time to Count Frequencies: " + (stopTime - startTime) + " milliseconds.");
        System.out.println("Number of Unique Words: " + frequenciesHash.size());
    }

    protected void buildTree() {
        System.out.println("\nCreating Huffman Tree...");
        long startTime = System.currentTimeMillis();
        MyPriorityQueue<HuffmanNode> storeHuffman = new MyPriorityQueue<>();
        for (int i = 0; i < frequenciesHash.size(); i++) {
            HuffmanNode node = new HuffmanNode();
            node.word = frequenciesHash.keys.get(i);
            node.weight = frequenciesHash.get(node.word);
            storeHuffman.insert(node);
        }
        if (!storeHuffman.isEmpty()) {
            while (storeHuffman.size() > 1) {
                HuffmanNode leftChild = storeHuffman.removeMin();
                HuffmanNode rightChild = storeHuffman.removeMin();
                HuffmanNode merged = new HuffmanNode(leftChild, rightChild);
                storeHuffman.insert(merged);
            }
            huffmanTree = storeHuffman.removeMin();
            extractCodes(huffmanTree, "");
        }
        long stopTime = System.currentTimeMillis();
        System.out.println("Time to Create Huffman Tree: " + (stopTime - startTime) + " milliseconds.");
    }

    protected void extractCodes(HuffmanNode node, String code){
        if(node.right == null && node.left == null){
            codesHash.put(node.word, code);
        } else {
            extractCodes(node.left, code + "0");
            extractCodes(node.right, code + "1");
        }
    }

    protected void encode(){
        System.out.println("\nEncoding Message...");
        long startTime = System.currentTimeMillis();
        StringBuilder encoder = new StringBuilder();
        StringBuilder last = new StringBuilder();
        for(String iterate = book.wordsAndSeparators.first(); iterate != null; iterate = book.wordsAndSeparators.next()){
            encoder.append(codesHash.get(iterate));
        }
        int padZeros = encoder.length() % 8;
        last.append("0".repeat(padZeros));
        last.append(codesHash.get(null));
        encoder.append(last);
        byte[] temp = new byte[encoder.length() / 8];
        for (int i = 0, j = 0; i < temp.length; i += 8, j++){
            byte b = (byte)Integer.parseInt((encoder.substring(i, i+8)),2);
            temp[j] = b;
        }

        encodedText = temp;
        long endTime = System.currentTimeMillis();
        System.out.println("Time to Encode: " + (endTime - startTime) + " milliseconds.");
    }

    protected void writeFiles() throws IOException {
        System.out.println("\nWriting Files...");
        long startTime = System.currentTimeMillis();
        FileOutputStream encoded = new FileOutputStream("./WarAndPeace-compressed.bin");
        encoded.write(encodedText);
        encoded.close();

        FileOutputStream codesFile = new FileOutputStream("./WarAndPeace-codes.txt");
        codesFile.write(codesHash.toString().getBytes());
        codesFile.close();
        long stopTime = System.currentTimeMillis();
        System.out.println("Time to Write Files: " + (stopTime - startTime) + " milliseconds.");
        System.out.println("Total Bytes Written: " + encodedText.length);
    }


}

//class FrequencyNode implements Comparable<FrequencyNode>{
//    public Character character = null;
//    public Integer count = 0;
//
//    @Override
//    public int compareTo(FrequencyNode other) {
//        return character.compareTo(other.character);
//    }
//
//    public String toString(){
//        return character + ":" + count;
//    }
//
//}

class HuffmanNode implements Comparable<HuffmanNode>{
    //public Character character = null;
    public String word = "";
    public Integer weight = 0;
    public HuffmanNode left;
    public HuffmanNode right;

    public HuffmanNode() {
        this.left = null;
        this.right = null;
    }

    public HuffmanNode(HuffmanNode left, HuffmanNode right){
        this.weight = left.weight + right.weight;
        this.left = left;
        this.right = right;
    }

    @Override
    public int compareTo(HuffmanNode other) {
        if (weight.compareTo(other.weight) == 0){
            return 0;
        } else if (weight < other.weight){
            return -1;
        } else {
            return 1;
        }
    }

    public String toString(){
        return word + ":" + weight;
    }

}

//class CodeNode implements Comparable<CodeNode>{
//    public Character character = null;
//    public String code = "";
//
//    public int compareTo(CodeNode other){
//        return character.compareTo(other.character);
//    }
//
//    public String toString(){
//        return character + ":" + code;
//    }
//
//}
