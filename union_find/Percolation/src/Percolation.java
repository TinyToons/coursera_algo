import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    // Grid size
    private final int  gridSize;
    // Count of open sites
    private int openSites;
    // isOpen[i]: is site(i) opened
    private final boolean[] isOpen;
    // index of virtual top and virtual bottom sites
    private final int vts, vbs;
    // WQUUF object
    // uf to test percolation
    private final WeightedQuickUnionUF uf;
    // ufFull to test if a site is full. Virtual bottom site is excluded
    private final WeightedQuickUnionUF ufFull;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n)
    {
        if (n <= 0) 
            throw new IllegalArgumentException("Grid size must be positive!");
        gridSize = n;
        openSites = 0;
        // grid index size: n2 + 2 virtual sites
        int gridIndexSize = n * n + 2;
        // Init uf
        uf = new WeightedQuickUnionUF(gridIndexSize);
        ufFull = new WeightedQuickUnionUF(gridIndexSize - 1);
        // virtual sites indexes
        vts = n * n;
        vbs = n * n + 1;
        // Init open flag of i: all the cells are blocked
        isOpen = new boolean[gridIndexSize];
        for (int i = 0; i < gridIndexSize; i++)
        {
            isOpen[i] = false;
        }
    }

    // get the array index from row and cell's column
    private int getIndex(int row, int col)
    {
        if (row < 1 || row > gridSize || col < 1 || col > gridSize)
            throw new IllegalArgumentException("Row and column must be between 1 and " + gridSize + "!");

        return (row-1) * gridSize + col - 1;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col)
    {
        int i = getIndex(row, col);
        if (!isOpen[i]) {
            isOpen[i] = true;
            openSites++;
            // union with adjacent open sites
            // left
            if (col > 1 && isOpen(row, col - 1)) {
                uf.union(i, i - 1);
                ufFull.union(i, i - 1);
            }
            // right
            if (col < gridSize && isOpen(row, col + 1)) {
                uf.union(i, i + 1);
                ufFull.union(i, i + 1);
            }
            // up
            // if the site is on the first row, connect to the virtual top site
            if (row == 1) {
                uf.union(i, vts);
                ufFull.union(i, vts);
            }
            // otherwise connect to the upper site
            if (row > 1 && isOpen(row - 1, col)) {
                uf.union(i, i - gridSize);
                ufFull.union(i, i - gridSize);
            }
            // down
            // if the site is on the last row, connect to virtual bottom site
            // ufFull is not updated since the vbs has been excluded from the site list
            if (row == gridSize)
                uf.union(i, vbs);
            // otherwise connect the lower site
            if (row < gridSize && isOpen(row + 1, col)) {
                uf.union(i, i + gridSize);
                ufFull.union(i, i + gridSize);
            }
        }

    }
    // is the site (row, col) open?
    public boolean isOpen(int row, int col)
    {
        return isOpen[getIndex(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col)
    {
        int index = getIndex(row, col);
        // Check if the site is open and connected to the virtual top
        return isOpen(row, col) && ufFull.find(index) == ufFull.find(vts);
    }
    // returns the number of open sites
    public int numberOfOpenSites()
    {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates()
    {    // Check if the virtual bottom site and the virtual top site are connected
        return uf.find(vts) == uf.find(vbs);
    }
    // For debugging reason
    public String toString()
    {
        char[][] grid = new char[gridSize][gridSize];
        StringBuilder sb = new StringBuilder();
        /* Create and print a chessboard. */
        char[] symbols = {'□', '■', 'F'};
        for (int y = 1; y <= gridSize; y++) {
            for (int x = 1; x <= gridSize; x++) {
                if (isFull(y, x))
                    grid[y-1][x-1] = symbols[2];
                else if (isOpen(y, x))
                    grid[y-1][x-1] = symbols[0];
                else
                    grid[y-1][x-1] = symbols[1];
            }
        }
        for (char[] row: grid) { 
            for (char site : row) {
                sb.append(site);
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    // test client (optional)
    public static void main(String[] args)
    {
        Percolation p = new Percolation(3);
        p.open(1, 2);
        StdOut.println(p.percolates() ? "Percolates" : " Not Yet");
        StdOut.println(p);
        p.open(2, 2);
        StdOut.println(p.percolates() ? "Percolates" : " Not Yet");
        StdOut.println(p);
        p.open(3, 3);
        StdOut.println(p.percolates() ? "Percolates" + p.numberOfOpenSites() : " Not Yet");
        StdOut.println(p);
        p.open(3, 1);
        StdOut.println(p.percolates() ? "Percolates" + p.numberOfOpenSites() : " Not Yet");
        p.open(2, 1);
        StdOut.println(p.percolates() ? "Percolates" + p.numberOfOpenSites() : " Not Yet");
        StdOut.println(p);
    }
}
