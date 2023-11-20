package com.rexel.common.handler;/**
 * @Author 董海
 * @Date 2022/11/17 15:50
 * @version 1.0
 */

import cn.hutool.json.JSONUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @ClassName ArrayStringTypeHandler
 * @Description mybatis  ArrayStringTypeHandler
 * @Author Hai.Dong
 * @Date 2022/11/17 15:50
 **/
@MappedTypes({String[].class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class ArrayLongTypeHandler extends BaseTypeHandler<Long[]> {

    private static Long[] l = new Long[]{};


    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Long[] parameter, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, JSONUtil.toJsonStr(parameter));

    }

    @Override
    public Long[] getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return JSONUtil.parseArray(resultSet.getString(columnName)).toArray(l);
    }

    @Override
    public Long[] getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return JSONUtil.parseArray(resultSet.getString(columnIndex)).toArray(l);
    }

    @Override
    public Long[] getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        return JSONUtil.parseArray(callableStatement.getString(columnIndex)).toArray(l);
    }
}
