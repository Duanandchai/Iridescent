package com.duan.controller;

import com.duan.entity.TbOpc;
import com.duan.service.OpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@Slf4j
public class OpcController {

    @Autowired
    private OpcService opcService;


    /**
     * 获取PLC.PLC1500节点下的数据
     * @return
     */
    @GetMapping("/getPLC1500")
    public List<TbOpc> getPLC1500Value(){
        return opcService.getPLC1500Value();
    }

    /**
     * 获取PLC.PLC1500.系统报警信号节点下的数据
     * @return
     */
    @GetMapping("/getWarnValue")
    public List<TbOpc> getWarnValue(){
        return opcService.getWarnValue();
    }

    /**
     * 获取PLC.PLC1500节点下的数据
     * @return
     */
    @GetMapping("/getRunningValue")
    public List<TbOpc> getRunningValue(){
        return opcService.getPLC1500Value();
    }



//    @GetMapping("/getAllNodesValue")
//    public void getAllNodesValue(){
//
//        System.out.println("getAllNodesValue方法开始执行");
////        TbOpc tbOpc = new TbOpc();
//        try {
//            //获取该节点下的所有节点数据
//            Set<String> nodeIdList = opcUtils.browse("PLC.PLC1500", opcUaClient);
//            System.out.println("遍历PLC.PLC1500下的节点");
//            for (String s : nodeIdList) {
//                if (!s.contains("_")){
//                    //获取到每个节点的地址值
//                    String address = extracted(s.substring(12));
//                    Object o = opcUtils.readValue(s, opcUaClient);
//                    TbOpc tbOpc = new TbOpc(s,address,  o.toString(),"PLC1500");
//                    //s为节点名称，o为节点数据
//                    opcService.save(tbOpc);
//                    log.info("获取到："+s+"节点的数据"+o);
//                }
//
//            }
//            //获取该节点下的所有节点数据
//
//            Set<String> nodeIdList2 = opcUtils.browse("PLC.PLC1500.系统报警信号", opcUaClient);
//            System.out.println("遍历PLC.PLC1500.系统报警信号");
//            for (String s : nodeIdList2) {
//                if (!s.contains("_")){
//
//                    //获取到每个节点的地址值
//                    String address = extracted(s.substring(12));
//                    Object o = opcUtils.readValue(s, opcUaClient);
//                    TbOpc tbOpc = new TbOpc(s,address,  o.toString(),"系统报警信号");
//                    //s为节点名称，o为节点数据
//                    opcService.save(tbOpc);
//                    log.info("获取到："+s+"节点的数据"+o);
//                }
//
//            }
//            //获取该节点下的所有节点数据
//            Set<String> nodeIdList3 = opcUtils.browse("PLC.PLC1500.系统运行信号", opcUaClient);
//            System.out.println("遍历PLC.PLC1500.系统运行信号");
//            for (String s : nodeIdList3) {
//                if (!s.contains("_")){
//                    //获取到每个节点的地址值
//                    String address = extracted(s.substring(12));
//                    Object o = opcUtils.readValue(s, opcUaClient);
//                    TbOpc tbOpc = new TbOpc(s,address,  o.toString(),"系统运行信号");
//                    //s为节点名称，o为节点数据
//                    opcService.save(tbOpc);
//                    log.info("获取到："+s+"节点的数据"+o);
//                }
//
//            }
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * 根据不同的节点，返回节点的address属性
//     * @param s
//     * @return
//     */
//    private static String extracted(String s) {
//
//        String address = null;
//        try {
//            String username = "Administrator";
//            String password = "123456";
//            String auth = username + ":" + password;
//            byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
//            String authHeaderValue = "Basic " + new String(encodedAuth);
//
//            URL url = new URL("http://127.0.0.1:57412/config/v1/project/channels/PLC/devices/PLC1500/tags/"+s);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            connection.setRequestProperty("Authorization", authHeaderValue);
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            String inputLine;
//            StringBuffer response = new StringBuffer();
//
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//            JSONObject jsonObject = JSONObject.parseObject(response.toString());
//            address = jsonObject.getString("servermain.TAG_ADDRESS");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return address;
//    }


}
