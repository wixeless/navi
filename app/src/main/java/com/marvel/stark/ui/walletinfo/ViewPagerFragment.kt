package com.marvel.stark.ui.walletinfo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.marvel.stark.R
import com.marvel.stark.di.factory.Injectable
import kotlinx.android.synthetic.main.fragment_view_pager.*
import kotlin.math.abs
import kotlin.math.max

/**Created by Jahongir on 6/15/2019.*/

class ViewPagerFragment : Fragment(), Injectable {

    private val safeArgs: ViewPagerFragmentArgs by navArgs()

    private val walletId: Long by lazy {
        safeArgs.walledId
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_view_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupBottomNavigation()
        refresh_layout.isEnabled = false
    }

    private fun setupBottomNavigation() {
        bottomNavigation.setOnNavigationItemSelectedListener {
            //Set orderInCategory for each item in menu resource
            viewPager.setCurrentItem(it.order, true)
            true
        }
    }

    private fun setupViewPager() {
        viewPager.adapter = getViewPagerAdapter()
        viewPager.setPageTransformer(viewPagerTransformer)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bottomNavigation.selectedItemId = bottomNavigation.menu.getItem(position).itemId
            }
        })
    }

    private fun getViewPagerAdapter(): FragmentStateAdapter {
        val fragments = arrayOf<Fragment>(HomeFragment.newInstance(walletId),
                WorkerFragment.newInstance(walletId), EarningFragment.newInstance(walletId))
        return object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int) = fragments[position]

            override fun getItemCount() = fragments.size

        }
    }

    private val viewPagerTransformer = ViewPager2.PageTransformer { page, position ->
        page.apply {
            val pageWidth = width
            val minScale = 0.85f
            Log.d("ViewPagerFragment", "$page: $position")
            when {
                position < -1 -> { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    alpha = 0f
                }
                position <= 0 -> { // [-1,0]
                    // Use the default slide transition when moving to the left page
                    // Fade the page in.
                    alpha = 1 + position
                    scaleX = 1f
                    scaleY = 1f
                    translationX = pageWidth * -position
                }
                position <= 1 -> { // (0,1]
                    // Fade the page out.
                    alpha = 1 - position
                }
                else -> { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    alpha = 0f
                }
            }
        }
    }
}