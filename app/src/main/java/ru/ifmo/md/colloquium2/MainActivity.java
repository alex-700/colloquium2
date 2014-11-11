package ru.ifmo.md.colloquium2;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ListActivity {
    private Button button;
    private EditText editText;
    private ListView listView;
    private List<Candidate> listOfCandidate;
    private CandidateAdapter candidateAdapter;
    private Voting voting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(android.R.id.list);

        Boolean savedVoting = null;
        Boolean savedFinish = null;

        if (savedInstanceState != null) {
            savedVoting = savedInstanceState.getBoolean("voting");
            savedFinish = savedInstanceState.getBoolean("finish");
        }
        if (savedFinish == null || savedVoting == null) {
            savedFinish = false;
            savedVoting = false;
        }
        candidateAdapter = new CandidateAdapter(savedVoting, savedFinish);
        if (savedVoting || savedFinish) {
            button.setEnabled(false);
        }
        listView.setAdapter(candidateAdapter);

        voting = new Voting(candidateAdapter, getApplicationContext());
        voting.startDBConnection();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voting.addCandidateDB(new Candidate(editText.getText().toString()));
            }
        });
        ((Button) findViewById(R.id.start)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setEnabled(false);
                voting.start();
            }
        });

        ((Button) findViewById(R.id.finish_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voting.finish();
            }
        });

        ((Button) findViewById(R.id.restart_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setEnabled(true);
                voting.restart();
                listView.setAdapter(candidateAdapter);
                voting = new Voting(candidateAdapter, getApplicationContext());
                voting.startDBConnection();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                voting.voice(i);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("voting", candidateAdapter.voting);
        outState.putBoolean("finish", candidateAdapter.finish);
    }
}
