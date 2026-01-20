package dsa;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanCodeImpl implements HuffmanCode {
    Map<Character, String> encoder = new HashMap<>();
    Map<String, Character> decoder = new HashMap<>();

    @Override
    public String encode(String text) {
        buildHuffmanTree(text);
        StringBuilder encodedString = new StringBuilder();
        for (char ch : text.toCharArray()) {
            encodedString.append(encoder.get(ch));
        }
        return encodedString.toString();

    }

    @Override
    public String decode(String encodedText) {
        StringBuilder decodedString = new StringBuilder();
        StringBuilder code = new StringBuilder();
        for (char ch : encodedText.toCharArray()) {
            code.append(ch);
            if (decoder.containsKey(code.toString())) {
                decodedString.append(decoder.get(code.toString()));
                code.setLength(0);
            }
        }
        return decodedString.toString();
    }

    private void buildHuffmanTree(String originalText) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char ch : originalText.toCharArray()) {
            frequencyMap.put(ch, frequencyMap.getOrDefault(ch, 0) + 1);
        }
        PriorityQueue<HuffmanNode> minHeap = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            minHeap.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }
        while (minHeap.size() > 1) {
            HuffmanNode left = minHeap.poll();
            HuffmanNode right = minHeap.poll();
            assert right != null;
            minHeap.add(new HuffmanNode('\0', left.getFrequency() + right.getFrequency(), left, right));
        }
        HuffmanNode root = minHeap.poll();
        generateCode(root, "");

    }

    private void generateCode(HuffmanNode node, String code) {
        if (node != null) {
            if (isLeaf(node)) {
                String assignedCode = code.isEmpty() ? "0" : code;
                encoder.put(node.getData(), assignedCode);
                decoder.put(assignedCode, node.getData());
            }
            generateCode(node.getLeft(), code + "0");
            generateCode(node.getRight(), code + "1");
        }
    }

    private boolean isLeaf(HuffmanNode node) {
        return node.getLeft() == null && node.getRight() == null;
    }

}
