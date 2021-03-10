package pt.atp.cidadesinteligentes.ententies

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")

class Notes (
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
            @ColumnInfo(name = "title") val title: String,
                    @ColumnInfo(name = "description") val description: String
    )