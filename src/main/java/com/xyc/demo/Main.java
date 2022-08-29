package com.xyc.demo;

import java.util.LinkedList;
import java.util.Objects;

public class Main {
    public static class Node{
        public int value;
        public Node left;
        public Node right;
    }
    public static void main(String[] args) {

    }

    private static void print(Node head){
        LinkedList<Node> cache = new LinkedList<>();
        cache.addLast(head);
        while (!cache.isEmpty()){
            int size = cache.size();
            for (int index = 0;index<size;index++){
                Node node = cache.removeFirst();
                System.out.println(node.value);
                if (Objects.nonNull(head.left)){
                    cache.addLast(head.left);
                }
                if (Objects.nonNull(head.right)){
                    cache.addLast(head.right);
                }
            }
        }
    }
}
