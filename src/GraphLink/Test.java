package GraphLink;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Vector;

public class Test {
    public static void main(String args[]) throws IOException {
        CreatGraph c = new CreatGraph();
        Graph g = c.getGraph();
        EdgeNode edge;
        g.RoundWeight();
        //g.SetWeight();
        g.ComputeWcd();
        //g.SortCd();
        //g.PrintGraph();
        g.CoreDec();
        //g.Delete();
        //g.Insert();
        //g.findEdge(55);

        Vector<Graph.GenericPair<Integer,Integer>> DeleteEdgeSet = g.GetEdgeSetFromFile();

        g.ComputeWd();
        g.EdgeSetDelete(DeleteEdgeSet);


        Vector<Graph.GenericPair<Integer,Integer>> InsertEdgeSet = g.GetEdgeSetFromFile();
        g.ComputeWd();
        g.EdgeSetInsert(InsertEdgeSet);


    }


}
