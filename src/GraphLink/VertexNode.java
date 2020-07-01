package GraphLink;

public class VertexNode {
    int vertex;         //顶点代号
    int weight;         //顶点权重
    int wcd = 0; //顶点的权重和
    int core;           //顶点的核值
    int cd = 0;
    int wd = 0;
    boolean removed = false;    //用于判断有没有被移除
    boolean visited = false;    //用于判断是否被访问
    boolean evicted = false;
    boolean over = false;
    boolean update = false;
    EdgeNode firstedge = new EdgeNode();
    public VertexNode(int ver, int wgt){
        this.vertex = ver;
        this.weight = wgt;
    }
    public VertexNode(int ver){
        this.vertex = ver;
    }
    public VertexNode(){
    };

    public int getWcd() {
        return wcd;
    }

    public void setWcd(int wcd) {
        this.wcd = wcd;
    }
}
