package com.duan.config;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.api.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.api.identity.IdentityProvider;
import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 该类是配置类
 * 因为后面的业务都需要ua客户端对象 因此把客户端对象注入到spring容器里
 * 其实就是OpcUtils里面的创建客户端的方法
 */
@Configuration
public class AppConfig {
    @Bean
    public OpcUaClient opcUaClient(){
        try {
            //获取安全策略
            List<EndpointDescription> endpointDescription = DiscoveryClient.getEndpoints("opc.tcp://127.0.0.1:49320").get();
            //过滤出一个自己需要的安全策略
            EndpointDescription endpoint = endpointDescription.stream()
                    .filter(e -> e.getSecurityPolicyUri().equals(SecurityPolicy.None.getUri()))
                    .findFirst().orElse(null);
            IdentityProvider identityProvider=new AnonymousProvider();
            // 设置配置信息
            OpcUaClientConfig config = OpcUaClientConfig.builder()
                    // opc ua 自定义的名称
                    .setApplicationName(LocalizedText.english("plc"))
                    // 地址
                    .setApplicationUri("opc.tcp://127.0.0.1:49320")
                    // 安全策略等配置
                    .setEndpoint(endpoint)
                    .setIdentityProvider(identityProvider)
                    //等待时间
                    .setRequestTimeout(UInteger.valueOf(5000))
                    .build();
            // 准备连接
            OpcUaClient opcClient =OpcUaClient.create(config);
            //开启连接
            opcClient.connect().get();
            //为了看效果，但是不优雅
            System.out.println("=====================创建OPC客户端连接成功===============================");
            return opcClient;
        } catch (Exception e) {
            e.printStackTrace();
            //为了看效果，但是不优雅
            System.out.println("===================================创建客户端失败====================================");

        }
        return null;

    }
}
