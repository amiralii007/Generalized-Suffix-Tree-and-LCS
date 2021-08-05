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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents a node of the generalized suffix tree graph
 *  @author : amirali khaneh angha & mahdieh naeemy
 */
public class Node {
Boolean checked = false;

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Boolean getChecked() {
        return checked;
    }


    private int[] data;

    private int lastIdx = 0;
    /**
     * The starting size of the int[] array containing the payload
     */
    private static final int START_SIZE = 0;
    /**
     * The increment in size used when the payload array is full
     */
    private static final int INCREMENT = 1;
    /**
     * The set of edges starting from this node
     */
    private final Map<Character, Edge> edges;
    /**
     * The suffix link as described in Ukkonen's paper.
     * if str is the string denoted by the path from the root to this, this.suffix
     * is the node denoted by the path that corresponds to str without the first char.
     */
    private Node suffix;

    private int resultCount = -1;

    /**
     * Creates a new Node
     */
    Node() {
        edges = new TreeEdgeMap();
        suffix = null;
        data = new int[START_SIZE];
    }


    public Collection<Integer> getData() {
        return getData(-1);
    }


    Collection<Integer> getData(int numElements) {
        Set<Integer> ret = new HashSet<Integer>();
        for (int num : data) {
            ret.add(num);
            if (ret.size() == numElements) {
                return ret;
            }
        }
        // need to get more matches from child nodes. This is what may waste time
        for (Edge e : edges.values()) {
            if (-1 == numElements || ret.size() < numElements) {
                for (int num : e.getDest().getData()) {
                    ret.add(num);
                    if (ret.size() == numElements) {
                        return ret;
                    }
                }
            }
        }
        return ret;
    }

    /**
     * Adds the given <tt>index</tt> to the set of indexes associated with <tt>this</tt>
     */
    void addRef(int index) {
        if (contains(index)) {
            return;
        }

        addIndex(index);

        // add this reference to all the suffixes as well
        Node iter = this.suffix;
        while (iter != null) {
            if (iter.contains(index)) {
                break;
            }
            iter.addRef(index);
            iter = iter.suffix;
        }

    }


    private boolean contains(int index) {
        int low = 0;
        int high = lastIdx - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int midVal = data[mid];

            if (midVal < index)
            low = mid + 1;
            else if (midVal > index)
            high = mid - 1;
            else
            return true;
        }
        return false;

    }


    protected int computeAndCacheCount() {
        computeAndCacheCountRecursive();
        return resultCount;
    }

    private Set<Integer> computeAndCacheCountRecursive() {
        Set<Integer> ret = new HashSet<Integer>();
        for (int num : data) {
            ret.add(num);
        }
        for (Edge e : edges.values()) {
            for (int num : e.getDest().computeAndCacheCountRecursive()) {
                ret.add(num);
            }
        }

        resultCount = ret.size();
        return ret;
    }


    public int getResultCount() throws IllegalStateException {
        if (-1 == resultCount) {
            throw new IllegalStateException("getResultCount() shouldn't be called without calling computeCount() first");
        }

        return resultCount;
    }

    void addEdge(char ch, Edge e) {
        edges.put(ch, e);
    }

    public Edge getEdge(char ch) {
        return edges.get(ch);
    }

    Map<Character, Edge> getEdges() {
        return edges;
    }

    Node getSuffix() {
        return suffix;
    }

    void setSuffix(Node suffix) {
        this.suffix = suffix;
    }

    private void addIndex(int index) {
        if (lastIdx == data.length) {
            int[] copy = new int[data.length + INCREMENT];
            System.arraycopy(data, 0, copy, 0, data.length);
            data = copy;
        }
        data[lastIdx++] = index;
    }
}
