package com.nuvio.app.features.addons

internal expect object AddonStorage {
    fun loadInstalledAddonUrls(profileId: Int): List<String>
    fun saveInstalledAddonUrls(profileId: Int, urls: List<String>)
    fun loadAddonEnabledStates(profileId: Int): Map<String, Boolean>
    fun saveAddonEnabledStates(profileId: Int, states: Map<String, Boolean>)
    fun loadCachedManifests(profileId: Int): Map<String, String>
    fun saveCachedManifests(profileId: Int, manifests: Map<String, String>)
}

/** Disk cache for addon HTTP responses, keyed by URL hash. */
internal expect object AddonHttpCache {
    /** Returns the cached payload for [key] if present and newer than [maxAgeMs], otherwise null. */
    fun load(key: String, nowEpochMs: Long, maxAgeMs: Long): String?

    /** Persists [payload] for [key], replacing any existing entry. */
    fun save(key: String, payload: String)
}

data class RawHttpResponse(
    val status: Int,
    val statusText: String,
    val url: String,
    val body: String,
    val headers: Map<String, String>,
)

/** Default safety limit for generic and plugin-provided HTTP responses. */
internal const val DefaultRawHttpResponseMaxBytes = 1024 * 1024

expect suspend fun httpGetText(url: String): String

expect suspend fun httpPostJson(url: String, body: String): String

expect suspend fun httpGetTextWithHeaders(
    url: String,
    headers: Map<String, String>,
): String

expect suspend fun httpPostJsonWithHeaders(
    url: String,
    body: String,
    headers: Map<String, String>,
): String

expect suspend fun httpRequestRaw(
    method: String,
    url: String,
    headers: Map<String, String>,
    body: String,
    followRedirects: Boolean = true,
    maxResponseBodyBytes: Int = DefaultRawHttpResponseMaxBytes,
): RawHttpResponse
