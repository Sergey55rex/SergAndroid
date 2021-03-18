package ru.netology.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.netology.dto.Post

class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {
    companion object {
        val DDL = """
        CREATE TABLE ${PostColumns.TABLE} (
            ${PostColumns.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${PostColumns.COLUMN_AUTHOR} TEXT NOT NULL,
            ${PostColumns.COLUMN_CONTENT} TEXT NOT NULL,
            ${PostColumns.COLUMN_PUBLISHED} TEXT NOT NULL,
            ${PostColumns.COLUMN_LIKED} BOOLEAN NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_LIKES} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_TO_SEND} BOOLEAN NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_TO_SENDS} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_VIEWING} BOOLEAN NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_VIEWINGS} INTEGER NOT NULL DEFAULT 0
            
        );
        """.trimIndent()
    }

    object PostColumns {
        const val TABLE = "posts"
        const val COLUMN_ID = "id"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_PUBLISHED = "published"
        const val COLUMN_LIKED = "liked"
        const val COLUMN_LIKES = "likes"
        const val COLUMN_TO_SEND = "toSend"
        const val COLUMN_TO_SENDS = "toSends"
        const val COLUMN_VIEWING = "viewing"
        const val COLUMN_VIEWINGS = "viewings"
        val ALL_COLUMNS = arrayOf(
            COLUMN_ID,
            COLUMN_AUTHOR,
            COLUMN_CONTENT,
            COLUMN_PUBLISHED,
            COLUMN_LIKED,
            COLUMN_LIKES,
            COLUMN_TO_SEND,
            COLUMN_TO_SENDS,
            COLUMN_VIEWING,
            COLUMN_VIEWINGS
        )
    }

    override fun getAll(): List<Post> {
        val posts = mutableListOf<Post>()
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            null,
            null,
            null,
            null,
            "${PostColumns.COLUMN_ID} DESC"
        ).use {
            while (it.moveToNext()) {
                posts.add(map(it))
            }
        }
        return posts
    }

    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            if (post.id != 0L) {
                put(PostColumns.COLUMN_ID, post.id)
            }
            // TODO: remove hardcoded values
            put(PostColumns.COLUMN_AUTHOR, "Me")
            put(PostColumns.COLUMN_CONTENT, post.content)
            put(PostColumns.COLUMN_PUBLISHED, "now")
        }
        val id = db.replace(PostColumns.TABLE, null, values)
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
        ).use {
            it.moveToNext()
            return map(it)
        }
    }

    override fun likeById(id: Long) {
        db.execSQL(
            """
           UPDATE posts SET
               likes = likes + CASE WHEN liked  THEN -1 ELSE 1 END,
               liked  = CASE WHEN liked  THEN 0 ELSE 1 END
           WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }

    override fun toSendsById(id: Long) {
        db.execSQL(
                """
           UPDATE posts SET
               toSends = toSends + CASE WHEN toSend THEN -1 ELSE 1 END,
               toSend = CASE WHEN toSend  THEN 0 ELSE 1 END
               
           WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }

    override fun viewingById(id: Long) {
        db.execSQL(
                """
           UPDATE posts SET
               viewings = viewings + CASE WHEN viewing THEN -1 ELSE 1 END,
               viewing = CASE WHEN viewing  THEN 0 ELSE 1 END
           WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }

    override fun removeById(id: Long) {
        db.delete(
            PostColumns.TABLE,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    private fun map(cursor: Cursor): Post {
        with(cursor) {
            return Post(
                id = getLong(getColumnIndexOrThrow(PostColumns.COLUMN_ID)),
                author = getString(getColumnIndexOrThrow(PostColumns.COLUMN_AUTHOR)),
                content = getString(getColumnIndexOrThrow(PostColumns.COLUMN_CONTENT)),
                published = getString(getColumnIndexOrThrow(PostColumns.COLUMN_PUBLISHED)),
                liked = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKED)) != 0,
                likes = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKES)),
                toSend = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_TO_SEND)) !=0,
                toSends = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_TO_SENDS)),
                viewing = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_VIEWING)) != 0,
                viewings = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_VIEWINGS)),
            )
        }
    }

}

