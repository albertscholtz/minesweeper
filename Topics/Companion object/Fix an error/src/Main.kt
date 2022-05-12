class Player(val id: Int, val name: String) {
    fun getInfo() = "$id, $name, $role"

    companion object {
        var role = "playable character"
    }
}

fun getPlayerInfo(player: Player) = player.getInfo()