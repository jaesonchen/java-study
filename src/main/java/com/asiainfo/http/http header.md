# 内容协商机制
在服务端驱动型协商机制或者主动协商机制中，浏览器（或者其他任何类型的用户代理）会随同 URL 发送一系列的消息头。这些消息头描述了用户倾向的选择。服务器则以此为线索，通过内部算法来选择最佳方案提供给客户端。
    
![http-nego](../../../../resources/images/http/http-nego.png)  
    
HTTP/1.1 规范指定了一系列的标准消息头用于启动服务端驱动型内容协商 (`Accept、Accept-Charset、 Accept-Encoding、Accept-Language`)。服务器会使用  `Vary` 消息头来说明实际上哪些消息头被用作内容协商的参考依据（确切来说是与之相关的响应消息头），这样可以使缓存的运作更有效。
    

## Accept
`Accept` 首部列举了用户代理希望接收的媒体资源的 MIME 类型。其中不同的 MIME 类型之间用逗号分隔，同时每一种 MIME 类型会配有一个品质因数(quality factor)，该参数明确了不同 MIME 类型之间的相对优先级。
    
`Accept: text/html, application/xhtml+xml, application/xml;q=0.9, */*;q=0.8`
    
`Accept` 首部的值由浏览器或其他类型的用户代理确定，并且会由于上下文环境的不同而不同，比如在获取 HTML 页面、图片文件、视频文件或者是脚本文件的时候，无论是通过在地址栏中输入资源地址来获取还是通过  `<img>、<video> 或 <audio>` 元素引用都是不一样的。浏览器可以自由使用它们认为最为合适的首部值
    

## Accept-Charset
`Accept-Charset` 首部用于告知服务器该客户代理可以理解何种形式的字符编码。按照传统，不同地区用户使用的的浏览器会被赋予不同的值，比如说西欧地区用户使用的浏览器中，该首部的值可能会是 `ISO-8859-1,utf-8;q=0.7,*;q=0.7` 。
    
如今 UTF-8 编码已经得到了广泛的支持，成为首选的字符编码类型，为了通过减少基于配置信息的信息熵来更好地保护隐私信息， 大多数浏览器会将 `Accept-Charset` 首部移除：`Internet Explorer 8、Safari 5、Opera 11 以及 Firefox 10` 都已经不再发送该首部。
    

## Accept-Encoding
`Accept-Encoding` 首部明确说明了（接收端）可以接受的内容编码形式（所支持的压缩算法）。该首部的值是一个Q因子清单（例如 `br, gzip;q=0.8`），用来提示不同编码类型值的优先级顺序。
    
`Accept-Encoding: deflate, gzip;q=1.0, *;q=0.5`
    
将 HTTP 消息进行压缩是一种最重要的提升 Web 站点性能的方法。该方法会减小所要传输的数据量的大小，节省可用带宽。浏览器总是会发送该首部，服务器则应该配置为接受它，并且采用一定的压缩方案。
    

## Accept-Language
`Accept-Language` 首部用来提示用户期望获得的自然语言的优先顺序。该首部的值是一个Q因子清单（例如 "de, en;q=0.7"）。用户代理的图形界面上所采用的语言通常可以用来设置为默认值，但是大多数浏览器允许设置不同优先级的语言选项。
    
大多数用户代理会为 `Accept-Language` 首部提供一个默认值，该值采用的是用户界面的显示的语言，通常终端用户不能对其进行修改，或者是不知道该怎么修改，或者是无法进行修改。
    
一旦用户覆盖了服务器端选择的语言选项，站点就不应该再使用语言检测技术，而应该忠于明确选择的语言选项。换句话说，只有站点的入口页面应该使用这个首部来选择合适的语言。
    

## Vary
与前面列举的 `Accept-*` 形式的由客户端发送的首部相反，`Vary` 首部是由服务器在响应中发送的。它标示了服务器在服务端驱动型内容协商阶段所使用的首部清单。这个首部是必要的，它可以用来通知缓存服务器决策的依据，这样它可以进行复现，使得缓存服务器在预防将错误内容提供给用户方面发挥作用。
    
