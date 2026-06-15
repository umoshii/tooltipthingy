package me.owdding.iconographic.features.tags

import me.owdding.iconographic.config.categories.tag.TagConfig
import me.owdding.iconographic.system.RegisterFeature
import me.owdding.iconographic.system.TooltipFeature
import me.owdding.iconographic.system.TooltipTag
import me.owdding.iconographic.utils.chat.ChatUtils
import net.minecraft.world.item.ItemStack
import tech.thatgravyboat.skyblockapi.api.datatype.DataTypes
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextStyle.font

@RegisterFeature
data object RarityTag : TooltipFeature() {
    override val enabled: Boolean get() = TagConfig.rarity
    override val priority: Int = 10

    override fun ItemStack.leftTags(): List<TooltipTag> {
        val rarity = DataTypes.RARITY() ?: return emptyList()

        val comp = if (DataTypes.RECOMBOBULATOR() == true) {
            val left = Text.of("> ") { font = ChatUtils.sparkles }
            val right = Text.of(" <") { font = ChatUtils.sparkles }
            Text.join(left, rarity.displayName, right)
        } else {
            Text.of(rarity.displayName)
        }

        return listOf(TooltipTag.literal(comp, rarity.color))
    }
}