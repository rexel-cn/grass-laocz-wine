package com.rexel.dview.api;

import com.rexel.dview.cons.Constants;
import com.rexel.dview.pojo.DViewVarInfo;
import com.rexel.dview.utils.CommonUtils;
import com.rexel.dview.utils.ParamUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: ShortGetVarValueByIndex
 * @Description: [3].通过变量索引, 选择读取某些变量值（用于短连接）
 * @Author: chunhui.qu@rexel.com.cn
 * @Date: 2020/5/28
 */
@Slf4j
public class ShortGetVarValueByIndex extends AbstractGetVarValue {
    private final String ipAddress;
    private final int port;

    /**
     * 构造函数
     *
     * @param ipAddress ip地址
     * @param port      端口号
     */
    public ShortGetVarValueByIndex(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    /**
     * 执行逻辑
     *
     * @param varType   变量类型（16进制）
     * @param timeRange 秒时间段（0:全部,1-3600秒）
     * @param indexList 索引列表
     * @return 变量值Map
     */
    public Map<Integer, DViewVarInfo> execute(byte varType, int timeRange, List<Object> indexList) throws IOException {
        Socket socket = null;
        OutputStream out = null;
        InputStream in = null;
        Map<Integer, DViewVarInfo> result = new HashMap<>(100);
        try {
            // 按最大长度分解
            List<List<Object>> splitList =
                    CommonUtils.listSplit(indexList, Constants.LIST_SPLIT_LEN);

            for (List<Object> onePackage : splitList) {
                // 生成请求参数
                byte[] param = ParamUtils.makeParam3(varType, timeRange, onePackage);
                if (param == null || param.length == 0) {
                    log.error("param error. varType=" + varType);
                    return null;
                }

                // 初始化Socket
                socket = new Socket(this.ipAddress, this.port);

                // 发送命令
                out = socket.getOutputStream();
                if (out == null) {
                    log.error("OutputStream == null");
                    return null;
                }
                out.write(param);
                out.flush();

                // 接收数据
                in = socket.getInputStream();
                if (in == null) {
                    log.error("InputStream == null");
                    return null;
                }

                // 解析数据头
                if (!checkHeaderSuccess(in)) {
                    log.error("headLogic error.");
                    return null;
                }

                // 解析数据体
                Map<Integer, DViewVarInfo> resultMap = bodyLogic(in, varType);
                if (resultMap != null && resultMap.size() > 0) {
                    result.putAll(resultMap);
                }
            }
        } catch (IOException e) {
            log.error("ShortGetVarValueByIndex exception." + e.getMessage());
            throw e;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 检查功能码
     */
    @Override
    public boolean checkResponseFuncCode(byte b1, byte b2) {
        return b1 != 0x27 || b2 != 0x1E;
    }
}
