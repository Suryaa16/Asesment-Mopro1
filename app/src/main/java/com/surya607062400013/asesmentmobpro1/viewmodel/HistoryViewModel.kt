package com.surya607062400013.asesmentmobpro1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.surya607062400013.asesmentmobpro1.FitcallApplication
import com.surya607062400013.asesmentmobpro1.data.local.entity.HistoryEntity
import com.surya607062400013.asesmentmobpro1.data.repository.HistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: HistoryRepository =
        (application as FitcallApplication).repository

    //State untuk semua history
    val allHistory = repository.getAllHistory()
    //State untuk recycle bin
    val recycleBin = repository.getRecycleBin()
    //State untuk filter type yang dipilih
    private val _selectedFilter = MutableStateFlow("All")
    val selectedFilter: StateFlow<String> = _selectedFilter.asStateFlow()
    //State untuk item yang sedang diedit
    private val  _selectedItem = MutableStateFlow<HistoryEntity?>(null)
    val selectedItem: StateFlow<HistoryEntity?> = _selectedItem.asStateFlow()

    //Simpan hasil kalkulasi
    fun insert(history: HistoryEntity) {
        viewModelScope.launch {
            repository.insert(history)
        }
    }
    //Update data
    fun update(history: HistoryEntity) {
        viewModelScope.launch {
            repository.update(history)
        }
    }
    //Soft delete untuk pindah ke recycle bin
    fun softDelete(id: Int) {
        viewModelScope.launch {
            repository.softDelete(id)
        }
    }
    //Restore dari recycle bin
    fun restore(id: Int) {
        viewModelScope.launch {
            repository.restore(id)
        }
    }
    //Hapus permanen
    fun deletePermanent(id: Int) {
        viewModelScope.launch {
            repository.deletePermanent(id)
        }
    }
    //Set filter
    fun setFilter(filter: String) {
        _selectedFilter.value = filter
    }
    //Set item yang dipilih untuk detail/edit
    fun selectItem(item: HistoryEntity?) {
        _selectedItem.value = item
    }
    //Get by id
    fun getById(id: Int, onResult: (HistoryEntity?) -> Unit) {
        viewModelScope.launch {
            onResult(repository.getById(id))
        }
    }
}