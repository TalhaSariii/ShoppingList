package com.talha.shoppinglist.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.talha.shoppinglist.model.Item
import com.talha.shoppinglist.roomdb.ItemDao
import com.talha.shoppinglist.roomdb.ItemDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemViewModel(application: Application) : AndroidViewModel(application)
{
    private val db = Room.databaseBuilder(
        getApplication(),
        ItemDatabase::class.java, "items"
    ).build()

    private  val  itemDao = db.itemDao()

    val itemList = mutableStateOf<List<Item>>(listOf())
    val selecteditem = mutableStateOf<Item>(Item("","","", ByteArray(1)))

    fun getItemList() {
        viewModelScope.launch(Dispatchers.IO) {
            itemList.value = itemDao.getItemWithNameAndId()
        }
    }

    fun getItem(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val item = itemDao.getItemById(id = id)
            item?.let {
                selecteditem.value = it
            }
        }
    }

    fun saveItem(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            itemDao.insert(item)
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            itemDao.delete(item)
        }
    }


}
