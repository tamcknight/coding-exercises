import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;



/**
 * Created by tim on 5/3/17.
 */
class Solution {

    Node root;
    Map<String, List<String>> parentChildMap;

    Solution() {
        parentChildMap = new HashMap<String, List<String>>();
    }

    class Node {
        private String key;
        private Node left;
        private Node right;

        Node(String aKey) {
            key = aKey;
            right = null;
            left = null;
        }

    }


    public enum ErrorCode {
        E1("Invalid Input Format"), E2("Duplicate Pair"), E3("Parent has more than two children"),
        E4("Multiple Roots"), E5("Tree contains a cycle");
        private String value;


        private ErrorCode(String value) {
            this.value = value;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //String data = "(K,M) (P,L) (L,S) (Y,A) (T,P) (T,K) (L,E) (K,Y)";

        String data = scanner.nextLine();
        Solution tree = new Solution();
        ErrorCode errorCode = tree.validateAndOrganize(data);

        if (errorCode == null) {
            tree.createTree();

        }
        if (errorCode != null){
            System.out.println(errorCode);

        } else {
            System.out.println(tree.toString());
        }


        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */



    }

    private ErrorCode validateAndOrganize(String input) {
        ErrorCode errorCode = null;

        if (input.endsWith(" ") || input.startsWith(" ")) {
            errorCode = ErrorCode.E1;
        }

        if (errorCode == null) {

            //check case and formate
            String[] inputArray = input.split(" ");


            for (String pair : inputArray) {
                if (!pair.matches("\\([A-Z],[A-Z]\\)")) {
                    errorCode = ErrorCode.E1;
                } else {

                    pair = pair.replace("(", "");
                    pair = pair.replace(")", "");
                    String[] pairValues = pair.split(",");

                    if (parentChildMap.containsKey(pairValues[0])) {
                        for (String value : parentChildMap.get(pairValues[0])) {

                            if (value.equals(pairValues[1])) {
                                errorCode = ErrorCode.E2;
                                break;
                            }
                        }
                    }


                    //add the children to the parents to track
                    if (parentChildMap.containsKey(pairValues[0])) {
                        //add the children to the parents to track
                        parentChildMap.get(pairValues[0]).add(pairValues[1]);
                    } else {
                        List<String> list = new ArrayList<>();
                        list.add(pairValues[1]);
                        parentChildMap.put(pairValues[0], list);
                    }
                }
            }

            if (errorCode == null) {
                for (String key : parentChildMap.keySet()) {

                    if (parentChildMap.get(key).size() > 2) {
                        errorCode = ErrorCode.E3;
                        break;
                    }

                }
            }
            //see if any children have 2 parents.

            //todo fix complexity
            if (errorCode == null) {
                List <String> roots = new ArrayList<>();
                for (String key : parentChildMap.keySet()) {

                    //go through and see if the key is a child
                    boolean isAlsoAChild = false;
                    for (String key2 : parentChildMap.keySet()){

                        if (parentChildMap.get(key2).contains(key)){
                            isAlsoAChild = true;
                        }

                    }
                    if (!isAlsoAChild){
                        roots.add(key);
                    }
                }
                if (roots.size() > 1){
                    errorCode = ErrorCode.E4;
                }
            }

            if (errorCode == null) {

                List <String> parents = new ArrayList<>();
                for (String key : parentChildMap.keySet()) {
                    for (String value :parentChildMap.get(key) ){
                        parents.add(key);
                        for (String otherKey : parentChildMap.keySet()){
                            if (otherKey != key && parentChildMap.get(otherKey).contains(value)){
                                parents.add(otherKey);
                            }

                        }
                        if (parents.size() > 1){
                            errorCode = ErrorCode.E5;
                            break;
                        } else {
                            parents.clear();
                        }
                    }

                }
            }

        }
        return errorCode;
    }

    void add(Node node, List<String> list ) {
        //sort so they're alphabetical

            //sort them alphabetically
            Collections.sort(list, new Comparator<String>() {
                @Override
                public int compare(String value1, String value2) {
                    return value1.compareTo(value2);
                }
            });
            for (String child : list) {
                if (node.left == null) {
                    node.left = new Node(child);
                    if (parentChildMap.containsKey(child)) {
                        add(node.left, parentChildMap.get(child));
                    }
                } else {
                    node.right = new Node(child);
                    if (parentChildMap.containsKey(child)) {
                        add(node.right, parentChildMap.get(child));
                    }
                }
            }

    }


    public void createTree() {
        if (root == null) {

            String rootString = findUltimateRoot();
            root = new Node(rootString);
            add(root, parentChildMap.get(rootString));
        }
    }

     Node find(String value, Node node) {

        if(node != null){
            if(node.key.equals(value)){
                return node;
            } else {
                Node foundNode = find(value, node.left);
                if(foundNode == null) {
                    foundNode = find(value, node.right);
                }
                return foundNode;
            }
        } else {
            return null;
        }
    }

    @Override
     public String toString() {
        StringBuilder sb = new StringBuilder();
        toString(sb, root);
        return sb.toString();
    }

    String findUltimateRoot(){
        String rootString = null;


        for (String key : parentChildMap.keySet()) {
            boolean isAlsoAChild = false;
            //go through and see if the key is a child
            for (String key2 : parentChildMap.keySet()){

                if (parentChildMap.get(key2).contains(key)){
                    isAlsoAChild = true;
                }

            }
            if (!isAlsoAChild){
                rootString = key;
            }
        }

        return rootString;
    }

    StringBuilder toString(StringBuilder string, Node node) {



        if (node != null) {
            string.append("("+node.key);
            toString(string.append(""), node.left);
            toString(string.append(""), node.right);
            string.append(")");
        }
        return string;
    }


}
