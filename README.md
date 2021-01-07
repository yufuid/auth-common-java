# 玉符Auth SDK
使用此SDK，可以快速集成玉符单点登录能力，完成用户授权。  
遵循[OIDC标准](https://openid.net/specs/openid-connect-core-1_0.html) 

## 使用方法
Maven项目中使用如下方式引入:

    <dependency>
        <groupId>com.yufuid</groupId>
        <artifactId>auth-common</artifactId>
        <version>1.0</version>
    </dependency>

Gradle项目中使用如下方式引入:

    compile group: 'com.yufuid', name: 'auth-common', version: '1.0'
      
代码中使用如下方法进行初始化：

    CoreAuthClient coreAuthClient = new CoreAuthClientBuilder(clientKeyPair, wellKnownUrl, redirect_uri).build();

可以使用如下方式发起授权请求：

    coreAuthClient.authorize();
    
使用如下方式获取用户信息：

    UserInfo userInfo = coreAuthClient.getUserInfo(code);