package ru.ifmo.md.colloquium2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Алексей on 11.11.2014.
 */
public class Voting {
    private CandidateDBHelper dbHelper;
    private SQLiteDatabase database;
    private List<Candidate> listOfCandidate;
    private CandidateAdapter candidateAdapter;
    private Context context;

    public Voting(CandidateAdapter candidateAdapter, Context context) {
        this.candidateAdapter = candidateAdapter;
        this.context = context;
        listOfCandidate = new ArrayList<Candidate>();
        this.candidateAdapter.setData(listOfCandidate);
        startDBConnection();
        candidateAdapter.notifyDataSetChanged();
    }

    void startDBConnection() {
        dbHelper = new CandidateDBHelper(context);
        database = dbHelper.getWritableDatabase();
        Cursor c = database.query(CandidateDBHelper.CANDIDATE_TABLE_NAME, new String[]{CandidateDBHelper.CANDIDATE_ID,
                CandidateDBHelper.CANDIDATE_NAME, CandidateDBHelper.CANDIDATE_COUNT}, "", null, null, null, null);
        int sum = 0;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Candidate candidate = new Candidate(c.getString(c.getColumnIndex(CandidateDBHelper.CANDIDATE_NAME)));
            candidate.setDB_ID(c.getLong(c.getColumnIndex(CandidateDBHelper.CANDIDATE_ID)));
            candidate.setCount(c.getInt(c.getColumnIndex(CandidateDBHelper.CANDIDATE_COUNT)));
            listOfCandidate.add(candidate);
            sum += candidate.getCount();
            candidateAdapter.notifyDataSetChanged();
        }
        candidateAdapter.setSum(sum);
    }

    void addCandidateDB (Candidate candidate) {
        ContentValues values = new ContentValues();
        values.put(CandidateDBHelper.CANDIDATE_NAME, candidate.getName());
        values.put(CandidateDBHelper.CANDIDATE_COUNT, candidate.getCount());
        long id = database.insert(CandidateDBHelper.CANDIDATE_TABLE_NAME, null, values);
        candidate.setDB_ID(id);
        listOfCandidate.add(candidate);
        candidateAdapter.notifyDataSetChanged();
    }

    void start() {
        candidateAdapter.start_voting();
    }

    public void finish() {
        candidateAdapter.finish();
    }

    public void restart() {
        dbHelper.deleteTable(database);
        listOfCandidate = new ArrayList<Candidate>();
        candidateAdapter.finish();
    }

    public void voice(int i) {
        if (candidateAdapter.voting) {
            Candidate candidate = candidateAdapter.getItem(i);
            candidate.setCount(candidate.getCount() + 1);
            updateCandidateDB(candidate);
            candidate.setCount(candidate.getCount() - 1);
            candidateAdapter.voice(i);
        }
    }

    public void updateCandidateDB(Candidate candidate) {
        ContentValues values = new ContentValues();
        values.put(CandidateDBHelper.CANDIDATE_NAME, candidate.getName());
        values.put(CandidateDBHelper.CANDIDATE_COUNT, candidate.getCount());
        database.update(CandidateDBHelper.CANDIDATE_TABLE_NAME, values, CandidateDBHelper.CANDIDATE_ID + "="
                + candidate.getDB_ID(), null);
    }



}
