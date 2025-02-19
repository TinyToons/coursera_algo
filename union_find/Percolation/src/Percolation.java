public class Percolation {

    private final int  gridSize;
    private int openSites;
    // id[i]: root(i); 
    // sz[i]: # of objects in the tree rooted at i
    // op[i]: is site(i) opened
    private int[] id, sz, op;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n)
    {
        if (n <= 0) 
            throw new IllegalArgumentException("n should be greater than 0!");
        gridSize = n;
        openSites = 0;
        // grid index size: n2 + 2 virtual sites
        int gs = n * n + 2;
        id = new int[gs];
        op = new int[gs];
        sz = new int[gs];
        // Init each root of i with itself
        // Init open flag of i: all the cells are blocked
        for (int i = 0; i < gs; i++) {
            id[i] = i;
            op[i] = 0;
            sz[i] = 1;
        }

    }

    // get the array index from row and cell's column
    private int getIndex(int row, int col)
    {
        if (row < 1 || row > gridSize || col < 1 || col > gridSize)
            throw new IllegalArgumentException(" 1 <= row <= " + gridSize + " and 1 <= col <= " + gridSize + "!");

        return (row-1) * gridSize + col;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col)
    {
        int i = getIndex(row, col);
        if (op[i] == 0)
        {
            op[i] = 1;
            openSites++;
        }
        // union with adjacent open sites
        // left
        if (col > 1 && isOpen(row, col-1))
            union(i, i-1);
        // right
        if (col < gridSize && isOpen(row, col+1))
            union(i, i+1);
        // up
        // if the site is on the first row, connect to the virtual top site
        if (row == 1)
            union(i, 0);
        if (row > 1 && isOpen(row-1,col))
            union(i, i - gridSize);
        // down
        // if the site is on the last row, connect to virtual bottom site
        if (row == gridSize)
            union(i, gridSize * gridSize + 1);
        if (row < gridSize && isOpen(row+1,col))
            union(i, i + gridSize);

    }
    // is the site (row, col) open?
    public boolean isOpen(int row, int col)
    {
        int index = getIndex(row, col);
        return op[index] == 1;
    }

    private int root(int i)
    {
        while (i != id[i])
        {
            // path compression simpler one-pass variant
            id[i] = id[id[i]];
            i = id[i];
        }
        return i;
    }

    private void union(int p, int q)
    {
        int i = root(p);
        int j = root(q);
        if (i == j) return;
        // keep the higher root index to manage redundant connections in a component
        if (sz[i] < sz[j]) {
            id[i] = j;
            sz[j] += sz[i];
        } 
        else {
            id[j] = i;
            sz[i] += sz[j];
        }
    }

    private boolean find(int p, int q)
    {
        return root(p) == root(q);
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col)
    {
        int index = getIndex(row, col);
        // check if the site is connected to the virtual top site
        return find(index, 0);
    }
    // returns the number of open sites
    public int numberOfOpenSites()
    {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates()
    {    // Check if the virtual bottom site and the virtual top site are connected
        return find(0, gridSize * gridSize + 1);
    }
    // For debug reason
    public String toString()
    {
        char[][] grid = new char[gridSize][gridSize];
        StringBuilder gs = new StringBuilder();
        /* Create and print a chessboard. */
        char[] colors = {'\u25A1', '\u25A0'};
        for(int y=1;y<=gridSize;y++){
            for (int x=1;x<=gridSize;x++){
                if (isOpen(y, x))
                    grid[y-1][x-1] = colors[0];
                else
                    grid[y-1][x-1] = colors[1];
            }
        }
        for (char[] row: grid) {
            for (char site : row) {
                gs.append(site);
            }
            gs.append('\n');
        }
        return gs.toString();
    }

    // test client (optional)
    public static void main(String[] args)
    {
        Percolation p = new Percolation(20);
        p.open(1, 1);
        System.out.println(p.percolates() ? "Percolates" : " Not Yet");
        System.out.println(p);
        p.open(1, 2);
        System.out.println(p.percolates() ? "Percolates" : " Not Yet");
        System.out.println(p);
        p.open(2, 1);
        System.out.println(p.percolates() ? "Percolates" + p.numberOfOpenSites() : " Not Yet");
        System.out.println(p);
    }
}
