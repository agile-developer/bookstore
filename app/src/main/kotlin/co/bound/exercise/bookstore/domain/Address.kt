package co.bound.exercise.bookstore.domain

data class Address(
    val addressLine1: String,
    val addressLine2: String = "",
    val postcode: String,
    val city: String,
    val country: String
) {
    fun validate() {
        if (addressLine1.isBlank() || postcode.isBlank() || city.isBlank() || country.isBlank()) {
            throw IllegalArgumentException("Required address fields missing")
        }
    }
}
