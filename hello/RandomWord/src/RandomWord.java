/******************************************************************************
 *  Compilation:  javac -classpath algs4.jar RandomWord.java
 *  Execution:    java -classpath RandomWord:algs4.jar RandomWord
 *
 *  Reads a sequence of words from standard input and prints one
 *  of those words uniformly at random. Do not store the words in
 *  an array or list. Instead, use Knuth’s method: when reading
 *  the ith word, select it with probability 1/i to be the champion,
 *  replacing the previous champion. After reading all of the words,
 *  print the surviving champion.
 *
 *  % java -classpath RandomWord:algs4.jar RandomWord
 *  heads tails \n^D
 *  heads
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        String currentWord, randomWord = null;

        int i = 1;
        do {
            currentWord = StdIn.readString();
            if (StdRandom.bernoulli(1.0/i++))
                randomWord = currentWord;
        }
        while (!StdIn.isEmpty());
        StdOut.println(randomWord);
    }
}
