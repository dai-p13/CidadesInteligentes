package pt.atp.cidadesinteligentes.db

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import pt.atp.cidadesinteligentes.dao.NoteDao
import pt.atp.cidadesinteligentes.ententies.Notes

class NoteRepository (private val noteDao: NoteDao){

    val allNotes: LiveData<List<Notes>> = noteDao.getAlphabetizedNotes()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(note: Notes){
        noteDao.insert(note)
    }

    suspend fun deleteAll(){
        noteDao.deleteAll()
    }

    suspend fun updateNote(note: Notes) {
        noteDao.updateNote(note)
    }

    suspend fun deleteById(id: Int?){
        noteDao.deleteById(id)
    }
}