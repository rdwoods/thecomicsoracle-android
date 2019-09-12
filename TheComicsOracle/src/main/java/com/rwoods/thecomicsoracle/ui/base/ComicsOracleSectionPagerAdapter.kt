package com.rwoods.thecomicsoracle.ui.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ComicsOracleSectionPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private val fragmentList: ArrayList<Fragment> = ArrayList()
    private val fragmentTitleList: ArrayList<String> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitleList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
    }


    /*fun getTabIcon(position: Int): Drawable? {
        when (position) {
            0 -> return ResourcesCompat.getDrawable(resources, R.drawable.ic_search_white_18dp, null)
            1 -> return ResourcesCompat.getDrawable(resources, R.drawable.ic_video_library_white_18dp, null)
            2 -> return ResourcesCompat.getDrawable(resources, R.drawable.ic_thumb_up_white_18dp, null)
        }
        return null
    }*/
}