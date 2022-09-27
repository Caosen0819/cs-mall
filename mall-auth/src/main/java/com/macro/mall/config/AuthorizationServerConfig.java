package com.macro.mall.config;


import com.macro.mall.components.JwtTokenEnhancer;
import com.macro.mall.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author  公众号：码猿技术专栏
 * 认证中心的配置
 * 也就是授权服务器配置
 * `@EnableAuthorizationServer`：这个注解标注这是一个认证中心
 * 继承AuthorizationServerConfigurerAdapter
 */
@EnableAuthorizationServer
@Configuration
@AllArgsConstructor
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * 令牌存储策略
     */
//    @Autowired
//    private TokenStore tokenStore;

    /**
     * 客户端存储策略，这里使用内存方式，后续可以存储在数据库
     */
    @Autowired
    private ClientDetailsService clientDetailsService;

    /**
     * Security的认证管理器，密码模式需要用到
     */
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private JwtAccessTokenConverter jwtAccessTokenConverter;
//
//    @Autowired
//    private OAuthServerAuthenticationEntryPoint authenticationEntryPoint;
//
//    @Autowired
//    private DataSource dataSource;

    private final PasswordEncoder passwordEncoder;
    private final UserServiceImpl userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenEnhancer jwtTokenEnhancer;

    /**
     * 配置客户端详情，并不是所有的客户端都能接入授权服务
     * 用来配置客户端详情服务（ClientDetailsService），
     * 客户端详情信息在这里进行初始化，
     * 你能够把客户端详情信息写死在这里或者是通过数据库来存储调取详情信息
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //使用JdbcClientDetailsService，从数据库中加载客户端的信息
//        System.out.println("JdbcClientDetailsService自己是有一个默认的字段的表的,里面有相应的查询的方法,");
//        System.out.println("所以程序是从数据库中的oauth_client_details表中加载客户端信息");
//        clients.withClientDetails(new JdbcClientDetailsService(dataSource));
        //TODO 暂定内存模式，后续可以存储在数据库中，更加方便
        clients.inMemory()
                .withClient("admin-app")
                .secret(passwordEncoder.encode("123456"))
                .scopes("all")
                .authorizedGrantTypes("password", "refresh_token")
                .accessTokenValiditySeconds(3600*24)
                .refreshTokenValiditySeconds(3600*24*7)
                .and()
                .withClient("portal-app")
                .secret(passwordEncoder.encode("123456"))
                .scopes("all")
                .authorizedGrantTypes("password", "refresh_token")
                .accessTokenValiditySeconds(3600*24)
                .refreshTokenValiditySeconds(3600*24*7);

    }
//
//    /**
//     * 令牌管理服务的配置
//     */
//    @Bean
//    public AuthorizationServerTokenServices tokenServices() {
//        System.out.println("令牌管理服务的配置");
//        DefaultTokenServices services = new DefaultTokenServices();
//        //客户端端配置策略
//        services.setClientDetailsService(clientDetailsService);
//        //支持令牌的刷新
//        services.setSupportRefreshToken(true);
//        //令牌服务
//        services.setTokenStore(tokenStore);
//        //access_token的过期时间
//        services.setAccessTokenValiditySeconds(60 * 60 * 24 * 3);
//        //refresh_token的过期时间
//        services.setRefreshTokenValiditySeconds(60 * 60 * 24 * 3);
//
//        //设置令牌增强，使用JwtAccessTokenConverter进行转换
//        services.setTokenEnhancer(jwtAccessTokenConverter);
//        return services;
//
//    }


    /**
     * 授权码模式的service，使用授权码模式authorization_code必须注入
     */
//    @Bean
//    public AuthorizationCodeServices authorizationCodeServices() {
//        return new JdbcAuthorizationCodeServices(dataSource);
//        //todo 授权码暂时存在内存中，后续可以存储在数据库中
////        return new InMemoryAuthorizationCodeServices();
//    }

    /**
     * 配置令牌访问的端点
     */
    @Override
    @SuppressWarnings("ALL")
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        System.out.println("配置令牌访问的端点");
        //将自定义的授权类型添加到tokenGranters中
//        List<TokenGranter> tokenGranters = new ArrayList<>(Collections.singletonList(endpoints.getTokenGranter()));
//        tokenGranters.add(new MobilePwdGranter(authenticationManager, tokenServices(), clientDetailsService,
//                new DefaultOAuth2RequestFactory(clientDetailsService)));
//
//        endpoints
//                //设置异常WebResponseExceptionTranslator，用于处理用户名，密码错误、授权类型不正确的异常
//                .exceptionTranslator(new OAuthServerWebResponseExceptionTranslator())
//                //授权码模式所需要的authorizationCodeServices
//                .authorizationCodeServices(authorizationCodeServices())
//                //密码模式所需要的authenticationManager
//                .authenticationManager(authenticationManager)
//                //令牌管理服务，无论哪种模式都需要
//                .tokenServices(tokenServices())
//                //添加进入tokenGranter
//                .tokenGranter(new CompositeTokenGranter(tokenGranters))
//                //只允许POST提交访问令牌，uri：/oauth/token
//                .allowedTokenEndpointRequestMethods(HttpMethod.POST);

        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> delegates = new ArrayList<>();
        delegates.add(jwtTokenEnhancer);
        delegates.add(accessTokenConverter());
        enhancerChain.setTokenEnhancers(delegates); //配置JWT的内容增强器

        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService) //配置加载用户信息的服务
                .accessTokenConverter(accessTokenConverter())
                .tokenEnhancer(enhancerChain);
    }

    /**
     * 配置令牌访问的安全约束
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
//        System.out.println("配置令牌访问的安全约束");
//        //自定义ClientCredentialsTokenEndpointFilter，用于处理客户端id，密码错误的异常
//        OAuthServerClientCredentialsTokenEndpointFilter endpointFilter = new OAuthServerClientCredentialsTokenEndpointFilter(security,authenticationEntryPoint);
//        endpointFilter.afterPropertiesSet();
//        security.addTokenEndpointAuthenticationFilter(endpointFilter);
//
//        security
//                .authenticationEntryPoint(authenticationEntryPoint)
//                //开启/oauth/token_key验证端口权限访问
//                .tokenKeyAccess("permitAll()")
//                //开启/oauth/check_token验证端口认证权限访问
//                .checkTokenAccess("permitAll()");
//                //一定不要添加allowFormAuthenticationForClients，否则自定义的OAuthServerClientCredentialsTokenEndpointFilter不生效
////                .allowFormAuthenticationForClients();

        security.allowFormAuthenticationForClients();

    }
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setKeyPair(keyPair());
        return jwtAccessTokenConverter;
    }

    @Bean
    public KeyPair keyPair() {
        //从classpath下的证书中获取秘钥对
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "123456".toCharArray());
        return keyStoreKeyFactory.getKeyPair("jwt", "123456".toCharArray());
    }
}