package GraphLink;
//这个类是用来存储边的
public class EdgeNode {
    int adjvex;
    EdgeNode next;

    public  EdgeNode(int adj, EdgeNode nt){
        this.adjvex = adj;
        this.next = nt;
    }
    public EdgeNode(){

    }

}
