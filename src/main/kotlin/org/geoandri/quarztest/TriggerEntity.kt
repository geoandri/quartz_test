package org.geoandri.quarztest

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*


@Entity(name = "triggers")
@Table(name = "triggers")
class TriggerEntity(
    @Id
    @Column(name = "id")
    var id: UUID,

    @Column(name = "status")
    val status: String
)

fun TriggerEntity.copy(status: String) = TriggerEntity(
    id = this.id,
    status = status
)