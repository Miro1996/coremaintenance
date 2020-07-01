package GraphLink;

import java.io.*;
import java.util.*;

public class Graph{
    private ArrayList<VertexNode> nodeList = new ArrayList<>();//存储图中的点
    int vertexNum; //图中点的数量
    int edgeNum;   //图中边的数量
    public int truevertex;
    ArrayList<Integer> VerSet = new ArrayList<Integer>();
    public static int en = 0;

    //图的初始化
    Graph(int size, int line) {
        for (int i = 0; i < size; i++) {
            VertexNode v = new VertexNode(i);
            getNodeList().add(v);
        }
        vertexNum = size;
        edgeNum = line;
    }

    Graph(int size) {
        for (int i = 0; i < size; i++) {
            VertexNode v = new VertexNode(i);
            getNodeList().add(v);
        }
        vertexNum = size;

    }

    public void getTruevertex() {
        for (int i = 1; i < getNodeList().size(); i++) {
            if (getNodeList().get(i).cd > 0) {
                truevertex++;
                VerSet.add(i);
            }
        }
    }

    public ArrayList<VertexNode> getNodeList() {
        return nodeList;
    }

    public void setNodeList(ArrayList<VertexNode> nodeList) {
        this.nodeList = nodeList;
    }

    public static class GenericPair<E extends Object, F extends Object> {
        private E first;
        private F second;

        public GenericPair() {
        }

        public E getFirst() {
            return first;
        }

        public void setFirst(E first) {
            this.first = first;
        }

        public F getSecond() {
            return second;
        }

        public void setSecond(F second) {
            this.second = second;
        }

        public boolean equals(Object b) {
            GenericPair a = (GenericPair) b;
            return a.getFirst() == first && a.getSecond() == second;
        }
    }

