package com.app.recyclerviewdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.recyclerviewdemo.base.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者:胡涛
 * 日期:2021-6-19
 * 时间:16:45
 * 功能:RecyclerView 的滑动到底部自动刷新的逻辑以及UI交互
 */
public class MainActivity extends AppCompatActivity {

    private List<Model> list;
    private RecyclerView recyclerView;
    private ModelAdapter adapter;
    private Handler handler;

    private int maxPageTotal = 5;
    private int currentPageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();
        recyclerView = findViewById(R.id.recyclerView);
        list = new ArrayList<>();
        adapter = new ModelAdapter(list, this);

        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        list.add(new Model("加载中", BaseRecyclerAdapter.HEADER_TYPE));
        adapter.notifyDataSetChanged();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                list.clear();
                for (int i = 0; i < 3; i++) {
                    list.add(new Model("普通数据: " + i, BaseRecyclerAdapter.NORMAL_TYPE));
                }
                adapter.notifyDataSetChanged();
                currentPageIndex = 1;
            }
        }, 2000);
    }

    private void refreshData() {
        list.clear();
        list.add(new Model("加载中", BaseRecyclerAdapter.HEADER_TYPE));
        adapter.notifyDataSetChanged();
        currentPageIndex = 1;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //一出加载进度条
                list.remove(list.size() - 1);

                for (int i = 0; i < 3; i++) {
                    list.add(new Model("普通数据:" + i, BaseRecyclerAdapter.NORMAL_TYPE));
                }
                adapter.notifyDataSetChanged();
                currentPageIndex = 1;
            }
        }, 2000);
    }

    public void refresh(View view) {
        refreshData();
    }

    private void loadMoreData() {
        if (currentPageIndex == maxPageTotal) {
            Toast.makeText(this, "没有了", Toast.LENGTH_SHORT).show();
            return;
        }

        list.add(new Model("加载更多中...", BaseRecyclerAdapter.FOOTER_TYPE));
        adapter.notifyDataSetChanged();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentPageIndex++;

                //把那个加载更多的最后一个item移除掉
                list.remove(list.size() - 1);

                for (int i = 0; i < 3; i++) {
                    list.add(new Model("普通数据:" + i, BaseRecyclerAdapter.NORMAL_TYPE));
                }
                if (currentPageIndex == maxPageTotal) {
                    list.add(new Model("没有更多数据", BaseRecyclerAdapter.FOOTER_TYPE));
                }

                adapter.notifyDataSetChanged();
            }
        }, 2000);
    }

    public void loadMore(View view) {
        loadMoreData();
    }
}
