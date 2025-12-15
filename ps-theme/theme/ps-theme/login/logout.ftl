<!doctype html>
<html>
<head>
  <meta charset="utf-8"/>
  <title>正在退出登录...</title>
  <style>
    body {
      font-family: Microsoft YaHei, SimSun, sans-serif;
      background:linear-gradient(135deg,  #c6e5fe 0%,  #66a1ea 100%);
      display: flex;
      align-items: center;
      justify-content: center;
      min-height: 100vh;
      margin: 0;
    }
    .logout-container {
      background: white;
      border-radius: 16px;
      padding: 40px;
      box-shadow: 0 4px 12px rgba(192, 0, 0, 0.2);
      text-align: center;
      max-width: 400px;
      border-left: 4px solid #C00000;
    }
    .logout-message {
      font-size: 18px;
      color: #333;
      margin-bottom: 30px;
      font-weight: 500;
    }
    .logout-loader {
      width: 40px;
      height: 40px;
      border: 4px solid #f3f3f3;
      border-top: 4px solid #C00000;
      border-radius: 50%;
      animation: spin 1s linear infinite;
      margin: 0 auto;
    }
    @keyframes spin {
      0% { transform: rotate(0deg); }
      100% { transform: rotate(360deg); }
    }
  </style>
</head>
<body>
  <div class="logout-container">
    <p class="logout-message">正在退出登录，稍候...</p>
    <div class="logout-loader"></div>
  </div>

  <script>
    (function() {
      try {
        // 构建 OIDC 注销 URL
        var realmUrl = "${url.realm}";
        var logoutUrl = realmUrl + "/protocol/openid-connect/logout";
        
        // 准备查询参数
        var params = new URLSearchParams();
        
        // 添加 id_token_hint(如果存在)
        <#if idTokenHint??>
        params.append("id_token_hint", "${idTokenHint}");
        </#if>
        
        // 添加 post_logout_redirect_uri
        var postLogoutUri = "${url.login!''}";
        if (!postLogoutUri) {
          postLogoutUri = window.location.origin;
        }
        params.append("post_logout_redirect_uri", postLogoutUri);
        
        // 拼接完整 URL 并跳转
        var fullLogoutUrl = logoutUrl + "?" + params.toString();
        window.location.replace(fullLogoutUrl);
      } catch (error) {
        // 降级策略:直接跳转到登录页
        console.error("注销出错:", error);
        setTimeout(function() {
          window.location.replace("${url.login!'/'}" || window.location.origin);
        }, 3000);
      }
    })();
  </script>
</body>
</html>
