<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>

<head>
  <title>主页</title>
</head>

<body>
<h2>欢迎!</h2>

<p>登录成功</p>

<!-- 文件上传表单 -->
<h3>文件上传</h3>
<form action="fileservice/upload" method="post" enctype="multipart/form-data">
  <label for="fileUpload">选择文件上传:</label>
  <input type="file" id="fileUpload" name="fileUpload"><br><br>
  <input type="submit" value="上传文件">
</form>

<!-- 文件下载表单 -->
<h3>文件下载</h3>
<%--
<form action="fileservice/download" method="get">
  <label for="url">输入要下载的文件 URL:</label>
  <input type="text" id="url" name="url"><br><br>
  <input id="getLink" type="submit" value="下载文件">
</form>
--%>
<input type="submit" id="downloadLink" value="下载文件">
<input type="button" id="userIpButton" value="userIp">
<input type="button" id="userAgentButton" value="userAgent">
<input type="button" id="cookiesButton" value="cookies">
<input type="button" id="uriViewsButton" value="uriViews">
</body>

<script>
  // 定义常量
  const urls = {
    userIp: 'http://localhost:8080/commservice/userIp.do',
    userAgent: 'http://localhost:8080/commservice/userAgent.do',
    cookies: 'http://localhost:8080/commservice/cookies.do',
    uriViews: 'http://localhost:8080/commservice/uriViews.do'
  };

  function bindButtonEvent(buttonId, urlKey) {
    document.getElementById(buttonId).addEventListener('click', function () {
      sendRequest(urls[urlKey], function (response) {
        console.log(response);
        window.alert(response)
      });
    });
  }

  bindButtonEvent('userIpButton', 'userIp');
  bindButtonEvent('userAgentButton', 'userAgent');
  bindButtonEvent('cookiesButton', 'cookies');
  bindButtonEvent('uriViewsButton', 'uriViews');

  document.getElementById('downloadLink').addEventListener('click', function () {
    window.alert("http://121.40.26.227:8080/downloads/one.jpg");
  });

  function sendRequest(url, callback) {
    var xhr = new XMLHttpRequest();
    xhr.open('GET', url);
    xhr.onload = function () {
      if (xhr.status === 200) {
        callback(xhr.responseText);
      } else {
        console.error('请求失败，状态码：' + xhr.status);
        // 可以在这里添加更详细的错误信息展示给用户
        alert('请求失败，状态码：' + xhr.status);
      }
    };
    xhr.onerror = function () {
      console.error('发生网络错误');
      // 可以在这里添加更详细的错误信息展示给用户
      alert('发生网络错误，请检查网络连接。');
    };
    xhr.send();
  }
</script>

</html>