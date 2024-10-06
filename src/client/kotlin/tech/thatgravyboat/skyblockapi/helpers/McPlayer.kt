package tech.thatgravyboat.skyblockapi.helpers

import net.minecraft.client.Minecraft
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack

object McPlayer {

    val self: Player?
        get() = Minecraft.getInstance().player

    val menu: AbstractContainerMenu?
        get() = self?.containerMenu

    val health: Int
        get() = self?.health?.toInt() ?: 0

    val maxHealth: Int
        get() = self?.maxHealth?.toInt() ?: 0

    val air: Int
        get() = self?.airSupply ?: 0

    val maxAir: Int
        get() = self?.maxAirSupply ?: 0

    val xpLevel: Int
        get() = self?.experienceLevel ?: 0

    val xpLevelProgress: Float
        get() = self?.experienceProgress ?: 0f

    val heldItem: ItemStack
        get() = self?.mainHandItem ?: ItemStack.EMPTY

    val inventory: List<ItemStack>
        get() = self?.inventory?.items ?: emptyList()

    val hotbar: List<ItemStack>
        get() = inventory.subList(0, 9) ?: buildList(9) { ItemStack.EMPTY }
}
