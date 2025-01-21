package org.geoandri.quarztest

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.*


@Entity(name = "sbom_processing_jobs")
@Table(name = "sbom_processing_jobs")
class SbomJobEntity(
    @Id
    @Column(name = "id")
    var id: UUID,

    @Column(name = "workspace_id")
    val workspaceId: UUID,

    @Column(name = "status")
    val status: String,

    @Column(name= "message")
    val message: String,

    @Column(name = "created_at")
    val createdAt: Instant,

    @Column(name = "updated_at")
    val updatedAt: Instant
)

fun SbomJobEntity.copy(status: String) = SbomJobEntity(
    id = this.id,
    workspaceId = this.workspaceId,
    status = status,
    message = this.message,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)

fun SbomJobEntity.toDTO() = JobDTOResponse(
    id = this.id,
    workspaceId = this.workspaceId,
    status = this.status,
    message = this.message,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)