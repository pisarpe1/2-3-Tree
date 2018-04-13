# Generic 2-3 Tree
<h2>Binary Tree vs Balanced 2-3 Tree</h2>
<p>A normal binary tree fails to produce a lg(n) lookup when the root node has a value small compared to the other values in the tree. Because of that, the resulting structure looks more like a vine than a tree as shown below where larger values pile up and forms almost a LinkedList kind of structure. For the following example, the order of adding values is [1,0,2,3,4,5]
                 <img width="519" alt="screen shot 2018-04-12 at 11 05 07 pm" src="https://user-images.githubusercontent.com/28474117/38719306-0be4c990-3ea6-11e8-90c1-7e721722e6a8.png"><p>
<p>In order to keep the shape of the tree intact, we have B-Trees that has all its leaves at the same height and is always balanced. 2-3 Tree is a specific form of B-Tree whose order is 3. If we consider the above example and the values in the same order, the 2-3 Tree looks as follows.</p>
<p><img width="500" alt="screen shot 2018-04-12 at 11 21 12 pm" src="https://user-images.githubusercontent.com/28474117/38719731-4a38e418-3ea8-11e8-86ab-d045977b4f95.png"></p>
<p>If you notice, all the leaves remain at the same height and the nodes can now have upto 2 keys and 3 children. The keys of a node divide the number line in such a way that value of the keys of left child of a node will be always less than it's 1st key, values of the keys of the middle child will be in between 1st and 2nd key and the value of the keys for the right child will be greater than the 2nd key. This is a key feature of the 2-3 Tree and must be maintained at any given point of modification.</p>
<h2>Properties of a 2-3 Tree</h2>
<ul>
  <li>Every internal node is a 2-node or a 3-node.</li>
  <li>All the leaves are at the same level.</li>
  <li>Data is kept in a sorted order.</li>
  <li>Searching, Insertion and Deletion of data belongs to <img src="http://latex.codecogs.com/svg.latex?\inline&space;\fn_phv&space;$\text{O}(log_3n)$" title="$\text{O}(log_3n)$"/> </li>
</ul>
<h2>API</h2>
The code is for a generic 2-3 Tree that utilizes the recursive nature of trees to express its structure with minimal and readable code.
<ul>
  <li>insert(T val): Inserts the specified value "val" of type "T".</li>
  <li>addAll(Collection<T> collection): Inserts all elements from the collection to the 2-3 Tree</li>
  <li>search(T query): Searches the tree for the specified "query". Will return the String Representation of the node containing the search value. The node containing the value might be bundled with another key.</li>
  <li>iterator(): Returns an iterator object that allows in-order traversal of the tree.</li>
  <li>clear(): Empties the the content of the tree.</li>
  <li>size(): Returns the size of the tree.</li>
</ul>
<h5>More functions like delete, removeAll, enhanced for loop will be implemented.</h5>