特殊值 '*' 意味着在服务端驱动型内容协商过程中同时采纳了未在首部中传递的信息来选择合适的内容。
    
Vary 首部是在 HTTP 协议的 1.1 版本中新添加的，而为了使缓存服务器恰当地工作，这个首部是必要的。缓存服务器为了能够与服务端驱动型内容协商机制协同工作，需要知道服务器选择传送内容的决策依据。这样的话，缓存服务器就可以重复该算法，直接提供恰当的内容，而不需要向服务器发送更多的请求。显然，通配符 '*' 阻碍了缓存机制发挥作用，因为缓存服务器并不知道该通配符究竟指代哪些元素。
    


# HTTP 缓存
重用已获取的资源能够有效的提升网站与应用的性能。Web 缓存能够减少延迟与网络阻塞，进而减少显示某个资源所用的时间。借助 HTTP 缓存，Web 站点变得更具有响应性。
    
缓存是一种保存资源副本并在下次请求时直接使用该副本的技术。当 web 缓存发现请求的资源已经被存储，它会拦截请求，返回该资源的拷贝，而不会去源服务器重新下载。这样带来的好处有：缓解服务器端压力，提升性能(获取资源的耗时更短了)。对于网站来说，缓存是达到高性能的重要组成部分。缓存需要合理配置，因为并不是所有资源都是永久不变的：重要的是对一个资源的缓存应截止到其下一次发生改变（即不能缓存过期的资源）。
    
虽然 HTTP 缓存不是必须的，但重用缓存的资源通常是必要的。然而常见的 HTTP 缓存只能存储 GET 响应，对于其他类型的响应则无能为力。
    
缓存的种类有很多，其大致可归为两类：私有与共享缓存。共享缓存存储的响应能够被多个用户使用。私有缓存只能用于单独用户。本文将主要介绍浏览器与代理缓存，除此之外还有网关缓存、CDN、反向代理缓存和负载均衡器等部署在服务器上的缓存方式，为站点和 web 应用提供更好的稳定性、性能和扩展性。
    

## 浏览器缓存(私有)
私有缓存只能用于单独用户。你可能已经见过浏览器设置中的“缓存”选项。浏览器缓存拥有用户通过 HTTP 下载的所有文档。这些缓存为浏览过的文档提供向后/向前导航，保存网页，查看源码等功能，可以避免再次向服务器发起多余的请求。它同样可以提供缓存内容的离线浏览。
    

## 代理缓存(共享)
共享缓存可以被多个用户使用。例如，ISP 或你所在的公司可能会架设一个 web 代理来作为本地网络基础的一部分提供给用户。这样热门的资源就会被重复使用，减少网络拥堵与延迟。


## 缓存控制 Cache-control
HTTP/1.1定义的 Cache-Control 头用来区分对缓存机制的支持情况， 请求头和响应头都支持这个属性。通过它提供的不同的值来定义缓存策略。

- `Cache-Control: no-store` 禁止进行缓存，缓存中不得存储任何关于客户端请求和服务端响应的内容。每次由客户端发起的请求都会下载完整的响应内容。

- `Cache-Control: no-cache` 强制确认缓存，此方式下，每次有请求发出时，缓存会将此请求发到服务器（译者注：该请求应该会带有与本地缓存相关的验证字段），服务器端会验证请求中所描述的缓存是否过期，若未过期（注：实际就是返回304），则缓存才使用本地缓存副本。

- `Cache-Control: public` 表示该响应可以被任何中间人（比如中间代理、CDN等）缓存，默认是private。

- `Cache-Control: private` 表示该响应是专用于某单个用户的，中间人不能缓存此响应，该响应只能应用于浏览器私有缓存中。

- `Cache-Control: must-revalidate` 一旦资源过期（比如已经超过max-age），在成功向原始服务器验证之前，缓存不能用该资源响应后续请求。

- `Cache-Control: max-age=<seconds>` 表示资源能够被缓存（保持新鲜）的最大时间。针对应用中那些不会改变的文件，通常可以手动设置一定的时长以保证缓存有效，例如图片、css、js等静态资源。
    
**禁止缓存**: `Cache-Control: no-cache, no-store, must-revalidate`
    
