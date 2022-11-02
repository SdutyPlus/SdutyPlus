package com.d108.sduty.ui

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.d108.sduty.R
import com.d108.sduty.databinding.ActivityMainBinding
import com.d108.sduty.ui.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.frame_main) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

        binding.apply {
            lifecycleOwner = this@MainActivity
            mainVM = viewModel
        }
//        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

    }



    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val view = currentFocus;          // 현재 터치 위치

        if (view != null && (ev?.action == MotionEvent.ACTION_UP
                    || ev?.action == MotionEvent.ACTION_MOVE)
            && view is EditText
            && !view.javaClass.name.startsWith("android.webkit.")) {
            // view 의 id 가 명시되어있지 않은 다른 부분을 터치 시

            var scrcoords = IntArray(2)
            view.getLocationOnScreen(scrcoords);        // 0 은 x 마지막 터치 위치에서 x 값
            // 1은 y 마지막 터치 위치에서 y 값

            val x = ev.rawX + view.getLeft() - scrcoords[0];
            val y = ev.rawY + view.getTop() - scrcoords[1];

            if (x < view.getLeft() || x > view.getRight()
                || y < view.getTop() || y > view.getBottom())
                (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow((this.window.decorView.applicationWindowToken), 0);
        }

        return super.dispatchTouchEvent(ev);
//        val focusView = currentFocus
//        if (focusView != null && ev != null) {
//            val rect = Rect()
//            focusView.getGlobalVisibleRect(rect)
//            val x = ev.x.toInt()
//            val y = ev.y.toInt()
//            if (!rect.contains(x, y)) {
//                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//                imm?.hideSoftInputFromWindow(focusView.windowToken, 0)
//                focusView.clearFocus()
//            }
//        }
//        return super.dispatchTouchEvent(ev)
    }
}