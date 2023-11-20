package com.rexel.dview.api;

import com.rexel.dview.cons.Constants;
import com.rexel.dview.pojo.DViewVarInfo;
import com.rexel.dview.pojo.DviewCommondResultData;
import com.rexel.dview.utils.ByteUtils;
import com.rexel.dview.utils.ParamUtils;
import com.rexel.dview.utils.SocketUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: ShortUpdateValueByIndex
 * @Description: [4].通过变量索引, 批量修改变量值
 * @Author: chunhui.qu@rexel.com.cn
 * @Date: 2020/2/21
 */
@Slf4j
public class ShortUpdateValueByIndex extends AbstractBase {
    private final String ipAddress;
    private final int port;

    /**
     * 构造函数
     *
     * @param ipAddress ip地址
     * @param port      端口号
     */
    public ShortUpdateValueByIndex(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    /**
     * 执行逻辑
     *
     * @param varType   变量类型（16进制）
     * @param valueList 变量值列表
     * @return ResultData：{statusCode：0 成功}
     */
    public DviewCommondResultData execute(byte varType, List<DViewVarInfo> valueList) {
        Socket socket = null;
        OutputStream out = null;
        InputStream in = null;
        DviewCommondResultData resultData = new DviewCommondResultData();
        try {
            // 初始化Socket
            socket = new Socket(this.ipAddress, this.port);

            // 生成请求参数
            byte[] param = ParamUtils.makeParam4(varType, valueList);
            if (param == null) {
                log.error("param error. varType=" + varType);
                resultData.setStatusCode(Constants.FAIL);
                resultData.setRemark("param error. varType=" + varType);
                return resultData;
            }
            log.info("param" + Arrays.toString(param));

            // 发送命令
            out = socket.getOutputStream();
            if (out == null) {
                log.error("OutputStream == null");
                resultData.setStatusCode(Constants.FAIL);
                resultData.setRemark("OutputStream == null");
                return resultData;
            }
            out.write(param);
            out.flush();

            // 接收数据
            in = socket.getInputStream();
            if (in == null) {
                log.error("InputStream == null");
                resultData.setStatusCode(Constants.FAIL);
                resultData.setRemark("InputStream == null");
                return resultData;
            }

            // 应答数据
            return myHeadLogic(in, resultData);
        } catch (IOException e) {
            log.error("ShortUpdateValueByIndex exception." + e.getMessage());
            resultData.setStatusCode(Constants.FAIL);
            resultData.setRemark("ShortUpdateValueByIndex exception:{" + e + "}");
            return resultData;

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
    }

    /**
     * 检查服务器应答消息头
     *
     * @param in         消息输入流
     * @param resultData
     * @return fale：失败、true：成功
     * @throws IOException e
     */
    private DviewCommondResultData myHeadLogic(InputStream in, DviewCommondResultData resultData) throws IOException {
        byte[] header = new byte[10];
        int len = SocketUtils.read(in, header);
        log.debug("header=" + Arrays.toString(header));
        if (len != header.length) {
            log.error("The returned byte length is incorrect;");

            resultData.setStatusCode(Constants.FAIL);
            resultData.setRemark("The returned byte length is incorrect;");
            return resultData;
        }

        //检查应答标识
        //Buffer[0][1]
        if (checkResponseIdent(header[0], header[1])) {
            log.error("checkResponseIdent error.");
        }

        //检查功能码
        //Buffer[2][3]
        if (checkResponseFuncCode(header[2], header[3])) {
            log.error("checkResponseFuncCode error.");
        }

        //检查变量类型
        //Buffer[4]
        if (checkResponseVarType(header[4])) {
            log.info("checkResponseVarType error.");
        }

        //检查应答结果是否正确
        //Buffer[5]
        byte result = header[5];
        if (checkResponseResult(result)) {
            log.error("checkResponseResult error. result=" + Constants.DVIEW_MSG_MAP.get((int) result));
        }

        // 成功计数
        // Buffer[6][7]
        byte[] okBuf = new byte[2];
        System.arraycopy(header, 6, okBuf, 0, okBuf.length);
        int okCount = ByteUtils.bytesToInt(okBuf);
        log.info("succeed count:" + okCount);

        // 失败计数
        // Buffer[8][9]
        byte[] ngBuf = new byte[2];
        System.arraycopy(header, 8, ngBuf, 0, ngBuf.length);
        int ngCount = ByteUtils.bytesToInt(ngBuf);
        log.info("failed count:" + ngCount);


        resultData.setRequestId(String.valueOf(header[0]) + String.valueOf(header[1]));
        resultData.setMethod(String.valueOf(header[2]) + String.valueOf(header[3]));
        resultData.setVariateType(header[4]);
        resultData.setStatusCode(Integer.valueOf(header[5]));
        resultData.setOkCount(okCount);
        resultData.setNgCount(ngCount);

        return resultData;
    }

    /**
     * 检查功能码
     */
    @Override
    public boolean checkResponseFuncCode(byte b1, byte b2) {
        return b1 != 0x27 || b2 != 0x1D;
    }


}
