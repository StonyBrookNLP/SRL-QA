/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author samuellouvan
 */
public class StringUtil {
    int THRESHOLD = 3;
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
    
    public static ArrayList<String> getMatch(List<String> tokens, ArrayList<String>targets)
    {
        ArrayList<String> results = new ArrayList<String>();
        Set<String> targetSet = new HashSet<String>(targets);
        for (String s : targetSet)
        {
            for (int j = 0; j < tokens.size(); j++)
            {
                if (StringUtils.getLevenshteinDistance(s, tokens.get(j)) < 0.3 * s.length() ) 
                    if (!results.contains(tokens.get(j)))
                        results.add(tokens.get(j));
            }
        }
        
        return results;
    }
    
    public static void main(String[] args)
    {
        System.out.println(Arrays.toString(getTokenAsArr("absorption | absorp", "\\|")));
    }
    
    
}
