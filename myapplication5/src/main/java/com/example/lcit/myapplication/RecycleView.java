package com.example.lcit.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${qiuweizhong} on 2017/3/15.
 */
public class RecycleView extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<String> date = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        for (int i = 0; i < 1000; i++) {
            date.add("" + i);
        }
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));//普通形式
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));//瀑布流形式
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL));//瀑布流形式
        MyAdapter myAdapter = new MyAdapter(date);
        mRecyclerView.setAdapter(myAdapter);

    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private List<String> date;
        private List<Integer> height;
        public MyAdapter(List<String> date){
            this.date = new ArrayList<>();
            this.height = new ArrayList<>();
            for (int i = 0; i < date.size(); i ++) {
                height.add((int) (100 + Math.random() * 300));
            }
            this.date = date;
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder myViewHolder = new MyViewHolder(LayoutInflater.from(RecycleView.this).inflate(R.layout.item_list,
                    parent, false));
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv.setText(date.get(position) + "RecycleView");
            ViewGroup.LayoutParams p = holder.tv.getLayoutParams();
//            LayoutParams p =  holder.tv.getLayoutParams();
            p.height = this.height.get(position);
            holder.tv.setLayoutParams(p);
//            holder.tv.setHeight((int)(Math.random() * 100));
//            holder.tv.setHeight(1010);
        }

        @Override
        public int getItemCount() {
            return date.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }
    }
}
