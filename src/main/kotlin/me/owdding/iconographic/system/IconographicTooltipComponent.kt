package me.owdding.iconographic.system

import me.owdding.iconographic.ExtractableTooltipLine
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent

class IconographicTooltipComponent(val line: ExtractableTooltipLine) : ClientTooltipComponent {

    var totalWidth: Int = 0

    override fun getWidth(font: Font): Int {
        return line.getWidth(font)
    }

    override fun getHeight(font: Font): Int {
        return line.getHeight(font)
    }

    override fun extractImage(font: Font, x: Int, y: Int, width: Int, height: Int, graphics: GuiGraphicsExtractor) {
        line.extract(graphics, this.totalWidth, x, y)
    }

    override fun extractText(graphics: GuiGraphicsExtractor, font: Font, x: Int, y: Int) {
    }
}