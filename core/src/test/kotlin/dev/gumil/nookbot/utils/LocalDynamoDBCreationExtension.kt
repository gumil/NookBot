package dev.gumil.nookbot.utils

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.local.main.ServerRunner
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer
import com.amazonaws.services.dynamodbv2.model.ScanRequest
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import java.net.ServerSocket

/**
 * Creates a local DynamoDB instance for testing.
 */
internal class LocalDynamoDBCreationExtension(
    private val tableName: String,
    private val primaryKeyName: String
) : BeforeAllCallback, AfterAllCallback, AfterEachCallback {

    private var server: DynamoDBProxyServer? = null
    lateinit var amazonDynamoDB: AmazonDynamoDB
        private set

    init {
        System.setProperty("java.library.path", "native-libs")
    }

    private val availablePort: String = ServerSocket(0).use { serverSocket ->
        serverSocket.localPort.toString()
    }

    override fun beforeAll(context: ExtensionContext?) {
        server = ServerRunner.createServerFromCommandLineArgs(
            arrayOf("-inMemory", "-port", availablePort)
        )?.also {
            it.start()
        }
        amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
            .withCredentials(AWSStaticCredentialsProvider(BasicAWSCredentials("access", "secret")))
            .withEndpointConfiguration(
                AwsClientBuilder.EndpointConfiguration(
                    "http://localhost:$availablePort",
                    "us-east-1"
                )
            )
            .build()
    }

    override fun afterEach(context: ExtensionContext?) {
        val scan = amazonDynamoDB.scan(ScanRequest().withTableName(tableName))
        amazonDynamoDB.deleteItem(tableName, mapOf(primaryKeyName to scan.items.first()[primaryKeyName]))
    }

    override fun afterAll(context: ExtensionContext?) {
        server?.stop()
    }
}
