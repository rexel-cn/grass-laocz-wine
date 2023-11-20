package com.rexel.common.vo;

import lombok.Data;

import java.util.List;

/**
 * @ClassName donghai
 * @Description 饼图
 * @Author Hai.Dong
 * @Date 2021/2/20 10:46
 **/
@Data
public class PieChartVO {

    private String name;
    private List<ShowData> data;
    private List<String> legend;

    public static class ShowData {
        private String name;
        private String value;

        public ShowData() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
