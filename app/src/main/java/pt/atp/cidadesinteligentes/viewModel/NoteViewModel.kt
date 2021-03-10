package pt.atp.cidadesinteligentes.viewModel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.atp.cidadesinteligentes.db.NoteDB
import pt.atp.cidadesinteligentes.db.NoteRepository
import pt.atp.cidadesinteligentes.ententies.Notes

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NoteRepository
    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
   val allNotes: LiveData<List<Notes>>

    init {
        val notesDao = NoteDB.getDatabase(application, viewModelScope).NoteDao()
        repository = NoteRepository(notesDao)
        allNotes = repository.allNotes
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(note: Notes) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
    }

    // delete all
    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

    fun updateCity(note: Notes) = viewModelScope.launch {
        repository.updateNote(note)
    }
}

