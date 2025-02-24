import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    // Grid size
    private final int  gs;
    // Count of open sites
    private int openSites;
    // op[i]: is site(i) opened
    private final boolean[] op;
    // index of virtual top and virtual bottom sites
    private final int vts, vbs;
    // WQUUF object
    private final WeightedQuickUnionUF uf;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n)
    {
        if (n <= 0) 
            throw new IllegalArgumentException("n should be greater than 0!");
        gs = n;
        openSites = 0;
        // grid index size: n2 + 2 virtual sites
        int gis = n * n + 2;
        // Init uf
        uf = new WeightedQuickUnionUF(gis);
        // Init open flag of i: all the cells are blocked
        op = new boolean[gis];
        for (int i = 0; i < gis; i++) op[i] = false;
        // virtual sites indexes
        vts = n * n;
        vbs = n * n + 1;

    }

    // get the array index from row and cell's column
    private int getIndex(int row, int col)
    {
        if (row < 1 || row > gs || col < 1 || col > gs)
            throw new IllegalArgumentException(" 1 <= row <= " + gs + " and 1 <= col <= " + gs + "!");

        return (row-1) * gs + col - 1;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col)
    {
        int i = getIndex(row, col);
        if (! op[i]) {
            op[i] = true;
            openSites++;
            // union with adjacent open sites
            // left
            if (col > 1 && isOpen(row, col - 1))
                uf.union(i, i - 1);
            // right
            if (col < gs && isOpen(row, col + 1))
                uf.union(i, i + 1);
            // up
            // if the site is on the first row, connect to the virtual top site
            if (row == 1)
                uf.union(i, vts);
            if (row > 1 && isOpen(row - 1, col))
                uf.union(i, i - gs);
            // down
            // if the site is on the last row, connect to virtual bottom site
            if (row == gs)
                uf.union(i, vbs);
            if (row < gs && isOpen(row + 1, col))
                uf.union(i, i + gs);
        }

    }
    // is the site (row, col) open?
    public boolean isOpen(int row, int col)
    {
        return op[getIndex(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col)
    {
        int index = getIndex(row, col);
        // check if the site is open and in the same set with the virtual top site
        return isOpen(row, col) && uf.find(index) == uf.find(vts);
    }
    // returns the number of open sites
    public int numberOfOpenSites()
    {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates()
    {    // Check if the virtual bottom site and the virtual top site are in the same set
        return uf.find(vts) == uf.find(vbs);
    }
    // For debug reason
    public String toString()
    {
        char[][] grid = new char[gs][gs];
        StringBuilder sb = new StringBuilder();
        /* Create and print a chessboard. */
        char[] colors = {'\u25A1', '\u25A0'};
        for(int y=1;y<=gs;y++){
            for (int x=1;x<=gs;x++){
                if (isOpen(y, x))
                    grid[y-1][x-1] = colors[0];
                else
                    grid[y-1][x-1] = colors[1];
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
