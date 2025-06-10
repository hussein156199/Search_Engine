import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PositionalReducer extends Reducer<Text, Text, Text, Text> {
    private Text result = new Text();

    @Override
    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
       
        Map<String, ArrayList<Integer>> docPositions = new HashMap<>();
       
        for (Text value : values) {
            String[] docIdAndPosition = value.toString().split(":");
            String docId = docIdAndPosition[0];
            int position = Integer.parseInt(docIdAndPosition[1]);
           
            if (!docPositions.containsKey(docId)) {
                docPositions.put(docId, new ArrayList<Integer>());  
            }
            docPositions.get(docId).add(position);  
        }
       
        StringBuilder resultStr = new StringBuilder();
        for (Map.Entry<String, ArrayList<Integer>> entry : docPositions.entrySet()) {
            resultStr.append(entry.getKey()).append(": ");
            for (int pos : entry.getValue()) {
                resultStr.append(pos).append(", ");
            }
            resultStr.deleteCharAt(resultStr.length() - 2);  
            resultStr.append(" ; ");
        } result.set(resultStr.toString());
        context.write(key, result); 
    }
}