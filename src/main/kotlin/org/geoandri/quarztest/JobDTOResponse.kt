package org.geoandri.quarztest

import java.time.Instant
import java.util.*

class JobDTOResponse(
    val id: UUID,
    val workspaceId: UUID,
    val status: String,
    val message: String,
    val createdAt: Instant,
    val updatedAt: Instant
)