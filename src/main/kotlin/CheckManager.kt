class CheckManager<K>(
    vararg pairs: Pair<K, Boolean>
) {
    private val values = pairs.toMap().toState()

    fun getKeys(): List<K> = values.keys.toList()

    fun get(key: K): Boolean? = values[key]

    fun getChecked(): K? = values.keys.find { values[it] == true }

    fun check(key: K) {
        values.forEach { (key, _) -> values[key] = false }
        values[key] = true
    }
}