import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

public class PositionalMapper extends Mapper<Object, Text, Text, Text> {
    private Text term = new Text();
    private Text position = new Text();
    private int positionCount;
    private String docId;  
    @Override
    public void setup(Context context) {
       
        FileSplit fileSplit = (FileSplit) context.getInputSplit();
        Path filePath = fileSplit.getPath();
        docId = filePath.getName(); 
    }
    @Override
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] words = line.split("\\s+"); 
        positionCount = 0;
        for (String word : words) {
            term.set(word);  
            position.set(docId + ":" + positionCount);  
            context.write(term, position);  
            positionCount++;
        }
    }
}