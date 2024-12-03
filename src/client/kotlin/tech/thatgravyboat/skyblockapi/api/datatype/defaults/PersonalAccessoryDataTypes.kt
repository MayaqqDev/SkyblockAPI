package tech.thatgravyboat.skyblockapi.api.datatype.defaults

import net.minecraft.nbt.NumericTag
import net.minecraft.world.item.ItemStack
import tech.thatgravyboat.skyblockapi.api.datatype.DataType
import tech.thatgravyboat.skyblockapi.api.datatype.defaults.GenericDataTypes.ID
import tech.thatgravyboat.skyblockapi.modules.Module
import tech.thatgravyboat.skyblockapi.utils.extentions.getTag

/**
 * Data types for things like personal compactor and deletor
 */
@Module
object PersonalAccessoryDataTypes {

    private fun ItemStack.getMaxItems(type: String) = when (ID.factory(this)) {
        "PERSONAL_${type}_7000" -> 12
        "PERSONAL_${type}_6000" -> 7
        "PERSONAL_${type}_5000" -> 3
        "PERSONAL_${type}_4000" -> 1
        else -> null
    }

    var PERSONAL_COMPACTOR_ITEMS: DataType<List<String?>> = DataType("personal_compactor") {
        val maxItems = it.getMaxItems("COMPACTOR") ?: return@DataType null
        buildList {
            for (i in 0 until maxItems) {
                add(it.getTag("personal_compact_$i")?.asString)
            }
        }
    }

    var PERSONAL_DELETOR_ITEMS: DataType<List<String?>> = DataType("personal_deletor") {
        val maxItems = it.getMaxItems("DELETOR") ?: return@DataType null
        buildList {
            for (i in 0 until maxItems) {
                add(it.getTag("personal_deletor_$i")?.asString)
            }
        }
    }

    var PERSONAL_ACCESSORY_ACTIVE: DataType<Boolean> = DataType("personal_accessory_active") {
        (it.getTag("PERSONAL_DELETOR_ACTIVE") as? NumericTag)?.asInt?.let { active -> active == 1 }
    }
}
