import java.util.LinkedList;

public class Testqueue {
    public static void main(String[]args){
        LinkedList<Integer> q = new LinkedList<>();
        q.offer(1);
        q.offer(2);
        q.offer(3);
        int i =q.poll();
        System.out.println(q);
    }
}
