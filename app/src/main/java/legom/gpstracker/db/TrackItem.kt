package legom.gpstracker.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("track")
data class TrackItem(
    @PrimaryKey (autoGenerate = true)
    val id: Int?,
    @ColumnInfo("time")
    val time: String,
    @ColumnInfo("date")
    val date: String,
    @ColumnInfo("distance")
    val distance: String,
    @ColumnInfo("velocity")
    val velocity: String,
    @ColumnInfo("geo_points")
    val geoPoints: String
)
