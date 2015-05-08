package qa.distantsupervision;

import java.io.FileNotFoundException;
import qa.ProcessFrameProcessor;

/**
 *
 * @author samuellouvan
 */
public class DistantSupervisionLabeler {
    private ProcessFrameProcessor proc; // To load all the data from process frame data
    private String processFrameFilename;
    private String corpusFile;

    public DistantSupervisionLabeler(String processFrameFilename, String corpusFile) {
        this.processFrameFilename = processFrameFilename;
        this.corpusFile = corpusFile;
    }
    
    public void init() throws FileNotFoundException
    {
        proc = new ProcessFrameProcessor(this.processFrameFilename);
        proc.loadProcessData();
    }
    
    
    
    public static void main(String[] args)
    {
        
    }
}
