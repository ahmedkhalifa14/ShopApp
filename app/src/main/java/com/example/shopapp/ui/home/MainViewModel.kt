package com.example.shopapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopapp.models.AuthResponse
import com.example.shopapp.models.Banner
import com.example.shopapp.models.CartItemData
import com.example.shopapp.models.Category
import com.example.shopapp.models.Complaint
import com.example.shopapp.models.Data
import com.example.shopapp.models.Faqs
import com.example.shopapp.models.FavouriteItem
import com.example.shopapp.models.MyResponse
import com.example.shopapp.models.Notifications
import com.example.shopapp.models.Product
import com.example.shopapp.models.Settings
import com.example.shopapp.repository.ApiRepoImpl
import com.example.shopapp.utils.Event
import com.example.shopapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ApiRepoImpl
) : ViewModel() {
    private val _categoryStatus =
        MutableStateFlow<Event<Resource<MyResponse<Data<Category>>>>>(Event(Resource.Init()))
    val categoryStatus: MutableStateFlow<Event<Resource<MyResponse<Data<Category>>>>> =
        _categoryStatus
    private val _allCategoryProductStatus =
        MutableStateFlow<Event<Resource<MyResponse<Data<Product>>>>>(Event(Resource.Init()))
    val allCategoryProductsStatus: MutableStateFlow<Event<Resource<MyResponse<Data<Product>>>>> =
        _allCategoryProductStatus


    private val _searchProductStatus =
        MutableStateFlow<Event<Resource<AuthResponse<Data<Product>>>>>(Event(Resource.Init()))
    val searchProductsStatus: MutableStateFlow<Event<Resource<AuthResponse<Data<Product>>>>> =
        _searchProductStatus

    private val _allProductStatus =
        MutableStateFlow<Event<Resource<AuthResponse<Data<Product>>>>>(Event(Resource.Init()))
    val allProductsStatus: MutableStateFlow<Event<Resource<AuthResponse<Data<Product>>>>> =
        _allProductStatus


    private val _bannersStatus =
        MutableStateFlow<Event<Resource<Banner>>>(Event(Resource.Init()))
    val bannersStatus: MutableStateFlow<Event<Resource<Banner>>> =
        _bannersStatus

    private val _cartStatus =
        MutableStateFlow<Event<Resource<AuthResponse<CartItemData>>>>(Event(Resource.Init()))
    val cartStatus: MutableStateFlow<Event<Resource<AuthResponse<CartItemData>>>> =
        _cartStatus
    private val _favouriteStatus =
        MutableStateFlow<Event<Resource<AuthResponse<Data<FavouriteItem>>>>>(Event(Resource.Init()))
    val favouriteStatus: MutableStateFlow<Event<Resource<AuthResponse<Data<FavouriteItem>>>>> =
        _favouriteStatus

    private val _addItemToCartStatus =
        MutableStateFlow<Event<Resource<AuthResponse<FavouriteItem>>>>(Event(Resource.Init()))
    val addItemToCartStatus: MutableStateFlow<Event<Resource<AuthResponse<FavouriteItem>>>> =
        _addItemToCartStatus

    private val _deleteItemFromCartsStatus =
        MutableStateFlow<Event<Resource<AuthResponse<CartItemData>>>>(Event(Resource.Init()))
    val deleteItemFromCartsStatus: MutableStateFlow<Event<Resource<AuthResponse<CartItemData>>>> =
        _deleteItemFromCartsStatus

    private val _updateCartItemQuantityStatus =
        MutableStateFlow<Event<Resource<AuthResponse<CartItemData>>>>(Event(Resource.Init()))
    val updateCartItemQuantityStatus: MutableStateFlow<Event<Resource<AuthResponse<CartItemData>>>> =
        _updateCartItemQuantityStatus


    private val _getSettingsStatus =
        MutableStateFlow<Event<Resource<AuthResponse<Settings>>>>(Event(Resource.Init()))
    val getSettingsStatus: MutableStateFlow<Event<Resource<AuthResponse<Settings>>>> =
        _getSettingsStatus

    private val _getFaqsStatus =
        MutableStateFlow<Event<Resource<AuthResponse<Data<Faqs>>>>>(Event(Resource.Init()))
    val getFaqsStatus: MutableStateFlow<Event<Resource<AuthResponse<Data<Faqs>>>>> =
        _getFaqsStatus


    private val _makeComplaintsStatus =
        MutableStateFlow<Event<Resource<AuthResponse<Complaint>>>>(Event(Resource.Init()))
    val makeComplaintsStatus: MutableStateFlow<Event<Resource<AuthResponse<Complaint>>>> =
        _makeComplaintsStatus

    private val _getNotificationsStatus =
        MutableStateFlow<Event<Resource<AuthResponse<Data<Notifications>>>>>(Event(Resource.Init()))
    val getNotificationsStatus: MutableStateFlow<Event<Resource<AuthResponse<Data<Notifications>>>>> =
        _getNotificationsStatus

    private val _addItemToFavouritesStatus =
        MutableStateFlow<Event<Resource<AuthResponse<FavouriteItem>>>>(Event(Resource.Init()))
    val addItemToFavouritesStatus: MutableStateFlow<Event<Resource<AuthResponse<FavouriteItem>>>> =
        _addItemToFavouritesStatus

    private val _deleteItemFromFavouritesStatus =
        MutableStateFlow<Event<Resource<AuthResponse<FavouriteItem>>>>(Event(Resource.Init()))
    val deleteItemFromFavouritesStatus: MutableStateFlow<Event<Resource<AuthResponse<FavouriteItem>>>> =
        _deleteItemFromFavouritesStatus


    fun getCategories() {
        viewModelScope.launch(Dispatchers.Main) {
            _categoryStatus.emit(Event(Resource.Loading()))
            val result = repository.getAllCategories()
            _categoryStatus.emit(Event(result))
        }
    }

    fun getAllCartItems(loading: Boolean) {
        viewModelScope.launch(Dispatchers.Main) {
            if (loading) _cartStatus.emit(Event(Resource.Loading()))
            val result = repository.getAllCartItems()
            _cartStatus.emit((Event(result)))
        }
    }

    fun getFavouriteItems(loading: Boolean) {
        viewModelScope.launch(Dispatchers.Main) {
            if (loading) _favouriteStatus.emit(Event(Resource.Loading()))
            val result = repository.getFavouriteItems()
            _favouriteStatus.emit((Event(result)))
        }
    }

    fun getProductsByCategoryID(id: Int, loading: Boolean) {
        viewModelScope.launch(Dispatchers.Main) {
            if (loading) _allCategoryProductStatus.emit(Event(Resource.Loading()))
            val result = repository.getProductsByCategoryID(id)
            _allCategoryProductStatus.emit(Event(result))
        }
    }

    fun searchForProducts(text: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _searchProductStatus.emit(Event(Resource.Loading()))
            val result = repository.searchForProducts(text)
            _searchProductStatus.emit(Event(result))
        }
    }

    fun getAllProducts() {
        viewModelScope.launch(Dispatchers.Main) {
            _allProductStatus.emit(Event((Resource.Loading())))
            val result = repository.getAllProducts()
            _allProductStatus.emit(Event(result))
        }
    }

    fun getBanners() {
        viewModelScope.launch(Dispatchers.Main) {
            _bannersStatus.emit(Event(Resource.Loading()))
            val result = repository.getBanners()
            _bannersStatus.emit(Event(result))
        }
    }

    fun addItemToCart(product_id: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            _addItemToCartStatus.emit(Event(Resource.Loading()))
            val result = repository.addItemToCart(product_id)
            _addItemToCartStatus.emit(Event(result))
        }
    }

    fun deleteItemFromCarts(id: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            _deleteItemFromCartsStatus.emit(Event(Resource.Loading()))
            val result = repository.deleteItemFromCarts(id)
            _deleteItemFromCartsStatus.emit(Event(result))
        }
    }

    fun updateCartItemQuantity(id: Int, quantity: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            _updateCartItemQuantityStatus.emit(Event(Resource.Loading()))
            val result = repository.updateCartItemQuantity(id, quantity)
            _updateCartItemQuantityStatus.emit(Event(result))
        }
    }


    fun getSettings() {
        viewModelScope.launch(Dispatchers.Main) {
            _getSettingsStatus.emit(Event(Resource.Loading()))
            val result = repository.getSettings()
            _getSettingsStatus.emit(Event(result))
        }
    }

    fun getFaqs() {
        viewModelScope.launch(Dispatchers.Main) {
            _getFaqsStatus.emit(Event(Resource.Loading()))
            val result = repository.getFaqs()
            _getFaqsStatus.emit(Event(result))
        }
    }

    fun makeComplaints(complaint: Complaint) {
        viewModelScope.launch(Dispatchers.Main) {
            _makeComplaintsStatus.emit(Event(Resource.Loading()))
            val result = repository.makeComplaints(complaint)
            _makeComplaintsStatus.emit(Event(result))
        }
    }

    fun getNotifications() {
        viewModelScope.launch(Dispatchers.Main) {
            _getNotificationsStatus.emit(Event(Resource.Loading()))
            val result = repository.getNotifications()
            getNotificationsStatus.emit(Event(result))
        }
    }

    fun addItemToFavourites(product_id: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            _addItemToFavouritesStatus.emit(Event(Resource.Loading()))
            val result = repository.addItemToFavourites(product_id)
            _addItemToFavouritesStatus.emit(Event(result))
        }
    }



    fun deleteItemFromFavourites(id: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            _deleteItemFromFavouritesStatus.emit(Event(Resource.Loading()))
            val result = repository.deleteItemFromFavourites(id)
            _deleteItemFromFavouritesStatus.emit(Event(result))
        }
    }
}




