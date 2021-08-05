/**
 * Copyright 2012 Alessandro Bahgat Shehata
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package main.java.com.algorihm.lcswithgsuffixtree.suffixtree;

import java.util.Collection;
import java.util.Collections;

/**
 * this tree creation is based on http://www.cs.helsinki.fi/u/ukkonen/SuffixT1withFigs.pdf and we using the ukkonen's algorithm
 * to create "On-line construction of suffix trees" so generally we just create the correct shape of java of this code
 * based on https://doc.lagout.org/Others/Data%20Structures/Advanced%20Data%20Structures%20%5BBrass%202008-09-08%5D.pdf ("Advanced data structure") book
 *
 * The important main methods are put and search:
 * Put adds the given key to the index, allowing for later retrieval of the given value.
 * Search can be used to retrieve the set of all the values that were put in the index with keys that contain a given input.
 *
 * In particular, after put(K, V), search(H) will return a set containing V for any string H that is substring of K.
 *
 * The overall complexity of the retrieval operation (search) is O(m) where m is the length of the string to search within the index.
 *
 */
public class GeneralizedSuffixTreeImp {

    /**
     * The index of the last item that was added to the GST
     */
    private int last = 0;
    /**
     * The root of the suffix tree
     */
    private final Node root = new Node();
    /**
     * The last leaf that was added during the update operation
     */
    private Node activeLeaf = root;

    /**
     * Searches for the given word within the GST.
     *
     * Returns all the indexes for which the key contains the <tt>word</tt> that was
     * supplied as input.
     *
     * @param word the key to search for
     * @return the collection of indexes associated with the input <tt>word</tt>
     */
    public Collection<Integer> search(String word) {
        return search(word, -1);
    }


    public Collection<Integer> search(String word, int results) {
        Node tmpNode = searchNode(word);
        if (tmpNode == null) {
            return Collections.EMPTY_LIST;
        }
        return tmpNode.getData(results);
    }


    public ResultInfo searchWithCount(String word, int count) {
        Node tmpNode = searchNode(word);
        if (tmpNode == null) {
            return new ResultInfo(Collections.EMPTY_LIST, 0);
        }

        return new ResultInfo(tmpNode.getData(count), tmpNode.getResultCount());
    }

    /**
     * Returns the tree node (if present) that corresponds to the given string.
     */
    private Node searchNode(String word) {

        Node currentNode = root;
        Edge currentEdge;

        for (int i = 0; i < word.length(); ++i) {
            char ch = word.charAt(i);

            currentEdge = currentNode.getEdge(ch);
            if (null == currentEdge) {

                return null;
            } else {
                String label = currentEdge.getLabel();
                int lenToMatch = Math.min(word.length() - i, label.length());
                if (!word.regionMatches(i, label, 0, lenToMatch)) {
                    return null;
                }

                if (label.length() >= word.length() - i) {
                    return currentEdge.getDest();
                } else {
                    // advance to next node
                    currentNode = currentEdge.getDest();
                    i += lenToMatch - 1;
                }
            }
        }

        return null;
    }

    /**
     * this is method to put new string in our tree which throw Exception if we do not indexed correctly
     */
    public void put(String key, int index) throws IllegalStateException {
        if (index < last) {
            throw new IllegalStateException("You should give the correct indexing to program .\n"+ index + "should be more than " + last);
        } else {
            last = index;
        }

        activeLeaf = root;

        String remainder = key;
        Node s = root;

        // proceed with tree construction (closely related to procedure in
        // Ukkonen's paper)
        String text = "";
        // iterate over the string, one char at a time
        for (int i = 0; i < remainder.length(); i++) {

            text = text.intern();

            Pair<Node, String> active = update(s, text, remainder.substring(i), index);
            active = canonize(active.getFirst(), active.getSecond());
            
            s = active.getFirst();
            text = active.getSecond();
        }

        if (null == activeLeaf.getSuffix() && activeLeaf != root && activeLeaf != s) {
            activeLeaf.setSuffix(s);
        }

    }

