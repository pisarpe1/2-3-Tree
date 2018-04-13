# Generic 2-3 Tree
<h2>Balanced 2-3 Search Tree</h2>
<p>A normal binary tree fails to produce a lg(n) lookup when the root node has a value small compared to the other values in the tree. As a result, the resulting structure looks more like a vine than a tree. In order to keep the shape of the tree intact, we have B-Trees that has all its leaves at the same height and is always balanced.</p>
<h2>Properties of a 2-3 Tree</h2>
<ul>
  <li>Every internal node is a 2-node or a 3-node.</li>
  <li>All the leaves are at the same level.</li>
  <li>Data is kept in a sorted order.</li>
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
