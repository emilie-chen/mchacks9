package dev.hongjun.sherbrooke

import kotlinx.serialization.*
import kotlinx.serialization.json.*

inline fun <reified T> toJson(value: T) = Json.encodeToString(value)
inline fun <reified T> fromJson(string: String) = Json.decodeFromString<T>(string)
