package amarr.torznab

import amarr.torznab.indexer.AmuleIndexer
import amarr.torznab.indexer.Indexer
import amarr.torznab.indexer.ThrottledException
import amarr.torznab.indexer.UnauthorizedException
import amarr.torznab.indexer.ddunlimitednet.DdunlimitednetIndexer
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import nl.adaptivity.xmlutil.XmlDeclMode
import nl.adaptivity.xmlutil.core.XmlVersion
import nl.adaptivity.xmlutil.serialization.XML


fun Application.torznabApi(amuleIndexer: AmuleIndexer, ddunlimitednetIndexer: DdunlimitednetIndexer) {
    routing {
        // Kept for legacy reasons
        get("/api") {
            call.handleRequests(amuleIndexer)
        }
        get("/indexer/amule/api") {
            call.handleRequests(amuleIndexer)
        }
        get("indexer/ddunlimitednet/api") {
            call.handleRequests(ddunlimitednetIndexer)
        }
    }
}

private suspend fun ApplicationCall.handleRequests(indexer: Indexer) {
    application.log.debug("Handling torznab request")
    val xmlFormat = XML {
        xmlDeclMode = XmlDeclMode.Charset
        xmlVersion = XmlVersion.XML10
    } // This API uses XML instead of JSON
    request.queryParameters["t"]?.let {
        when (it) {
            "caps" -> {
                application.log.debug("Handling caps request")
                respondText(xmlFormat.encodeToString(indexer.capabilities()), contentType = ContentType.Application.Xml)
            }

            "tvsearch" -> performSearch(indexer, xmlFormat)
            "search" -> performSearch(indexer, xmlFormat)

            else -> throw IllegalArgumentException("Unknown action: $it")
        }
    } ?: throw IllegalArgumentException("Missing action")
}

private suspend fun ApplicationCall.performSearch(indexer: Indexer, xmlFormat: XML) {
    val query = request.queryParameters["q"].orEmpty()
    val season = request.queryParameters["season"].orEmpty()
    val episode = request.queryParameters["episode"].orEmpty().padStart(2, '0')

    val finalQuery = "$query ${season}x$episode"
    
    val offset = request.queryParameters["offset"]?.toIntOrNull() ?: 0
    val limit = request.queryParameters["limit"]?.toIntOrNull() ?: 100
    val cat = request.queryParameters["cat"]?.split(",")?.map { cat -> cat.toInt() } ?: emptyList()
    application.log.debug("Handling search request: {}, {}, {}, {}", finalQuery, offset, limit, cat)
    try {
        respondText(
            xmlFormat.encodeToString(indexer.search(query, offset, limit, cat)),
            contentType = ContentType.Application.Xml
        )
    } catch (e: ThrottledException) {
        application.log.warn("Throttled, returning 403")
        respondText("You are being throttled. Retry in a few minutes.", status = HttpStatusCode.Forbidden)
    } catch (e: UnauthorizedException) {
        application.log.warn("Unauthorized, returning 401")
        respondText("Unauthorized, check your credentials.", status = HttpStatusCode.Unauthorized)
    }
}
