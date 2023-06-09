package com.blood.ui.fragments.info

import android.content.Context
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.GridLayoutManager
import com.blood.base.BaseFragment
import com.blood.base.recyclerview.BaseRecyclerViewListener
import com.blood.data.InfoKnowledge
import com.blood.ui.adapters.BloodPressureInfoKnowledgeAdapter
import com.blood.ui.fragments.home.HomeFragment
import com.blood.ui.fragments.home.HomeFragmentDirections
import com.blood.ui.fragments.home.HomeViewModel
import com.blood.ui.fragments.home.IHomeUi
import com.blood.utils.FirebaseUtils
import com.blood.utils.ViewUtils.textTrim
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentInfoBinding

class InfoFragment : BaseFragment<HomeViewModel, FragmentInfoBinding>(
    R.layout.fragment_info, HomeViewModel::class.java
), BaseRecyclerViewListener<InfoKnowledge> {
    var iHomeUi: IHomeUi? = null
    var adapter: BloodPressureInfoKnowledgeAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment?.parentFragment is HomeFragment) {
            iHomeUi = parentFragment?.parentFragment as HomeFragment
        }
    }

    override fun initView() {
        FirebaseUtils.eventDisplayInfoScreen()
        with(binding) {
            adapter = BloodPressureInfoKnowledgeAdapter(
                InfoKnowledge.getListBloodPressure(), this@InfoFragment
            )
            rcvInfo.adapter = adapter
            rcvInfo.layoutManager = GridLayoutManager(requireContext(), 3)

            edtSearch.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    val textSearch = edtSearch.textTrim()

                    val dataList = InfoKnowledge.getListBloodPressure()
                    if (textSearch.isNotEmpty()) {
                        val itr = dataList.iterator()
                        while (itr.hasNext()) {
                            val info = itr.next()
                            if (!getString(info.title).contains(textSearch, true)) {
                                itr.remove()
                            }
                        }
                    }

                    if (textSearch.isNotEmpty()) {
                        FirebaseUtils.eventEnterSearchNameInfo(textSearch)
                    }
                    adapter?.updateData(dataList)
                }
                false
            }
        }
    }

    override fun onClick(data: InfoKnowledge, position: Int) {
        FirebaseUtils.eventClickInfoDetail()
        adsUtils.interInfo.showInterAdsBeforeNavigate(requireContext(), true) {
            iHomeUi?.navigateTo(
                HomeFragmentDirections.actionHomeFragmentToInfoDetailFragment(
                    data
                )
            )
        }
    }
}