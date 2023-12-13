package com.duan.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duan.entity.TbOpc;
import com.duan.mapper.OpcMapper;
import com.duan.service.OpcService;
import com.duan.utils.OpcUtils;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;

@Service
public class OpcServiceImpl extends ServiceImpl<OpcMapper, TbOpc>implements OpcService {

    @Autowired
    private OpcUtils opcUtils;

    @Autowired
    private OpcUaClient opcUaClient;

    /**
     * 获取PLC1500节点下的数据
     * @return
     */

    public List<TbOpc> getPLC1500Value() {
        try {
            //遍历获取PLC.PLC1500下的所有节点key
            Set<String> browse = opcUtils.browse("PLC.PLC1500", opcUaClient);
            List<TbOpc> opcList = new ArrayList<>();
            //读取信息 s:节点key值
            for (String s : browse) {
                //获取节点的address属性
                String address = extracted(s.substring(12));
                // o:读取到的值
                Object o = opcUtils.readValue(s, opcUaClient);
                TbOpc tbOpc = new TbOpc(s,address,o.toString(),"PLC1500");
                opcList.add(tbOpc);
            }
            return opcList;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取系统运行状态的节点信息
     * @return
     */

    public List<TbOpc> getRunningValue() {
        try {
            //遍历获取PLC.PLC1500.系统运行信号下的所有节点key
            Set<String> browse = opcUtils.browse("PLC.PLC1500.系统运行信号", opcUaClient);
            List<TbOpc> opcList = new ArrayList<>();
            //读取信息 s:节点key值
            for (String s : browse) {
                //获取节点的address属性
                String address = extracted(s.substring(12));
                // o:读取到的值
                Object o = opcUtils.readValue(s, opcUaClient);
                TbOpc tbOpc = new TbOpc(s,address,o.toString(),"系统运行信号");
                opcList.add(tbOpc);
            }
            return opcList;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取系统报警信号下所有的节点信息
     * @return
     */

    public List<TbOpc> getWarnValue() {
        try {
            //遍历获取PLC.PLC1500.系统报警信号下的所有节点key
            Set<String> browse = opcUtils.browse("PLC.PLC1500.系统报警信号", opcUaClient);
            List<TbOpc> opcList = new ArrayList<>();
            //读取信息 s:节点key值
            for (String s : browse) {
                //获取节点的address属性
                String address = extracted(s.substring(12));
                // o:读取到的值
                Object o = opcUtils.readValue(s, opcUaClient);
                TbOpc tbOpc = new TbOpc(s,address,o.toString(),"系统报警信号");
                opcList.add(tbOpc);
            }
            return opcList;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 根据不同的节点，返回节点的address属性
     * @param s
     * @return
     */
    private static String extracted(String s) {

        String address = null;
        try {
            //获取该节点的地址值，通过拼接http请求来拼接访问不同节点的路径
            //Basic Auth验证，并且携带用户名和密码
            String username = "Administrator";
            String password = "123456";
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
            String authHeaderValue = "Basic " + new String(encodedAuth);

            URL url = new URL("http://127.0.0.1:57412/config/v1/project/channels/PLC/devices/PLC1500/tags/"+s);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", authHeaderValue);
            //通过输入流读取响应数据，然后通过stringbuffer对象，产生可修改的字符串
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //解析字符串，截取字符串，获取地址
            JSONObject jsonObject = JSONObject.parseObject(response.toString());
            address = jsonObject.getString("servermain.TAG_ADDRESS");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return address;
    }


}
