package cl.dv.catimages.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CatEntity::class], version = 1)
abstract class CatDatabase: RoomDatabase() {

    abstract fun getCatDao(): CatDao
}