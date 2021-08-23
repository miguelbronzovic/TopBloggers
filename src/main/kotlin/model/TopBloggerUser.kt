package model

/**
 * Represents a top user entity to calculate the top blogging score
 */
data class TopBloggerUser(
    val id: Int,
    val name: String,
    val score: Double
) {
    override fun toString(): String = "$name - $id, Score: $score"
}
