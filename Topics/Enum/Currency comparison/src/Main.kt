enum class Country(val countryName: String, val currency: String) {
    GERMANY("Germany", "Euro"),
    MALI("Mali", "CFA franc"),
    DOMINICA("Dominica", "Eastern Caribbean dollar"),
    CANADA("Canada", "Canadian dollar"),
    SPAIN("Spain", "Euro"),
    AUSTRALIA("Australia", "Australian dollar"),
    BRAZIL("Brazil", "Brazilian real"),
    SENEGAL("Senegal", "CFA franc"),
    FRANCE("France", "Euro"),
    GRENADA("Grenada", "Eastern Caribbean dollar"),
    KIRIBATI("Kiribati", "Australian dollar");

    companion object {
        fun sameCurrency(a: String, b: String) : Boolean {
            var cur1 = ""
            var cur2 = ""
            for (enum in values()) {
                if (sameName(enum, a)) {
                    cur1 = enum.currency
                } else if (sameName(enum, b)) {
                    cur2 = enum.currency
                }
            }
            return cur1 == cur2
        }

        private fun sameName(enum: Country, a: String) = enum.countryName == a
    }
}

fun main() {
    val (c1, c2) = readln().split(" ")
    print(Country.sameCurrency(c1, c2))
}