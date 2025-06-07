import java.net.URL
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

// Configuration values
val swaggerDir = File(project.projectDir, "swagger")
val swaggerFile = File(swaggerDir, "swagger.json")
val swaggerUrl = "https://localhost:7136/swagger/v1/swagger.json"

// Task to download Swagger specification
tasks.register("downloadSwagger") {
    group = "openapi"
    description = "Downloads the Swagger specification from the API server"

    doLast {
        if (!swaggerDir.exists()) {
            swaggerDir.mkdirs()
        }

        logger.lifecycle("Downloading Swagger specification from $swaggerUrl")

        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.socketFactory)
            HttpsURLConnection.setDefaultHostnameVerifier { _, _ -> true }

            // Make the request
            val connection = URL(swaggerUrl).openConnection() as HttpsURLConnection
            connection.requestMethod = "GET"

            if (connection.responseCode == 200) {
                connection.inputStream.use { input ->
                    swaggerFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                logger.lifecycle("✓ Downloaded Swagger JSON to ${swaggerFile.absolutePath}")
            } else {
                throw GradleException("Failed to download Swagger JSON. Response code: ${connection.responseCode}")
            }
        } catch (e: Exception) {
            logger.warn("⚠ Could not download Swagger specification: ${e.message}")

            if (swaggerFile.exists()) {
                logger.warn("ℹ Using existing Swagger file: ${swaggerFile.absolutePath}")
            } else {
                logger.error("✗ Swagger specification file not found")
                logger.error("  Please ensure your API is running or manually download the file")
                logger.error("  Expected location: ${swaggerFile.absolutePath}")
                throw GradleException("Swagger specification file not found")
            }
        }
    }
}

// Expose the swagger file path as a project property
project.extra["swaggerFilePath"] = swaggerFile.absolutePath
