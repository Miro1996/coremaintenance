package GraphLink;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.io.BufferedReader;
public class CreatGraph {

    public Graph getGraph(){
        int edgeNode[] = new int[2];
        int size = 0;
        int line = 0;
        int soph = 0;
        Graph g = null;
        Scanner scan = new Scanner(System.in);
        System.out.println("请输入要读取的文件名:");
        String filename = scan.nextLine();
        File  file = new File(filename);
        BufferedReader reader = null;
        BufferedReader r = null;

        try{
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            //一次读一行，直到读入文件结束
            //第一次读文件，找到图中序号最大的端点，这个最大值用来对图进行初始化
            while ((tempString = reader.readLine()) != null){
                line ++;
                String temp[] = tempString.split("\\s+");
                edgeNode[0] = Integer.parseInt(temp[0]);
                edgeNode[1] = Integer.parseInt(temp[1]);
                if(edgeNode[0] > size){
                    size = edgeNode[0];
                }
                if(edgeNode[1] > size){
                    size = edgeNode[1];
                }

            }
            g= new Graph(size + 1,line);//图的初始化
            reader.close();//关闭文件
            //第二次读文件，此次操作完成了图的邻接表结构构造。
            r = new BufferedReader(new FileReader(file));
            while((tempString = r.readLine()) != null){
                soph =  0;
                String temp[] =  tempString.split("\\s+");
                edgeNode[0] = Integer.parseInt(temp[0]);
                edgeNode[1] = Integer.parseInt(temp[1]);
                //EdgeNode e = g.getNodeList().get(edgeNode[0]).firstedge;
//                while(e.adjvex != 0){
//                    if(e.adjvex == edgeNode[1]){
//                        soph = 1;
//                        break;
//                    }
//                }
//                if(soph == 1){
//                    continue;
//                }
                EdgeNode a = new EdgeNode(edgeNode[0],null);
                a.next = g.getNodeList().get(edgeNode[1]).firstedge;
                g.getNodeList().get(edgeNode[1]).firstedge = a;

                EdgeNode b = new EdgeNode(edgeNode[1],null);
                b.next = g.getNodeList().get(edgeNode[0]).firstedge;
                g.getNodeList().get(edgeNode[0]).firstedge = b;

                }
                r.close();

        }catch (IOException e){
            e.printStackTrace();
        }finally{
            if(reader != null){
                try{
                    reader.close();
                }catch (IOException el){

                }
            }
        }

        return g;
    }




}
