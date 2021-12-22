public class Main {

    public static void main(String[] args) {
        SkipList mySkipList = new SkipList();

        // inserting test data
        mySkipList.insert(2); mySkipList.insert(10); mySkipList.insert(15);
        mySkipList.insert(16); mySkipList.insert(31); mySkipList.insert(71);
        mySkipList.insert(86); mySkipList.insert(89); mySkipList.insert(91);
        mySkipList.insert(96);

        System.out.println("Number of Layers = " + mySkipList.getLayers());
        System.out.print("\n");

        for(int i = mySkipList.getLayers() - 1; i >= 0; i--)
            mySkipList.printLayer(i);
        System.out.print("\n");

        // deleting an existing number
        mySkipList.delete(91);

        for(int i = mySkipList.getLayers() - 1; i >= 0; i--)
            mySkipList.printLayer(i);
        System.out.print("\n");

        // deleting a non-existing number
        mySkipList.delete(101);

        for(int i = mySkipList.getLayers() - 1; i >= 0; i--)
            mySkipList.printLayer(i);
        System.out.print("\n");
    }
}
