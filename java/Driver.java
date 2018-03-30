
public class Driver {

    public static void main(String[] args) throws Exception {
        UnitMultiplication multiplication = new UnitMultiplication();
        UnitSum sum = new UnitSum();

        //hadoop jar pr.jar Driver /transition /pagerank /output 3
        //args0: dir of transition.txt
        //args1: dir of PageRank.txt
        //args2: dir of unitMultiplication result
        //args3: times of convergence
        String transitionMatrix = args[0];
        String prMatrix = args[1];  //prefix
        String unitState = args[2]; //subPr prefix
        int count = Integer.parseInt(args[3]);  // iteration #
        for(int i=0;  i<count;  i++) {
            String[] args1 = {transitionMatrix, prMatrix+i, unitState+i};
            multiplication.main(args1);
            String[] args2 = {unitState + i, prMatrix+(i+1)};
            sum.main(args2);
        }
    }
}
