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
            "金长风荷叶烤鸡" -> R.drawable.jinchangfeng_heyekaoji
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
            // 韩式炸鸡商品
            "prod_005" -> R.drawable.hanshi_zhaji_4  // 韩式炸鸡全家桶
            "prod_031" -> R.drawable.hanshi_zhaji_1  // 韩式炸鸡腿
            "prod_032" -> R.drawable.hanshi_zhaji_2  // 韩式炸鸡翅
            "prod_033" -> R.drawable.hanshi_zhaji_3  // 年糕炒拉面
            // 瑞幸咖啡商品
            "prod_009" -> R.drawable.ruixin_4  // 生椰拿铁
            "prod_010" -> R.drawable.ruixin_1  // 美式咖啡
            "prod_034" -> R.drawable.ruixin_2  // 厚乳拿铁
            "prod_035" -> R.drawable.ruixin_3  // 陨石拿铁
            // 湘味轩商品
            "prod_003" -> R.drawable.xiangweixuan_4  // 剁椒鱼头
            "prod_008" -> R.drawable.xiangweixuan_2  // 毛血旺
            "prod_025" -> R.drawable.xiangweixuan_5  // 农家小炒肉
            "prod_026" -> R.drawable.xiangweixuan_1  // 口味虾
            "prod_027" -> R.drawable.xiangweixuan_3  // 湘西外婆菜
            // 粤式早茶商品
            "prod_004" -> R.drawable.yueshi_zaocha_5  // 虾饺
            "prod_007" -> R.drawable.yueshi_zaocha_1  // 烧鹅
            "prod_028" -> R.drawable.yueshi_zaocha_2  // 流沙包
            "prod_029" -> R.drawable.yueshi_zaocha_3  // 叉烧包
            "prod_030" -> R.drawable.yueshi_zaocha_4  // 肠粉
            // 茶百道商品
            "prod_011" -> R.drawable.chabaidao_1  // 杨枝甘露
            "prod_012" -> R.drawable.chabaidao_2  // 珍珠奶茶
            "prod_036" -> R.drawable.chabaidao_3  // 芋泥波波奶茶
            "prod_037" -> R.drawable.chabaidao_4  // 烧仙草
            // 蜜雪冰城商品
            "prod_013" -> R.drawable.mixuebingc_1  // 柠檬水
            "prod_014" -> R.drawable.mixuebingc_2  // 冰淇淋奶茶
            "prod_038" -> R.drawable.mixuebingc_3  // 冰淇淋圣代
            // 星巴克商品
            "prod_015" -> R.drawable.xingbake_3  // 焦糖玛奇朵
            "prod_016" -> R.drawable.xingbake_1  // 抹茶拿铁
            "prod_039" -> R.drawable.xingbake_2  // 摩卡咖啡
            // 喜茶商品
            "prod_017" -> R.drawable.xicha  // 多肉葡萄
            "prod_018" -> R.drawable.xicha_1  // 芝芝莓莓
            "prod_040" -> R.drawable.xicha_2 // 芒芒甘露
            else -> 0 // 返回0表示没有对应的图片，显示默认图标
        }
    }
}
