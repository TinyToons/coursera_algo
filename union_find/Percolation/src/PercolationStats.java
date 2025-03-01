import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import static java.lang.Math.sqrt;

public class PercolationStats {
    // Trials
    private final int t;
    // Trial's fraction of open sites
    private final double[] f_op;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials)
    {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("Grid size and number of trials must be positive!");
        // set number of sites and number of trials
        // Number of sites
        int ns = n * n;
        t = trials;
        // Open sites cache
        f_op = new double[t];
        // Trials loop
        for (int i = 0; i < t; i++) {
            // Percolation class
            Percolation p = new Percolation(n);
            // Randomly open sites until percolation occurs
            while(! p.percolates()) {
                // Choose a site at random
                int row = StdRandom.uniformInt(1, n+1);
                int col = StdRandom.uniformInt(1, n+1);
                if (!p.isOpen(row, col)) {
                    p.open(row, col);
                }
            }
            // Record fraction of open sites for this trial
            f_op[i] = (double) p.numberOfOpenSites() / ns;
        }
    }

    // sample mean of percolation threshold
    public double mean(){
        return StdStats.mean(f_op);
    }

    // sample standard deviation of percolation threshold
    public double stddev(){
        return StdStats.stddev(f_op);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo(){
        return mean() - 1.96 * stddev() / sqrt(t);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi(){
        return mean() + 1.96 * stddev() / sqrt(t);
    }

    // test client (see below)
    public static void main(String[] args)
    {
        if (args.length != 2)
        {
            throw new IllegalArgumentException("Only 2 Parameters n and T are expected!");
        }
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, t);
        StdOut.printf("mean                    = %1$-17f", ps.mean());
        StdOut.println();
        StdOut.printf("stddev                  = %1$-18f", ps.stddev());
        StdOut.println();
        StdOut.print("95% confidence interval = [");
        StdOut.printf("%1$-16.15f, %2$-16.15f]", ps.confidenceLo(), ps.confidenceHi());
    }

}
