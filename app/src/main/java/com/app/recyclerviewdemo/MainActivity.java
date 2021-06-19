package com.app.recyclerviewdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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

    private static final String TAG = "MainActivity";
    private List<Model> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private ModelAdapter adapter;
    private Handler handler;

    private int maxPageTotal = 5;
    private int currentPageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        setConfig();

        //刷新数据
        refreshData();

        //滑动监听
        setListener();
    }

    private void setConfig() {
        GridLayoutManager manager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                //重点在这里，这个getSpanSize方法的返回值其实指的是单个item所占用的我们设置的每行的item个数
                Model model = list.get(position);
                switch (model.getViewType()) {
                    case BaseRecyclerAdapter.HEADER_TYPE:
                        return 2;
                    case BaseRecyclerAdapter.NORMAL_TYPE:
                        return 1;
                    case BaseRecyclerAdapter.FOOTER_TYPE:
                        return 2;
                    default:
                        return 2;
                }
            }
        });
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private void init() {
        handler = new Handler();
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new ModelAdapter(list, this);
    }

    private void setListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
                    int lastPos = manager.findLastVisibleItemPosition();

                    if (manager.getChildCount() > 0 && lastPos > manager.getItemCount() - 2
                            && manager.getItemCount() > manager.getChildCount()) {
                        Log.i(TAG, "lastPos ======= 开始加载数据 =========> " + lastPos);
                        if (!isLoading) {
                            loadMoreData();
                        }
                    }
                }
            }
        });

        adapter.setOnItemClickListener(new ModelAdapter.OnItemClickListener<Model>() {
            @Override
            public void onItemClick(int position, Model model) {
                Toast.makeText(MainActivity.this, position + ", " + model.getContent(), Toast.LENGTH_SHORT).show();
            }
        });
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

                for (int i = 0; i < 10; i++) {
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

    //限制列表在加载状态下，是无法触发再次加载的
    private boolean isLoading = false;

    private void loadMoreData() {
        if (currentPageIndex >= maxPageTotal) {
            Toast.makeText(this, "没有了", Toast.LENGTH_SHORT).show();
            return;
        }

        list.add(new Model("加载更多中...", BaseRecyclerAdapter.FOOTER_TYPE));
        adapter.notifyDataSetChanged();

        //锁住再次加载的bug
        isLoading = true;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentPageIndex++;

                //把那个加载更多的最后一个item移除掉
                list.remove(list.size() - 1);

                for (int i = 0; i < 10; i++) {
                    list.add(new Model("普通数据:" + i, BaseRecyclerAdapter.NORMAL_TYPE));
                }
                if (currentPageIndex == maxPageTotal) {
                    list.add(new Model("没有更多数据", BaseRecyclerAdapter.FOOTER_TYPE));
                }

                adapter.notifyDataSetChanged();

                //释放可以触发再次加载的锁
                isLoading = false;
            }
        }, 2000);
    }

    public void loadMore(View view) {
        loadMoreData();
    }
}
