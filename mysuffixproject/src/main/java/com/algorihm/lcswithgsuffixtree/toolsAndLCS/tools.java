/**
 * Copyright 2012 Alessandro Bahgat Shehata
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package main.java.com.algorihm.lcswithgsuffixtree.toolsAndLCS;


import main.java.com.algorihm.lcswithgsuffixtree.suffixtree.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
/**
 * a class for all tools method such as method to remove unexpected characters
 * But the main use of this class is utility to calculate LCS
 * which create completely by ours
 * this method work with o(n) time complexity
 *  @author : amirali khaneh angha & mahdieh naeemy
 */
public class tools {

    /**
     * lower case all the inputs and just standards characters
     */

    public static String normalize(String in) {
        StringBuilder out = new StringBuilder();
        String l = in.toLowerCase();
        for (int i = 0; i < l.length(); ++i) {
            char c = l.charAt(i);
            if (c >= 'a' && c <= 'z' || c >= '0' && c <= '9') {
                out.append(c);
            }
        }
        return out.toString();
    }

    /*
    corrcet form of LCS with O(n) time complexity
        */
    final static int MAX_CHAR = 256;

    public String myLCS(Node node) {
//        boolean check = false;
//        HashSet<Node> set = new HashSet<>();
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < MAX_CHAR; i++) {
//            temp = new StringBuilder();
            try {
                if (node.getEdge((char) i) != null) {
//                    System.out.println(node.getEdge((char) i).getLabel());
                    if (node.getEdge((char) i).getDest().getData().size() == 2/*&& !node.getEdge((char) i).getDest().getChecked()*/) {
                        /*node.getEdge((char) i).getDest().setChecked(true);*/
//                        System.out.println(node.getEdge((char) i).getLabel());
                        StringBuilder l = new StringBuilder(node.getEdge((char) i).getLabel());
                        l.append(myLCS(node.getEdge((char) i).getDest()));
                        if (temp.length() <= l.length())
                            temp = l;
//                        temp.append(myDoTravers(node.getEdge((char) i).getDest()));
//                        System.out.println(node.getEdge((char) i).getDest() + " ezafe ?? " + node.getEdge((char) i).getLabel());
//                        set.add(node.getEdge((char) i).getDest());
//                        set.addAll(myDoTravers(node.getEdge((char) i).getDest()));
                    }
                }
            } catch (Exception e) {
//                System.out.println(i);
            }
/*            if (answer.length()<=temp.length()){
                answer = temp.toString();
                check = true;
            }
        }
        if (check)
            return answer;
        else return "";*/
        }
        return temp.toString();
    }

    ArrayList arrayList = new ArrayList();

    public ArrayList myDoTraversal(Node node) {
        ArrayList temp = new ArrayList();
        if (node == null) {
            return temp;
        }
        for (int i = 0; i < MAX_CHAR; i++) {
            temp.clear();
            try {
                if (node.getEdge((char) i) != null) {
                    System.out.println(node.getEdge((char) i).getLabel());
                    if (node.getEdge((char) i).getDest().getData().size() == 2) {
                        ArrayList arrayList1 = myDoTraversal(node.getEdge((char) i).getDest());
                        arrayList.add(node.getEdge((char) i).getLabel());
                        String s = "";
                        for (int q = arrayList1.size() - 1; q > 0; q--) {
                            s = s.concat((String) arrayList1.get(q));
                        }
//                    arrayList1.stream().forEach(e->e.wait());
                        System.out.println(s + " ///////////////////////////////////" /*+tree.search(s) */);
                        if (arrayList1.size() != 0 /*&& tree.search(s).size()>0*/)
                            temp.addAll(arrayList1);
                    }
                }
                if (temp.size() > arrayList.size()) {
                    arrayList.clear();
                    for (int s = 0; s < temp.size(); s++) {
                        arrayList.add(temp.get(s));
                    }
                }
            } catch (Exception e) {
                System.out.println(i);
            }
        }
        return arrayList;
    }

    /*
      Computes substrings of string
     */
    public static Set<String> getSubstrings(String str) {
        Set<String> ret = new HashSet<String>();
        // compute all substrings
        for (int len = 1; len <= str.length(); ++len) {
            for (int start = 0; start + len <= str.length(); ++start) {
                String itstr = str.substring(start, start + len);
                ret.add(itstr);
            }
        }

        return ret;
    }
}
