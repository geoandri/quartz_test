package org.geoandri.quarztest

class JobNotFoundException : RuntimeException() {
    override val message: String?
        get() = "Job not found"
}