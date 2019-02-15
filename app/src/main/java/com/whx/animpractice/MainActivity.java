package com.whx.animpractice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.whx.animpractice.behavior.BottomBehaviorActivity;
import com.whx.animpractice.behavior.SwipeBehaviorActivity;
import com.whx.animpractice.property.ObjectAnimatorTestActivity;
import com.whx.animpractice.property.ValueAnimatorTestActivity;
import com.whx.animpractice.transition.TransitionActivityA;
import com.whx.animpractice.transition.change_activity.SwitchAnimActivity;


public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button toAppbar = findViewById(R.id.to_appbar);
        toAppbar.setOnClickListener(v -> startActivity(new Intent(this, AppbarLayoutTestActivity.class)));

        findViewById(R.id.to_bottom_behavior).setOnClickListener(v -> startActivity(new Intent(this, BottomBehaviorActivity.class)));

        findViewById(R.id.to_swipe_behavior).setOnClickListener(v -> startActivity(new Intent(this, SwipeBehaviorActivity.class)));

        findViewById(R.id.to_value_anim).setOnClickListener(v -> startActivity(new Intent(this, ValueAnimatorTestActivity.class)));

        findViewById(R.id.to_object_anim).setOnClickListener(v -> startActivity(new Intent(this, ObjectAnimatorTestActivity.class)));

        findViewById(R.id.to_transition).setOnClickListener(v -> startActivity(new Intent(this, TransitionActivityA.class)));

        findViewById(R.id.to_activity_switch).setOnClickListener(v -> startActivity(new Intent(this, SwitchAnimActivity.class)));
    }
}
