package com.example.junemon.uploadfilteringimage_firebase.ui.adapter.imagefilteradapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Created by ian on 07/02/19.
 */

class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    private val mFragmentList = arrayListOf<Fragment>()
    private val mFragmentTitleList = arrayListOf<String>()

    override fun getItem(p0: Int): Fragment {
        return mFragmentList.get(p0)
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList.get(position)
    }

    fun addFragment(frag: Fragment?, tittle: String) {
        frag?.let { mFragmentList.add(it) }
        mFragmentTitleList.add(tittle)
    }
}