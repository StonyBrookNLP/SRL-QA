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
    public static boolean isHeader(String line) {
        String fields[] = line.split("\t");
        if (fields[0].equalsIgnoreCase("process") && fields[1].equalsIgnoreCase("undergoer")) {
            return true;
        }
        return false;
    }
    public static boolean contains(String str, String[] strArr)
    {
        for (String s : strArr)
        {
            if(s.trim().equalsIgnoreCase(str) || s.replaceAll("\\s+", "").equalsIgnoreCase(str))
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
    
    public static boolean isCapitalWord(String word)
    {
        for (int i = 0; i < word.length(); i++)
        {
            if (!Character.isUpperCase(word.charAt(i)))
                return false;
        }
        
        return true;
    }
    
    public static boolean isValidSentence(String sent, List<String> tokens)
    {
        int cnt = 0;
        for (int i = 0; i < sent.length(); i++)
        {
            if (sent.charAt(i) == '.')
                cnt++;
        }
        if (cnt >  1 || cnt == 0)
            return false;
        
        if (sent.contains("?") || sent.contains("_") || sent.contains("<")|| sent.toLowerCase().contains("condensation")
            || sent.toLowerCase().contains("melting") || sent.toLowerCase().contains("freezing"))
            return false;
        
        if (sent.length() > 135)
            return false;
        if (sent.length() < 20)
            return false;
        int cntNom = 0;
        for (String token : tokens)
        {
            if (isCapitalWord(token))
                return false;
            if (token.endsWith("tion"))
                cntNom++;
            if (cntNom > 1)
                return false;
        }
        return true;
    }


    public static String getTokensWithSeparator(ArrayList<String> arr, String separator)
    {
        StringBuilder sb = new StringBuilder();
        for (String s : arr)
        {
            sb.append(s.trim());
            sb.append("|");
        }
        String str = sb.toString();
        return str.substring(0,str.length()-1);
    }

    public static void main(String[] args)
    {
        System.out.println(Arrays.toString(getTokenAsArr("absorption | absorp", "\\|")));
    }
    
    
}