**缓存静态资源**: `Cache-Control:public, max-age=31536000`
    

## 缓存新鲜度
理论上来讲，当一个资源被缓存存储后，该资源应该可以被永久存储在缓存中。由于缓存只有有限的空间用于存储资源副本，所以缓存会定期地将一些副本删除，这个过程叫做缓存驱逐。另一方面，当服务器上面的资源进行了更新，那么缓存中的对应资源也应该被更新，由于HTTP是C/S模式的协议，服务器更新一个资源时，不可能直接通知客户端及其缓存，所以双方必须为该资源约定一个过期时间，在该过期时间之前，该资源（缓存副本）就是新鲜的，当过了过期时间后，该资源（缓存副本）则变为陈旧的。
    
ETag 首部字段的含义，它是资源的唯一标识。`ETag: "82e22293907ce725faf67773957acd12"`
    
当客户端发起一个请求时，缓存检索到已有一个对应的陈旧资源（缓存副本），则将缓存资源的 ETag 值放入 If-None-Match 首部，然后发给目标服务器，以此来检查该资源副本是否是依然还是算新鲜的，若服务器返回了 `304 Not Modified`（该响应不会有带有实体信息），则表示此资源副本是新鲜的，这样一来，可以节省一些带宽。若服务器通过 If-None-Match 或 If-Modified-Since判断后发现已过期，那么会带有该资源的实体内容返回。
    
`If-None-Match: "82e22293907ce725faf67773957acd12"`
    

## Connection
Connection 消息头决定当前的请求完成后，是否会关闭网络连接。如果该值是`keep-alive`，网络连接就是持久的，不会关闭，使得对同一个服务器的请求可以继续在该连接上完成。
    
```
Connection: keep-alive
Connection: close
```
    
HTTP/1.1的请求默认使用一个长连接。
    

## 带Vary头的响应
`Vary` HTTP 响应头决定了对于后续的请求头，是请求一个新的资源还是使用缓存的文件。
    
当缓存服务器收到一个请求，只有当前的请求和原始（缓存）的请求头跟缓存的响应头里的Vary都匹配，才能使用缓存的响应。


# Keep-Alive
`Keep-Alive` 是一个通用消息头，允许消息发送者暗示连接的状态，还可以用来设置超时时长和最大请求数。需要将 `Connection` 首部的值设置为  `keep-alive` 这个首部才有意义。
    
`Keep-Alive: parameters`:
- timeout：指定了一个空闲连接需要保持打开状态的最小时长（以秒为单位）。
- max：在连接关闭之前，在此连接可以发送的请求的最大值。
    
```
HTTP/1.1 200 OK
Connection: Keep-Alive
Content-Encoding: gzip
Content-Type: text/html; charset=utf-8
Date: Thu, 11 Aug 2016 15:23:13 GMT
Keep-Alive: timeout=5, max=1000
Last-Modified: Mon, 25 Jul 2016 04:32:39 GMT
Server: Apache

(body)
```
    

## Authorization
HTTP协议中的 `Authorization` 请求消息头含有服务器用于验证用户代理身份的凭证，通常会在服务器返回`401 Unauthorized` 状态码以及`WWW-Authenticate` 消息头之后在后续请求中发送此消息头。
    
username:password 字符串用 base64 方式编码(YWxhZGRpbjpvcGVuc2VzYW1l)
    
`Authorization: Basic YWxhZGRpbjpvcGVuc2VzYW1l`
    

## Content-Type
`Content-Length` 是一个实体消息首部，用来指明发送给接收方的消息主体的大小，即用十进制数字表示的body字节。
    
`Content-Length: <length>`
    

## Content-Length
`Content-Type` 实体头部用于指示资源的MIME类型。
    
```
Content-Type: text/html; charset=utf-8

Content-Type: multipart/form-data; boundary=something
```
    
- `media-type` 资源或数据的 MIME type 。
- `charset` 字符编码标准。
- `boundary` 对于多部分数据块，boundary 是必需的，其包括来自一组字符的1到70个字符。它用于封装消息的多个部分的边界。
   
```
<form action="/" method="post" enctype="multipart/form-data">
  <input type="text" name="description" value="some text">
  <input type="file" name="myFile">
  <button type="submit">Submit</button>
</form>
```
    
