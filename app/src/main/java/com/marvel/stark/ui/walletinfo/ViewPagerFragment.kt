package com.marvel.stark.ui.walletinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import com.marvel.stark.R
import com.marvel.stark.di.factory.Injectable
import com.marvel.stark.di.factory.ViewModelFactory
import com.marvel.stark.ui.ToolbarViewModel
import com.marvel.stark.ui.walletinfo.home.HomeFragment
import com.marvel.stark.ui.walletinfo.payout.PayoutFragment
import com.marvel.stark.ui.walletinfo.worker.WorkerFragment
import kotlinx.android.synthetic.main.fragment_view_pager.*
import javax.inject.Inject
import kotlin.math.abs

/**Created by Jahongir on 6/15/2019.*/

class ViewPagerFragment : Fragment(), Injectable {

    private val safeArgs: ViewPagerFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory


    private val walletId: Long by lazy {
        safeArgs.walledId
    }

    private val toolbarViewModel: ToolbarViewModel by activityViewModels { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_view_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupBottomNavigation()
        //refresh_layout.isEnabled = false
        /*activity?.let {
            toolbarViewModel = ViewModelProviders.of(it).get(ToolbarViewModel::class.java)
        }*/
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
        viewPager.setPageTransformer(true, viewPagerTransformer2)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                val menuItem = bottomNavigation.menu.getItem(position)
                bottomNavigation.selectedItemId = menuItem.itemId
                toolbarViewModel?.title?.postValue(menuItem.title.toString())
            }

            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        })
    }

    private fun getViewPagerAdapter(): FragmentStatePagerAdapter {
        val fragments = arrayOf<Fragment>(HomeFragment.newInstance(walletId),
                WorkerFragment.newInstance(walletId), PayoutFragment.newInstance(walletId))
        return object : FragmentStatePagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int) = fragments[position]

            override fun getCount() = fragments.size

        }
    }

    private val viewPagerTransformer = ViewPager.PageTransformer { page, position ->
        page.apply {
            val pageWidth = width
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

    private val viewPagerTransformer2 = ViewPager.PageTransformer { page, position ->
        val minScale = 0.75f
        page.apply {
            val pageWidth = width
            when {
                position < -1 -> { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    alpha = 0f
                }
                position <= 0 -> { // [-1,0]
                    // Use the default slide transition when moving to the left page
                    alpha = 1f
                    translationX = 0f
                    scaleX = 1f
                    scaleY = 1f
                }
                position <= 1 -> { // (0,1]
                    // Fade the page out.
                    alpha = 1 - position

                    // Counteract the default slide transition
                    translationX = pageWidth * -position

                    // Scale the page down (between MIN_SCALE and 1)
                    val scaleFactor = (minScale + (1 - minScale) * (1 - abs(position)))
                    scaleX = scaleFactor
                    scaleY = scaleFactor
                }
                else -> { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    alpha = 0f
                }
            }
        }
    }
}