    private Pair<Boolean, Node> testAndSplit(final Node inputs, final String stringPart, final char t, final String remainder, final int value) {
        // descend the tree as far as possible
        Pair<Node, String> ret = canonize(inputs, stringPart);
        Node s = ret.getFirst();
        String str = ret.getSecond();

        if (!"".equals(str)) {
            Edge g = s.getEdge(str.charAt(0));

            String label = g.getLabel();
            // must see whether "str" is substring of the label of an edge
            if (label.length() > str.length() && label.charAt(str.length()) == t) {
                return new Pair<Boolean, Node>(true, s);
            } else {
                // need to split the edge
                String newlabel = label.substring(str.length());
                assert (label.startsWith(str));

                // build a new node
                Node r = new Node();
                // build a new edge
                Edge newedge = new Edge(str, r);

                g.setLabel(newlabel);

                // link s -> r
                r.addEdge(newlabel.charAt(0), g);
                s.addEdge(str.charAt(0), newedge);

                return new Pair<Boolean, Node>(false, r);
            }

        } else {
            Edge e = s.getEdge(t);
            if (null == e) {
                return new Pair<Boolean, Node>(false, s);
            } else {
                if (remainder.equals(e.getLabel())) {
                    // update payload of destination node
                    e.getDest().addRef(value);
                    return new Pair<Boolean, Node>(true, s);
                } else if (remainder.startsWith(e.getLabel())) {
                    return new Pair<Boolean, Node>(true, s);
                } else if (e.getLabel().startsWith(remainder)) {
                    // need to split as above
                    Node newNode = new Node();
                    newNode.addRef(value);

                    Edge newEdge = new Edge(remainder, newNode);

                    e.setLabel(e.getLabel().substring(remainder.length()));

                    newNode.addEdge(e.getLabel().charAt(0), e);

                    s.addEdge(t, newEdge);

                    return new Pair<Boolean, Node>(false, s);
                } else {
                    // they are different words. No prefix. but they may still share some common substr
                    return new Pair<Boolean, Node>(true, s);
                }
            }
        }

    }


    private Pair<Node, String> canonize(final Node s, final String inputstr) {

        if ("".equals(inputstr)) {
            return new Pair<Node, String>(s, inputstr);
        } else {
            Node currentNode = s;
            String str = inputstr;
            Edge g = s.getEdge(str.charAt(0));
            // descend the tree as long as a proper label is found
            while (g != null && str.startsWith(g.getLabel())) {
                str = str.substring(g.getLabel().length());
                currentNode = g.getDest();
                if (str.length() > 0) {
                    g = currentNode.getEdge(str.charAt(0));
                }
            }

            return new Pair<Node, String>(currentNode, str);
        }
    }

    private Pair<Node, String> update(final Node inputNode, final String stringPart, final String rest, final int value) {
        Node s = inputNode;
        String tempstr = stringPart;
        char newChar = stringPart.charAt(stringPart.length() - 1);

        // line 1
        Node oldroot = root;

        // line 1b
        Pair<Boolean, Node> ret = testAndSplit(s, tempstr.substring(0, tempstr.length() - 1), newChar, rest, value);

        Node r = ret.getSecond();
        boolean endpoint = ret.getFirst();

        Node leaf;
        while (!endpoint) {
            Edge tempEdge = r.getEdge(newChar);
            if (null != tempEdge) {
                // such a node is already present. This is one of the main differences from Ukkonen's case:
                // the tree can contain deeper nodes at this stage because different strings were added by previous iterations.
                leaf = tempEdge.getDest();
            } else {
                // must build a new leaf
                leaf = new Node();
                leaf.addRef(value);
                Edge newedge = new Edge(rest, leaf);
                r.addEdge(newChar, newedge);
            }

            if (activeLeaf != root) {
                activeLeaf.setSuffix(leaf);
            }
            activeLeaf = leaf;

            if (oldroot != root) {
                oldroot.setSuffix(r);
            }

            oldroot = r;

            if (null == s.getSuffix()) { // root node
                assert (root == s);
                // this is a special case to handle what is referred to as node _|_ on the paper
                tempstr = tempstr.substring(1);
            } else {
                Pair<Node, String> canret = canonize(s.getSuffix(), safeCutLastChar(tempstr));
                s = canret.getFirst();
                // use intern to ensure that tempstr is a reference from the string pool
                tempstr = (canret.getSecond() + tempstr.charAt(tempstr.length() - 1)).intern();
            }

            // line 7
            ret = testAndSplit(s, safeCutLastChar(tempstr), newChar, rest, value);
            r = ret.getSecond();
            endpoint = ret.getFirst();

        }

        // line 8
        if (oldroot != root) {
            oldroot.setSuffix(r);
        }
        oldroot = root;

        return new Pair<Node, String>(s, tempstr);
    }

    public Node getRoot() {
        return root;
    }

    private String safeCutLastChar(String seq) {
        if (seq.length() == 0) {
            return "";
        }
        return seq.substring(0, seq.length() - 1);
    }

    public int computeCount() {
        return root.computeAndCacheCount();
    }


    public static class ResultInfo {

        /**
         * The total number of results present in the database
         */
        public int totalResults;
        /**
         * The collection of (some) results present in the GST
         */
        public Collection<Integer> results;

        public ResultInfo(Collection<Integer> results, int totalResults) {
            this.totalResults = totalResults;
            this.results = results;
        }
    }

    /**
     * A private class used to return a tuples of two elements
     */
    private class Pair<A, B> {

        private final A first;
        private final B second;

        public Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }

        public A getFirst() {
            return first;
        }

        public B getSecond() {
            return second;
        }
    }
}
