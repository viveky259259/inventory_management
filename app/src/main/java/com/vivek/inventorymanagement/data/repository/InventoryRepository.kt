package com.vivek.inventorymanagement.data.repository

import com.vivek.inventorymanagement.data.api.InventoryHttpClient
import com.vivek.inventorymanagement.data.api.dtos.InventoryItemDto
import com.vivek.inventorymanagement.data.api.dtos.InventoryItemListDto
import com.vivek.inventorymanagement.data.api.services.InventoryApiService
import com.vivek.inventorymanagement.data.database.inventory.InventoryDatabaseImp
import com.vivek.inventorymanagement.data.database.inventory.entities.ItemEntity
import com.vivek.inventorymanagement.features.inventory.enums.InventoryFilterOptionEnum
import com.vivek.inventorymanagement.features.inventory.model.Item
import retrofit2.Response
import javax.inject.Inject

class InventoryRepository @Inject constructor(inventoryDb: InventoryDatabaseImp) :
    IInventoryRepository(inventory = InventoryHttpClient()) {
    val _inventoryDb = inventoryDb

    override suspend fun getInventoryItems(): List<Item> {
        var resultItems: List<Item> = ArrayList<Item>()
        val itemEntities: List<ItemEntity> = _inventoryDb.getInventoryDatabase().itemDao().getAll()
        if (itemEntities.isNotEmpty()) {
            resultItems = itemEntities.map { each ->
                Item.getItemFromItemEntity(each)
            }
        } else {
            val service: InventoryApiService =
                inventory.getBaseAdapter().create(InventoryApiService::class.java)
            val data: Response<InventoryItemListDto>? = service.getInventoryList().execute()

            if (data?.isSuccessful == true) {
                val itemList: List<InventoryItemDto>? = data.body()?.data?.items

                itemList?.let { tempItemList ->
                    resultItems = tempItemList.map { each ->
                        Item.getItemFromItemDto(each)
                    }

                    if (resultItems.isNotEmpty()) {
                        val resultItemEntities: List<ItemEntity> = resultItems.map { each ->
                            ItemEntity.getItemEntity(each)
                        }
                        _inventoryDb.getInventoryDatabase().itemDao().insertAll(resultItemEntities)
                    }
                }
            }
        }

        return resultItems
    }

    override suspend fun getInventorySearchItems(
        searchText: String,
        searchType: InventoryFilterOptionEnum
    ): List<Item> {
        var resultItems: List<Item> = ArrayList<Item>()

        val itemEntites: List<ItemEntity> = when (searchType) {
            InventoryFilterOptionEnum.FILTER_BY_NAME -> _inventoryDb.getInventoryDatabase()
                .itemDao().getItemsByName(searchText)
            InventoryFilterOptionEnum.FILTER_BY_PRICE -> _inventoryDb.getInventoryDatabase()
                .itemDao().getItemsByPrice(searchText)
        }

        itemEntites.let { tempItemEntites ->
            resultItems = tempItemEntites.map { each ->
                Item.getItemFromItemEntity(each)
            }
        }
        return resultItems
    }


}