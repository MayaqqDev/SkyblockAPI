package tech.thatgravyboat.skyblockapi.api.area.farming

import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.chat.ChatReceivedEvent
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.events.info.TabWidget
import tech.thatgravyboat.skyblockapi.api.events.info.TabWidgetChangeEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockAreas
import tech.thatgravyboat.skyblockapi.api.location.SkyblockArea
import tech.thatgravyboat.skyblockapi.modules.Module
import tech.thatgravyboat.skyblockapi.utils.extentions.parseFormattedInt
import tech.thatgravyboat.skyblockapi.utils.regex.RegexUtils.anyMatch
import tech.thatgravyboat.skyblockapi.utils.regex.RegexUtils.match
import tech.thatgravyboat.skyblockapi.utils.regex.Regexes

@Module
object TrapperAPI {

    private val peltsRegex = Regexes.create("scoreboard.farming.trapper.pelts", "Pelts: (?<pelts>[\\d,kmb]+)")
    private val peltsTabListRegex = Regexes.create("tablist.widget.trapper.pelts", " Pelts: (?<pelts>[\\d,kmb]+)")
    private val animalRegex = Regexes.create(
        "chat.farming.trapper.animals",
        "\\[NPC] Trevor: You can find your (?<type>\\w+) animal near the (?<location>.*).",
    )

    var pelts: Int = 0
        private set

    var trackedType: TrapperAnimalType = TrapperAnimalType.UNKNOWN
        private set

    var trackedLocation: SkyblockArea = SkyBlockAreas.NONE
        private set

    @Subscription
    fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        peltsRegex.anyMatch(event.added, "pelts") { (pelts) ->
            this.pelts = pelts.parseFormattedInt()
        }
    }

    @Subscription
    fun onTabListWidgetUpdate(event: TabWidgetChangeEvent) {
        if (event.widget != TabWidget.TRAPPER) return
        peltsTabListRegex.anyMatch(event.new, "pelts") { (pelts) ->
            this.pelts = pelts.parseFormattedInt()
        }
    }

    @Subscription
    fun onChatMessage(event: ChatReceivedEvent) {
        animalRegex.match(event.text, "type", "location") { (type, location) ->
            trackedType = TrapperAnimalType.fromString(type)
            trackedLocation = SkyblockArea(location)
        }
    }
}

enum class TrapperAnimalType {
    TRACKABLE,
    UNTRACKABLE,
    UNDETECTED,
    ENDANGERED,
    ELUSIVE,
    UNKNOWN;

    companion object {
        fun fromString(string: String): TrapperAnimalType =
            runCatching { valueOf(string.uppercase()) }.getOrDefault(UNKNOWN)
    }
}
