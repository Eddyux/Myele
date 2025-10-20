package com.example.myele.ui.takeout

import android.content.Context
import com.example.myele.data.DataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Takeout页面的Presenter
 */
class TakeoutPresenter(
    private val view: TakeoutContract.View,
    private val context: Context
) : TakeoutContract.Presenter {

    private val repository = DataRepository(context)

    override fun onViewCreated() {
        view.showLoading()
        CoroutineScope(Dispatchers.IO).launch {
            val restaurants = repository.loadRestaurants()
            withContext(Dispatchers.Main) {
                view.hideLoading()
                view.updateRestaurants(restaurants)
            }
        }
    }

    override fun onCategorySelected(category: String) {
        // 根据分类筛选餐厅
    }

    override fun onBackClicked() {
        // 返回操作由View层处理
    }

    override fun onDestroy() {
        // 清理资源
    }
}
