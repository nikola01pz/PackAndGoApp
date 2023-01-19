package com.example.packandgo

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PageAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> PackingList()
            1 -> ToVisit()
            2 -> PhotoIdeas()
            else -> throw Resources.NotFoundException("Position not found")
        }
    }
    override fun getItemCount(): Int = 3
}