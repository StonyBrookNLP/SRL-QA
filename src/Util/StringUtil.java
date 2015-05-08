/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author samuellouvan
 */
public class StringUtil {
    
    public static String[] getTokenAsArr(String str, String separator)
    {
        return str.split(separator);
    }
    
    public static ArrayList<String> getTokenAsList(String str, String separator)
    {
        String strs [] = str.split(separator);
        ArrayList<String> results = new ArrayList<String>();
        
        for (String s :strs)
            results.add(s.trim());
        
        return results;
    }
    
    public static boolean contains(String str, String[] strArr)
    {
        for (String s : strArr)
        {
            if(s.trim().equalsIgnoreCase(str))
                return true;
        }
        
        return false;
    }
    
    public static boolean contains(String str, String regex)
    {
        return true;
    }
    
    public ArrayList<String> getMatch(List<String> tokens, ArrayList<String>targets)
    {
        for (int i = 0; i < targets.size(); i++)
        {
            for (int j = 0; j < tokens.size(); j++)
            {
                
            }
        }
    }
    
    public static void main(String[] args)
    {
        System.out.println(Arrays.toString(getTokenAsArr("absorption | absorp", "\\|")));
    }
    
    
}
