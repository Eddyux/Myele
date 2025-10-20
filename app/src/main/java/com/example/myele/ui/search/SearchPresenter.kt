package com.example.myele.ui.search

/**
 * Search页面的Presenter
 */
class SearchPresenter(private val view: SearchContract.View) : SearchContract.Presenter {

    private val searchHistory = mutableListOf("备注", "北京烤鸭")
    private val searchSuggestions = listOf("coco都可")

    override fun onViewCreated() {
        view.updateSearchHistory(searchHistory)
        view.updateSearchSuggestions(searchSuggestions)
        loadRecommendedStores()
    }

    override fun onSearchTextChanged(text: String) {
        // 可以根据输入文本更新推荐
    }

    override fun onSearchClicked(keyword: String) {
        if (keyword.isNotBlank() && !searchHistory.contains(keyword)) {
            searchHistory.add(0, keyword)
        }
    }

    override fun onHistoryItemClicked(keyword: String) {
        // 点击历史记录项
    }

    override fun onClearHistoryClicked() {
        searchHistory.clear()
        view.updateSearchHistory(emptyList())
    }

    override fun onBackClicked() {
        // 返回操作由View层处理
    }

    override fun onDestroy() {
        // 清理资源
    }

    private fun loadRecommendedStores() {
        val stores = listOf(
            StoreRecommendation("1", "北京烤鸭", null, 275553),
            StoreRecommendation("2", "美滋滋烤肉拌饭", null, 236389),
            StoreRecommendation("3", "KFC", null, 189234)
        )
        view.updateRecommendedStores(stores)
    }
}
