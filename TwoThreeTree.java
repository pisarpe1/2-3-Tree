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
            this.keys.add(x);                                 // vlož x do poslední pozice
            int lastIndex = this.keys.size()-1;
            for (int i = 0; i < lastIndex; i++)               //  Opakovaně zkontrolujte klíč v indexu i, pokud je větší než klíč v indexu keySize  
            {
                if (this.keys.get(i).compareTo(this.keys.get(lastIndex)) > 0)
                {
                    T temp = this.keys.get(i);                //  Pokud ano, vyměňte je a zvyšte i
                    this.keys.set(i, this.keys.get(lastIndex));
                    this.keys.set(lastIndex, temp);
                }
            }
        }

        private boolean insert(T x)
        {
            if (this.keys.contains(x))                                               // Pokud uzel obsahuje klíč, vraťte false
                return false;

            int i = 0;
            while (i < this.keys.size() && x.compareTo(this.keys.get(i)) > 0)        // Najděte správného potomka, kterého chcete vložit
                i++;
            boolean childWasSplit;

            if (i < this.children.size())                               // Uzel není list, takže mohu rekurzivně vložit
                childWasSplit = this.children.get(i).insert(x);
            else                                                        // Je to list, stačí přidat klíč a pak zkontrolovat, zda je třeba rozdělit
            {
                this.addKey(x);
                size++;                                                 // Klíč nebyl duplikát a mohl být vložen do stromu, proto zvětšujte jeho velikost
                {
                    this.splitify();                                    // Rozdělte uzel a vraťte true, abyste informovali rodiče
                    return true;                                        // Potomek byl rozdělen
                }
                return false;
            }

            if (childWasSplit)
            {                                                           // Potomek byl rozdělen a rodič může být 2-uzel nebo 3-uzel
                Node tempChild = this.children.get(i);                  // Kopie rozděleného potomka, protože se změní přepsáním potomků rodičů
                this.addKey(tempChild.keys.get(0));                     // Přesunutí klíče od potomka k rodiči"
                this.children.set(i, tempChild.children.get(0));        // Aktuální potomek je nahrazen levým potomkem
                this.children.add(i+1, tempChild.children.get(1));      // Přidejte pravého potomka do dalšího indexu. ArrayList provede řazení automaticky.
                if (this.children.size() == 4)                          // Uzel je opravdu plný, byl to 3-uzel a nyní se stal 4-uzlem, takže to bylo
                {
                    this.splitify();                                    // potřebuje přerozdělit své potomky vytvořením uzlů ze sebe
                    return true;
                }
            }                                                           // Byl to 2-uzel, takže se potomci rozštěpeného potomka staly potomky rodičů a
            return false;                                               // je to klíč upravený uvnitř nadřazeného tak, aby se stal 3-uzlem
        }

        private void splitify()
        {
            Node left = new Node(this.keys.get(0));
            Node right = new Node(this.keys.get(2));
            if (this.children.size() == 4)                      // Pokud je uzel přeplněný, vyrovnejte vše
            {                                                   // tím, že vlevo dáte nejvýše dva uzly
                left.children.add(this.children.get(0));        // a nejvíce dva uzly k pravému potomku
                left.children.add(this.children.get(1));
                right.children.add(this.children.get(2));
                right.children.add(this.children.get(3));
            }
            this.children.clear();
            this.children.add(left);                            // nastavení nového pravého a levého potomka
            this.children.add(right);

            T tempKey = this.keys.get(1);                       // Také klíče byly předány potomkům
            this.keys.clear();                                  // tak to upravte. Nakonec by tam měl být pouze prostřední klíč
            this.keys.add(tempKey);                             
        }

        private Node search(T val)
        {
            if (this.children.size() == 0 || this.keys.contains(val))          // Pokud je uzel list nebo má klíč, vraťte tento uzel
                return this;
            else
            {
                int i = 0;
                while (i < this.keys.size() && val.compareTo(this.keys.get(i)) > 0)       // Nebo rekurzivně prohledejte příslušnou větev pomocí indexu i
                    i++;                                                                 
                return this.children.get(i).search(val);
            }
        }

        public String toString()            // toString metoda
        {
            return this.keys.get(0) + (this.keys.size() == 2 ? " " + this.keys.get(1) : "");
        }
    }
}
