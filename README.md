# 玉符Auth SDK

[![License](http://img.shields.io/:license-mit-blue.svg?style=flat)](http://doge.mit-license.org)

玉符Auth SDK遵循[OIDC标准](https://openid.net/specs/openid-connect-core-1_0.html) ，可以帮助开发者快速集成玉符单点登录能力，完成用户授权。 

> 支持java1.8及以上版本

## 快速开始
Maven项目中使用如下方式引入:

    <dependency>
        <groupId>com.yufuid</groupId>
        <artifactId>auth-common</artifactId>
        <version>1.0</version>
    </dependency>

Gradle项目中使用如下方式引入:

    compile group: 'com.yufuid', name: 'auth-common', version: '1.0'

## 使用方法

### 功能一 使用SDK获取玉符身份信息  

接入玉符身份信息，首先需要开发一个回调接口用于接收玉符的一次性授权码  
- redirect_uri


在玉符新建OIDC应用，可以得到如下信息：  
- clientID
- clientSecret
- Well-Known地址 

代码中使用如下方法进行初始化：

    ClientKeyPair clientKeyPair = new ClientKeyPair({clientID}, {clientSecret});
    CoreAuthClient coreAuthClient = new CoreAuthClientBuilder(clientKeyPair, wellKnownUrl, redirect_uri).build();

当用户未登录时，可以使用如下方式获得构建授权请求地址。  
重定向到此地址即可发起授权请求：

    coreAuthClient.authorize();

用户授权完成后，会跳转到redirect_uri，并在查询参数里携带授权码code。  
使用code调用如下方法即可获取用户信息：

    UserInfo userInfo = coreAuthClient.getUserInfo(code);

---
### 功能二 使用SDK签署和验证JWT令牌

从玉符网站可以生成ServiceAccount，格式如下所示  

```
{
  "clientId": "5af1****",
  "privateKey": {
    "p": "2pT_****",
    "kty": "RSA",
    "q": "mSRY****",
    "d": "X7X2****",
    "e": "AQAB",
    "use": "sig",
    "kid": "4e36****",
    "qi": "CRQv****",
    "dp": "tgcZ****",
    "dq": "Ga2j****",
    "n": "gsIV****"
  }
}
```

代码中使用如下方法进行初始化：

    TokenClient tokenClient = new TokenClient(serviceAccount)

根据具体的业务需求，传入对应的Audience  
即可生成token用于校验

    String token = tokenClient.generateJWS({audience});
    
也可以传入jwt的token，验证token的有效性并取出token中字段

    JWT jwt = tokenClient.verify(token);
    
