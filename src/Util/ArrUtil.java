/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

/**
 *
 * @author samuellouvan
 */
public class ArrUtil {
    public static int getIdxMax(double[] arr)
    {
        double max = Double.MIN_VALUE;
        int idxMax = 0;
        for (int i = 0; i < arr.length; i++)
        {
            if (arr[i] > max)
            {
                max = arr[i];
                idxMax = i;
            }
        }
        
        return idxMax;
    }
}
