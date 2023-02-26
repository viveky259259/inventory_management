package com.vivek.inventorymanagement.features.inventory.view.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.vivek.inventorymanagement.R
import com.vivek.inventorymanagement.databinding.FragmentInventoryProductListViewBinding
import com.vivek.inventorymanagement.features.inventory.enums.ProductViewTypeEnum
import com.vivek.inventorymanagement.features.inventory.model.Item
import com.vivek.inventorymanagement.features.inventory.view.adapter.InventoryProductAdapter
import com.vivek.inventorymanagement.features.inventory.viewModel.MainActivityViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class InventoryProductListViewFragment : Fragment(R.layout.fragment_inventory_product_list_view) {
    private lateinit var mAdapter: InventoryProductAdapter
    private val mActivityViewModel: MainActivityViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inflateRecyclerView(view)
//        listenForInventoryData()
        listener()
    }

    /** [inflateRecyclerView] assigns [InventoryProductAdapter] to recyclerview with id [R.id.product_recycler_view] */
    private fun inflateRecyclerView(view: View) {
        val binding: FragmentInventoryProductListViewBinding =
            FragmentInventoryProductListViewBinding.bind(view)
        val recyclerView: RecyclerView = binding.productRecyclerView
        mAdapter = InventoryProductAdapter(ProductViewTypeEnum.LIST, ArrayList<Item>())
        recyclerView.adapter = mAdapter
    }

    /**
     * [listenForInventoryData] observes on @inventoryItemList liveData from [MainActivityViewModel]
     * And, calls @updateInventoryItems function in [InventoryProductAdapter] to update items
     * */
    private fun listenForInventoryData() {
        val inventoryListObserver = Observer<List<Item>> { newItemList ->
            mAdapter.updateInventoryItems(newItemList)
        }
        mActivityViewModel.inventoryItemList.observe(viewLifecycleOwner, inventoryListObserver)
    }

    private fun listener() {

        mActivityViewModel.success.map {
            print("e")
        }
        mActivityViewModel.loading.onEach {
            print(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        mActivityViewModel.success.onEach {
            mAdapter
                .updateInventoryItems(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)


//        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
//                launch(Dispatchers.Main) {
//                    mActivityViewModel.get1().collect { newItemList ->
////                        when (newItemList) {
////                            is InventoryViewState.Error -> print("Error")
////                            is InventoryViewState.Success -> mAdapter.updateInventoryItems(
////                                newItemList.items
////                            )
////                        }
//                        mAdapter
//                            .updateInventoryItems(newItemList)
//                    }
//                }
//            }
//        }
    }
}