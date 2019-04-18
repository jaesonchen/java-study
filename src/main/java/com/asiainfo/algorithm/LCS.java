package com.asiainfo.algorithm;

import com.asiainfo.datastructure.ArrayList;
import com.asiainfo.datastructure.ArrayStack;
import com.asiainfo.datastructure.List;
import com.asiainfo.datastructure.Stack;

/**
 * 最长公共子序列（LCS: Longest Common Subsequence）是一个在一个序列集合中（通常为两个序列）用来查找所有序列中最长子序列的问题。
 * 一个数列 ，如果分别是两个或多个已知数列的子序列，且是所有符合此条件序列中最长的，则称为已知序列的最长公共子序列。
 * 
 * @author       zq
 * @date         2018年1月2日  下午4:06:25
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class LCS {

    public static void main(String[] args) {

        String str1 = "abcdefgh", str2 = "badfxgahn";
        Matrix matrix = lcs(str1, str2);
        printMatrix(matrix.getMatrix());
        printMatrix(matrix.getAccessMatrix());
        
        System.out.println(String.format("lcs=%d, sequence=%s", 
                matrix.getMatrix()[str1.length()][str2.length()], 
                getSequence(str1, str2, matrix.accessMatrix)));
        
        List<String> list = getAllSequence(str1, str2, matrix);
        System.out.println(String.format("all sequence=%s", list.size()));
        for (String str : list) {
            System.out.println(new StringBuilder(str).reverse().toString());
        }
    }

    /**
     * 矩阵大小：matrix[str1.length() + 1][str2.length() + 1]
     * 矩阵填充, matrix[str1.length][str2.length]的值即为lcs的长度
     * accessFlag: 0=left up, 1=left, 2=up, 3=left or up
     * 
     * @param str1
     * @param str2
     * @return
     */
    public static Matrix lcs(String str1, String str2) {
        
        int[][] matrix = new int[str1.length() + 1][str2.length() + 1];
        Arrow[][] accessMatrix = new Arrow[str1.length()][str2.length()];
        //填充矩阵
        for (int i = 1; i <= str1.length(); i++) {
            for (int j = 1; j <= str2.length(); j++) {
                //如果横竖（i,j）对应的两个元素相等，该格子的值matrix[i, j] = matrix[i - 1][j - 1] + 1
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    matrix[i][j] = matrix[i - 1][j - 1] + 1;
                    accessMatrix[i - 1][j - 1] = Arrow.LEFT_UP;
                } else {
                    //如果不等，取matrix[i - 1][j] 和 matrix[i][j - 1]的最大值
                    matrix[i][j] = (matrix[i - 1][j] >= matrix[i][j - 1]) ? matrix[i - 1][j] : matrix[i][j - 1];
                    accessMatrix[i - 1][j - 1] = (matrix[i - 1][j] == matrix[i][j - 1]) ? Arrow.LEFT_OR_UP : 
                        (matrix[i - 1][j] > matrix[i][j - 1]) ? Arrow.UP : Arrow.LEFT;
                }
            }
        }
        return new Matrix(matrix, accessMatrix);
    }
    
    /**
     * 取矩阵的lcs序列
     * 
     * @param str1
     * @param str2
     * @param accessMatrix
     * @return
     */
    public static String getSequence(String str1, String str2, Arrow[][] accessMatrix) {
        
        Stack<Character> stack = new ArrayStack<>();
        nextStep(str1, accessMatrix, str1.length() - 1, str2.length() - 1, stack);
        StringBuilder result = new StringBuilder();
        while (!stack.isEmpty()) {
            result.append(stack.pop());
        }
        return result.toString();
    }
    
    /**
     * lcs 子序列
     * 
     * @param str1
     * @param matrix
     * @param i
     * @param j
     * @param stack
     */
    protected static void nextStep(String str1, Arrow[][] accessMatrix, int i, int j, Stack<Character> stack) {
        
        if (i < 0 || j < 0) {
            return;
        }
        //left up
        if (Arrow.LEFT_UP == accessMatrix[i][j]) {
            stack.push(str1.charAt(i));
            nextStep(str1, accessMatrix, i - 1, j - 1, stack);
        } else {
            //left or up 选择up 
            nextStep(str1, accessMatrix, (Arrow.LEFT == accessMatrix[i][j]) ? i : (i - 1), 
                    (Arrow.LEFT == accessMatrix[i][j]) ? (j - 1) : j, stack);
        }
    }
    
    /**
     * 所有子序列
     * 
     * @param str1
     * @param str2
     * @param matrix
     * @return
     */
    public static List<String> getAllSequence(String str1, String str2, Matrix matrix) {
        
        List<String> list = getAllSequence(str1, matrix.getAccessMatrix(), str1.length() - 1, str2.length() - 1, new ArrayList<>());
        List<String> result = new ArrayList<>();
        for (String str : list) {
            if (!result.contains(str)) {
                result.add(str);
            }
        }
        return result;
    }
    
    /**
     * 所有子序列
     * 
     * @param str1
     * @param accessMatrix
     * @param i
     * @param j
     * @param list
     * @return
     */
    protected static List<String> getAllSequence(String str1, Arrow[][] accessMatrix, int i, int j, List<String> list) {
        
        if (i < 0 || j < 0) {
            return list;
        }
        //left up
        if (Arrow.LEFT_UP == accessMatrix[i][j]) {
            return getAllSequence(str1, accessMatrix, i - 1, j - 1, addCharacter(list, str1.charAt(i)));
        } else if (Arrow.LEFT == accessMatrix[i][j]) {
            return getAllSequence(str1, accessMatrix, i, j - 1, list);
        } else if (Arrow.UP == accessMatrix[i][j]) {
            return getAllSequence(str1, accessMatrix, i - 1, j, list);
        } else {
            List<String> copy = new ArrayList<String>();
            copy.addAll(list);
            // left
            List<String> result = getAllSequence(str1, accessMatrix, i, j - 1, list);
            // up
            result.addAll(getAllSequence(str1, accessMatrix, i - 1, j, copy));
            return result;
        }
    }
    
    /**
     * 记录一个字符
     * 
     * @param list
     * @param c
     * @return
     */
    protected static List<String> addCharacter(List<String> list, Character c) {
        
        if (list.isEmpty()) {
            list.add(String.valueOf(c));
            return list;
        }
        List<String> result = new ArrayList<>();
        for (String str : list) {
            result.add(str + c);
        }
        return result;
    }
    
    /**
     * 打印矩阵
     * 
     * @param matrix
     */
    protected static void printMatrix(int[][] matrix) {
        
        System.out.println("==========matrix=========");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println("==========matrix=========");
    }
    
    protected static <T> void printMatrix(T[][] matrix) {
        
        System.out.println("==========accessMatrix=========");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println("==========accessMatrix=========");
    }
    
    static class Matrix {
        
        int[][] matrix;
        Arrow[][] accessMatrix;
        
        public Matrix(int[][] matrix, Arrow[][] accessMatrix) {
            this.matrix = matrix;
            this.accessMatrix = accessMatrix;
        }
        public int[][] getMatrix() {
            return matrix;
        }
        public Arrow[][] getAccessMatrix() {
            return accessMatrix;
        }
    }
    
    enum Arrow {
        
        LEFT_UP("0"), LEFT("1"), UP("2"), LEFT_OR_UP("3");
        
        private String symbol;
        private Arrow(String symbol) {
            this.symbol = symbol;
        }
        public String getSymbol() {
            return symbol;
        }
        @Override public String toString() {
            return this.symbol;
        }
    }
}
