import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    // Grid size
    private final int  gridSize;
    // Count of open sites
    private int openSites;
    // isOpen[i]: is site(i) opened
    // isFull[i] :is site(i) full
    private final boolean[] isOpen, isFullCache;
    // index of virtual top and virtual bottom sites
    private final int vts, vbs;
    // WQUUF object
    private final WeightedQuickUnionUF uf;

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
        // Init open flag of i: all the cells are blocked
        isOpen = new boolean[gridIndexSize];
        isFullCache = new boolean[gridIndexSize];
        for (int i = 0; i < gridIndexSize; i++)
        {
            isOpen[i] = false;
            isFullCache[i] = false;
        }
        // virtual sites indexes
        vts = n * n;
        vbs = n * n + 1;

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
        if (! isOpen[i]) {
            isOpen[i] = true;
            openSites++;
            // union with adjacent open sites
            // left
            if (col > 1 && isOpen(row, col - 1))
                uf.union(i, i - 1);
            // right
            if (col < gridSize && isOpen(row, col + 1))
                uf.union(i, i + 1);
            // up
            // if the site is on the first row, connect to the virtual top site
            if (row == 1)
                uf.union(i, vts);
            // otherwise connect to the upper site
            if (row > 1 && isOpen(row - 1, col))
                uf.union(i, i - gridSize);
            // down
            // if the site is on the last row, connect to virtual bottom site
            if (row == gridSize)
                uf.union(i, vbs);
            // otherwise connect the lower site
            if (row < gridSize && isOpen(row + 1, col))
                uf.union(i, i + gridSize);
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
        // Check the cache if the site is already full
        if (isFullCache[index]) return true;
        // Check if the site is open and connected to the virtual top
        boolean full = isOpen(row, col) && uf.find(index) == uf.find(vts);
        isFullCache[index] = full;
        return full;
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
        char[] symbols = {'□', '■', '●'};
        for(int y=1;y<=gridSize;y++){
            for (int x=1;x<=gridSize;x++){
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
        Percolation p = new Percolation(2);
        p.open(2, 1);
        System.out.println(p.percolates() ? "Percolates" : " Not Yet");
        System.out.println(p);
        p.open(1, 2);
        System.out.println(p.percolates() ? "Percolates" : " Not Yet");
        System.out.println(p);
        p.open(1, 1);
        System.out.println(p.percolates() ? "Percolates" + p.numberOfOpenSites() : " Not Yet");
        System.out.println(p);
    }
}
