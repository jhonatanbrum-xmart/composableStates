package com.codelabs.state

import java.util.UUID

class FloatingIcon(
    val id: UUID = UUID.randomUUID(), val initialX: Float, val initialY: Float, val targetY: Float, val imageIndex: Int
)