package com.example.myele_sim.data

import com.example.myele_sim.model.Address

/**
 * 地址管理器 - 管理运行时的地址变更
 * 仅在应用运行期间保存变更，重启后恢复到JSON原始数据
 */
object AddressManager {
    // 运行时新增的地址
    private val runtimeAddresses = mutableListOf<Address>()

    // 运行时删除的地址ID
    private val deletedAddressIds = mutableSetOf<String>()

    /**
     * 添加新地址（运行时）
     */
    fun addAddress(address: Address) {
        runtimeAddresses.add(address)
    }

    /**
     * 删除地址（运行时）
     */
    fun deleteAddress(addressId: String) {
        // 如果是运行时添加的地址，直接从列表中移除
        runtimeAddresses.removeIf { it.addressId == addressId }
        // 如果是原始JSON中的地址，记录到删除列表
        deletedAddressIds.add(addressId)
    }

    /**
     * 更新地址（运行时）
     */
    fun updateAddress(address: Address) {
        val index = runtimeAddresses.indexOfFirst { it.addressId == address.addressId }
        if (index != -1) {
            runtimeAddresses[index] = address
        } else {
            // 如果不是运行时地址，添加到运行时列表（相当于覆盖原地址）
            runtimeAddresses.add(address)
        }
    }

    /**
     * 获取运行时添加的地址
     */
    fun getRuntimeAddresses(): List<Address> {
        return runtimeAddresses.toList()
    }

    /**
     * 获取运行时删除的地址ID列表
     */
    fun getDeletedAddressIds(): Set<String> {
        return deletedAddressIds.toSet()
    }

    /**
     * 清空所有运行时变更
     */
    fun clearAll() {
        runtimeAddresses.clear()
        deletedAddressIds.clear()
    }
}
