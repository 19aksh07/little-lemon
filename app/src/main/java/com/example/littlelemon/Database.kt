package com.example.littlelemon

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Entity(tableName = "menu_items")
data class MenuItemEntity(
    @androidx.room.PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val price: String,
    val image: String,
    val category: String = ""
)

@Dao
interface MenuDao {
    @Query("SELECT * FROM menu_items ORDER BY title COLLATE NOCASE")
    fun observeAll(): LiveData<List<MenuItemEntity>>

    @Query("SELECT COUNT(*) FROM menu_items")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<MenuItemEntity>)
}

@Database(entities = [MenuItemEntity::class], version = 2, exportSchema = false)
abstract class MenuDatabase : RoomDatabase() {
    abstract fun menuDao(): MenuDao

    companion object {
        fun create(context: Context): MenuDatabase = Room.databaseBuilder(
            context.applicationContext,
            MenuDatabase::class.java,
            "little_lemon_menu.db"
        ).fallbackToDestructiveMigration().build()
    }
}

fun MenuItemNetwork.toEntity(): MenuItemEntity = MenuItemEntity(
    id = id,
    title = title,
    description = description,
    price = price,
    image = image,
    category = category
)
