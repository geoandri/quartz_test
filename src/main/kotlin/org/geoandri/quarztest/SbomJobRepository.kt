package org.geoandri.quarztest

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SbomJobRepository: JpaRepository<SbomJobEntity, UUID>{

    fun findAllByStatus(status: String): List<SbomJobEntity>
}