import java.util.Random;

public class SkipList {
    private Node head;
    private Node tail;

    private final int NEGATIVE_INF = Integer.MIN_VALUE;
    private final int POSITIVE_INF = Integer.MAX_VALUE;
    private final int MAX_NUM_LEVELS = 4;

    private int numOfLevels = 0;
    private Random coinFlip = new Random();

    public SkipList(){
        this.head = new Node(NEGATIVE_INF);
        this.tail = new Node(POSITIVE_INF);
        head.next = tail;
        tail.prev = head;
    }

    public Node search(int key){
        Node current_node;

        for(current_node = head; current_node.below != null; current_node = current_node.below) {
            while (key >= current_node.next.key) {
                current_node = current_node.next;
            }
        }

        return current_node;
    }

    private void createLevel() {
        Node _head = new Node(NEGATIVE_INF);
        Node _tail = new Node(POSITIVE_INF);

        _head.next = _tail;
        _tail.prev = _head;

        _head.below = this.head;
        _tail.below = this.tail;
        this.head.above = _head;
        this.tail.above = _tail;

        this.head = _head;
        this.tail = _tail;
    }

    private void increaseLevel(int level){
        if(level >= this.numOfLevels){
            this.numOfLevels++;
            this.createLevel();
        }
    }

    private void setBeforeAndAfterReferences(Node dummyNode, Node newNode) {
        newNode.next = dummyNode.next;
        newNode.prev = dummyNode;
        dummyNode.next.prev = newNode;
        dummyNode.next = newNode;
    }

    private void setAboveAndBelowReferences(Node position, int key, Node newNode, Node nodeBeforeNewNode) {
        if(nodeBeforeNewNode != null){
            while (nodeBeforeNewNode.next.key != key){
                nodeBeforeNewNode = nodeBeforeNewNode.next;
            }

            newNode.below = nodeBeforeNewNode.next;
            nodeBeforeNewNode.next.above = newNode;
        }

        if(position != null){
            if(position.next.key == key){
                newNode.above = position.next;
            }
        }
    }

    private Node insertAfterAbove(Node position, Node dummyNode, int key) {
        Node newNode = new Node(key);
        Node nodeBeforeNewNode = position.below.below;

        this.setBeforeAndAfterReferences(dummyNode, newNode);
        this.setAboveAndBelowReferences(position, key, newNode, nodeBeforeNewNode);

        return newNode;
    }

    public Node insert(int key){
        Node position = this.search(key);
        Node dummyNode;

        int level = -1;
        int number_of_heads = -1;

        if(position.key == key){
            return position;
        }

        do {
            number_of_heads++;
            level++;

            this.increaseLevel(level);

            dummyNode = position;

            while (position.above == null){
                position = position.prev;
            }

            position = position.above;
            dummyNode = this.insertAfterAbove(position, dummyNode, key);

        } while (this.coinFlip.nextBoolean() == true && level < this.MAX_NUM_LEVELS - 2);

        return dummyNode;
    }

    private void removeNodeReferences(Node nodeToBeRemoved) {
        Node afterNodeToBeRemoved = nodeToBeRemoved.next;
        Node beforeNodeToBeRemoved = nodeToBeRemoved.prev;

        beforeNodeToBeRemoved.next = afterNodeToBeRemoved;
        afterNodeToBeRemoved.prev = beforeNodeToBeRemoved;
    }

    public Node delete(int key){
        Node nodeToBeRemoved = this.search(key);

        if(nodeToBeRemoved.key != key) return null;

        this.removeNodeReferences(nodeToBeRemoved);

        while(nodeToBeRemoved != null){
            this.removeNodeReferences(nodeToBeRemoved);

            if(nodeToBeRemoved.above != null){
                nodeToBeRemoved = nodeToBeRemoved.above;
            }
            else {
                break;
            }
        }

        return nodeToBeRemoved;
    }

    public int getLayers(){ return this.numOfLevels + 1; }

    public void printLayer(int wantedLevel){
        Node startNode = this.head;
        Node highestLevel = startNode;

        int level = numOfLevels;

        while (highestLevel != null){
            if(wantedLevel == level) System.out.print("LEVEL #" + level + ": ");

            while (startNode != null){

                if(startNode.key == NEGATIVE_INF){
                    if(wantedLevel == level) System.out.print("[-INF]");
                }
                else if (startNode.key == POSITIVE_INF){
                    if(wantedLevel == level) System.out.print("[+INF]\n");
                }
                else{
                    if(wantedLevel == level) System.out.print("[" + startNode.key + "]");
                }

                if (startNode.next != null){
                    if(wantedLevel == level) System.out.print(" - ");
                }

                startNode = startNode.next;
            }

            highestLevel = highestLevel.below;
            startNode = highestLevel;
            level--;
        }
    }

}
