package com.example.myele.utils

import com.example.myele.R

/**
 * 图片资源工具类
 */
object ImageUtils {
    /**
     * 根据餐厅名称获取对应的图片资源ID
     */
    fun getRestaurantImage(restaurantName: String): Int {
        return when (restaurantName) {
            "川香麻辣烫" -> R.drawable.chuanxiang_malatang
            "老北京炸酱面" -> R.drawable.laobeijing_zhajangmian
            "湘味轩" -> R.drawable.xiangweixuan
            "粤式早茶" -> R.drawable.yueshi_zaocha
            "韩式炸鸡" -> R.drawable.hanshi_zhaji
            "瑞幸咖啡" -> R.drawable.luckin_coffee
            "茶百道" -> R.drawable.chabaidao
            "蜜雪冰城" -> R.drawable.mixue
            "星巴克" -> R.drawable.starbucks
            "喜茶" -> R.drawable.heytea
            else -> 0 // 返回0表示没有对应的图片
        }
    }

    /**
     * 获取用户头像
     */
    fun getUserAvatar(): Int {
        return R.drawable.user_avatar
    }

    /**
     * 获取骑手头像
     */
    fun getRiderAvatar(): Int {
        return R.drawable.rider_avatar
    }

    /**
     * 获取爆品团图片
     * @param index 图片索引，0-4分别对应hot_deal.png和hot_deal_1.png到hot_deal_4.png
     */
    fun getHotDealImage(index: Int = 0): Int {
        return when (index) {
            0 -> R.drawable.hot_deal
            1 -> R.drawable.hot_deal_1
            2 -> R.drawable.hot_deal_2
            3 -> R.drawable.hot_deal_3
            4 -> R.drawable.hot_deal_4
            else -> R.drawable.hot_deal // 默认返回第一张
        }
    }

    /**
     * 获取所有爆品团图片列表
     */
    fun getAllHotDealImages(): List<Int> {
        return listOf(
            R.drawable.hot_deal,
            R.drawable.hot_deal_1,
            R.drawable.hot_deal_2,
            R.drawable.hot_deal_3,
            R.drawable.hot_deal_4
        )
    }

    /**
     * 根据商品ID获取对应的图片资源ID
     */
    fun getProductImage(productId: String): Int {
        return when (productId) {
            "prod_001" -> R.drawable.prod_001  // 麻辣烫（大份）
            "prod_002" -> R.drawable.prod_002  // 老北京炸酱面
            "prod_006" -> R.drawable.prod_006  // 可乐
            "prod_019" -> R.drawable.prod_019  // 土豆粉
            "prod_020" -> R.drawable.prod_020  // 红油抄手
            "prod_021" -> R.drawable.prod_021  // 酸辣粉
            "prod_022" -> R.drawable.prod_022  // 京酱肉丝
            "prod_023" -> R.drawable.prod_023  // 炸灌肠
            "prod_024" -> R.drawable.prod_024  // 老北京鸡肉卷
            else -> 0 // 返回0表示没有对应的图片，显示默认图标
        }
    }
}
