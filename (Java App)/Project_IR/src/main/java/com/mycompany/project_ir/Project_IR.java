package com.mycompany.project_ir;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class Project_IR 
{
 

//=============================================((((((Functions))))))====================================================================   
   
//((((((((((((((((((((((((((1)))))))))))))))))))))
   //Display (Positional_Index)
    
    public static void Positional_Index(String filePath) 
    {
       
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) 
        {
            String line;
            
            while ((line = reader.readLine()) != null) 
            {
                System.out.println(line);  
            }
        } catch (IOException e) 
        {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }

   
    //=====================================================================================
    //((((((((((((((((((((((((((((((((2)))))))))))))))))))))))))))))
    //Display TF_Matrix
    
    public static void print_TF_Matrix(String filePath) 
    {
        
        Map<String, Map<String, Integer>> termFrequencies = new LinkedHashMap<>();

        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) 
        {
            String line;
            while ((line = reader.readLine()) != null) 
            {
                
                String[] parts = line.split("\t");
                if (parts.length < 2) continue;

                String term = parts[0].trim();
                String docData = parts[1].trim();

                
                String[] docEntries = docData.split(";");
                Map<String, Integer> docFreqs = new HashMap<>();
                for (String entry : docEntries) 
                {
                    String[] docParts = entry.split(":");
                    if (docParts.length < 2) continue;

                    String docId = docParts[0].trim();
                    int frequency = docParts[1].trim().split(" ").length; // Count positions

                    docFreqs.put(docId, frequency > 0 ? 1 : 0); // Binary frequency (presence = 1)
                }

                
                termFrequencies.put(term, docFreqs);
            }
        } catch (IOException e) 
        {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        
        List<String> allDocs = Arrays.asList("doc1", "doc2", "doc3", "doc4", "doc5", "doc6", "doc7", "doc8", "doc9", "doc10");

        // Print header
        System.out.print("Term");
        for (String doc : allDocs) 
        {
            System.out.print("\t" + doc);
        }
        System.out.println();

        
        for (Map.Entry<String, Map<String, Integer>> entry : termFrequencies.entrySet()) 
        {
            String term = entry.getKey();
            Map<String, Integer> docFreqs = entry.getValue();

           
            System.out.print(term); 
            for (String doc : allDocs) 
            {
                System.out.print("\t" + docFreqs.getOrDefault(doc, 0));
            }
            System.out.println(); 
        }
    }
    
    //=========================================================================================
    //((((((((((((((((((((((((((((((((((((((3))))))))))))))))))))))))))))))))))))))))))
    //Display WTF_Matrix (1+ log tf)
    
      public static void print_WTF_Matrix(String filePath) 
      {
        
       
        Map<String, Map<String, Integer>> termFrequencies = new LinkedHashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) 
        {
            String line;
            while ((line = reader.readLine()) != null)
            {

                String[] parts = line.split("\t");
                if (parts.length < 2) continue;

                String term = parts[0].trim();
                String docData = parts[1].trim();

               
                String[] docEntries = docData.split(";");
                Map<String, Integer> docFreqs = new HashMap<>();
               
                for (String entry : docEntries) 
                {
                    String[] docParts = entry.split(":");
                    if (docParts.length < 2) continue;

                    String docId = docParts[0].trim();
                    int frequency = docParts[1].trim().split(" ").length; 

                    docFreqs.put(docId, frequency);
                }

                
                termFrequencies.put(term, docFreqs);
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

       
        List<String> allDocs = Arrays.asList("doc1", "doc2", "doc3", "doc4", "doc5", "doc6", "doc7", "doc8", "doc9", "doc10");

       
        System.out.print("Term");
        for (String doc : allDocs) 
        {
            System.out.print("\t" + doc);
        }
        System.out.println();

       
        for (Map.Entry<String, Map<String, Integer>> entry : termFrequencies.entrySet()) 
        {
            String term = entry.getKey();
            Map<String, Integer> docFreqs = entry.getValue();

            System.out.print(term); 

            // Calculate and print the weighted TF (1 + log10(tf)) for each document
            for (String doc : allDocs) 
            {
                int tf = docFreqs.getOrDefault(doc, 0); 
                
                // Calculate the weighted TF using the formula: 1 + log10(tf)
                double weightedTF = tf > 0 ? 1 + Math.log10(tf) : 0;  
                System.out.print("\t" + String.format("%.1f", weightedTF)); 
            }
            System.out.println(); 
        }
    }
  //================================================================================
  //==========================((((4))))))===========================================
 //Display DF_IDF matrix (log N\ df) 
      
 public static void printDF_IDF(String filePath) 
 {
        Map<String, Set<String>> termDocMap = new HashMap<>();
        int totalDocuments = 10; 

       
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) 
        {
            String line;
            while ((line = reader.readLine()) != null) 
            {

               
                String[] parts = line.split("\t");
                if (parts.length < 2) continue;

                String docData = parts[1].trim();
                String[] docEntries = docData.split(";");

                
                for (String entry : docEntries) 
                {
                    String[] docParts = entry.split(":");
                    if (docParts.length < 2) continue;

                    String term = parts[0].trim();
                    String docId = docParts[0].trim();

                
                    termDocMap.putIfAbsent(term, new HashSet<>());
                    termDocMap.get(term).add(docId); 
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Print DF and IDF results
        System.out.println("Term\tDF\tIDF");

        
        for (Map.Entry<String, Set<String>> entry : termDocMap.entrySet()) 
        {
            String term = entry.getKey();
            Set<String> docSet = entry.getValue();

            
            int df = docSet.size();

           
            double idf = Math.log10((double) totalDocuments / df);

          
            System.out.printf("%s\t%d\t%.5f\n", term, df, idf);
        }
    }
    
 //=============================================================================
 //=========================((((5)))))=========================================
 // Display TF _ IDF Matrix
    
 public static void printTFIDFMatrix(String filePath) 
 {
        Map<String, Map<String, List<Integer>>> termDocPositions = new HashMap<>();
        int totalDocuments = 10; 

        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) 
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] parts = line.split("\t");
                if (parts.length < 2) continue;

                String term = parts[0].trim();
                String docData = parts[1].trim();
                String[] docEntries = docData.split(";");

                for (String entry : docEntries) 
                {
                    String[] docParts = entry.split(":");
                    if (docParts.length < 2) continue;

                    String docId = docParts[0].trim();
                    String positions = docParts[1].trim();
                    List<Integer> positionList = new ArrayList<>();

                   
                    String[] positionArray = positions.split(" ");
                    for (String pos : positionArray) 
                    {
                        positionList.add(Integer.valueOf(pos));
                    }

                    
                    termDocPositions.putIfAbsent(term, new HashMap<>());
                    termDocPositions.get(term).put(docId, positionList);
                }
            }
        } catch (IOException e) 
        {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        
        System.out.println("Term\td1\td2\td3\td4\td5\td6\td7\td8\td9\td10");

        List<String> allDocs = Arrays.asList("doc1", "doc2", "doc3", "doc4", "doc5", "doc6", "doc7", "doc8", "doc9", "doc10");

        
        for (Map.Entry<String, Map<String, List<Integer>>> termEntry : termDocPositions.entrySet()) 
        {
            String term = termEntry.getKey();
            Map<String, List<Integer>> docPositions = termEntry.getValue();

           
            int df = docPositions.size();
            double idf = Math.log10((double) totalDocuments / df);

          
            Map<String, Integer> termFrequencies = new HashMap<>();
            for (String doc : allDocs) 
            {
                termFrequencies.put(doc, 0);
            }

            
            for (Map.Entry<String, List<Integer>> docEntry : docPositions.entrySet()) 
            {
                String docId = docEntry.getKey();
                List<Integer> positions = docEntry.getValue();
                termFrequencies.put(docId, positions.size()); 
            }

            // Print the TF-IDF values for each document
            System.out.print(term); 

            for (String doc : allDocs) 
            {
                int tf = termFrequencies.get(doc);
                double tfidf = tf * idf; // TF-IDF calculation
                System.out.print("\t" + String.format("%.5f", tfidf)); 
            }
            System.out.println(); 
        }
    }
    
 //===========================================================================
 //(((((((((((((((((((((((((((((((((((6))))))))))))))))))))))))))))))))))
 //Display  Document Lengths
 
  public static void printDocumentLengths(String filePath) 
  {
        Map<String, Map<String, List<Integer>>> termDocPositions = new HashMap<>();
        int totalDocuments = 10;  

        // Step 1: Parse the positional index file
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) 
        {
            String line;
            while ((line = reader.readLine()) != null) 
            {
                String[] parts = line.split("\t");
                if (parts.length < 2) continue;

                String term = parts[0].trim();
                String docData = parts[1].trim();
                String[] docEntries = docData.split(";");

                for (String entry : docEntries) 
                {
                    String[] docParts = entry.split(":");
                    if (docParts.length < 2) continue;

                    String docId = docParts[0].trim();
                    String positions = docParts[1].trim();
                    List<Integer> positionList = new ArrayList<>();

                    
                    if (!positions.isEmpty()) 
                    {
                        String[] positionArray = positions.split(" ");
                        for (String pos : positionArray) {
                            positionList.add(Integer.valueOf(pos.trim()));
                        }
                    }

                    
                    termDocPositions.putIfAbsent(term, new HashMap<>());
                    termDocPositions.get(term).put(docId, positionList);
                }
            }
        } catch (IOException e) 
        {
            System.out.println("Error reading the file: " + e.getMessage());
            return;
        }

        
        Map<String, Map<String, Double>> documentTFIDF = new HashMap<>();
        List<String> allDocs = Arrays.asList("doc1", "doc2", "doc3", "doc4", "doc5", "doc6", "doc7", "doc8", "doc9", "doc10");

        for (Map.Entry<String, Map<String, List<Integer>>> termEntry : termDocPositions.entrySet()) {
            String term = termEntry.getKey();
            Map<String, List<Integer>> docPositions = termEntry.getValue();

           
            int df = docPositions.size();
            double idf = Math.log10((double) totalDocuments / df);

            
            Map<String, Integer> termFrequencies = new HashMap<>();
            for (String doc : allDocs) 
            {
                termFrequencies.put(doc, 0);
            }

            for (Map.Entry<String, List<Integer>> docEntry : docPositions.entrySet()) 
            {
                String docId = docEntry.getKey();
                List<Integer> positions = docEntry.getValue();
                termFrequencies.put(docId, positions.size());
            }

            
            for (String doc : allDocs) 
            {
                int tf = termFrequencies.get(doc);
                double tfidf = tf * idf; 

                documentTFIDF.putIfAbsent(doc, new HashMap<>());
                documentTFIDF.get(doc).put(term, tfidf);
            }
        }

        
        System.out.println("Document Lengths:\n");
        for (String doc : allDocs) 
        {
            double length = 0.0;
         
            if (documentTFIDF.containsKey(doc))
            {
                for (double tfidf : documentTFIDF.get(doc).values()) 
                {
                    length += tfidf * tfidf;
                }
            }
            length = Math.sqrt(length); 
            System.out.println(doc + " length " + String.format("%.5f", length));
        }
    }

 //======================================================================================
  //(((((((((((((((((((((((((((((((7))))))))))))))))))))))))))))))))))) 
  // Display Normalized TF _ IDF
  
  public static void printNormalizedTFIDF(String filePath) 
  {
        Map<String, Map<String, List<Integer>>> termDocPositions = new HashMap<>();
        int totalDocuments = 10; 

        // Step 1: Parse the positional index file
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) 
        {
            String line;
            while ((line = reader.readLine()) != null) 
            {
                String[] parts = line.split("\t");
                if (parts.length < 2) continue;

                String term = parts[0].trim();
                String docData = parts[1].trim();
                String[] docEntries = docData.split(";");

                for (String entry : docEntries) 
                {
                    String[] docParts = entry.split(":");
                    if (docParts.length < 2) continue;

                    String docId = docParts[0].trim();
                    String positions = docParts[1].trim();
                    List<Integer> positionList = new ArrayList<>();

                    // Add positions to the list
                    if (!positions.isEmpty())
                    {
                        String[] positionArray = positions.split(" ");
                        for (String pos : positionArray)
                        {
                            positionList.add(Integer.valueOf(pos.trim()));
                        }
                    }

                    // Add the document and its positions to the term-doc map
                    termDocPositions.putIfAbsent(term, new HashMap<>());
                    termDocPositions.get(term).put(docId, positionList);
                }
            }
        } catch (IOException e) 
        {
            System.out.println("Error reading the file: " + e.getMessage());
            return;
        }

        // Calculate TF, IDF, and TF-IDF for each term
        
        Map<String, Map<String, Double>> documentTFIDF = new HashMap<>();
        Map<String, Double> documentLengths = new HashMap<>();
        List<String> allDocs = Arrays.asList("doc1", "doc2", "doc3", "doc4", "doc5", "doc6", "doc7", "doc8", "doc9", "doc10");

        for (Map.Entry<String, Map<String, List<Integer>>> termEntry : termDocPositions.entrySet()) 
        {
            String term = termEntry.getKey();
            Map<String, List<Integer>> docPositions = termEntry.getValue();

            // Document Frequency (DF)
            int df = docPositions.size();
            double idf = Math.log10((double) totalDocuments / df);

            // Prepare TF values for each document
            Map<String, Integer> termFrequencies = new HashMap<>();
            for (String doc : allDocs)
            {
                termFrequencies.put(doc, 0);
            }

            for (Map.Entry<String, List<Integer>> docEntry : docPositions.entrySet()) 
            {
                String docId = docEntry.getKey();
                List<Integer> positions = docEntry.getValue();
                termFrequencies.put(docId, positions.size()); // TF is the count of occurrences
            }

           
            for (String doc : allDocs) 
            {
                int tf = termFrequencies.get(doc);
                double tfidf = tf * idf; // TF-IDF calculation

                documentTFIDF.putIfAbsent(doc, new HashMap<>());
                documentTFIDF.get(doc).put(term, tfidf);
            }
        }

      
        for (String doc : allDocs) {
            double length = 0.0;
            if (documentTFIDF.containsKey(doc))
            {
                for (double tfidf : documentTFIDF.get(doc).values())
                {
                    length += tfidf * tfidf;
                }
            }
            documentLengths.put(doc, Math.sqrt(length)); 
        }

        
        Map<String, Map<String, Double>> normalizedTFIDF = new HashMap<>();
        for (String doc : allDocs) 
        {
            double docLength = documentLengths.getOrDefault(doc, 0.0);
            if (docLength == 0) continue;

            normalizedTFIDF.put(doc, new HashMap<>());
            for (Map.Entry<String, Double> termEntry : documentTFIDF.getOrDefault(doc, new HashMap<>()).entrySet())
            {
                String term = termEntry.getKey();
                double tfidf = termEntry.getValue();
                normalizedTFIDF.get(doc).put(term, tfidf / docLength); 
            }
        }

        // Step 5: Print the normalized TF-IDF matrix
        System.out.println("Normalized TF-IDF Matrix:\n");
        System.out.print("Terms");
        for (int i = 1; i <= totalDocuments; i++) 
        {
            System.out.print("\td" + i);
        }
        System.out.println();

        // Print each term's normalized TF-IDF across all documents
        Set<String> terms = termDocPositions.keySet();
        for (String term : terms) 
        {
            System.out.print(term);
            for (int i = 1; i <= totalDocuments; i++)
            {
                String doc = "doc" + i;
                double value = normalizedTFIDF.getOrDefault(doc, new HashMap<>()).getOrDefault(term, 0.0);
                System.out.print("\t" + String.format("%.5f", value));
            }
            System.out.println();
        }
    }
 
 //=================================================================================================================
  //(((((((((((((((((((((((((((((((((((((8))))))))))))))))))))))))))))))))))))))))))))))))
 // Display Query ( TF-raw     1+log(TF)  TF-IDF     IDF        Normalized ) with query length + Normalized TF-IDF Matrix (Filtered by Query Terms)
 //+Multiplying Normalized Query TF-IDF with Document TF-IDF + Ranked Documents based on similarity:
  
 public static void processAndDisplayResults1(String query, String filename) throws IOException 
   {
        Map<String, Map<String, List<Integer>>> termDocPositions = new HashMap<>();
        int totalDocuments = 10;

        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) 
        {
            String line;
            while ((line = reader.readLine()) != null) 
            {
                String[] parts = line.split("\t");
                if (parts.length < 2) continue;

                String term = parts[0].trim();
                String docData = parts[1].trim();
                String[] docEntries = docData.split(";");

                for (String entry : docEntries) 
                {
                    String[] docParts = entry.split(":");
                    if (docParts.length < 2) continue;

                    String docId = docParts[0].trim();
                    String positions = docParts[1].trim();
                    List<Integer> positionList = new ArrayList<>();

                    
                    if (!positions.isEmpty()) 
                    {
                        String[] positionArray = positions.split(" ");
                        for (String pos : positionArray) 
                        {
                            positionList.add(Integer.valueOf(pos.trim()));
                        }
                    }

                   
                    termDocPositions.putIfAbsent(term, new HashMap<>());
                    termDocPositions.get(term).put(docId, positionList);
                }
            }
        } catch (IOException e) 
        {
            System.out.println("Error reading the file: " + e.getMessage());
            return;
        }

        // Compute TF, IDF, and TF-IDF for the query
        String[] queryTerms = query.split(" ");
        Map<String, Double> queryTF = new HashMap<>();
        for (String term : queryTerms) 
        {
            queryTF.put(term, queryTF.getOrDefault(term, 0.0) + 1);
        }

        // Compute TF, IDF, and TF-IDF for query
        Map<String, Double> queryTFIDF = new HashMap<>();
        double queryLengthSquared = 0.0;
        for (String term : queryTF.keySet()) 
        {
            double tf = 1 + Math.log10(queryTF.get(term)); // TF for the query
            double idf = Math.log10(10.0 / (termDocPositions.containsKey(term) ? termDocPositions.get(term).size() : 1)); // IDF for query term
            double tfidf = tf * idf;
            queryTFIDF.put(term, tfidf);
            queryLengthSquared += tfidf * tfidf;
        }

        double queryLength = Math.sqrt(queryLengthSquared);
        for (String term : queryTFIDF.keySet()) 
        {
            queryTFIDF.put(term, queryTFIDF.get(term) / queryLength);
        }

        // Display the query and its components
        System.out.println("\n\n============ After inserting queries ==========\n");
        System.out.println("------- (((( Query )))) -------\n");
        System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s%n", "Query", "TF-raw", "1+log(TF)", "TF-IDF", "IDF", "Normalized");

        for (String term : queryTFIDF.keySet()) {
            double tf = queryTF.get(term); // TF for the query
            double logTf = 1 + Math.log10(tf);
            double idf = Math.log10(10.0 / (termDocPositions.containsKey(term) ? termDocPositions.get(term).size() : 1)); // IDF for query term
            double tfIdf = tf * idf;
            double normalized = queryTFIDF.get(term); 
            System.out.printf("%-10s %-10.2f %-10.4f %-10.4f %-10.4f %-10.4f%n", term, tf, logTf, tfIdf, idf, normalized);
        }

        System.out.println("\nquery length: " + String.format("%.6f", queryLength));  
        System.out.println("\n-------------------------------------------------");
        Map<String, Map<String, Double>> documentTFIDF = new HashMap<>();
        Map<String, Double> documentLengths = new HashMap<>();
        List<String> allDocs = Arrays.asList("doc1", "doc2", "doc3", "doc4", "doc5", "doc6", "doc7", "doc8", "doc9", "doc10");

       
        for (Map.Entry<String, Map<String, List<Integer>>> termEntry : termDocPositions.entrySet()) {
            String term = termEntry.getKey();
            Map<String, List<Integer>> docPositions = termEntry.getValue();

          
            int df = docPositions.size();
            double idf = Math.log10((double) totalDocuments / df);

          
            Map<String, Integer> termFrequencies = new HashMap<>();
            for (String doc : allDocs) {
                termFrequencies.put(doc, 0);
            }

            for (Map.Entry<String, List<Integer>> docEntry : docPositions.entrySet()) 
            {
                String docId = docEntry.getKey();
                List<Integer> positions = docEntry.getValue();
                termFrequencies.put(docId, positions.size()); 
            }

            for (String doc : allDocs) 
            {
                int tf = termFrequencies.get(doc);
                double tfidf = tf * idf; // TF-IDF calculation

                documentTFIDF.putIfAbsent(doc, new HashMap<>());
                documentTFIDF.get(doc).put(term, tfidf);
            }
        }

        // Calculate the length of each document
        for (String doc : allDocs) 
        {
            double length = 0.0;
            if (documentTFIDF.containsKey(doc)) 
            {
                for (double tfidf : documentTFIDF.get(doc).values()) 
                {
                    length += tfidf * tfidf;
                }
            }
            documentLengths.put(doc, Math.sqrt(length));
        }

        // Normalize TF-IDF
        Map<String, Map<String, Double>> normalizedTFIDF = new HashMap<>();
        for (String doc : allDocs) 
        {
            double docLength = documentLengths.getOrDefault(doc, 0.0);
            if (docLength == 0) continue;

            normalizedTFIDF.put(doc, new HashMap<>());
            for (Map.Entry<String, Double> termEntry : documentTFIDF.getOrDefault(doc, new HashMap<>()).entrySet()) 
            {
                String term = termEntry.getKey();
                double tfidf = termEntry.getValue();
                normalizedTFIDF.get(doc).put(term, tfidf / docLength);
            }
        }

        // Filter by query terms
        Set<String> queryTermSet = new HashSet<>(Arrays.asList(query.split("\\s+")));
        
        System.out.println("Normalized TF-IDF Matrix (Filtered by Query Terms):\n");

        // First, find documents that match the query terms
        Set<String> matchingDocs = new HashSet<>();
        for (String term : queryTermSet) 
        {
            if (termDocPositions.containsKey(term)) 
            {
                for (String doc : allDocs) 
                {
                    double tfidfValue = normalizedTFIDF.getOrDefault(doc, new HashMap<>()).getOrDefault(term, 0.0);
                    if (tfidfValue > 0.0) 
                    {
                        matchingDocs.add(doc);
                    }
                }
            }
        }

       
        if (matchingDocs.isEmpty()) 
        {
            System.out.println("No documents match the query terms.");
            return;
        }

        
        System.out.print("Terms");
        for (String doc : matchingDocs) 
        {
            System.out.print("\t" + doc);
        }
        System.out.println();

      
        for (String term : queryTermSet) 
        {
            if (termDocPositions.containsKey(term)) 
            {
                System.out.print(term);
                for (String doc : matchingDocs) 
                {
                    double value = normalizedTFIDF.getOrDefault(doc, new HashMap<>()).getOrDefault(term, 0.0);
                    System.out.print("\t" + String.format("%.5f", value));
                }
                System.out.println();
            }
        }

        // Multiply query normalized TF-IDF with document normalized TF-IDF and display
        System.out.println("\n-------------------------------------------------");
        System.out.println("Multiplying Normalized Query TF-IDF with Document TF-IDF:");
        System.out.println("((-----------------Similarity--------------------))\n");
        for (String doc : matchingDocs) {
            double dotProduct = 0.0;
            for (String term : queryTermSet) {
                double queryNorm = queryTFIDF.getOrDefault(term, 0.0);
                double docNorm = normalizedTFIDF.getOrDefault(doc, new HashMap<>()).getOrDefault(term, 0.0);
                dotProduct += queryNorm * docNorm;
            }

           
            System.out.println("Doc: " + doc + " |  similarity : " + String.format("%.5f", dotProduct));
        }
        Map<String, Double> docDotProductMap = new HashMap<>();

   
    for (String doc : matchingDocs) 
    {
      double dotProduct = 0.0;
       for (String term : queryTermSet)  
       {
        double queryNorm = queryTFIDF.getOrDefault(term, 0.0);
        double docNorm = normalizedTFIDF.getOrDefault(doc, new HashMap<>()).getOrDefault(term, 0.0);
        dotProduct += queryNorm * docNorm;
       }
        
        docDotProductMap.put(doc, dotProduct);
    }

          
           List<Map.Entry<String, Double>> sortedDocs = new ArrayList<>(docDotProductMap.entrySet());
           sortedDocs.sort((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()));

             // Display the ranked documents
            System.out.println("\n-------------------------------------------------");
            System.out.println("Ranked Documents based on similarity:\n");

            
            System.out.printf("%-10s %-10s%n", "Rank", "Document");

             // Print the ranked documents
             int rank = 1;
             for (Map.Entry<String, Double> entry : sortedDocs) 
             {
                   String doc = entry.getKey();
                   double dotProduct = entry.getValue();
                   System.out.printf("%-10d %-10s |  similarity : %.5f%n", rank, doc, dotProduct);
                   rank++;
             }
        
  }
  

