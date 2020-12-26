package com.ix.ibrahim7.socketio.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.*

class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var lf = ArrayList<Fragment>()
    private var lt = ArrayList<String>()


    fun getLf(): ArrayList<Fragment> {
        return lf
    }

    fun setLf(lf: ArrayList<Fragment>) {
        this.lf = lf
    }

    fun getLt(): ArrayList<String> {
        return lt
    }

    fun setLt(lt: ArrayList<String>) {
        this.lt = lt
    }

    fun addFragment(fragment: Fragment?, title: String?) {
        lf.add(fragment!!)
        lt.add(title!!)
    }

    override fun getItem(position: Int): Fragment = lf[position]

    override fun getCount(): Int = lf.size

    override fun getPageTitle(position: Int): CharSequence? = lt[position]

}