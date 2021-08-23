import model.Comment
import model.Post
import model.User
import model.TopBloggerUser
import resources.Data

/**
# Fueled Kotlin Exercise
A blogging platform stores the following information that is available through separate API endpoints:
+ model.user accounts
+ blog model.posts for each model.user
+ model.comments for each blog post

### Objective
The organization needs to identify the 3 most engaging bloggers on the platform. Using only Kotlin and the Kotlin standard library, output the top 3 users with the highest average number of model.comments per post in the following format:
`[name]` - `[id]`, Score: `[average_comments]`
Instead of connecting to a remote API, we are providing this data in form of JSON files, which have been made accessible through a custom Resource enum with a `data` method that provides the contents of the file.

### What we're looking to evaluate
1. How you choose to model your data
2. How you transform the provided JSON data to your data model
3. How you use your models to calculate this average value
4. How you use this data point to sort the users

 */

// 1. First, start by modeling the data objects that will be used.

private const val TOP_BLOGGERS = 3

fun main(vararg args: String) {
    // 2. Next, decode the JSON source using `[Data.getUsers()]`

    // fetch comments, and create a map grouping the total comments by postId
    val comments = Data.getComments<List<Comment>>()
    val commentsPerPost = comments.groupingBy { it.postId }
        .eachCount()
        .toMap()

    // fetch the posts and group the posts Ids with users Ids
    val posts = Data.getPosts<List<Post>>()
    val usersPosts = posts.groupBy({ it.userId }, { it.id })

    val users = Data.getUsers<List<User>>()

    val bloggerList = mutableListOf<TopBloggerUser>()

    // for all users, check
    users.forEach { u ->
        // even on jsons there are no users with 0 posts, we do the check in case data changes
        usersPosts[u.id]?.let { postIds ->
            val totalPosts = postIds.size

            var totalComments = 0

            postIds.forEach { postId ->
                totalComments += commentsPerPost[postId] ?: 0
            }

            val score = totalComments / totalPosts.toDouble()

            // add user info and score to new entity
            bloggerList.add(TopBloggerUser(u.id, u.name, score))
        }
    }

    println("** TOP 3 USERS **")
    println(
        bloggerList.sortedByDescending { u -> u.score }
            .take(TOP_BLOGGERS)
    )

    // [Leanne Graham - 1, Score: 6.6, Clementine Bauch - 3, Score: 6.3, Ervin Howell - 2, Score: 6.1]
}
