/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import qa.ProcessFrame;
import qa.ProcessFrameProcessor;
/**
 *
 * @author samuellouvan
 */
public class ProcessFrameUtil {
    
    public static ProcessFrame createProcessFrame(String processName, ArrayList<String> undergoer, ArrayList<String> enabler, ArrayList<String> trigger,
                                                  ArrayList<String> result, String sentence)
    {
        ProcessFrame frame = new ProcessFrame();
        frame.setProcessName(processName);
        if (!undergoer.isEmpty())
            frame.setUnderGoer(StringUtil.getTokensWithSeparator(undergoer,ProcessFrameProcessor.SEPARATOR));
        else
            frame.setUnderGoer("");
        if (!enabler.isEmpty())
            frame.setEnabler(StringUtil.getTokensWithSeparator(enabler,ProcessFrameProcessor.SEPARATOR));
        else
            frame.setEnabler("");
        if (!trigger.isEmpty())
            frame.setTrigger(StringUtil.getTokensWithSeparator(trigger,ProcessFrameProcessor.SEPARATOR));
        else 
            frame.setTrigger("");
        if (!result.isEmpty())
            frame.setResult(StringUtil.getTokensWithSeparator(result,ProcessFrameProcessor.SEPARATOR));
        else
            frame.setResult("");
        frame.setUnderSpecified("");
        frame.setRawText(sentence);
        return frame;
    }
    
    public static void dumpFramesToFile(ArrayList<ProcessFrame> arr, String fileName) throws FileNotFoundException
    {
        PrintWriter writer = new PrintWriter(fileName);
        for (ProcessFrame p : arr)
        {
            writer.println(p.getProcessName()+"\t"+p.getUnderGoer()+"\t"+p.getEnabler()+"\t"+p.getTrigger()+"\t"+p.getResult()+"\t"+p.getUnderSpecified()+"\t"+p.getRawText());
        }
        writer.close();
        
    }
}