请求报文：
    
```
POST /foo HTTP/1.1
Content-Length: 68137
Content-Type: multipart/form-data; boundary=---------------------------974767299852498929531610575

---------------------------974767299852498929531610575
Content-Disposition: form-data; name="description" 

some text
---------------------------974767299852498929531610575
Content-Disposition: form-data; name="myFile"; filename="foo.txt" 
Content-Type: text/plain 

(content of the uploaded file foo.txt)
---------------------------974767299852498929531610575
```
    

## Content-Disposition
`Content-Disposition` 消息头指示响应的内容该以何种形式展示，是以内联的形式（即网页或者页面的一部分），还是以附件的形式下载并保存到本地（另存为窗口）。
     
在`multipart/form-data`类型的请求消息体中，`Content-Disposition` 消息头可以被用在multipart消息体的子部分中，用来给出其对应字段的相关信息。
    
### 作为响应消息中的消息头：在HTTP场景中，第一个参数或者是`inline`（默认值，表示回复中的消息体会以页面的一部分或者整个页面的形式展示）或者是`attachment`（意味着消息体应该被下载到本地；大多数浏览器会呈现一个“保存为”的对话框，将filename的值预填为下载后的文件名，假如它存在的话）。
    
```
Content-Disposition: inline
Content-Disposition: attachment
Content-Disposition: attachment; filename="filename.jpg"
```
    
### 作为form 请求消息multipart body中的消息头
    
```
Content-Disposition: form-data
Content-Disposition: form-data; name="fieldName"
Content-Disposition: form-data; name="fieldName"; filename="filename.jpg"
```
    

## Location
`Location` 首部指定的是需要将页面重新定向至的地址。一般在响应码为3xx的响应中才会有意义。
    
发送新请求，获取Location指向的新页面所采用的方法与初始请求使用的方法以及重定向的类型相关：
- `303 See Also` 始终使用 GET 方法，而 `307 Temporary Redirect` 和 `308 Permanent Redirect` 则不转变初始请求中的所使用的方法。
- `301 Moved Permanently` 和 `302 Found` 在大多数情况下不会转变初始请求中的方法。
    
状态码为上述之一的所有响应都会带有一个Location首部。
    
`Location: /index.html`
    

## Server
Server 首部包含了处理请求的源头服务器所用到的软件相关信息。
    
`Server: Apache/2.4.1 (Unix)`
    

## Host
Host 请求头指明了服务器的域名（对于虚拟主机来说），以及（可选的）服务器监听的TCP端口号。
    
HTTP/1.1 的所有请求报文中必须包含一个Host头字段。如果一个 HTTP/1.1 请求缺少 Host 头字段或者设置了超过一个的 Host 头字段，一个 `400 Bad Request` 状态码会被返回。
    
```
Host: <host>:<port>

Host: developer.cdn.mozilla.net
```
    

## Origin
请求首部字段 Origin 指示了请求来自于哪个站点。该字段仅指示服务器名称，并不包含任何路径信息。该首部用于 CORS 请求或者 POST 请求。除了不包含路径信息，该字段与 Referer 首部字段相似。
    
```
Origin: <scheme> "://" <host> [ ":" <port> ]

Origin: https://developer.mozilla.org
```
    

## Referer
Referer 首部包含了当前请求页面的来源页面的地址，即表示当前页面是通过此来源页面里的链接进入的。服务端一般使用 Referer 首部识别访问来源，可能会以此进行统计分析、日志记录以及缓存优化等。
    
```
Referer: <url>

Referer: https://developer.mozilla.org/en-US/docs/Web/JavaScript
```
    
在以下两种情况下，Referer 不会被发送：
- 来源页面采用的协议为表示本地文件的 `file:///` 或者 `data` URI；
- 当前请求页面采用的是非安全协议，而来源页面采用的是安全协议（HTTPS）。
    

## Date
Date 是一个通用首部，其中包含了报文创建的日期和时间。
    
```
Date: <day-name>, <day> <month> <year> <hour>:<minute>:<second> GMT

Date: Wed, 21 Oct 2015 07:28:00 GMT
```
    

