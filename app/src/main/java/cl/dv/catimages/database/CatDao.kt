package cl.dv.catimages.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update

@Dao
interface CatDao {

    @Insert
    fun insertAll(vararg gatos: CatEntity)

    @Update
    fun updateCat(gatos: CatEntity)
}