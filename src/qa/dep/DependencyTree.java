/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.dep;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import qa.WordNet;

/**
 * Dependency tree.
 *
 * @author Ritwik Banerjee
 */
public class DependencyTree extends TreeMap<Integer, DependencyNode> {

    /**
     * Constructs a dependency tree comprising of a single artificial root node.
     */
    public DependencyTree() {
        put(DependencyNode.ROOT_ID, new DependencyNode());
    }

    /**
     * Creates a dependency tree from its CoNLL-X representation.
     */
    public static DependencyTree fromCoNLLFormatString(String conllFormatString) {
        DependencyTree tree = new DependencyTree();
        String[] conllFormatLines = conllFormatString.split("\n");
        for (String line : conllFormatLines) {
            DependencyNode node = DependencyNode.fromCoNLLFormatString(line);
            tree.put(node.getId(), node);
        }
        return tree;
    }

    public DependencyNode getSubjectOf(DependencyNode predicateNode) {
        DependencyNode sbjNode = null;
        if (hasDependentsOfType(predicateNode, "nsubj")) {
            sbjNode = childOfNodeByRelation(predicateNode, "nsubj");
        } else if (hasDependentsOfType(predicateNode, "nsubjpass")) {
            sbjNode = childOfNodeByRelation(predicateNode, "nsubjpass");
        }

        return sbjNode;
    }

    public DependencyNode getObjectOf(DependencyNode predicateNode) {
        DependencyNode objNode = null;
        if (hasDependentsOfType(predicateNode, "dobj")) {
            objNode = childOfNodeByRelation(predicateNode, "dobj");
        } else if (hasDependentsOfType(predicateNode, "pobj")) {
            objNode = childOfNodeByRelation(predicateNode, "pobj");
        }

        return objNode;
    }

    /**
     * Returns the relation label of the node closest to the one with the given
     * id that is a nominal subject.
     */
    public String getNearestSubjectRelationLabel(int id) {
        List<DependencyNode> subjectNodes = values().stream()
                .filter(node -> !node.getForm().equals("ROOT")
                        && node.getRelationLabel().matches("(?i).*subj.*"))
                .collect(Collectors.toList());

        int diff = Integer.MAX_VALUE;
        DependencyNode nearestSubjectNode = subjectNodes.isEmpty() ? null : subjectNodes.get(0);

        for (DependencyNode node : subjectNodes) {
            if (Math.abs(node.getId() - id) < diff) {
                diff = Math.abs(node.getId() - id);
                nearestSubjectNode = node;
            }
        }

        return nearestSubjectNode == null ? "" : nearestSubjectNode.getRelationLabel();
    }

    /**
     * Returns the previous (i.e. to the left of node with given id) node with a
     * specified POS tag
     */
    public DependencyNode getPreviousNodeWithCoarsePOS(int id, String tag) {
        for (int node_id = id - 1; node_id > 0; node_id--) {
            if (get(node_id).getPos().equals(tag)) {
                return get(node_id);
            }
        }
        return null;
    }

    public DependencyNode getPreviousNodeWithCoarsePOS(DependencyNode node, String tag) {
        return getPreviousNodeWithCoarsePOS(node.getId(), tag);
    }

    public DependencyNode getNextNodeWithCoarsePOS(int id, String tag) {
        for (int node_id = id + 1; node_id < lastKey(); node_id++) {
            if (get(node_id).getPos().equals(tag)) {
                return get(node_id);
            }
        }
        return null;
    }

    public DependencyNode getNextNodeWithCoarsePOS(DependencyNode node, String tag) {
        return getNextNodeWithCoarsePOS(node.getId(), tag);
    }

    public ArrayList<DependencyNode> getNode(ArrayList<String> words) {
        ArrayList<DependencyNode> nodes = new ArrayList<DependencyNode>();

        for (String word : words) {
            for (Map.Entry<Integer, DependencyNode> entry : this.entrySet()) {
                if (entry.getValue().getForm().equalsIgnoreCase(word)) {
                    nodes.add(entry.getValue());
                }
            }
        }
        return nodes;
    }

