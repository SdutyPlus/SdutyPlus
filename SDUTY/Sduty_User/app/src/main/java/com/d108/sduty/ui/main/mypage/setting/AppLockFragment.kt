package com.d108.sduty.ui.main.mypage.setting

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.d108.sduty.R
import com.d108.sduty.adapter.AppListAdapter
import com.d108.sduty.common.ApplicationClass.Companion.appLockPref
import com.d108.sduty.databinding.FragmentAppLockBinding
import com.d108.sduty.model.AppInfo
import com.d108.sduty.ui.main.mypage.dialog.AccessibilityDialog
import com.d108.sduty.ui.viewmodel.MainViewModel
import com.d108.sduty.utils.navigateBack
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "AppLockFragment"

class AppLockFragment : Fragment() {
    private lateinit var binding: FragmentAppLockBinding

    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var packageManager: PackageManager
    private lateinit var packages: List<PackageInfo>

    private var userAppList = listOf<AppInfo>()
    private lateinit var allAppList: List<AppInfo>

    private lateinit var appListAdapter: AppListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainViewModel.displayBottomNav(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAppLockBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkAccessibilityPermission()
        initView()
    }

    private fun initView() {
        binding.apply {
            animationView.speed = 2f
            animationView.playAnimation()
            ivBack.setOnClickListener {
                navigateBack(requireActivity())
            }
            // 탭 레이아웃 선택 이벤트
            tabAppList.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab!!.position) {
                        0 -> {
                            appListAdapter.list = userAppList
                        }
                        1 -> {
                            appListAdapter.list = allAppList
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {
                    when (tab!!.position) {
                        0 -> {
                            appListAdapter.list = userAppList
                        }
                        1 -> {
                            appListAdapter.list = allAppList
                        }
                    }
                }
            })
        }



        // 앱 목록에서 전체 앱, 사용자 앱을 구분하여 리스트를 만든다.
        CoroutineScope(Dispatchers.IO).launch {
            // 설치된 앱 목록을 받아온다.
            packageManager = requireActivity().packageManager
            packages = packageManager.getInstalledPackages(0)
            getAppList()
            getUserAppList()
            updateAdapter(userAppList)
        }
        // 초기화면은 사용자 앱 목록을 보여준다.
        updateAdapter(userAppList)
    }

    private fun getAppList() {
        allAppList = packages.filter {
            it.packageName != resources.getString(R.string.app_package_name)
        }.map {
            val icon = packageManager.getApplicationIcon(it.packageName)
            val label = it.applicationInfo.loadLabel(packageManager).toString()
            val packageName = it.packageName
            val isBanned = appLockPref.getBoolean(packageName, false)

            AppInfo(icon, label, packageName, isBanned)
        }.sortedBy { it.label }
    }

    private fun getUserAppList() {
        userAppList = packages.filter {
            ((it.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 1) && it.packageName != resources.getString(R.string.app_package_name)
        }.map {
            val icon = packageManager.getApplicationIcon(it.packageName)
            val label = it.applicationInfo.loadLabel(packageManager).toString()
            val packageName = it.packageName
            val isBanned = appLockPref.getBoolean(packageName, false)

            AppInfo(icon, label, packageName, isBanned)
        }.sortedBy { it.label }
        CoroutineScope(Dispatchers.Main).launch {
            appListAdapter.list = userAppList
            binding.apply {
                animationView.pauseAnimation()
                animationView.visibility = View.GONE
            }
        }

    }

    // 리사이클러뷰 갱신
    private fun updateAdapter(appList: List<AppInfo>) {
        appListAdapter = AppListAdapter(requireActivity())
        appListAdapter.onClickAppInfoItem = object : AppListAdapter.OnClickAppInfoItem {
            override fun onClick(view: View, fragmentActivity: FragmentActivity, position: Int) {
                checkAccessibilityPermission()

                val isBanned = !appList[position].isBanned
                appList[position].isBanned = isBanned

                // sharedPreference 에 저장
                appLockPref.edit().putBoolean(appList[position].packageName, isBanned).apply()

                allAppList.filter { appList[position].packageName == it.packageName }
                    .map { it.isBanned = isBanned }
                userAppList.filter { appList[position].packageName == it.packageName }
                    .map { it.isBanned = isBanned }

                appListAdapter.notifyItemChanged(position)
            }
        }
        binding.rvAppList.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = appListAdapter
        }
    }

    private fun checkAccessibilityPermission() {
        val accessibilityManager =
            requireActivity().getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val list =
            accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)

        val result = list.find {
            it.resolveInfo.serviceInfo.packageName == requireActivity().packageName
        }
        if (result == null) {
            AccessibilityDialog().show(
                this.requireActivity().supportFragmentManager,
                "AccessibilityDialog"
            )
        }
    }

//    private fun setPermissions() {
//        val permissionDialog = AlertDialog.Builder(requireContext())
//        permissionDialog.apply {
//            setTitle("접근성 권한 설정")
//            setMessage("앱을 사용하기 위해 접근성 권한이 필요합니다.")
//            setPositiveButton("허용", object : DialogInterface.OnClickListener {
//                override fun onClick(p0: DialogInterface?, p1: Int) {
//                    requireContext().startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
//                }
//            }).create().show()
//        }
//    }
}