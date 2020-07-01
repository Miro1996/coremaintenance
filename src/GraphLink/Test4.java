package GraphLink;

import java.io.IOException;
import java.util.Vector;

public class Test4 {
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
        Vector<Graph.GenericPair<Integer,Integer>> InsertEdgeSet = g.GetEdgeSetFromFile();
        g.ComputeWd();
        g.EdgeSetInsert(InsertEdgeSet);

        Vector<Graph.GenericPair<Integer,Integer>> DeleteEdgeSet = g.GetEdgeSetFromFile();

        g.ComputeWd();
        g.EdgeSetDelete(DeleteEdgeSet);





    }
}
