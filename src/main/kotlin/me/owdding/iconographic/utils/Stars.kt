package me.owdding.iconographic.utils

import me.owdding.iconographic.Iconographic
import net.minecraft.resources.Identifier

enum class Stars {
    BASE,
    POINTY,
    MASTER,
    MASTER_1,
    MASTER_2,
    MASTER_3,
    MASTER_4,
    MASTER_5,
    ;

    val id: Identifier = Iconographic.id("stars/${name.lowercase()}")
}