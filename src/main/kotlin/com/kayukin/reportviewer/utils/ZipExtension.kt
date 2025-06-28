package com.kayukin.reportviewer.utils

import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

fun ZipInputStream.entriesAsSequence(): Sequence<ZipEntry> = sequence {
    var entry: ZipEntry? = nextEntry
    while (entry != null) {
        yield(entry)
        entry = nextEntry
    }
}