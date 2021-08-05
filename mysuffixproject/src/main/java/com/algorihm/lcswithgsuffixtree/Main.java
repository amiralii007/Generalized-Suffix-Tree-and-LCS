package main.java.com.algorihm.lcswithgsuffixtree;


import main.java.com.algorihm.lcswithgsuffixtree.suffixtree.GeneralizedSuffixTreeImp;
import main.java.com.algorihm.lcswithgsuffixtree.toolsAndLCS.tools;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        System.out.println("...... Hi , this is the test of suffix tree and LCS Algorithm ......\n" +
                "   first of all we show you our test case and the answer\n" +
                "   after that you can put your two string and challenge our code *.*\n" +
                "   our first string is : MichellAngello\n" +
                "   and the next string is : AngelinHeaven");
        
        GeneralizedSuffixTreeImp tree = new GeneralizedSuffixTreeImp();
        tree.put("MichellAngello", 0);
        tree.put("AngelinHeaven", 1);

        System.out.println("**=> so the answer of our test case is : " + new tools().myLCS(tree.getRoot()));

        System.out.println("    now it's your turn please input two strings to see answers\n" +
                "please input first one");
        Scanner scanner = new Scanner(System.in);

        GeneralizedSuffixTreeImp tree1 = new GeneralizedSuffixTreeImp();
        tree1.put(scanner.nextLine(), 0);

        System.out.println("good now please input next one");
        tree1.put(scanner.nextLine(), 1);

        System.out.println("**=> so the answer of our test case is : " + new tools().myLCS(tree1.getRoot()));

    }
}
