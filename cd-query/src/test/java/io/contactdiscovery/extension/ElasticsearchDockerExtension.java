package io.contactdiscovery.extension;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * @author Mykola Yashchenko
 */
public class ElasticsearchDockerExtension implements BeforeAllCallback, AfterAllCallback {

    private static final String IMAGE_NAME = "elasticsearch";
    private static final String[] PORTS = {"9300", "9300"};

    private final DockerClient dockerClient = dockerClient();

    private String id;

    @Override
    public void afterAll(final ExtensionContext extensionContext) throws Exception {
        final ContainerConfig containerConfig = ContainerConfig.builder()
                .image(IMAGE_NAME).exposedPorts(PORTS)
                .build();

        final ContainerCreation containerCreation = dockerClient.createContainer(containerConfig);
        dockerClient.startContainer(id = containerCreation.id());

        waitForPort(9300, 1000);
    }

    @Override
    public void beforeAll(final ExtensionContext extensionContext) throws Exception {
        try {
            dockerClient.killContainer(id);
            dockerClient.removeContainer(id);
        } finally {
            dockerClient.close();
        }
    }

    private DockerClient dockerClient() {
        try {
            return DefaultDockerClient.fromEnv().build();
        } catch (final DockerCertificateException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void waitForPort(int port, long timeoutInMillis) {
        final SocketAddress address = new InetSocketAddress("localhost", port);
        long totalWait = 0;
        while (true) {
            try {
                SocketChannel.open(address);
                return;
            } catch (final IOException e) {
                try {
                    Thread.sleep(100);
                    totalWait += 100;
                    if (totalWait > timeoutInMillis) {
                        throw new IllegalStateException("Timeout while waiting for port " + port);
                    }
                } catch (final InterruptedException ie) {
                    throw new IllegalStateException(ie);
                }
            }
        }
    }
}