//==========================================================================================================
// Function Display part of Normalized TF-IDF Matrix (Filtered by Query Terms) +  Multiplying Normalized Query TF-IDF with Document TF-IDF Matrix:
//product (query * matched docs)
   
 public static void processAndDisplayResults2(String query, String filename) throws IOException 
 {
    Map<String, Map<String, List<Integer>>> termDocPositions = new HashMap<>();
    int totalDocuments = 10;

  
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) 
    {
        String line;
        while ((line = reader.readLine()) != null)
        {
            String[] parts = line.split("\t");
            if (parts.length < 2) continue;

            String term = parts[0].trim();
            String docData = parts[1].trim();
            String[] docEntries = docData.split(";");

            for (String entry : docEntries) 
            {
                String[] docParts = entry.split(":");
                if (docParts.length < 2) continue;

                String docId = docParts[0].trim();
                String positions = docParts[1].trim();
                List<Integer> positionList = new ArrayList<>();

                // Add positions to the list
                if (!positions.isEmpty()) 
                {
                    String[] positionArray = positions.split(" ");
                    for (String pos : positionArray) 
                    {
                        positionList.add(Integer.valueOf(pos.trim()));
                    }
                }

              
                termDocPositions.putIfAbsent(term, new HashMap<>());
                termDocPositions.get(term).put(docId, positionList);
            }
        }
    } catch (IOException e) 
    {
        System.out.println("Error reading the file: " + e.getMessage());
        return;
    }

    // Compute TF, IDF, and TF-IDF for the query
    String[] queryTerms = query.split(" ");
    Map<String, Double> queryTF = new HashMap<>();
    for (String term : queryTerms) 
    {
        queryTF.put(term, queryTF.getOrDefault(term, 0.0) + 1);
    }

    // Compute TF, IDF, and TF-IDF for query
    Map<String, Double> queryTFIDF = new HashMap<>();
    double queryLengthSquared = 0.0;
    for (String term : queryTF.keySet())
    {
        double tf = 1 + Math.log10(queryTF.get(term));
        double idf = Math.log10(10.0 / (termDocPositions.containsKey(term) ? termDocPositions.get(term).size() : 1)); // IDF for query term
        double tfidf = tf * idf;
        queryTFIDF.put(term, tfidf);
        queryLengthSquared += tfidf * tfidf;
    }

    double queryLength = Math.sqrt(queryLengthSquared);
    for (String term : queryTFIDF.keySet())
    {
        queryTFIDF.put(term, queryTFIDF.get(term) / queryLength);
    }

   
    Map<String, Map<String, Double>> documentTFIDF = new HashMap<>();
    Map<String, Double> documentLengths = new HashMap<>();
    List<String> allDocs = Arrays.asList("doc1", "doc2", "doc3", "doc4", "doc5", "doc6", "doc7", "doc8", "doc9", "doc10");

    // Calculate TF, IDF, and TF-IDF for each term in the document corpus
    for (Map.Entry<String, Map<String, List<Integer>>> termEntry : termDocPositions.entrySet()) 
    {
        String term = termEntry.getKey();
        Map<String, List<Integer>> docPositions = termEntry.getValue();

        
        int df = docPositions.size();
        double idf = Math.log10((double) totalDocuments / df);

        
        Map<String, Integer> termFrequencies = new HashMap<>();
        for (String doc : allDocs) 
        {
            termFrequencies.put(doc, 0);
        }

        for (Map.Entry<String, List<Integer>> docEntry : docPositions.entrySet()) 
        {
            String docId = docEntry.getKey();
            List<Integer> positions = docEntry.getValue();
            termFrequencies.put(docId, positions.size());
        }

        for (String doc : allDocs) {
            int tf = termFrequencies.get(doc);
            double tfidf = tf * idf; 

            documentTFIDF.putIfAbsent(doc, new HashMap<>());
            documentTFIDF.get(doc).put(term, tfidf);
        }
    }

   
    for (String doc : allDocs) 
    {
        double length = 0.0;
        if (documentTFIDF.containsKey(doc)) 
        {
            for (double tfidf : documentTFIDF.get(doc).values()) 
            {
                length += tfidf * tfidf;
            }
        }
        documentLengths.put(doc, Math.sqrt(length));
    }

    // Normalize TF-IDF
    Map<String, Map<String, Double>> normalizedTFIDF = new HashMap<>();
    for (String doc : allDocs) 
    {
        double docLength = documentLengths.getOrDefault(doc, 0.0);
        if (docLength == 0) continue;

        normalizedTFIDF.put(doc, new HashMap<>());
        for (Map.Entry<String, Double> termEntry : documentTFIDF.getOrDefault(doc, new HashMap<>()).entrySet()) 
        {
            String term = termEntry.getKey();
            double tfidf = termEntry.getValue();
            normalizedTFIDF.get(doc).put(term, tfidf / docLength);
        }
    }

    // Filter by query terms
    Set<String> queryTermSet = new HashSet<>(Arrays.asList(query.split("\\s+")));

    System.out.println("\n-------------------------------------------------");
    System.out.println("Normalized TF-IDF Matrix (Filtered by Query Terms):\n");

    
    Set<String> matchingDocs = new HashSet<>();
    for (String term : queryTermSet) 
    {
        if (termDocPositions.containsKey(term)) 
        {
            for (String doc : allDocs) 
            {
                double tfidfValue = normalizedTFIDF.getOrDefault(doc, new HashMap<>()).getOrDefault(term, 0.0);
                if (tfidfValue > 0.0) 
                {
                    matchingDocs.add(doc);
                }
            }
        }
    }

  
    if (matchingDocs.isEmpty()) {
        System.out.println("No documents match the query terms.");
        return;
    }

    
    System.out.print("Terms");
    for (String doc : matchingDocs) 
    {
        System.out.print("\t" + doc);
    }
    System.out.println();

   
    for (String term : queryTermSet) 
    {
        if (termDocPositions.containsKey(term)) 
        {
            System.out.print(term);
            for (String doc : matchingDocs) 
            {
                double value = normalizedTFIDF.getOrDefault(doc, new HashMap<>()).getOrDefault(term, 0.0);
                System.out.print("\t" + String.format("%.5f", value));
            }
            System.out.println();
        }
    }

    System.out.println("\n---------------------------------------------------------------");
    System.out.println("Multiplying Normalized Query TF-IDF with Document TF-IDF Matrix:");
    System.out.println("product (query * matched docs)\n");
  
    System.out.print("Terms");
    for (String doc : matchingDocs) 
    {
        System.out.print("\t" + doc);
    }
    System.out.println();


    for (String term : queryTermSet) 
    {
        System.out.print(term);
        for (String doc : matchingDocs) 
        {
            double queryNorm = queryTFIDF.getOrDefault(term, 0.0);
            double docNorm = normalizedTFIDF.getOrDefault(doc, new HashMap<>()).getOrDefault(term, 0.0);
            double product = queryNorm * docNorm;
            System.out.print("\t" + String.format("%.5f", product));
        }
        System.out.println();
    }
}
  
 //================================================================================================================
 //(((((((((((((((((((((((((((((((((((((((((((((((((((9))))))))))))))))))))))))))))))))))))))))))))))))))))))))
 
 public static void processAndDisplayResults_withboolean1(String query, String filename) throws IOException 
 {
        Map<String, Map<String, List<Integer>>> termDocPositions = new HashMap<>();
        int totalDocuments = 10;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) 
        {
            String line;
            while ((line = reader.readLine()) != null) 
            {
                String[] parts = line.split("\t");
                if (parts.length < 2) continue;

                String term = parts[0].trim();
                String docData = parts[1].trim();
                String[] docEntries = docData.split(";");

                for (String entry : docEntries) 
                {
                    String[] docParts = entry.split(":");
                    if (docParts.length < 2) continue;

                    String docId = docParts[0].trim();
                    String positions = docParts[1].trim();
                    List<Integer> positionList = new ArrayList<>();

                    if (!positions.isEmpty()) 
                    {
                        String[] positionArray = positions.split(" ");
                        for (String pos : positionArray)
                        {
                            positionList.add(Integer.valueOf(pos.trim()));
                        }
                    }

                    termDocPositions.putIfAbsent(term, new HashMap<>());
                    termDocPositions.get(term).put(docId, positionList);
                }
            }
        } catch (IOException e)
        {
            System.out.println("Error reading the file: " + e.getMessage());
            return;
        }

        // Process query with AND and NOT operators
        String[] queryTerms = query.split(" ");
        Set<String> queryTermSet = new HashSet<>();
        List<String> operators = new ArrayList<>();

        // Parse the query to handle AND and NOT
        for (String term : queryTerms) 
        {
            if (term.equalsIgnoreCase("AND") || term.equalsIgnoreCase("NOT")) 
            {
                operators.add(term);
            } 
            else 
            {
                queryTermSet.add(term);
            }
        }

        // Compute TF, IDF, and TF-IDF for the query
        Map<String, Double> queryTF = new HashMap<>();
        for (String term : queryTermSet) 
        {
            queryTF.put(term, queryTF.getOrDefault(term, 0.0) + 1);
        }

        Map<String, Double> queryTFIDF = new HashMap<>();
        double queryLengthSquared = 0.0;
        for (String term : queryTF.keySet()) 
        {
            double tf = 1 + Math.log10(queryTF.get(term)); // TF for the query
            double idf = Math.log10(10.0 / (termDocPositions.containsKey(term) ? termDocPositions.get(term).size() : 1)); // IDF for query term
            double tfidf = tf * idf;
            queryTFIDF.put(term, tfidf);
            queryLengthSquared += tfidf * tfidf;
        }

        double queryLength = Math.sqrt(queryLengthSquared);
        for (String term : queryTFIDF.keySet()) 
        {
            queryTFIDF.put(term, queryTFIDF.get(term) / queryLength);
        }

        // Display the query and its components
        System.out.println("\n\n============ After inserting queries ==========\n");
        System.out.println("------- (((( Query )))) -------\n");
        System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s%n", "Query", "TF-raw", "1+log(TF)", "TF-IDF", "IDF", "Normalized");

        for (String term : queryTFIDF.keySet()) 
        {
            double tf = queryTF.get(term); // TF for the query
            double logTf = 1 + Math.log10(tf);
            double idf = Math.log10(10.0 / (termDocPositions.containsKey(term) ? termDocPositions.get(term).size() : 1)); // IDF for query term
            double tfIdf = tf * idf;
            double normalized = queryTFIDF.get(term);
            System.out.printf("%-10s %-10.2f %-10.4f %-10.4f %-10.4f %-10.4f%n", term, tf, logTf, tfIdf, idf, normalized);
        }

        System.out.println("\nquery length: " + String.format("%.6f", queryLength));
        System.out.println("\n-------------------------------------------------");

        Map<String, Map<String, Double>> documentTFIDF = new HashMap<>();
        Map<String, Double> documentLengths = new HashMap<>();
        List<String> allDocs = Arrays.asList("doc1", "doc2", "doc3", "doc4", "doc5", "doc6", "doc7", "doc8", "doc9", "doc10");

        for (Map.Entry<String, Map<String, List<Integer>>> termEntry : termDocPositions.entrySet()) 
        {
            String term = termEntry.getKey();
            Map<String, List<Integer>> docPositions = termEntry.getValue();

            int df = docPositions.size();
            double idf = Math.log10((double) totalDocuments / df);

            Map<String, Integer> termFrequencies = new HashMap<>();
            for (String doc : allDocs) 
            {
                termFrequencies.put(doc, 0);
            }

            for (Map.Entry<String, List<Integer>> docEntry : docPositions.entrySet()) 
            {
                String docId = docEntry.getKey();
                List<Integer> positions = docEntry.getValue();
                termFrequencies.put(docId, positions.size());
            }

            for (String doc : allDocs) 
            {
                int tf = termFrequencies.get(doc);
                double tfidf = tf * idf; // TF-IDF calculation

                documentTFIDF.putIfAbsent(doc, new HashMap<>());
                documentTFIDF.get(doc).put(term, tfidf);
            }
        }

        // Calculate the length of each document
        for (String doc : allDocs) 
        {
            double length = 0.0;
            if (documentTFIDF.containsKey(doc)) 
            {
                for (double tfidf : documentTFIDF.get(doc).values())
                {
                    length += tfidf * tfidf;
                }
            }
            documentLengths.put(doc, Math.sqrt(length));
        }

        // Normalize TF-IDF
        Map<String, Map<String, Double>> normalizedTFIDF = new HashMap<>();
        for (String doc : allDocs) 
        {
            double docLength = documentLengths.getOrDefault(doc, 0.0);
            if (docLength == 0) continue;

            normalizedTFIDF.put(doc, new HashMap<>());
            for (Map.Entry<String, Double> termEntry : documentTFIDF.getOrDefault(doc, new HashMap<>()).entrySet()) 
            {
                String term = termEntry.getKey();
                double tfidf = termEntry.getValue();
                normalizedTFIDF.get(doc).put(term, tfidf / docLength);
            }
        }

        
        System.out.println("Normalized TF-IDF Matrix (Filtered by Query Terms):\n");

      
        Set<String> matchingDocs = new HashSet<>();
        for (String term : queryTermSet) 
        {
            if (termDocPositions.containsKey(term)) 
            {
                for (String doc : allDocs) 
                {
                    double tfidfValue = normalizedTFIDF.getOrDefault(doc, new HashMap<>()).getOrDefault(term, 0.0);
                    if (tfidfValue > 0.0) 
                    {
                        matchingDocs.add(doc);
                    }
                }
            }
        }

        if (matchingDocs.isEmpty()) 
        {
            System.out.println("No documents match the query terms.");
            return;
        }

        System.out.print("Terms");
        for (String doc : matchingDocs) 
        {
            System.out.print("\t" + doc);
        }
        System.out.println();

        for (String term : queryTermSet)
        {
            if (termDocPositions.containsKey(term)) 
            {
                System.out.print(term);
                for (String doc : matchingDocs) 
                {
                    double value = normalizedTFIDF.getOrDefault(doc, new HashMap<>()).getOrDefault(term, 0.0);
                    System.out.print("\t" + String.format("%.5f", value));
                }
                System.out.println();
            }
        }

        // Multiply query normalized TF-IDF with document normalized TF-IDF and display
        System.out.println("\n-------------------------------------------------");
        System.out.println("Multiplying Normalized Query TF-IDF with Document TF-IDF:");
        System.out.println("((-----------------Similarity--------------------))\n");
        for (String doc : matchingDocs) 
        {
            double dotProduct = 0.0;
            for (String term : queryTermSet) 
            {
                double queryNorm = queryTFIDF.getOrDefault(term, 0.0);
                double docNorm = normalizedTFIDF.getOrDefault(doc, new HashMap<>()).getOrDefault(term, 0.0);
                dotProduct += queryNorm * docNorm;
            }

            System.out.println("Doc: " + doc + " |  similarity : " + String.format("%.5f", dotProduct));
        }

        Map<String, Double> docDotProductMap = new HashMap<>();

        for (String doc : matchingDocs) 
        {
            double dotProduct = 0.0;
            for (String term : queryTermSet) 
            {
                double queryNorm = queryTFIDF.getOrDefault(term, 0.0);
                double docNorm = normalizedTFIDF.getOrDefault(doc, new HashMap<>()).getOrDefault(term, 0.0);
                dotProduct += queryNorm * docNorm;
            }

            docDotProductMap.put(doc, dotProduct);
        }

        List<Map.Entry<String, Double>> sortedDocs = new ArrayList<>(docDotProductMap.entrySet());
        sortedDocs.sort((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()));

        // Display the ranked documents
        System.out.println("\n-------------------------------------------------");
        System.out.println("Ranked Documents based on similarity:\n");

        System.out.printf("%-10s %-10s%n", "Rank", "Document");

        int rank = 1;
        for (Map.Entry<String, Double> entry : sortedDocs)
        {
            String doc = entry.getKey();
            double dotProduct = entry.getValue();
            System.out.printf("%-10d %-10s |  similarity : %.5f%n", rank, doc, dotProduct);
            rank++;
        }
    }
 //--------------------------------------------------------------------------------------------------------------
 public static void processAndDisplayResults_withboolean2(String query, String filename) throws IOException 
 {
    Map<String, Map<String, List<Integer>>> termDocPositions = new HashMap<>();
    int totalDocuments = 10;

    
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) 
    {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\t");
            if (parts.length < 2) continue;

            String term = parts[0].trim();
            String docData = parts[1].trim();
            String[] docEntries = docData.split(";");

            for (String entry : docEntries) 
            {
                String[] docParts = entry.split(":");
                if (docParts.length < 2) continue;

                String docId = docParts[0].trim();
                String positions = docParts[1].trim();
                List<Integer> positionList = new ArrayList<>();

                
                if (!positions.isEmpty()) 
                {
                    String[] positionArray = positions.split(" ");
                    for (String pos : positionArray) 
                    {
                        positionList.add(Integer.valueOf(pos.trim()));
                    }
                }

                termDocPositions.putIfAbsent(term, new HashMap<>());
                termDocPositions.get(term).put(docId, positionList);
            }
        }
    } catch (IOException e)
    {
        System.out.println("Error reading the file: " + e.getMessage());
        return;
    }

    
    String[] queryTerms = query.split(" ");
    List<String> includeTerms = new ArrayList<>();
    List<String> excludeTerms = new ArrayList<>();
    boolean isExcludeMode = false;

    for (String term : queryTerms) 
    {
        if (term.equalsIgnoreCase("AND")) 
        {
            isExcludeMode = false; // Reset exclude mode after AND
        } else if (term.equalsIgnoreCase("AND NOT")) 
        {
            isExcludeMode = true; // Set exclude mode for AND NOT
        } else {
            if (isExcludeMode)
            {
                excludeTerms.add(term);
            } else 
            {
                includeTerms.add(term);
            }
        }
    }

   
    Map<String, Double> queryTF = new HashMap<>();
    for (String term : includeTerms) 
    {
        queryTF.put(term, queryTF.getOrDefault(term, 0.0) + 1);
    }

   
    Map<String, Double> queryTFIDF = new HashMap<>();
    double queryLengthSquared = 0.0;
    for (String term : queryTF.keySet()) 
    {
        double tf = 1 + Math.log10(queryTF.get(term));
        double idf = Math.log10(10.0 / (termDocPositions.containsKey(term) ? termDocPositions.get(term).size() : 1)); // IDF for query term
        double tfidf = tf * idf;
        queryTFIDF.put(term, tfidf);
        queryLengthSquared += tfidf * tfidf;
    }

    double queryLength = Math.sqrt(queryLengthSquared);
    for (String term : queryTFIDF.keySet()) {
        queryTFIDF.put(term, queryTFIDF.get(term) / queryLength);
    }

    // Calculate TF, IDF, and TF-IDF for each term in the document corpus
    Map<String, Map<String, Double>> documentTFIDF = new HashMap<>();
    Map<String, Double> documentLengths = new HashMap<>();
    List<String> allDocs = Arrays.asList("doc1", "doc2", "doc3", "doc4", "doc5", "doc6", "doc7", "doc8", "doc9", "doc10");

    for (Map.Entry<String, Map<String, List<Integer>>> termEntry : termDocPositions.entrySet()) 
    {
        String term = termEntry.getKey();
        Map<String, List<Integer>> docPositions = termEntry.getValue();

        int df = docPositions.size();
        double idf = Math.log10((double) totalDocuments / df);

        Map<String, Integer> termFrequencies = new HashMap<>();
        for (String doc : allDocs) 
        {
            termFrequencies.put(doc, 0);
        }

        for (Map.Entry<String, List<Integer>> docEntry : docPositions.entrySet()) 
        {
            String docId = docEntry.getKey();
            List<Integer> positions = docEntry.getValue();
            termFrequencies.put(docId, positions.size());
        }

        for (String doc : allDocs) 
        {
            int tf = termFrequencies.get(doc);
            double tfidf = tf * idf;
            documentTFIDF.putIfAbsent(doc, new HashMap<>());
            documentTFIDF.get(doc).put(term, tfidf);
        }
    }

    // Calculate document lengths
    for (String doc : allDocs) 
    {
        double length = 0.0;
        if (documentTFIDF.containsKey(doc)) 
        {
            for (double tfidf : documentTFIDF.get(doc).values()) 
            {
                length += tfidf * tfidf;
            }
        }
        documentLengths.put(doc, Math.sqrt(length));
    }

    // Normalize TF-IDF values for each document
    Map<String, Map<String, Double>> normalizedTFIDF = new HashMap<>();
    for (String doc : allDocs)
    {
        double docLength = documentLengths.getOrDefault(doc, 0.0);
        if (docLength == 0) continue;

        normalizedTFIDF.put(doc, new HashMap<>());
        for (Map.Entry<String, Double> termEntry : documentTFIDF.getOrDefault(doc, new HashMap<>()).entrySet()) 
        {
            String term = termEntry.getKey();
            double tfidf = termEntry.getValue();
            normalizedTFIDF.get(doc).put(term, tfidf / docLength);
        }
    }

    // Filter documents based on query terms (AND and AND NOT)
    Set<String> matchingDocs = new HashSet<>(allDocs);

    // Apply AND logic (keep documents that contain all include terms)
    for (String term : includeTerms)
    {
        if (termDocPositions.containsKey(term)) 
        {
            matchingDocs.removeIf(doc -> !termDocPositions.get(term).containsKey(doc));  // Keep docs that have this term
        }
    }

    // Apply AND NOT logic (exclude documents that contain exclude terms)
    for (String term : excludeTerms) {
        if (termDocPositions.containsKey(term)) 
        {
            matchingDocs.removeIf(doc -> termDocPositions.get(term).containsKey(doc));  // Exclude docs that have this term
        }
    }

    if (matchingDocs.isEmpty())
    {
        System.out.println("No documents match the query terms.");
        return;
    }

    // Print the normalized TF-IDF matrix for matching documents
    System.out.println("\n-------------------------------------------------");
    System.out.println("Normalized TF-IDF Matrix (Filtered by Query Terms):\n");

    System.out.print("Terms");
    for (String doc : matchingDocs) 
    {
        System.out.print("\t" + doc);
    }
    System.out.println();

    for (String term : includeTerms)
    {
        if (termDocPositions.containsKey(term)) 
        {
            System.out.print(term);
            for (String doc : matchingDocs)
            {
                double value = normalizedTFIDF.getOrDefault(doc, new HashMap<>()).getOrDefault(term, 0.0);
                System.out.print("\t" + String.format("%.5f", value));
            }
            System.out.println();
        }
    }

    // Multiply normalized query TF-IDF with document TF-IDF matrix
    System.out.println("\n---------------------------------------------------------------");
    System.out.println("Multiplying Normalized Query TF-IDF with Document TF-IDF Matrix:");
    System.out.println("product (query * matched docs)\n");

    System.out.print("Terms");
    for (String doc : matchingDocs) 
    {
        System.out.print("\t" + doc);
    }
    System.out.println();

    for (String term : includeTerms)
    {
        System.out.print(term);
        for (String doc : matchingDocs)
        {
            double queryNorm = queryTFIDF.getOrDefault(term, 0.0);
            double docNorm = normalizedTFIDF.getOrDefault(doc, new HashMap<>()).getOrDefault(term, 0.0);
            double product = queryNorm * docNorm;
            System.out.print("\t" + String.format("%.5f", product));
        }
        System.out.println();
    }
}

 
 
 
 
 
 
 
 
 
 
 
 
