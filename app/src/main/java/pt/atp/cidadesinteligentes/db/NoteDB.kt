package pt.atp.cidadesinteligentes.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pt.atp.cidadesinteligentes.dao.NoteDao
import pt.atp.cidadesinteligentes.ententies.Notes

@Database(entities = arrayOf(Notes::class), version = 1, exportSchema = false)
public abstract class NoteDB : RoomDatabase() {

    abstract fun NoteDao(): NoteDao

    private class NoteDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var noteDao = database.NoteDao()

                    // Delete all content here.
                    /*noteDao.deleteAll()

                    // Add sample cities.
                    var note = Notes(1, "Nota 1", "descriçao 1")
                    noteDao.insert(note)
                    note = Notes(2, "Nota 2", "descriçao 2")
                    noteDao.insert(note)
                    note = Notes(3, "Nota 3", "descriçao 3")
                    noteDao.insert(note)*/

                }
            }
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: NoteDB? = null

        fun getDatabase(context: Context,  scope: CoroutineScope): NoteDB {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return  tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDB::class.java,
                    "note_database"
                )
                    //estratégia de destruição
                    .fallbackToDestructiveMigration()
                    .addCallback(NoteDatabaseCallback(scope))
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}
