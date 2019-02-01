package com.whx.animpractice.transition.share_element;

import android.app.SharedElementCallback;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.whx.animpractice.R;

import java.util.List;
import java.util.Map;

public class ActivityBrowse extends AppCompatActivity implements MyPageAdapter.OnImageLoadComplete {

    public static final String EXTRA_START_POSITION = "start_position";

    private int            mStartPosition;
    private int            mCurrentPosition;
    private boolean        mIsReturning;
    private MyPageAdapter  adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_pager);
        initShareElement();
        mStartPosition = getIntent().getIntExtra(EXTRA_START_POSITION, 0);
        initView();
        initEvent();
        initData();
    }

    @Override
    public void finishAfterTransition() {
        mIsReturning = true;
        Intent data = new Intent();
        data.putExtra(BrowseShareElementTransitionActivity.EXTRA_START_POSITION, mStartPosition);
        data.putExtra(BrowseShareElementTransitionActivity.EXTRA_CURRENT_POSITION, mCurrentPosition);
        setResult(RESULT_OK, data);
        super.finishAfterTransition();
    }

    @Override
    public void startEnterTransition() {
        if (mCurrentPosition == mStartPosition) {
            adapter.getItemView().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    adapter.getItemView().getViewTreeObserver().removeOnPreDrawListener(this);
                    startPostponedEnterTransition();
                    return true;
                }
            });
        }
    }

    private void initView() {
        mCurrentPosition = mStartPosition;
        ViewPager pager = findViewById(R.id.viewPager);
        adapter = new MyPageAdapter(this);
        adapter.setData(getIntent().getStringArrayListExtra("imageUrls"));

        pager.setAdapter(adapter);
        pager.setCurrentItem(mCurrentPosition);
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
            }
        });
    }

    private void initEvent() {

    }

    private void initData() {

    }

    private void initShareElement() {
        postponeEnterTransition();
        setEnterSharedElementCallback(mCallback);
    }

    private final SharedElementCallback mCallback = new SharedElementCallback() {

        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (mIsReturning) {
                ImageView sharedElement = adapter.getItemView();
                if (sharedElement == null) {
                    names.clear();
                    sharedElements.clear();
                } else if (mStartPosition != mCurrentPosition) {
                    names.clear();
                    names.add(sharedElement.getTransitionName());
                    sharedElements.clear();
                    sharedElements.put(sharedElement.getTransitionName(), sharedElement);
                }
            }
        }
    };
}

