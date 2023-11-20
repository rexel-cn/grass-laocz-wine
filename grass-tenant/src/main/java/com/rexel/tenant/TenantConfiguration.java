package com.rexel.tenant;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.rexel.tenant.security.TenantSecurityWebFilter;
import com.rexel.tenant.web.TenantContextWebFilter;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@AllArgsConstructor
public class TenantConfiguration {

    /**
     * 自定义租户处理器
     *
     * @return TenantHandler
     */
    @Bean
    @ConditionalOnMissingBean(TenantLineHandler.class)
    public TenantLineHandler tenantHandler() {
        return new CommonTenantHandler();
    }

    @Bean
    public FilterRegistrationBean<TenantContextWebFilter> tenantContextWebFilter() {
        FilterRegistrationBean<TenantContextWebFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TenantContextWebFilter());
        registrationBean.setOrder(-104);
        return registrationBean;
    }

    //Spring Security Filter 默认为 -100，可见 org.springframework.boot.autoconfigure.security.SecurityProperties 配置属性类
    @Bean
    public FilterRegistrationBean<TenantSecurityWebFilter> tenantSecurityWebFilter() {
        FilterRegistrationBean<TenantSecurityWebFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TenantSecurityWebFilter());
        registrationBean.setOrder(-99);
        return registrationBean;
    }


    /**
     * 分页插件
     *
     * @param tenantHandler 自定义租户处理器
     * @return PaginationInterceptor
     *//*
    @Bean
    public PaginationInnerInterceptor paginationInterceptor(TenantLineHandler tenantHandler) {
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        paginationInterceptor.
        List<ISqlParser> sqlParserList = new ArrayList<>();
        TenantSqlParser tenantSqlParser = new TenantSqlParser();
        tenantSqlParser.setTenantHandler(tenantHandler);
        sqlParserList.add(tenantSqlParser);
        paginationInterceptor.setSqlParserList(sqlParserList);
        return paginationInterceptor;
    }*/
    /**
     * 新多租户插件配置,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存万一出现问题
     */

}