    public boolean isExistPathFrom(DependencyNode from, DependencyNode to) {
        DependencyNode currentNode = from;
        boolean found = false;
        while (!currentNode.isRoot() && !found) {
            if (currentNode.getId() == to.getId()) {
                found = true;
            } else {
                currentNode = this.get(currentNode.getHeadID());
            }
        }

        return found;
    }

    public DependencyNode get(int id) {
        try {
            return super.get(id);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public DependencyNode headOf(DependencyNode node) {
        return get(node.getHeadID());
    }

    public DependencyNode headOf(int node_id) {
        return get(get(node_id).getHeadID());
    }

    public Set<DependencyNode> dependentsOf(DependencyNode node) {
        return dependentsOf(node.getId());
    }

    public Set<DependencyNode> dependentsOf(int node_id) {
        Predicate<DependencyNode> isDependent = n -> n.getHeadID() == node_id;
        return values().stream().filter(isDependent).collect(Collectors.toSet());
    }

    public Set<DependencyNode> dependentsOfType(DependencyNode node, String type) {
        return dependentsOfType(node.getId(), type);
    }

    public Set<DependencyNode> dependentsOfType(int node_id, String type) {
        Predicate<DependencyNode> hasType = n -> n.getRelationLabel().equals(type);
        return dependentsOf(node_id).stream().filter(hasType).collect(Collectors.toSet());
    }

    public Set<DependencyNode> dependentsWithPOSTag(int node_id, String pos) {
        Predicate<DependencyNode> hasTag = n -> n.getPos().equals(pos);
        return dependentsOf(node_id).stream().filter(hasTag).collect(Collectors.toSet());
    }

    public boolean hasDependentsOfType(DependencyNode node, String type) {
        return dependentsOfType(node, type).size() > 0;
    }

    /**
     * Prints this dependency tree in the CoNLL-X format.
     *
     * @return The CoNLL-X format representation of this dependency tree.
     */
    public String toString() {
        OptionalInt max_tokensize_optint = values().stream().map(DependencyNode::getForm).mapToInt(String::length).max();
        int max_tokensize = max_tokensize_optint.isPresent() ? max_tokensize_optint.getAsInt() : 0;
        int formwidth = max_tokensize + 2;
        List<String> conllOutput = new ArrayList<>();
        for (int i : navigableKeySet()) {
            if (i == 0) {
                continue;
            }
            DependencyNode node = get(i);
            String form = Strings.padEnd(node.getForm(), formwidth, ' ');
            String lemma = Strings.padEnd(node.getLemma(), 6, ' ');
            String cpos = Strings.padEnd(node.getCpos(), 6, ' ');
            String pos = Strings.padEnd(node.getPos(), 6, ' ');
            String rel = Strings.padEnd(node.getRelationLabel(), 6, ' ');
            conllOutput.add(Joiner.on("    ").join(node.getId(), form, lemma, cpos, "_", node.getHeadID(), rel, "_", "_"));
        }
        return Joiner.on('\n').join(conllOutput);
    }

    /*public String toRawString() {
     StringBuilder sentenceBuilder = new StringBuilder();
     for (int i = 1; i < lastKey(); i++) {
     String curr_token = get(i).getForm();
     if (!(curr_token.equals("-") || get(i-1).getForm().endsWith("-") || PUNCTUATION_STR.contains(curr_token)))
     sentenceBuilder.append(" ");
     sentenceBuilder.append(curr_token);
     }
     return sentenceBuilder.toString();
     }*/
    public DependencyNode childOfNodeByRelation(DependencyNode node, String relation) {
        Predicate<DependencyNode> IsMatchingChild = tnode -> tnode.getId() > DependencyNode.ROOT_ID
                && tnode.getHeadID() == node.getId()
                && (relation == null || tnode.getRelationLabel().equals(relation));
        DependencyNode child = null;
        try {
            Optional<DependencyNode> optionalNode = values().stream().filter(IsMatchingChild).findFirst();
            if (optionalNode.isPresent()) {
                child = optionalNode.get();
            }
        } catch (NullPointerException ignore) { /* NullPointerException will be thrown if the 'node' arg is null */ }

        return child;
    }

    public Set<DependencyNode> childrenOfNodeByRelation(DependencyNode node, String relation) {
        Predicate<DependencyNode> IsMatchingChild = tnode -> tnode.getId() > DependencyNode.ROOT_ID
                && tnode.getHeadID() == node.getId()
                && (relation == null || tnode.getRelationLabel().equals(relation));
        Set<DependencyNode> children = new HashSet<>();
        try {
            children = values().stream().filter(IsMatchingChild).collect(Collectors.toSet());
        } catch (NullPointerException ignore) { /* NullPointerException will be thrown if the 'node' arg is null */ }

        return children;
    }

    /**
     * Finds and returns the {@code ID} of the first {@code DependencyNode} in
     * this {@code DependencyTree} with a specified POS tag, which may be fine
     * or coarse, and occurring after a specified {@code pivot} ID.
     *
     * @param pivot         <code>ID</code> of node whose successor is searched.
     * @param partOfSpeech The part-of-speech tag to be matched.
     * @param coarse If <code>true</code>, matches the coarse POS tag, else
     * matches the fine-grained tag.
     * @return The first node with the specified POS tag occurring after the
     * node with the given {@code id}. Returns -1 if no such node is present.
     */
    public int getSucceedingNodeWithPOS(int pivot, String partOfSpeech, boolean coarse) {
        for (int index = pivot + 1; index <= lastKey(); index++) {
            String indexPOS = coarse ? get(index).getCpos() : get(index).getPos();
            if (indexPOS.equals(partOfSpeech)) {
                return index;
            }
        }
        return -1; // no node with the given POS tag appears after the pivot
    }

    /**
     * Finds and returns the {@code ID} of the last {@code DependencyNode} in
     * this {@code DependencyTree} with a specified POS tag, which may be fine
     * or coarse, and occurring before a specified {@code pivot} ID.
     *
     * @param pivot         <code>ID</code> of node whose predecessor is searched.
     * @param partOfSpeech The part-of-speech tag to be matched.
     * @param coarse If <code>true</code>, matches the coarse POS tag, else
     * matches the fine-grained tag.
     * @return The last node with the specified POS tag occurring before the
     * node with the given {@code id}. Returns -1 if no such node is present.
     */
    public int getPrecedingNodeWithPOS(int pivot, String partOfSpeech, boolean coarse) {
        for (int index = pivot - 1; index >= firstKey(); index--) {
            String indexPOS = coarse ? get(index).getCpos() : get(index).getPos();
            if (indexPOS.equals(partOfSpeech)) {
                return index;
            }
        }
        return -1;
    }

    public static void removeSubtree(Map<Integer, DependencyNode> tree, DependencyNode root) {
        List<DependencyNode> dependents = new ArrayList<>();
        try {
            dependents = tree.values()
                    .stream()
                    .filter(n -> n.getId() > DependencyNode.ROOT_ID && n.getHeadID() == root.getId())
                    .collect(Collectors.toList());
        } catch (NullPointerException ignore) { /* NullPointerException will be thrown if the 'node' arg is null */ }

        for (DependencyNode dependent : dependents) {
            removeSubtree(tree, dependent);
        }
        tree.remove(root.getId(), root);
    }

    public boolean isNounModifier(DependencyNode node)
    {
        if (node.isRoot())
            return false;
        
        if (node.getPos().contains("NN"))
        {
            DependencyNode parent = this.get(node.getHeadID());
            if (parent.getPos().contains("NN"))
                return true;
        }
        return false;
    }
    public ArrayList<String> getWordMatchType(ArrayList<DependencyNode> trigger, String[] types, WordNet wn) {
        ArrayList<String> argMatches = new ArrayList<String>();
        for (DependencyNode node : trigger) {
            for (Map.Entry<Integer, DependencyNode> entry : this.entrySet()) {
                if (!entry.getValue().getForm().equalsIgnoreCase(node.getForm())) {
                    if (!entry.getValue().isRoot() && !isNounModifier(entry.getValue()) &&isExistPathFrom(entry.getValue(), node) &&  wn.isMatchType(entry.getValue().getLemma().toLowerCase(), types)) {
                        if (!argMatches.contains(entry.getValue().getForm()))
                            argMatches.add(entry.getValue().getForm());
                    }
                }

            }
        }
        return argMatches;
    }

}
