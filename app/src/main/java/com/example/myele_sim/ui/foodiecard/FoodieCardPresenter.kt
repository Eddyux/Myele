package com.example.myele_sim.ui.foodiecard

import com.example.myele_sim.data.DataRepository
import com.example.myele_sim.model.Coupon
import com.example.myele_sim.model.CouponType
import com.example.myele_sim.model.Restaurant
import com.example.myele_sim.model.SuperFoodieCard
import java.util.Date

/**
 * FoodieCard页面的Presenter实现
 */
class FoodieCardPresenter(private val repository: DataRepository) : FoodieCardContract.Presenter {
    private var view: FoodieCardContract.View? = null
    private var allRestaurants: List<Restaurant> = emptyList()
    private var currentFilter: String = "explosive"

    override fun attachView(view: FoodieCardContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun loadData() {
        view?.showLoading()

        // 加载超级吃货卡列表
        val foodieCards = generateFoodieCards()
        view?.showFoodieCards(foodieCards)

        // 加载爆红包套餐
        val explosivePackages = generateExplosivePackages()
        view?.showExplosivePackages(explosivePackages)

        // 加载餐厅列表
        allRestaurants = repository.getRestaurants()
        filterRestaurants(currentFilter)

        view?.hideLoading()
    }

    override fun filterRestaurants(type: String) {
        currentFilter = type
        when (type) {
            "explosive" -> {
                // 显示爆红包商家（筛选有大额优惠的商家）
                val filteredRestaurants = allRestaurants.filter {
                    it.coupons.isNotEmpty() || it.deliveryFee == 0.0
                }
                view?.showRestaurants(filteredRestaurants)
            }
            "all" -> {
                // 显示全平台商家
                view?.showRestaurants(allRestaurants)
            }
        }
    }

    override fun sortRestaurants(sortType: String) {
        val sortedRestaurants = when (sortType) {
            "综合" -> allRestaurants
            "人气" -> allRestaurants.sortedByDescending { it.salesVolume }
            else -> allRestaurants
        }
        view?.showRestaurants(sortedRestaurants)
    }

    private fun generateFoodieCards(): List<SuperFoodieCard> {
        return listOf(
            SuperFoodieCard(
                cardId = "card1",
                name = "超级吃货卡 含1盒Zeal罐头+8元宠物红包",
                price = 3.9,
                validDays = 30,
                monthlyDiscountLimit = 100.0,
                benefits = listOf("爆红包商家专享", "5元×6张", "无门槛")
            )
        )
    }

    private fun generateExplosivePackages(): List<Coupon> {
        val now = Date()
        val future = Date(now.time + 30L * 24 * 60 * 60 * 1000) // 30天后

        return listOf(
            Coupon(
                couponId = "pkg1",
                name = "爆红包商家专享 5元×6张",
                type = CouponType.REDUCTION,
                discountAmount = 5.0,
                minOrderAmount = null,
                validFrom = now,
                validUntil = future,
                status = com.example.myele_sim.model.CouponStatus.AVAILABLE,
                description = "无门槛 可免费爆涨 赠22元×2张 满58可用"
            ),
            Coupon(
                couponId = "pkg2",
                name = "爆红包商家专享 5元×12张",
                type = CouponType.REDUCTION,
                discountAmount = 5.0,
                minOrderAmount = null,
                validFrom = now,
                validUntil = future,
                status = com.example.myele_sim.model.CouponStatus.AVAILABLE,
                description = "无门槛 可免费爆涨 赠22元×2张 满58可用"
            ),
            Coupon(
                couponId = "pkg3",
                name = "全平台商家 3元×24张",
                type = CouponType.REDUCTION,
                discountAmount = 3.0,
                minOrderAmount = null,
                validFrom = now,
                validUntil = future,
                status = com.example.myele_sim.model.CouponStatus.AVAILABLE,
                description = "可免费爆涨 惊喜折扣1折起"
            )
        )
    }
}
