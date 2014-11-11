package ru.ifmo.md.colloquium2;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by Алексей on 11.11.2014.
 */
public class Candidate implements Comparable<Candidate> {
    private String name;
    private int count;
    private long DB_ID;

    public long getDB_ID() {
        return DB_ID;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setDB_ID(long DB_ID) {
        this.DB_ID = DB_ID;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public Candidate(String name) {
        this.name = name;
    }

    public void voice() {
        count++;
    }


    @Override
    public int compareTo(Candidate candidate) {
        return candidate.getCount() - count;
    }
}
