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

/**
 *   create Edge class for connect nodes to create suffix
 *   each Edge represents next node and the label which is the substring that we have until the next nodes
 * @author : amirali khaneh angha & mahdieh naeemy
 */
public class Edge {
    private String label;
    private Node dest;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Node getDest() {
        return dest;
    }

    public void setDest(Node dest) {
        this.dest = dest;
    }

    public Edge(String label, Node dest) {
        this.label = label;
        this.dest = dest;
    }

}
