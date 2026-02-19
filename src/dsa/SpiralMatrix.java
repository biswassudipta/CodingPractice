package dsa;

import java.util.ArrayList;
import java.util.List;

public class SpiralMatrix {
    public void spiralOrder(int[][] matrix) {
        List<Integer> result=new ArrayList<>();
        int top = 0;
        int bottom = matrix.length;
        int left = 0;
        int right = matrix[0].length;
        int i=0;
        int j=0;
        while(top < bottom && left < right) {
            if(j<right) {
                while (j < right) {
                    result.add(matrix[i][j]);
                    j++;

                }
                j--;
                top++;
                i++;
            }else{
                break;
            }
            if(i<bottom) {
                while (i < bottom) {
                    result.add(matrix[i][j]);
                    i++;
                }
                i--;
                right--;
                j--;
            }else{
                break;
            }
            if(j>=left) {
                while (j >= left) {
                    result.add(matrix[i][j]);
                    j--;
                }
                j++;
                bottom--;
                i--;
            }else{
                break;
            }
            if(i>=top) {
                while (i >= top) {
                    result.add(matrix[i][j]);
                    i--;
                }
                i++;
                left++;
                j++;
            }else{
                break;
            }

        }


        for(int m=1;m<=result.size();m++){
            System.out.print(result.get(m-1)+" ");
            if((m%matrix[0].length)==0){
                System.out.println();
            }
        }
    }
}
