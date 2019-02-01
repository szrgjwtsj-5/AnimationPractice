package com.whx.animpractice.transition.share_element;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;

import com.whx.animpractice.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BrowseShareElementTransitionActivity extends AppCompatActivity implements MyRecyclerAdapter.ItemClickListener {

    public static final String EXTRA_START_POSITION   = "start_position";
    public static final String EXTRA_CURRENT_POSITION = "current_position";

    private Activity mActivity;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter adapter;
    private Bundle mReenterState;
    private List<String> urls;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_recycler);
        initShareElement();
        mActivity = this;
        mContext = this;
        initView();
        initEvent();
        initData();
    }

    @Override
    public void onActivityReenter(int requestCode, Intent data) {
        super.onActivityReenter(requestCode, data);
        mReenterState = new Bundle(data.getExtras());
        int startingPosition = mReenterState.getInt(EXTRA_START_POSITION);
        int currentPosition = mReenterState.getInt(EXTRA_CURRENT_POSITION);
        if (startingPosition != currentPosition) {
            mRecyclerView.scrollToPosition(currentPosition);
        }
        postponeEnterTransition();

        mRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                mRecyclerView.requestLayout();

                startPostponedEnterTransition();
                return true;
            }
        });

//        mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v,
//                                       int left,
//                                       int top,
//                                       int right,
//                                       int bottom,
//                                       int oldLeft,
//                                       int oldTop,
//                                       int oldRight,
//                                       int oldBottom) {
//                mRecyclerView.removeOnLayoutChangeListener(this);
//                final RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
//                View viewAtPosition = layoutManager.findViewByPosition(currentPosition);
//                // Scroll to position if the view for the current position is null (not currently part of
//                // layout manager children), or it's not completely visible.
//                if (viewAtPosition == null || layoutManager
//                        .isViewPartiallyVisible(viewAtPosition, false, true)) {
//                    mRecyclerView.post(() -> layoutManager.scrollToPosition(currentPosition));
//                }
//            }
//        });
    }

    @Override
    public void onItemClick(@NotNull View view, @NotNull String url, int position) {
        Intent intent = new Intent(mContext, ActivityBrowse.class);
        intent.putExtra(ActivityBrowse.EXTRA_START_POSITION, position);
        intent.putStringArrayListExtra("imageUrls", new ArrayList<>(urls));
        ActivityOptions compat = ActivityOptions.makeSceneTransitionAnimation(mActivity, view, view.getTransitionName());
        startActivity(intent, compat.toBundle());
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recyclerView);
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new MyRecyclerAdapter();
        adapter.setOnItemClickListener(this);

        mRecyclerView.setAdapter(adapter);
    }

    private void initEvent() {

    }

    private void initData() {
        urls = Arrays.asList("https://ws1.sinaimg.cn/large/0065oQSqly1fw0vdlg6xcj30j60mzdk7.jpg",
                "https://ws1.sinaimg.cn/large/0065oQSqly1fvexaq313uj30qo0wldr4.jpg",
                "https://ww1.sinaimg.cn/large/0065oQSqly1ftu6gl83ewj30k80tites.jpg",
                "http://ww1.sinaimg.cn/large/0065oQSqgy1ftt7g8ntdyj30j60op7dq.jpg",
                "https://ww1.sinaimg.cn/large/0065oQSqgy1ftrrvwjqikj30go0rtn2i.jpg",
                "https://ww1.sinaimg.cn/large/0065oQSqly1ftf1snjrjuj30se10r1kx.jpg",
                "https://ww1.sinaimg.cn/large/0065oQSqly1ftdtot8zd3j30ju0pt137.jpg",
                "http://ww1.sinaimg.cn/large/0073sXn7ly1ft82s05kpaj30j50pjq9v.jpg",
                "https://ww1.sinaimg.cn/large/0065oQSqly1ft5q7ys128j30sg10gnk5.jpg",
                "https://ww1.sinaimg.cn/large/0065oQSqgy1ft4kqrmb9bj30sg10fdzq.jpg");

        adapter.setData(urls);
    }

    private void initShareElement() {
        setExitSharedElementCallback(mCallback);
    }

    private final SharedElementCallback mCallback = new SharedElementCallback() {

        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (mReenterState != null) {
                //从别的界面返回当前界面
                int startingPosition = mReenterState.getInt(EXTRA_START_POSITION);
                int currentPosition = mReenterState.getInt(EXTRA_CURRENT_POSITION);
                if (startingPosition != currentPosition) {
                    String newTransitionName = urls.get(currentPosition);
                    View newSharedElement = mRecyclerView.findViewWithTag(newTransitionName);
                    if (newSharedElement != null) {
                        names.clear();
                        names.add(newTransitionName);
                        sharedElements.clear();
                        sharedElements.put(newTransitionName, newSharedElement);
                    }
                }
                mReenterState = null;
            } else {
                //从当前界面进入到别的界面
                View navigationBar = findViewById(android.R.id.navigationBarBackground);
                View statusBar = findViewById(android.R.id.statusBarBackground);
                if (navigationBar != null) {
                    names.add(navigationBar.getTransitionName());
                    sharedElements.put(navigationBar.getTransitionName(), navigationBar);
                }
                if (statusBar != null) {
                    names.add(statusBar.getTransitionName());
                    sharedElements.put(statusBar.getTransitionName(), statusBar);
                }
            }
        }
    };
}
