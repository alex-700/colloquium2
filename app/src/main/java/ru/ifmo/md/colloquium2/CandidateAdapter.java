package ru.ifmo.md.colloquium2;

import android.graphics.Color;
import android.provider.BaseColumns;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Алексей on 11.11.2014.
 */
public class CandidateAdapter extends BaseAdapter {

    private List<Candidate> data;
    public boolean voting;
    public boolean finish;
    private int sum;

    public void setSum(int sum) {
        this.sum = sum;
    }

    public CandidateAdapter(boolean voting, boolean finish) {
        this.data = new ArrayList<Candidate>();
        this.voting = voting;
        this.finish = finish;
    }

    public CandidateAdapter(List<Candidate> data) {
        this.data = data;
        voting = false;
        finish = false;
        sum = 0;
    }

    public void setData(List<Candidate> data) {
        this.data = data;
        sum = 0;
        for (Candidate candidate : data) {
            sum += candidate.getCount();
        }
    }

    public void start_voting() {
        voting = true;
        finish = false;
        notifyDataSetChanged();
    }

    public void voice(int i) {
        if (voting) {
            getItem(i).voice();
            sum++;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Candidate getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private String to_percent(double x) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        return df.format(x * 100) +"%";
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v;
        if (view == null) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.candidate_layout, viewGroup, false);
        } else {
            v = view;
        }
        Candidate current = getItem(i);
        TextView textView = (TextView) v.findViewById(R.id.name);
        textView.setText(current.getName());


        if (voting) {
            textView = (TextView) v.findViewById(R.id.voice_count);
            String s = String.valueOf(current.getCount() + " ");
            textView.setText(s);

            textView = (TextView) v.findViewById(R.id.percent);
            if (sum == 0) {
                textView.setText("0.00");
            } else {
                textView.setText(to_percent((double) current.getCount() / sum));
            }
        } else {
            textView = (TextView) v.findViewById(R.id.voice_count);
            textView.setText("");

            textView = (TextView) v.findViewById(R.id.percent);
            textView.setText("");
        }
        if (i % 2 == 0) {
            v.setBackgroundColor(0x0F000000);
        } else {
            v.setBackgroundColor(Color.WHITE);
        }
        if (finish && i == 0) {
            v.setBackgroundColor(Color.RED);
        }
        return v;
    }

    public void finish() {
        Collections.sort(data);
        notifyDataSetChanged();
        finish = true;
    }
}
