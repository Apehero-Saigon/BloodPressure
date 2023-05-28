package com.blood.ui.fragments.info

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentInfoBinding
import com.blood.base.BaseFragment
import com.blood.base.recyclerview.BaseRecyclerViewListener
import com.blood.data.InfoKnowledge
import com.blood.ui.adapters.BloodPressureInfoKnowledgeAdapter
import com.blood.ui.fragments.home.HomeFragment
import com.blood.ui.fragments.home.HomeFragmentDirections
import com.blood.ui.fragments.home.HomeViewModel
import com.blood.ui.fragments.home.IHomeUi

class InfoFragment : BaseFragment<HomeViewModel, FragmentInfoBinding>(
    R.layout.fragment_info, HomeViewModel::class.java
), BaseRecyclerViewListener<InfoKnowledge> {
    var iHomeUi: IHomeUi? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment?.parentFragment is HomeFragment) {
            iHomeUi = parentFragment?.parentFragment as HomeFragment
        }
    }

    override fun initView() {
        binding.rcvInfo.adapter = BloodPressureInfoKnowledgeAdapter(getListBloodPressure(), this)
        binding.rcvInfo.layoutManager = GridLayoutManager(requireContext(), 3)
    }

    private fun getListBloodPressure() = mutableListOf(
        InfoKnowledge(
            1, R.drawable.img_post_01, R.string.post_01_title, R.string.post_01_content
        ), InfoKnowledge(
            2, R.drawable.img_post_03, R.string.post_03_title, R.string.post_03_content
        ), InfoKnowledge(
            3, R.drawable.img_post_04, R.string.post_04_title, R.string.post_04_content
        ), InfoKnowledge(
            4, R.drawable.img_post_06, R.string.post_06_title, R.string.post_06_content
        ), InfoKnowledge(
            5, R.drawable.img_post_07, R.string.post_07_title, R.string.post_07_content
        ), InfoKnowledge(
            6, R.drawable.img_post_09, R.string.post_09_title, R.string.post_09_content
        ), InfoKnowledge(
            8, R.drawable.img_post_10, R.string.post_10_title, R.string.post_10_content
        ), InfoKnowledge(
            9, R.drawable.img_post_11, R.string.post_11_title, R.string.post_11_content
        ), InfoKnowledge(
            10, R.drawable.img_post_12, R.string.post_12_title, R.string.post_12_content
        )
    )

    override fun onClick(data: InfoKnowledge, position: Int) {
        adsUtils.interInfo.showInterAdsBeforeNavigate(requireContext(), true) {
            iHomeUi?.navigateTo(
                HomeFragmentDirections.actionHomeFragmentToInfoDetailFragment(
                    data
                )
            )
        }
    }
}