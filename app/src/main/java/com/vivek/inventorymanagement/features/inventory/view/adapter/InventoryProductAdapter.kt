package com.vivek.inventorymanagement.features.inventory.view.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.vivek.inventorymanagement.R
import com.vivek.inventorymanagement.features.inventory.enums.ProductViewTypeEnum
import com.vivek.inventorymanagement.features.inventory.model.Item
import com.vivek.inventorymanagement.features.inventory.view.viewHolder.ViewHolderGridView
import com.vivek.inventorymanagement.features.inventory.view.viewHolder.ViewHolderListView

class InventoryProductAdapter(
    private val viewTypeEnum: ProductViewTypeEnum,
    private var items: List<Item>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /** @updateInventoryItems is used to update items for adapter*/
    fun updateInventoryItems(newItems: List<Item>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        return when (viewTypeEnum) {
            ProductViewTypeEnum.LIST -> ViewHolderListView(
                inflater.inflate(
                    R.layout.list_item_inventory, parent, false
                )
            )
            ProductViewTypeEnum.GRID -> ViewHolderGridView(
                inflater.inflate(
                    R.layout.grid_item_inventory, parent, false
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return viewTypeEnum.type
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (viewTypeEnum) {
            ProductViewTypeEnum.LIST -> {
                bindViewForViewHolderListView(
                    holder,
                    item = items[position],
                    isLastItem = position == items.size - 1
                )

            }

            ProductViewTypeEnum.GRID -> {
                bindViewForViewHolderGridView(holder, item = items[position])
            }

        }
    }

    private fun bindViewForViewHolderGridView(holder: RecyclerView.ViewHolder, item: Item) {
        val viewHolder: ViewHolderGridView = holder as ViewHolderGridView
        viewHolder.binding.productItemName.text = item.name
        viewHolder.binding.productItemPrice.text = item.price
        viewHolder.binding.productItemImage.load(Uri.parse(item.imageUrl)) {
            placeholder(R.drawable.placeholder_broken_image)
            error(R.drawable.placeholder_broken_image)
        }
    }

    private fun bindViewForViewHolderListView(
        holder: RecyclerView.ViewHolder,
        item: Item,
        isLastItem: Boolean
    ) {
        val viewHolder: ViewHolderListView = holder as ViewHolderListView

        viewHolder.binding.productItemName.text = item.name
        viewHolder.binding.productItemPrice.text = item.price
        viewHolder.binding.productExtraInfo.text = item.extra
        viewHolder.binding.productItemImage.load(Uri.parse(item.imageUrl)) {
            placeholder(R.drawable.placeholder_broken_image)
            error(R.drawable.placeholder_broken_image)
        }
        if (!isLastItem) {
            viewHolder.binding.itemDivider.visibility = View.VISIBLE
        } else {
            viewHolder.binding.itemDivider.visibility = View.GONE
        }
    }
}