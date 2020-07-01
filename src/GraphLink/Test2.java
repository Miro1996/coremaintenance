package GraphLink;

import java.io.IOException;
import java.util.Vector;

public class Test2 {
    public static void main(String args[]) throws IOException {
        CreatGraph c = new CreatGraph();
        Graph g = c.getGraph();
        EdgeNode edge;
        g.RoundWeight();
        //g.SetWeight();
        g.ComputeWcd();
        //g.SortCd();
        //g.PrintGraph();
        long start = System.currentTimeMillis();
        g.CoreDec();
        long end   =  System.currentTimeMillis();
        System.out.println(end - start);
        g.Delete();
        g.Insert();
        //g.findEdge(55);



    }
}
