package com.mikuac.shiro.entity;

import java.util.HashMap;

import static java.util.Arrays.sort;

public class Node implements Comparable<Node> {
    //哈夫曼树节点
    public char c;
    public int weight;
    public Node left;
    public Node right;

    //构造方法
    public Node(char c, int weight) {
        this.c = c;
        this.weight = weight;
    }

    public Node(Node left, Node right) {
        this.c = ' ';
        this.weight = left.weight + right.weight;
        this.left = left;
        this.right = right;
    }

    public Node() {

    }


    public static Node creatTree(Node[] nodes) {
        //nodes为排好序的节点数组
        //创建哈夫曼树
        while (nodes.length > 1) {
            Node left = nodes[0];
            Node right = nodes[1];
            Node parent = new Node(left, right);
            Node[] temp = new Node[nodes.length - 1];
            temp[0] = parent;
            for (int i = 2; i < nodes.length; i++) {
                temp[i - 1] = nodes[i];
            }
            nodes = temp;
            sort(nodes);
        }
        return nodes[0];
    }

    public static int getWeightSum(Node root) {
        if (root == null) {
            return 0;
        }
        return (root.weight + getWeightSum(root.left) + getWeightSum(root.right));
    }

    //getHuffmanCode
    public void getHuffmanCode(HashMap<Character, String> huffmanCode, String code) {
        if (this.left == null && this.right == null) {
            huffmanCode.put(this.c, code);
        } else {
            if (this.left != null) {
                this.left.getHuffmanCode(huffmanCode, code + "0");
            }
            if (this.right != null) {
                this.right.getHuffmanCode(huffmanCode, code + "1");
            }
        }
    }

    @Override
    public int compareTo(Node o) {
        return this.weight - o.weight;
    }
}
