package com.ren.kotlin_module

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.ren.middleware.Module
import com.ren.middleware.ResourceUtils
import kotlinx.android.synthetic.main.activity_other.*


@Route(path = Module.KOTLIN)
class OtherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layoutId = ResourceUtils.getLayoutId(application, "activity_other")
        setContentView(layoutId)

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavHostFragment.findNavController(nav_fragment).navigateUp()
    }
}