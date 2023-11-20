package com.rexel.dview.test;

import com.rexel.dview.api.ShortGetVarNameAndIndex;
import com.rexel.dview.api.ShortGetVarValueByIndex;
import com.rexel.dview.pojo.DViewVarInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyTest {
    private static final String ADDRESS = "localhost";
    private static final int PORT = 5002;

    public static void main(String[] args) throws IOException {
        byte varType = com.rexel.dview.cons.Constants.VAR_TYPE_MAP.get("AR");

        ShortGetVarNameAndIndex getVarNameAndIndex = new ShortGetVarNameAndIndex(ADDRESS, PORT);
        Map<Integer, String> indexMap = getVarNameAndIndex.execute(varType, 0, 5);

        List<Object> indexList = new ArrayList<>();
        indexMap.forEach((index, name) -> indexList.add(index));

        ShortGetVarValueByIndex varValueApi = new ShortGetVarValueByIndex(ADDRESS, PORT);
        Map<Integer, DViewVarInfo> indexValueMap = varValueApi.execute(varType, 0, indexList);

        System.out.println("----indexValueMap=" + indexValueMap.toString());
    }
}
