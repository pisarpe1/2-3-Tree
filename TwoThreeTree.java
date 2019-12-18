import java.util.*;

class TwoThreeTree<T extends Comparable<T>> implements Iterable
{
    private Node root;
    private int size;
    private boolean modified = true;
    private Traversal traverse;

    TwoThreeTree()
    {
        root = null;
        traverse = new Traversal();
        size = 0;
    }

    void addAll(Collection<T> collection)
    {
        for (T item: collection)
            this.insert(item);
        modified = true;
    }

    void insert(T x)
    {
        if (root == null)                           // Pokud je root nulový, pak vytvoří nový root s null jako nadřazeným
        {
            root = new Node(x);
            size++;
        }
        root.insert(x);                             // Jinak vyvolá rekurzivní vložení
        modified = true;
    }

    int size()
    {
        return size;
    }

    boolean search(T x)
    {
        if (root == null)                           // Pokud je root nulový, pak strom neexistuje a vrací null
            return false;
        return root.search(x).keys.contains(x);
    }

    void clear()
    {
        root = null;
        size = 0;
    }

    public Iterator<T> iterator()
    {
        if (modified)                                 // Vytváří uspořádaný propojený seznam , pouze pokud byl strom upraven
        {
            traverse.traverseTree();                  // Procházení stromue
            modified = false;                         // Nastaví flag na false, protože se právě vytvořil propojený seznam
        }
        return traverse.ordered.iterator();           // Delegujte úlohu iterátoru na vestavěný iterátor propojeného seznamu.
    }

    //-----------------------------------------Inner Iterator Class-------------------------------------------------//

    private class Traversal                            // vytvoření třídy Traversal
    {
        ArrayList<T> ordered;

        void traverseTree()
        {
            ordered = new ArrayList<>();                // Resetuje seřazený seznam. traverseTree se volána pouze v případě modifikace
            traverse(root);                             // Initializace traversal z root.
        }

        void traverse(Node n)
        {
            if (n.children.size() == 0)                  // Pokud je to listový uzel, přidá to do propojeného seznamu veškerý klíč
                ordered.addAll(n.keys);
            else
            {
                traverse(n.children.get(0));              // Jinak nejprve přejděte levou větev
                ordered.add(n.keys.get(0));               // Po dokončení přidá nový klíč 1 uzlu n, aby byl zachován.
                traverse(n.children.get(1));              // Potom projde prostřední nebo pravou větev n
                if (n.children.size() == 3)               // Pokud existují 3 větve, musíme to ještě projít.
                {
                    ordered.add(n.keys.get(1));           // Než se to projde, přidá do seznamu druhou klávesu n, protože vše bude větší než v pravé větvi.
                    traverse(n.children.get(2));          // Potom se projde poslední větev a přidá všechny nalezené uzly v pořadí.
                }
            }
        }
    }

    //-------------------------------------------INNER CLASS NODE---------------------------------------------------//
    
    
    private class Node                                     // vytvoření třídy Node
    {
        ArrayList<T> keys = new ArrayList<>(3);
        ArrayList<Node> children = new ArrayList<>(4);

        Node(T data)
        {
            keys.add(data);
        }

        private void addKey(T x)
        {
            this.keys.add(x);                                 // Insert x at the last position
            int lastIndex = this.keys.size()-1;
            for (int i = 0; i < lastIndex; i++)               // Repeatedly check the key at index i if its greater than key at index keySize
            {
                if (this.keys.get(i).compareTo(this.keys.get(lastIndex)) > 0)
                {
                    T temp = this.keys.get(i);                // If it is, swap them and increase the i
                    this.keys.set(i, this.keys.get(lastIndex));
                    this.keys.set(lastIndex, temp);
                }
            }
        }

        private boolean insert(T x)
        {
            if (this.keys.contains(x))                                               // If the node contains key, return false
                return false;

            int i = 0;
            while (i < this.keys.size() && x.compareTo(this.keys.get(i)) > 0)        // Find the correct child to insert at.
                i++;
            boolean childWasSplit;

            if (i < this.children.size())                               // The node is not a leaf, so I can recursively insert.
                childWasSplit = this.children.get(i).insert(x);
            else                                                        // Its a leaf, just add the key and then check if it needs to split.
            {
                this.addKey(x);
                size++;                                                 // Key wasn't a duplicate a could be inserted in the tree, therefore increase the size.
                if (this.keys.size() == 3)
                {
                    this.splitify();                                    // Split the node and return true to let the parent know
                    return true;                                        // that child was split.
                }
                return false;
            }

            if (childWasSplit)
            {                                                           // Child was split and parent might be a 2-node or 3-node.
                Node tempChild = this.children.get(i);                  // Copy of the split child because it will get modified by overwriting parent's children
                this.addKey(tempChild.keys.get(0));                     // "Moving the child's key to the parent"
                this.children.set(i, tempChild.children.get(0));        // The current child is replaced by its left child
                this.children.add(i+1, tempChild.children.get(1));// Add the right child to the next index. ArrayList does the shifting automatically.
                if (this.children.size() == 4)                           // The node is really full, it was 3-node and now it became 4-node, so it
                {
                    this.splitify();                                    // needs to redistribute its children by creating nodes out of itself.
                    return true;
                }
            }                                                           // It was a 2-node so the split child's children became parent's children and
            return false;                                               // and it's key adjusted inside the parent so it became a 3-node.
        }

        private void splitify()
        {
            Node left = new Node(this.keys.get(0));
            Node right = new Node(this.keys.get(2));
            if (this.children.size() == 4)                      // If the node is overfull, balance everything
            {                                                   // by giving left-most two nodes to the left child
                left.children.add(this.children.get(0));        // and the right most two nodes to the right child.
                left.children.add(this.children.get(1));
                right.children.add(this.children.get(2));
                right.children.add(this.children.get(3));
            }
            this.children.clear();
            this.children.add(left);                            // Set the new left and right child of "this"
            this.children.add(right);

            T tempKey = this.keys.get(1);                       // Also, the keys have been passed to the children
            this.keys.clear();                                  // so modify it. In the end, only the middle key should be
            this.keys.add(tempKey);                             // there.
        }

        private Node search(T val)
        {
            if (this.children.size() == 0 || this.keys.contains(val))                     // If the node is a leaf or has the key, return that node
                return this;
            else
            {
                int i = 0;
                while (i < this.keys.size() && val.compareTo(this.keys.get(i)) > 0)       // Else recursively search that appropriate branch by making use
                    i++;                                                                  // of the index i.
                return this.children.get(i).search(val);
            }
        }

        public String toString()            // toString method
        {
            return this.keys.get(0) + (this.keys.size() == 2 ? " " + this.keys.get(1) : "");
        }
    }
}
