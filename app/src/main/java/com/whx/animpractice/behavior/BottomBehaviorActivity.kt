package com.whx.animpractice.behavior

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import com.whx.animpractice.R
import com.whx.animpractice.emmm.EasyPickerView
import com.whx.animpractice.emmm.PickerScrollView
import com.whx.animpractice.emmm.WheelView

class BottomBehaviorActivity : AppCompatActivity() {
    private var mBottomSheetDialog: BottomSheetDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_behavior)

        val btn = findViewById<Button>(R.id.bottom_btn)
//        val view = findViewById<LinearLayout>(R.id.bottom_view)
        val dialogBtn = findViewById<Button>(R.id.bottom_dialog)

//        val sheetBehavior = BottomSheetBehavior.from(view)

//        sheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//
//            }
//
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//
//            }
//        })
//
//        // 下滑的时候是否可以隐藏
//        sheetBehavior.isHideable = true
//        btn.setOnClickListener {
//            if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
//                sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//                btn.text = "hide bottom"
//            } else {
//                sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
//                btn.text = "show bottom"
//            }
//        }

        dialogBtn.setOnClickListener {
            try {
                showBottomDialog()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun showBottomDialog() {
        if (mBottomSheetDialog == null) {
            mBottomSheetDialog = BottomSheetDialog(this)

            val view = LayoutInflater.from(this).inflate(R.layout.bottom_dialog, null)
            val pick = view.findViewById<WheelView>(R.id.pick)

            pick.setData(arrayListOf(WheelView.WheelData(1, "1L"),
                    WheelView.WheelData(2, "2L"),
                    WheelView.WheelData(3, "3L"),
                    WheelView.WheelData(4, "4L"),
                    WheelView.WheelData(5, "5L")))

            pick.setOnScrollChangedListener(object : WheelView.OnScrollChangedListener {
                override fun onScrollChanged(index: Int) {
//                    Toast.makeText(this@BottomBehaviorActivity, curIndex, Toast.LENGTH_SHORT).show()
                }

                override fun onScrollFinished(index: Int) {
                    Toast.makeText(this@BottomBehaviorActivity, index.toString(), Toast.LENGTH_SHORT).show()
                }
            })

            mBottomSheetDialog?.let {
                it.setContentView(view)

//                val field = it::class.java.getDeclaredField("mBehavior")
//                field.isAccessible = true
//
//                val behavior = field.get(it) as BottomSheetBehavior<*>
//                behavior.peekHeight = resources.getDimensionPixelSize(R.dimen.dp_250)
            }



        } else {
            mBottomSheetDialog!!.show()
        }
    }
}