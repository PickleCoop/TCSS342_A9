import java.util.PriorityQueue;

public class MyPriorityQueue<Type extends Comparable<Type>> {

    protected MyArrayList<Type> heap;

    public MyPriorityQueue() {
        this.heap = new MyArrayList<>();
    }

    public void insert(Type item){
        if (heap.size() == 0){
            heap.insert(item, 0);
        } else {
            heap.insert(item, heap.size());
        }
        bubbleUp();

    }

    public Type removeMin(){
        Type store;
        Type min = heap.get(0);
        heap.set(0, heap.get(heap.size() - 1));
        heap.set(heap.size() - 1, min);
        store = heap.remove(heap.size() - 1);
        sinkDown();
        return store;

    }

    public Type min(){
        if (heap == null){
            return null;
        } else {
            return heap.get(0);
        }
    }

    public int size(){
        return heap.size();
    }

    public boolean isEmpty(){
        return heap.isEmpty();
    }

    public String toString(){
        if (heap == null){
            return "[]";
        } else {
            return heap.toString();
        }
    }

    protected void bubbleUp(){
        int index = heap.size() - 1;
        Type temp;
        while (index > 0){
            if (heap.get(index).compareTo(heap.get(parent(index))) < 0){
                temp = heap.get(parent(index));
                heap.set(parent(index), heap.get(index));
                heap.set(index, temp);
            } else {
                return;
            }
            index = parent(index);
        }
    }

    protected void sinkDown() {
        int index = 0;
        Type temp = null;
        while (left(index) < heap.size()) {
            int smallestChild = left(index);
            if ((right(index) < heap.size()) && (heap.get(right(index)).compareTo(heap.get(smallestChild)) < 0)) {
                smallestChild = right(index);
            }
            if (heap.get(index).compareTo(heap.get(smallestChild)) > 0) {
                temp = heap.get(index);
                heap.set(index, heap.get(smallestChild));
                heap.set(smallestChild, temp);
            } else {
                return;
            }
            index = smallestChild;
        }
    }

    protected int parent(int index){
        return (index - 1) / 2;
    }

    protected int right(int index){
        return 2 * index + 2;
    }

    protected int left(int index){
        return 2 * index + 1;
    }

}
