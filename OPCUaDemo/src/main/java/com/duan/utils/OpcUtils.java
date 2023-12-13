package com.duan.utils;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.api.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.api.identity.IdentityProvider;
import org.eclipse.milo.opcua.sdk.client.api.identity.UsernameProvider;
import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
/**
 * 该类是连接opc客户端的工具类
 * 提供了创建客户端对象、读取客户端节点、读取单个节点的值、关闭客户端连接方法
 */
@Component
@Slf4j
public class OpcUtils {

    /**
     * 方法描述: 创建客户端
     * @param username
     * @param password
     * @return {@link OpcUaClient}
     * @throws
     */
    public  OpcUaClient createClient(String username,String password){
        try {
            //获取安全策略
            List<EndpointDescription> endpointDescription = DiscoveryClient.getEndpoints("opc.tcp://127.0.0.1:49320").get();
            //过滤出一个自己需要的安全策略
            EndpointDescription endpoint = endpointDescription.stream()
                    //这里为了方便测试选用的是匿名登录
                    .filter(e -> e.getSecurityPolicyUri().equals(SecurityPolicy.None.getUri()))
                    .findFirst().orElse(null);
            IdentityProvider identityProvider=new AnonymousProvider();
            //如果传入的参数有用户名和密码就使用用户名密码验证
            if(!StringUtils.isEmpty(username)||!StringUtils.isEmpty(password)){
                identityProvider=new UsernameProvider(username,password);
            }
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
            log.info("连接成功。。。success");
            return opcClient;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("======== opc connection fail ========");
        }
        return null;
    }

    /**
     * 方法描述: 查询某个节点下的所有节点
     * @param identifier 节点名称
     * @param client 客户端对象
     * @return keys 该节点名称下的所有节点名称
     * @throws
     */
    public  Set<String> browse(String identifier, OpcUaClient client) throws Exception {
        Set<String> keys=new HashSet<>(500);
        browse(identifier,keys,client);
        return keys;
    }

    /**
     * 方法描述: 查询某个节点下的所有节点
     * @param identifier  节点名称
     * @param client      客户端对象
     * @return
     * @throws
     */
    private  Set<String> browse(String identifier, Set<String> keys, OpcUaClient client) throws Exception {
        NodeId nodeId = Identifiers.ObjectsFolder;
        if(!StringUtils.isEmpty(identifier)){
            //namespaceIndex是默认为2
            nodeId = new NodeId(2, identifier);
        }

        BrowseDescription browse = new BrowseDescription(
                nodeId,
                BrowseDirection.Forward,
                Identifiers.References,
                true,
                UInteger.valueOf(NodeClass.Object.getValue() | NodeClass.Variable.getValue()),
                UInteger.valueOf(BrowseResultMask.All.getValue())
        );

        BrowseResult browseResult = client.browse(browse).get();
        ReferenceDescription[] references = browseResult.getReferences();

        for (ReferenceDescription reference : references) {
            keys.add(reference.getNodeId().getIdentifier().toString());
            if(reference.getNodeClass().getValue()==NodeClass.Object.getValue()){
                browse(reference.getNodeId().getIdentifier().toString(),keys,client);
            }
        }
        return keys;
    }

    /**
     * 方法描述: 读取单个节点值
     * @param identifier 节点名称
     * @param client     客户端对象
     * @return {@link Object}
     * @throws
     */
    public  Object readValue(String identifier, OpcUaClient client){

        NodeId nodeId = new NodeId(2, identifier);

        DataValue value = null;
        try {
            client.readValue(0.0, TimestampsToReturn.Both,nodeId);
            value = client.readValue(0.0, TimestampsToReturn.Both, nodeId).get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if(Objects.nonNull(value)&&Objects.nonNull(value.getValue())&&Objects.nonNull(value.getValue().getValue())) {
            return value.getValue().getValue();
        }
        return null;
    }


    /**
     * 方法描述: 断开连接
     * @param client 客户端对象
     * @return
     */
    public  void disconnect(OpcUaClient client){
        try {
            client.disconnect().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


}
