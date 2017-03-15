package com.example.lcit.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * listview的使用，今天回忆了一下listview的使用
 */
public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private List<String> date = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.listView);
        for (int i = 0; i < 100; i++) {
            date.add(i + "");
        }
        MyAdapter myAdapter = new MyAdapter(this, date);
        mListView.setAdapter(myAdapter);

    }

    class MyAdapter extends BaseAdapter {
        private Context mContext;
        private List<String> date;
        public MyAdapter(Context context, List<String> date) {
            this.mContext = context;
            this.date = date;
        }
        @Override
        public int getCount() {
            return date.size();
        }

        @Override
        public Object getItem(int i) {
            return date.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            MyViewHolder myViewHolder = null;
            if (view == null) {
                view = View.inflate(mContext, R.layout.item_list, null);
                myViewHolder = new MyViewHolder();
                myViewHolder.mTextView = (TextView) view.findViewById(R.id.tv);
                view.setTag(myViewHolder);
            } else {
                myViewHolder = (MyViewHolder) view.getTag();
            }
            if (i == date.size() - 1) {
                myViewHolder.mTextView.setText("+");
            } else {
                myViewHolder.mTextView.setText(date.get(i));
            }
            myViewHolder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(mContext, RecycleView.class));
                }
            });
            return view;
        }

        class MyViewHolder {
            TextView mTextView;
        }
    }
}