    /******************
     * 读入每个点的权重
     *****************/
    public void SetWeight() {
        Scanner scan = new Scanner(System.in);
        System.out.print("请输入具有权重的文件夹名称:");
        String filename = scan.nextLine();
        File file = new File(filename);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                String temp[] = tempString.split("\\s+");
                getNodeList().get(Integer.parseInt(temp[0])).weight = Integer.parseInt(temp[1]);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*******************
     以邻接表的形式打印出图
     *******************/

    public void PrintGraph() {
        EdgeNode edge;
        for (int i = 1; i < getNodeList().size(); i++) {


            edge = getNodeList().get(i).firstedge;
            if (edge.adjvex == 0) {
                continue;
            }
            System.out.print(getNodeList().get(i).vertex);
            while (edge.adjvex != 0) {
                System.out.print("-->" + edge.adjvex);
                edge = edge.next;
            }
            System.out.println();
        }

    }

    /****************
     设置图中每个点的wcd值
     ****************/
    public void ComputeWcd() {
        EdgeNode edge;
        for (int i = 1; i < getNodeList().size(); i++) {
            edge = getNodeList().get(i).firstedge;
            while (edge.adjvex != 0) {
                getNodeList().get(i).wcd += getNodeList().get(edge.adjvex).weight;
                getNodeList().get(i).cd += getNodeList().get(edge.adjvex).weight;
                edge = edge.next;
            }

        }
    }

    /****************
     对图的核数进行静态分解
     ****************/
    public void CoreDec() throws IOException {
        int minindex = 0;
        int wcd;
        int k = 0;
        int j = 0;
        EdgeNode edge;
        ArrayList<VertexNode> vnode = this.getNodeList();
        while (j < getNodeList().size() - 1) {
            int min = 0x7fffffff;
            //找集合中最小的wcd以及它的索引
            for (int i = 1; i < vnode.size(); i++) {
                if (vnode.get(i).removed == true) {//如果这个节点已被记录为移除，就进入下次循环
                    continue;
                }
                wcd = vnode.get(i).wcd;
                if (wcd < min) {
                    min = wcd;
                    minindex = i;
                }
            }
            if (min > k) {
                vnode.get(minindex).core = min;
                k = min;
            } else {
                vnode.get(minindex).core = k;
            }
            //接下来进行删除点以及它的影响的操作
            edge = vnode.get(minindex).firstedge;
            while (edge.adjvex != 0) {
                vnode.get(edge.adjvex).wcd = vnode.get(edge.adjvex).wcd - vnode.get(minindex).weight;
                edge = edge.next;
            }
            vnode.get(minindex).removed = true;
            j++;

        }

        ComputeWd();
        OutputStream os = new FileOutputStream("OutputCoreNumber1.txt");
        PrintWriter pw = new PrintWriter(os);

        for (int i = 1; i < vnode.size(); i++) {
            EdgeNode eg = vnode.get(i).firstedge;
            if (eg.adjvex == 0 || vnode.get(i).core == 0) {
                continue;
            }
            pw.print(i + "  " + vnode.get(i).core + "\n");

        }

        pw.close();
        os.close();

    }

    /******************************
     插入一条边后，找到核数可能改变的集合
     *****************************/
    public Graph IncSubcore(int r) {
       /* if(getNodeList().get(r).core == 0){
            Graph grp = new Graph(this.vertexNum);
            grp = this;
            for(int i = 1; i < getNodeList().size(); i++){
                if(getNodeList().get(i).cd > 0){
                    grp.truevertex++;
                    grp.VerSet.add(i);
                }
            }
            return grp;
        }*/
        for (int i = 1; i < getNodeList().size(); i++) {
            getNodeList().get(i).visited = false;
            getNodeList().get(i).over = false;
        }
        ArrayList<Integer> verset = new ArrayList<Integer>();
        LinkedList<Integer> queue = new LinkedList<Integer>();
        EdgeNode edge;
        Graph g = new Graph(this.vertexNum);
        int k = getNodeList().get(r).core;
        //System.out.println(r+"的核数值为:"+k);
        queue.offer(r);
       // g.getNodeList().get(r).core = getNodeList().get(r).core;
        g.getNodeList().get(r).weight = getNodeList().get(r).weight;

        getNodeList().get(r).visited = true;
        while (queue.isEmpty() != true) {
            int u = queue.poll();
            verset.add(u);
            edge = getNodeList().get(u).firstedge;
            while (edge.adjvex != 0) {
                if (getNodeList().get(edge.adjvex).core >= k) {
                    g.getNodeList().get(edge.adjvex).weight = getNodeList().get(edge.adjvex).weight;
                    //g.getNodeList().get(edge.adjvex).core = getNodeList().get(edge.adjvex).core;
                    //g.getNodeList().get(u).cd = g.getNodeList().get(u).cd + getNodeList().get(edge.adjvex).vertex;
                    g.getNodeList().get(u).cd = g.getNodeList().get(u).cd + getNodeList().get(edge.adjvex).weight;
                    if (getNodeList().get(edge.adjvex).visited == false) {
                        queue.offer(edge.adjvex);

                        EdgeNode a = new EdgeNode(u, null);
                        a.next = g.getNodeList().get(edge.adjvex).firstedge;
                        g.getNodeList().get(edge.adjvex).firstedge = a;

                        EdgeNode b = new EdgeNode(edge.adjvex, null);
                        b.next = g.getNodeList().get(u).firstedge;
                        g.getNodeList().get(u).firstedge = b;

                        getNodeList().get(edge.adjvex).visited = true;
                    } else if (getNodeList().get(edge.adjvex).over != true) {
                        // System.out.println("u:"+u+"edge.adjvex:"+edge.adjvex);
                        EdgeNode a = new EdgeNode(u, null);
                        a.next = g.getNodeList().get(edge.adjvex).firstedge;
                        g.getNodeList().get(edge.adjvex).firstedge = a;

                        EdgeNode b = new EdgeNode(edge.adjvex, null);
                        b.next = g.getNodeList().get(u).firstedge;
                        g.getNodeList().get(u).firstedge = b;

                    }

                }

                edge = edge.next;
            }
            getNodeList().get(u).over = true;


        }
        g.truevertex = verset.size();
        g.VerSet = (ArrayList<Integer>) verset.clone();

        return g;
    }

    /******************************
     插入一条边后的核值维护算法
     *****************************/
    public void Insert() throws IOException {
        en = 0;
        long t = 0;
        Timer time = new Timer();
        Scanner scan = new Scanner(System.in);
        System.out.println("请输入要插入的边");
        int u = scan.nextInt();
        int v = scan.nextInt();
        time.start();
        int m = 1;
        int r = u;
        int l = v;
        boolean flag = false;
        if (getNodeList().get(u).core > getNodeList().get(v).core) {
            r = v;
            l = u;
        }

        EdgeNode a = new EdgeNode(r, null);
        a.next = getNodeList().get(l).firstedge;
        getNodeList().get(l).firstedge = a;
        EdgeNode b = new EdgeNode(l, null);
        b.next = getNodeList().get(r).firstedge;
        getNodeList().get(r).firstedge = b;

        Graph h = this.IncSubcore(r);//这一步是为了找到插入一条边后图中所有点的核值大于等于下界的子图。
        int kr = h.getNodeList().get(r).core;
        //h.PrintGraph();
        // for(int i = 1; i < h.getNodeList().size(); i++)
        //{System.out.println(i+"的核数为:"+h.getNodeList().get(i).core);
        //  System.out.println(i+"的cd值:"+h.getNodeList().get(i).cd);}
        //h.getNodeList().get(r).cd = h.getNodeList().get(r).cd + h.getNodeList().get(l).vertex;
        //h.getNodeList().get(l).cd = h.getNodeList().get(l).cd + h.getNodeList().get(r).vertex;

        //h.getNodeList().get(r).cd = h.getNodeList().get(r).cd + h.getNodeList().get(l).weight;
        //h.getNodeList().get(l).cd = h.getNodeList().get(l).cd + h.getNodeList().get(r).weight;


        while (en < h.truevertex) {//h里还有没被evict的点
            //System.out.println("en:"+en);
            int min = 0x7fffffff;
            for (int i = 0; i < h.VerSet.size(); i++) {

                if (h.getNodeList().get(h.VerSet.get(i)).evicted == true || h.getNodeList().get(h.VerSet.get(i)).cd == 0) {
                    continue;
                }


                if (min > h.getNodeList().get(h.VerSet.get(i)).cd) {
                    min = h.getNodeList().get(h.VerSet.get(i)).cd;
                    m = h.VerSet.get(i);

                }
            }

//            for (int i = 1; i < h.getNodeList().size(); i++) {
//                if (h.getNodeList().get(i).evicted == true || h.getNodeList().get(i).cd == 0) {
//                    continue;
//                }
//                if (min > h.getNodeList().get(i).cd) {
//                    min = h.getNodeList().get(i).cd;
//                    m = i;
//
//                }
//            }
            //System.out.println("min"+min+"m"+m);
            //(h.getNodeList().get(m).core >= (kr + h.getNodeList().get(l).vertex)) {
            h.getNodeList().get(m).core = h.getNodeList().get(m).cd;
            h.getNodeList().get(m).update = true;
            if(h.getNodeList().get(m).core >= (kr + h.getNodeList().get(l).weight)) {
                break;
            }
            h.Evict(m);
            }
        //for(int i = 1; i < h.getNodeList().size(); i++)
        //  System.out.println(h.getNodeList().get(i).core);
        t = time.elapsed();
        for (int i = 0; i < h.VerSet.size(); i++) {
            if (h.getNodeList().get(h.VerSet.get(i)).update == true) {
                getNodeList().get(h.VerSet.get(i)).core = h.getNodeList().get(h.VerSet.get(i)).core;
            }

        }
        OutputStream os = new FileOutputStream("InsertOneEdge.txt");
        PrintWriter pw = new PrintWriter(os);

        for (int i = 1; i < getNodeList().size(); i++) {
            if (getNodeList().get(i).core != 0) {
                pw.print(i + "的核数值为：" + getNodeList().get(i).core + "\n");
            }
        }

        pw.close();
        os.close();
        System.out.println("Insert耗费时间t:" + t);
    }

    /******************************
     递归移除点以及对应的影响
     *****************************/
    public void Evict(int v) {
        getNodeList().get(v).evicted = true;
        en++;
        EdgeNode edge,e;
        edge = getNodeList().get(v).firstedge;


        while (edge.adjvex != 0) {
            if (getNodeList().get(edge.adjvex).evicted == true) {
                edge = edge.next;
                continue;
            }
            // System.out.println("en:"+en+"v"+v+"edge adjvex:"+edge.adjvex+"edge cd:"+ getNodeList().get(edge.adjvex).cd );
            getNodeList().get(edge.adjvex).cd = getNodeList().get(edge.adjvex).cd - getNodeList().get(v).weight;
            if (getNodeList().get(edge.adjvex).cd < getNodeList().get(v).core) {
                getNodeList().get(edge.adjvex).core = getNodeList().get(v).core;
                getNodeList().get(edge.adjvex).update = true;
                // System.out.println(edge.adjvex+"核数更新为:"+getNodeList().get(edge.adjvex).core);
                Evict(edge.adjvex);
            }
            edge = edge.next;
        }
    }


    /******************************
     删除一条边后，找到核数可能改变的点的集合
     *****************************/
    public Graph DecSubcore(int r, int l) {
        for (int i = 1; i < getNodeList().size(); i++) {
            getNodeList().get(i).over = false;
            getNodeList().get(i).visited = false;

        }
        ArrayList<Integer> verset = new ArrayList<Integer>();
        LinkedList<Integer> queue = new LinkedList<Integer>();
        EdgeNode edge;
        Graph g = new Graph(this.vertexNum);
        int k = getNodeList().get(r).core;
        queue.offer(r);
        //g.getNodeList().get(r).core = getNodeList().get(r).core;
        g.getNodeList().get(r).weight = getNodeList().get(r).weight;
        getNodeList().get(r).visited = true;
        while (queue.isEmpty() != true) {
            int u = queue.poll();
            verset.add(u);
            g.getNodeList().get(u).weight = getNodeList().get(u).weight;
            g.getNodeList().get(u).visited = true;
            edge = getNodeList().get(u).firstedge;
            while (edge.adjvex != 0) {
                if (getNodeList().get(edge.adjvex).core >= (k - getNodeList().get(l).weight)) {
                    g.getNodeList().get(u).cd = g.getNodeList().get(u).cd + getNodeList().get(edge.adjvex).weight;
                    if (getNodeList().get(edge.adjvex).visited == false) {
                        queue.offer(edge.adjvex);
                        //g.getNodeList().get(edge.adjvex).core = getNodeList().get(edge.adjvex).core;
                        //System.out.println("u:"+u+"edge.adjvex:"+edge.adjvex);
                        EdgeNode a = new EdgeNode(u, null);
                        a.next = g.getNodeList().get(edge.adjvex).firstedge;
                        g.getNodeList().get(edge.adjvex).firstedge = a;

                        EdgeNode b = new EdgeNode(edge.adjvex, null);
                        b.next = g.getNodeList().get(u).firstedge;
                        g.getNodeList().get(u).firstedge = b;

                        getNodeList().get(edge.adjvex).visited = true;
                    } else if (getNodeList().get(edge.adjvex).over != true) {
                        // System.out.println("u:"+u+"edge.adjvex:"+edge.adjvex);
                        EdgeNode a = new EdgeNode(u, null);
                        a.next = g.getNodeList().get(edge.adjvex).firstedge;
                        g.getNodeList().get(edge.adjvex).firstedge = a;

                        EdgeNode b = new EdgeNode(edge.adjvex, null);
                        b.next = g.getNodeList().get(u).firstedge;
                        g.getNodeList().get(u).firstedge = b;

                    }

                }

                edge = edge.next;
            }
            getNodeList().get(u).over = true;

        }
        g.truevertex = verset.size();
        g.VerSet = (ArrayList<Integer>) verset.clone();
        return g;

    }

    /******************************
     删除一条边的核值维护算法
     *****************************/
    public void Delete() throws IOException {
        en = 0;
        long t = 0;
        Timer time = new Timer();
        Scanner scan = new Scanner(System.in);
        System.out.println("请输入要删除的边");
        int u = scan.nextInt();
        int v = scan.nextInt();
        time.start();
        int r = u;
        int l = v;
        int m = 1;
        if (getNodeList().get(u).core > getNodeList().get(v).core) {
            r = v;
            l = u;
        }
        if (getNodeList().get(u).core == getNodeList().get(v).core) {
            if (getNodeList().get(u).core - getNodeList().get(v).weight > getNodeList().get(v).core - getNodeList().get(u).weight) {
                r = v;
                l = u;
            } else {
                r = u;
                l = v;
            }
        }

        //g.getNodeList().get(r).cd = g.getNodeList().get(r).cd - g.getNodeList().get(l).weight;
        //g.getNodeList().get(l).cd = g.getNodeList().get(l).cd - g.getNodeList().get(r).weight;
        //G <---- G - (r,l)
        EdgeNode edge = getNodeList().get(r).firstedge;
        EdgeNode pre = edge;
        while (edge != null & edge.adjvex != l) {
            pre = edge;
            edge = edge.next;
        }
        if (edge != null & pre == edge) {
            getNodeList().get(r).firstedge = edge.next;
        } else if (edge != null) {
            pre.next = edge.next;
        }
        edge = getNodeList().get(l).firstedge;
        pre = edge;
        while (edge != null & edge.adjvex != r) {
            pre = edge;
            edge = edge.next;
        }
        if (edge != null & pre == edge) {
            getNodeList().get(l).firstedge = edge.next;
        } else if (edge != null) {
            pre.next = edge.next;
        }

        Graph g = this.DecSubcore(r, l);
        int k = g.getNodeList().get(r).core;
        //g.PrintGraph();
        //System.out.println(g.truevertex);
        while (en < g.truevertex) {//h里还有没被evict的点
            // System.out.println("en:" + en);
            int min = 0x7fffffff;
            for (int i = 0; i < g.VerSet.size(); i++) {

                if (g.getNodeList().get(g.VerSet.get(i)).evicted == true || g.getNodeList().get(g.VerSet.get(i)).cd == 0) {
                    continue;
                }


                if (min > g.getNodeList().get(g.VerSet.get(i)).cd) {
                    min = g.getNodeList().get(g.VerSet.get(i)).cd;
                    m = g.VerSet.get(i);

                }
            }
//            for (int i = 1; i < g.getNodeList().size(); i++) {
//                if (g.getNodeList().get(i).evicted == true || g.getNodeList().get(i).cd == 0) {
//                    continue;
//                }
//                if (min > g.getNodeList().get(i).cd) {
//                    min = g.getNodeList().get(i).cd;
//                    m = i;
//
//                }
//            }
            //System.out.println("min"+min+"m"+m);
            //System.out.println(g.getNodeList().get(m).core +"    "+g.getNodeList().get(r).core+"\n");

            g.getNodeList().get(m).core = g.getNodeList().get(m).cd;
            g.getNodeList().get(m).update = true;
            if (g.getNodeList().get(m).core > k) {
                break;
            }
            g.Evict(m);

        }
        for (int i = 0; i < g.VerSet.size(); i++) {
            if (g.getNodeList().get(g.VerSet.get(i)).update == true) {
                getNodeList().get(g.VerSet.get(i)).core = g.getNodeList().get(g.VerSet.get(i)).core;
            }

        }

        t = time.elapsed();
        OutputStream os = new FileOutputStream("Deleteoneedge.txt");
        PrintWriter pw = new PrintWriter(os);

        for (int i = 1; i < getNodeList().size(); i++) {
            if (getNodeList().get(i).core != 0) {
                pw.print(i + "的核数值为：" + getNodeList().get(i).core + "\n");
            }
        }

        pw.close();
        os.close();
        System.out.println("Delete耗费时间t:" + t);
    }


    /************************
     * 插入一个边集的核数维护办法
     ************************/
    public void EdgeSetInsert(Vector<GenericPair<Integer, Integer>> EdgeSet) throws IOException {
        int EdgeCore;
        int min;
        int max;
        int kk=0;
        Graph h;
        int iter = 0, t1 = 0, t2 = 0;
        Timer time = new Timer();
        long startTime = System.currentTimeMillis();   //获取开始时间
        while (EdgeSet.isEmpty() != true) {
            ++iter;
            System.out.println("第 "  + iter + "次迭代");
            int r = 0;
            min = 0x7fffffff;
            max = 0;
            for (int i = 0; i < EdgeSet.size(); i++) {
                //找边集中核数最小的边的核数
                int u = EdgeSet.get(i).getFirst();
                int v = EdgeSet.get(i).getSecond();
                if (getNodeList().get(u).core < getNodeList().get(v).core) {
                    EdgeCore = getNodeList().get(u).core;
                } else {
                    EdgeCore = getNodeList().get(v).core;
                }
                if (min > EdgeCore) {
                    min = EdgeCore;
                }

            }
            for (int i = 0; i < EdgeSet.size(); i++) {
                //找W_k(l)
                int u = EdgeSet.get(i).getFirst();
                int v = EdgeSet.get(i).getSecond();
                if (getNodeList().get(u).core > getNodeList().get(v).core) {
                    if (getNodeList().get(v).core == min) {
                        if (getNodeList().get(u).weight > max) {
                            max = getNodeList().get(u).weight;
                        }
                    }
                }
                if (getNodeList().get(u).core < getNodeList().get(v).core) {
                    if (getNodeList().get(u).core == min) {
                        if (getNodeList().get(v).weight > max) {
                            max = getNodeList().get(v).weight;
                        }
                    }
                }
                if (getNodeList().get(u).core == getNodeList().get(v).core) {
                    if (getNodeList().get(u).core == min) {
                        if (getNodeList().get(u).weight > getNodeList().get(v).weight) {
                            if (getNodeList().get(u).weight > max) {
                                max = getNodeList().get(u).weight;
                            }
                        } else {
                            if (getNodeList().get(v).weight > max) {
                                max = getNodeList().get(v).weight;
                            }
                        }
                    }
                }
            }

            Vector<GenericPair<Integer, Integer>> Ek = ComputeInsertEdgeSet(min, max, EdgeSet);

            for(int i =0; i < Ek.size(); i++){
                System.out.println(Ek.get(i).getFirst() +"  "+Ek.get(i).getSecond());
                kk++;
            }
            for (int i = 0; i < Ek.size(); i++) {
                int Fir = Ek.get(i).getFirst();
                int Sec = Ek.get(i).getSecond();
                if (getNodeList().get(Fir).core == min) {
                    r = Fir;
                    break;
                }
                if (getNodeList().get(Sec).core == min) {
                    r = Sec;
                    break;
                }
            }

            InsertToG(Ek);

            for (int i = 0; i < Ek.size(); i++) {
                EdgeSet.removeElement(Ek.get(i));
            }

            if (EdgeSet.isEmpty() == true) {
                System.out.println("EdgeSet empty");
            }

            h = IncSubgraph(getNodeList().get(r).core);

            for (int num = 1; num < getNodeList().size(); num++) {
                h.getNodeList().get(num).evicted = false;
            }
            en = 0;
            time.start();
            while (en < h.truevertex) {
                int v = h.VerSet.get(0);
                int minimum = 0x7fffffff;
                for (int i = 0; i < h.VerSet.size(); i++) {
                    if (h.getNodeList().get(h.VerSet.get(i)).evicted == true) {
                        continue;
                    }
                    if (minimum > h.getNodeList().get(h.VerSet.get(i)).cd) {
                        minimum = h.getNodeList().get(h.VerSet.get(i)).cd;
                        v = h.VerSet.get(i);

                    }
                }
                t1 += time.elapsed();
                time.start();


                h.getNodeList().get(v).core = h.getNodeList().get(v).cd;
                h.getNodeList().get(v).update = true;
                if (h.getNodeList().get(v).core >= (min + max)) {
                    break;
                }
                h.Evict(v);

                t2 += time.elapsed();

            }
            for (int i = 0; i < h.VerSet.size(); i++) {
                if (h.getNodeList().get(h.VerSet.get(i)).update == true) {
                    getNodeList().get(h.VerSet.get(i)).core = h.getNodeList().get(h.VerSet.get(i)).core;
                }

            }
            ComputeWd();
        }
        long endTime = System.currentTimeMillis(); //获取结束时间

        System.out.println("程序运行时间： " + (endTime - startTime) + "ms" + "   ,t1:" + t1 + ",t2:" + t2+",kk:" +kk);
        OutputStream os = new FileOutputStream("InsertCoreNumber1.txt");
        PrintWriter pw = new PrintWriter(os);

        for (int i = 1; i < getNodeList().size(); i++) {
            if (getNodeList().get(i).core != 0) {
                pw.print(i + "的核数值为：" + getNodeList().get(i).core + "\n");
            }
        }

        pw.close();
        os.close();

    }

    public void InsertToG(Vector<GenericPair<Integer, Integer>> Ek) {
        for (int i = 0; i < Ek.size(); i++) {
            int u = Ek.get(i).getFirst();
            int v = Ek.get(i).getSecond();
            EdgeNode a = new EdgeNode(u, null);
            a.next = getNodeList().get(v).firstedge;
            getNodeList().get(v).firstedge = a;

            EdgeNode b = new EdgeNode(v, null);
            b.next = getNodeList().get(u).firstedge;
            getNodeList().get(u).firstedge = b;

            getNodeList().get(v).cd = getNodeList().get(v).cd + getNodeList().get(u).weight;
            getNodeList().get(u).cd = getNodeList().get(u).cd + getNodeList().get(v).weight;


        }

    }


    public Vector<GenericPair<Integer, Integer>> ComputeInsertEdgeSet(int min, int max, Vector<GenericPair<Integer, Integer>> EdgeSet) {
        Vector<GenericPair<Integer, Integer>> E = new Vector<GenericPair<Integer, Integer>>();
        boolean Color[] = new boolean[getNodeList().size()];
        for (int i = 0; i < EdgeSet.size(); i++) {
            int u = EdgeSet.get(i).getFirst();
            int v = EdgeSet.get(i).getSecond();
            if (getNodeList().get(u).core <= getNodeList().get(v).core) {
                if (getNodeList().get(u).core == min && Color[u] == false) {
                    E.add(EdgeSet.get(i));
                    Color[u] = true;
                }
                if (getNodeList().get(u).core > min && getNodeList().get(u).core <= (min + max)) {
                    if ((getNodeList().get(u).wd + getNodeList().get(v).weight <= (min + max)) && Color[u] == false) {
                        E.add(EdgeSet.get(i));
                        Color[u] = true;
                    }
                }
            }
            if (getNodeList().get(u).core > getNodeList().get(v).core) {
                if (getNodeList().get(v).core == min && Color[v] == false) {
                    E.add(EdgeSet.get(i));
                    Color[v] = true;
                }
                if (getNodeList().get(v).core > min && getNodeList().get(v).core <= (min + max)) {
                    if ((getNodeList().get(v).wd + getNodeList().get(u).weight <= (min + max)) && Color[v] == false) {
                        E.add(EdgeSet.get(i));
                        Color[v] = true;
                    }
                }
            }
        }
        return E;
    }

    public void ComputeWd() {
        for (int i = 1; i < getNodeList().size(); i++) {
            EdgeNode edge = getNodeList().get(i).firstedge;
            while (edge.adjvex != 0) {
                if (getNodeList().get(edge.adjvex).core >= getNodeList().get(i).core) {
                    getNodeList().get(i).wd = getNodeList().get(i).wd + getNodeList().get(edge.adjvex).weight;
                }
                edge = edge.next;
            }
        }
    }

    public Vector<GenericPair<Integer, Integer>> GetEdgeSetFromFile() {
        Vector<GenericPair<Integer, Integer>> ES = new Vector<GenericPair<Integer, Integer>>();
        int flag = 0;
        Scanner scan = new Scanner(System.in);
        System.out.println("请输入要处理的边的文件名:");

        String filename = scan.nextLine();
        File file = new File(filename);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            //一次读一行，直到读入文件结束
            while ((tempString = reader.readLine()) != null) {
                flag = 0;
                GenericPair<Integer, Integer> edge = new GenericPair<Integer, Integer>();
                String temp[] = tempString.split("\\s+");
                int node1 = Integer.parseInt(temp[0]);
                int node2 = Integer.parseInt(temp[1]);
//                for(int i = 0; i < ES.size(); i++){
//                    if((ES.get(i).getFirst() == node1 && ES.get(i).getSecond() == node2)||(ES.get(i).getSecond() == node1 && ES.get(i).getFirst() == node2)){
//                        flag = 1;
//                        break;
//                    }
//                }
//                if(flag == 1){
//                    continue;
//                }
                edge.setFirst(node1);
                edge.setSecond(node2);
                ES.add(edge);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException el) {

                }
            }
        }
        return ES;
    }

    public void FindSubgraph(int LowBound) {

    }

    public void EdgeSetDelete(Vector<GenericPair<Integer, Integer>> EdgeSet) throws IOException {
        int EdgeCore;
        int min;
        int max;
        int kk =0;
        Timer t = new Timer();
        // Vector<Integer> VertexSet = new Vector<>();
        int iter = 0;
        long start = System.currentTimeMillis();
        long t1 = 0, t2 = 0, t3 = 0, t4 = 0, t5 = 0, t6 = 0, t7 = 0;
        //获取开始时间
        while (EdgeSet.isEmpty() != true) {

            ++iter;
            System.out.println("第 "  + iter + "次迭代");
            min = 0x7fffffff;
            max = 0;

            t.start();
            for (int i = 0; i < EdgeSet.size(); i++) {
                //找边集中核数最小的边的核数
                //System.out.println("i:"+i+"size"+EdgeSet.size());
                int u = EdgeSet.get(i).getFirst();
                int v = EdgeSet.get(i).getSecond();
                if (getNodeList().get(u).core < getNodeList().get(v).core) {
                    EdgeCore = getNodeList().get(u).core;
                } else {
                    EdgeCore = getNodeList().get(v).core;
                }
                if (min > EdgeCore) {
                    min = EdgeCore;
                }

            }

            t1 += t.elapsed();

            t.start();
            for (int i = 0; i < EdgeSet.size(); i++) {
                //找W_k(l)
                int u = EdgeSet.get(i).getFirst();
                int v = EdgeSet.get(i).getSecond();
                if (getNodeList().get(u).core > getNodeList().get(v).core) {
                    if (getNodeList().get(v).core == min) {
                        if (getNodeList().get(u).weight > max) {
                            max = getNodeList().get(u).weight;
                        }
                    }
                }
                if (getNodeList().get(u).core < getNodeList().get(v).core) {
                    if (getNodeList().get(u).core == min) {
                        if (getNodeList().get(v).weight > max) {
                            max = getNodeList().get(v).weight;
                        }
                    }
                }
                if (getNodeList().get(u).core == getNodeList().get(v).core) {
                    if (getNodeList().get(u).core == min) {
                        if (getNodeList().get(u).weight > getNodeList().get(v).weight) {
                            if (getNodeList().get(u).weight > max) {
                                max = getNodeList().get(u).weight;
                            }
                        } else {
                            if (getNodeList().get(v).weight > max) {
                                max = getNodeList().get(v).weight;
                            }
                        }
                    }
                }
            }

            t2 += t.elapsed();

            Vector<GenericPair<Integer, Integer>> Ek = ComputeDeleteEdgeSet(min, max, EdgeSet);

            for(int i =0; i < Ek.size(); i++){
                System.out.println(Ek.get(i).getFirst() +"  "+Ek.get(i).getSecond());
                 kk++;
            }
            t.start();

            t3 += t.elapsed();

            t.start();
            DeleteFromG(Ek);
            for (int i = 0; i < Ek.size(); i++) {
                EdgeSet.removeElement(Ek.get(i));
            }


            Graph h =IncSubgraph(min - max);
            // Graph h = DecSubcore(r, l);
            t4 += t.elapsed();

            for (int num = 1; num < getNodeList().size(); num++) {
                h.getNodeList().get(num).evicted = false;
            }
            en = 0;
            t.start();
            Timer timer = new Timer();
            while (en < h.truevertex) {

                int v = h.VerSet.get(0);
                int minimum = 0x7fffffff;
                timer.start();

                for (int i = 0; i < h.VerSet.size(); i++) {

                    if (h.getNodeList().get(h.VerSet.get(i)).evicted == true) {
                        continue;
                    }


                    if (minimum > h.getNodeList().get(h.VerSet.get(i)).cd) {
                        minimum = h.getNodeList().get(h.VerSet.get(i)).cd;
                        v = h.VerSet.get(i);

                    }
                }

                //System.out.println(getNodeList().size());
                t6 += timer.elapsed();

                timer.start();


                h.getNodeList().get(v).core = h.getNodeList().get(v).cd;
                //getNodeList().get(v).core = h.getNodeList().get(v).core;
                h.getNodeList().get(v).update = true;
                if (h.getNodeList().get(v).core > min) {
                    break;
                }
                h.Evict(v);
                t7 += timer.elapsed();
            }
            t5 += t.elapsed();

            for (int i = 1; i < getNodeList().size(); i++) {
                if (h.getNodeList().get(i).update == true) {
                    getNodeList().get(i).core = h.getNodeList().get(i).core;
                }

            }
            ComputeWd();
            // VertexSet.removeAllElements();
           // System.out.println("t1: " + t1 + ", t2: " + t2 + ",t3: " + t3 + ",t4: " + t4 + ",t5:" + t5 + ",t6:" + t6 + ",t7:" + t7+"  kk:"+kk);
        }
        long end = System.currentTimeMillis(); //获取结束时间
        System.out.println("t1: " + t1 + ", t2: " + t2 + ",t3: " + t3 + ",t4: " + t4 + ",t5:" + t5 + ",t6:" + t6 + ",t7:" + t7+"  kk:"+kk);
        System.out.println("程序运行时间： " + (end - start) + "ms");
        OutputStream os = new FileOutputStream("DeleteCoreNumber1.txt");
        PrintWriter pw = new PrintWriter(os);

        for (int i = 1; i < getNodeList().size(); i++) {
            if (getNodeList().get(i).core != 0) {
                pw.print(i + "的核数值为：" + getNodeList().get(i).core + "\n");
            }
        }

        pw.close();
        os.close();
    }

    public Vector<GenericPair<Integer, Integer>> ComputeDeleteEdgeSet(int min, int max, Vector<GenericPair<Integer, Integer>> EdgeSet) {
        Vector<GenericPair<Integer, Integer>> E = new Vector<GenericPair<Integer, Integer>>();
        boolean Color[] = new boolean[getNodeList().size()];
        for (int i = 0; i < EdgeSet.size(); i++) {
            int u = EdgeSet.get(i).getFirst();
            int v = EdgeSet.get(i).getSecond();
            if (getNodeList().get(u).core <= getNodeList().get(v).core) {
                if (getNodeList().get(u).core == min && Color[u] == false) {
                    E.add(EdgeSet.get(i));
                    Color[u] = true;
                }
                if (getNodeList().get(u).core >= (min - max) && getNodeList().get(u).core < min) {
                    if ((getNodeList().get(u).wd - getNodeList().get(v).weight >= (min - max)) && Color[u] == false) {
                        E.add(EdgeSet.get(i));
                        Color[u] = true;
                    }
                }
            }
            if (getNodeList().get(u).core > getNodeList().get(v).core) {
                if (getNodeList().get(v).core == min && Color[v] == false) {
                    E.add(EdgeSet.get(i));
                    Color[v] = true;
                }
                if (getNodeList().get(v).core >= (min - max) && getNodeList().get(v).core < min) {
                    if ((getNodeList().get(v).wd - getNodeList().get(u).weight >= (min - max)) && Color[v] == false) {
                        E.add(EdgeSet.get(i));
                        Color[v] = true;
                    }
                }
            }
        }
        // for (int i = 0; i < E.size(); i++) {
        //   System.out.println(E.get(i).getFirst() + " 分隔  " + E.get(i).getSecond());
        //}
        return E;
    }

    public void DeleteFromG(Vector<GenericPair<Integer, Integer>> Ek) {
        int r;
        int l;
        int u;
        int v;
        for (int i = 0; i < Ek.size(); i++) {
            u = Ek.get(i).getFirst();
            v = Ek.get(i).getSecond();
            r = u;
            l = v;
            if (getNodeList().get(u).core > getNodeList().get(v).core) {
                r = v;
                l = u;
            }
            if (getNodeList().get(u).core == getNodeList().get(v).core) {
                if (getNodeList().get(u).core - getNodeList().get(v).weight > getNodeList().get(v).core - getNodeList().get(u).weight) {
                    r = v;
                    l = u;
                } else {
                    r = u;
                    l = v;
                }
            }

            getNodeList().get(r).cd = getNodeList().get(r).cd - getNodeList().get(l).weight;

            getNodeList().get(l).cd = getNodeList().get(l).cd - getNodeList().get(r).weight;
            //G <---- G - (r,l)
            EdgeNode edge = getNodeList().get(r).firstedge;
            EdgeNode pre = edge;
            while (edge != null & edge.adjvex != l) {
                pre = edge;
                edge = edge.next;
            }
            if (edge != null & pre == edge) {
                getNodeList().get(r).firstedge = edge.next;
            } else if (edge != null) {
                pre.next = edge.next;
            }
            edge = getNodeList().get(l).firstedge;
            pre = edge;
            while (edge != null & edge.adjvex != r) {
                pre = edge;
                edge = edge.next;
            }
            if (edge != null & pre == edge) {
                getNodeList().get(l).firstedge = edge.next;
            } else if (edge != null) {
                pre.next = edge.next;
            }
        }
    }

    public void RoundWeight() {
        Scanner scan = new Scanner(System.in);
        System.out.println("请输入要读取的文件名:");
        String filename = scan.nextLine();
        File file = new File(filename);
        BufferedReader reader = null;
        BufferedReader r = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            //一次读一行，直到读入文件结束
            //第一次读文件，找到图中序号最大的端点，这个最大值用来对图进行初始化
            while ((tempString = reader.readLine()) != null) {
                String temp[] = tempString.split("\\s+");
                int node = Integer.parseInt(temp[0]);
                float weight = Float.parseFloat(temp[1]);
                int nodeWeight = Math.round(weight * 2000);
                getNodeList().get(node).weight = nodeWeight;

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException el) {

                }
            }
        }

    }

    public Graph IncSubgraph(int Cnum) {
        Graph g = new Graph(this.vertexNum);
        int vn = 0;
        for (int i = 1; i < getNodeList().size(); i++) {
            getNodeList().get(i).visited = false;
            getNodeList().get(i).over = false;
            if(getNodeList().get(i).core >= Cnum && getNodeList().get(i).weight > 0){
                vn ++;
                g.VerSet.add(i);
            }
        }

        //EdgeNode e;
        for(int i =0; i < g.VerSet.size(); i++){
            int u = g.VerSet.get(i);

            EdgeNode edge = getNodeList().get(u).firstedge;
            g.getNodeList().get(u).weight = getNodeList().get(u).weight;
           //g.getNodeList().get(u).core = getNodeList().get(u).core;
            getNodeList().get(u).visited = true;
           while(edge.adjvex!=0){
               if(g.VerSet.contains(edge.adjvex)){
                   g.getNodeList().get(u).cd =g.getNodeList().get(u).cd + getNodeList().get(edge.adjvex).weight;
                   if(getNodeList().get(edge.adjvex).visited == false){

                       EdgeNode a = new EdgeNode(u, null);
                       a.next = g.getNodeList().get(edge.adjvex).firstedge;
                       g.getNodeList().get(edge.adjvex).firstedge = a;

                       EdgeNode b = new EdgeNode(edge.adjvex, null);
                       b.next = g.getNodeList().get(u).firstedge;
                       g.getNodeList().get(u).firstedge = b;
                       getNodeList().get(edge.adjvex).visited = true;
                   }
                   else if (getNodeList().get(edge.adjvex).over != true) {
                       // System.out.println("u:"+u+"edge.adjvex:"+edge.adjvex);
                       EdgeNode a = new EdgeNode(u, null);
                       a.next = g.getNodeList().get(edge.adjvex).firstedge;
                       g.getNodeList().get(edge.adjvex).firstedge = a;

                       EdgeNode b = new EdgeNode(edge.adjvex, null);
                       b.next = g.getNodeList().get(u).firstedge;
                       g.getNodeList().get(u).firstedge = b;

                   }
               }

               edge = edge.next;
           }
            getNodeList().get(u).over = true;
        }
        g.truevertex = vn;
        return g;
    }


  public void findEdge (int n) throws IOException {
      OutputStream os = new FileOutputStream(n+"wk.txt");
      PrintWriter pw = new PrintWriter(os);
        for(int i = 1; i < getNodeList().size(); i++){
            if(getNodeList().get(i).core == n){

                EdgeNode e = getNodeList().get(i).firstedge;
                while(e.adjvex != 0){
                    if(getNodeList().get(e.adjvex).core > n)
                        pw.println(i + "   " + e.adjvex);
                     if(getNodeList().get(e.adjvex).core == n)
                         if (i < e.adjvex)
                             pw.println(i + "   "+ e.adjvex);
                    e = e.next;
                }
            }
        }
        pw.close();
        os.close();
  }


}


