package me.owdding.iconographic.features.tags

import me.owdding.iconographic.config.categories.tag.TagConfig
import me.owdding.iconographic.system.RegisterFeature
import me.owdding.iconographic.system.TooltipFeature
import me.owdding.iconographic.system.TooltipTag
import me.owdding.iconographic.utils.Stars
import me.owdding.iconographic.utils.chat.DisplayColor
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import tech.thatgravyboat.skyblockapi.api.datatype.DataTypes
import tech.thatgravyboat.skyblockapi.utils.text.TextProperties.stripped
import kotlin.math.max
import kotlin.math.min

@RegisterFeature
data object StarsFeature : TooltipFeature() {
    override val enabled: Boolean get() = TagConfig.stars
    override val priority: Int = 2

    private val starIcons = setOf("✪", "➊", "➋", "➌", "➍", "➎")
    private val starIconRegex = Regex("\\s*(?:${starIcons.joinToString("|")})+")

    private val colors = listOf(DisplayColor.GOLD, DisplayColor.LIGHT_PURPLE, DisplayColor.AQUA)

    private val masterStars = listOf(
        Stars.MASTER_1,
        Stars.MASTER_2,
        Stars.MASTER_3,
        Stars.MASTER_4,
        Stars.MASTER_5
    )

    override fun ItemStack.applies(): Boolean = DataTypes.STAR_COUNT() != null

    override fun ItemStack.rightTags(): List<TooltipTag> {
        val stars = DataTypes.STAR_COUNT()?.takeUnless { it == 0 } ?: return emptyList()

        val baseTier = max(0, stars - 5) / 5
        val moreTier = stars - 5 * (baseTier + 1)

        val isDungeon = DataTypes.CATEGORY()?.isDungeon == true

        return buildList {
            val amount = min(5, stars)
            repeat(amount) { index ->
                val isMasterStar = isDungeon && stars > 5 && index < (stars - 5)

                val color = if (isMasterStar) DisplayColor.WHITE
                else if (index < moreTier) colors[(baseTier + 1).coerceAtMost(colors.lastIndex)] else colors[baseTier]

                val iconId = if (isMasterStar) {
                    masterStars.getOrElse(index) { Stars.MASTER }.id
                } else {
                    Stars.BASE.id
                }

                add(TooltipTag.identifier(iconId, 11, 11, color))
            }
        }
    }

    override fun ItemStack.nameReplacement(original: Component): Component? {
        if (starIcons.none { it in original.stripped }) return null

        return Component.empty().withStyle(original.style).also { result ->
            original.siblings.forEach { sibling ->
                val trimmed = sibling.stripped.replace(starIconRegex, "")
                result.append(Component.literal(trimmed).withStyle(sibling.style))
            }
        }
    }
}