package com.asiainfo.algorithm;

import java.util.ArrayList;
import java.util.List;

/**   
 * Longest Common Substring 最长公共子字符串
 * 
 * @author chenzq  
 * @date 2019年4月9日 下午2:43:46
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class LCSDP {

    public static void main(String[] args) {
        
        int[][] matrix = getMatrix("abab", "baba");
        printMatrix(matrix);
        System.out.println("maxlength=" + getMaxLength(matrix));
        for (String lcs : getAllLCS("abab", matrix)) {
            System.out.println(lcs);
        }
    }
    
    public static int[][] getMatrix(String str1, String str2) {
        
        int[][] matrix = new int[str1.length()][str2.length()];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (str1.charAt(i) == str2.charAt(j)) {
                    matrix[i][j] = (i > 0 && j > 0) ? (matrix[i - 1][j -1] + 1) : 1;
                }
            }
        }
        return matrix;
    }
    
    public static int getMaxLength(int[][] matrix) {
        int max = 0;
        for (int[] row : matrix) {
            for (int item : row) {
                if (item > max) {
                    max = item;
                }
            }
        }
        return max;
    }
    
    public static List<String> getAllLCS(String str1, int[][] matrix) {
        List<String> list = new ArrayList<>();
        List<Cell> cells = new ArrayList<>();
        int max = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] > max) {
                    max = matrix[i][j];
                    cells.clear();
                    cells.add(new Cell(i, j));
                } else if (matrix[i][j] == max) {
                    cells.add(new Cell(i, j));
                }
            }
        }
        for (Cell cell : cells) {
            StringBuilder sb = new StringBuilder();
            for (int i = cell.row, j = cell.col; i >= 0 && j >=0 && matrix[i][j] > 0; i--, j--) {
                sb.append(str1.charAt(i));
            }
            list.add(sb.reverse().toString());
        }
        return list;
    }

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
    
    static class Cell {
        int row;
        int col;
        public Cell(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }
}
