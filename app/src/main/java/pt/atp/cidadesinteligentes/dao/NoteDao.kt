package pt.atp.cidadesinteligentes.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pt.atp.cidadesinteligentes.ententies.Notes

@Dao
interface NoteDao{
    @Query("SELECT * FROM notes_table ORDER BY title ASC")
    fun getAlphabetizedNotes(): LiveData<List<Notes>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(notes: Notes)

    @Query("DELETE FROM notes_table")
    suspend fun deleteAll()

    @Update
    suspend fun updateNote(notes: Notes)

    @Query("DELETE FROM notes_table WHERE id ==:id")
    suspend fun deleteById(id: Int?)

}