//===============================================((((((Main))))))=============================================================================  
    
    public static void main(String[] args) throws IOException 
        {
           Scanner scanner = new Scanner(System.in);
          while (true) {
            System.out.println("\n========================(((Menu)))=======================\n");
            System.out.println("1. Display Positional Index..? ");
            System.out.println("2. ((TF)) term frequency for each term in each document (Display it)..?");
            System.out.println("3. ((WTF)) = (1+log(tf)) term frequency for each term in each document (Display it)..?");
            System.out.println("4. Display Matrix of (( df , idf)) ..?");
            System.out.println("5. Display Matrix of (( TF * idf)) ..?");
            System.out.println("6. Display Matrix of Document length..?");
            System.out.println("7. Display Matrix of Normalized tf.idf..?");
            System.out.println("8. After inserting  queries (( phrase query )) ");
            System.out.println("9. After inserting  queries (( phrase query with AND , NOT AND )) ");
            System.out.print("\nChoose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); 

            switch(option)
            {
                case 1 -> 
                {
                    
                   String filePath = "D:\\output\\output.txt";
                   System.out.println("\n(((---------------- Positional Index -----------------)))\n");
                   Positional_Index(filePath);
                   
                }
                case 2 -> 
                {  
                  String filePath = "D:\\output\\output.txt";
                  System.out.println("\n(((----------------- Matrix of TF -----------------)))\n");
                  print_TF_Matrix(filePath);
                }
                case 3 -> 
                {
                 String filePath = "D:\\output\\output.txt";
                  System.out.println("\n(((----------------- Matrix of WTF (1+ log(tf)) -----------------)))\n");
                  print_WTF_Matrix(filePath);
    
                }
                case 4 -> 
                {
                  String filePath4 = "D:\\output\\output.txt";
                  System.out.println("\n(((----------------- Matrix of ( df , idf ) -----------------)))\n");
                    printDF_IDF(filePath4);
                
                }
                 case 5 -> 
                {
                    String filePath4 = "D:\\output\\output.txt";
                    System.out.println("\n(((----------------- Matrix of ( TF * idf  ) ------------------)))\n");
                    printTFIDFMatrix(filePath4);
                  
                   
                
                }
                case 6 -> 
                {
                   String filePath5 = "D:\\output\\output.txt";  
                   System.out.println("\n(((-----------------  Document length -----------------)))\n");
                   printDocumentLengths(filePath5); 
                }
                 case 7 -> 
                {
                   String filePath5 = "D:\\output\\output.txt";  
                   System.out.println("\n(((-----------------  Normalized tf.idf -----------------)))\n");
                   printNormalizedTFIDF(filePath5); 
                }
                case 8 -> 
                {
                 
                   String filePath9 = "D:\\output\\output.txt";
                   System.out.print("Enter your query: ");
                   String query = scanner.nextLine();
                   
                   if (!query.matches("[a-zA-Z\\s]+")) 
                   { 
                         System.out.println("Error: Query contains invalid characters!!");
                         break;
                   }

                   
                   processAndDisplayResults1(query, filePath9);
                   processAndDisplayResults2(query, filePath9);
                  
                   
                      
                }
                 case 9 -> 
                {
                 
                   String filePath9 = "D:\\output\\output.txt";
                   System.out.print("Enter your query: ");
                   String query = scanner.nextLine();
                   
                   if (!query.matches("[a-zA-Z\\s]+")) 
                   { 
                         System.out.println("Error: Query contains invalid characters!!");
                         break;
                   }

                   
                   
                   processAndDisplayResults_withboolean1(query, filePath9);
                   processAndDisplayResults_withboolean2(query, filePath9);
                      
                }
                              
                 default ->
                    System.out.println("Invalid option, please try again.");
            }
        }

        }
}













        

