export default[{
  "id" : 139,
  "url" : "/articles/tests1/index.html",
  "subject" : "点滴文章",
  "title" : "标题测试1",
  "content" : "标题测试1标题测试1标题测试1标题测试1标题测试1标题测试1标题测试1标题测试1标题测试1标题测试1标题测试1标题测试1 package com.jumper.jit.config; import org.springframework.context.annotation.Bean; import org.springframework.context.annotation.Configuration; import org.springframework.security.config.annotation.web.builders.HttpSecurity; import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer; import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.security.web.SecurityFilterChain; @Configuration @EnableWebSecurity public class WebSecurityConfig { @Bean public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { http.authorizeHttpRequests(request -&gt; request.requestMatchers(\"/css/**\", \"/img/**\", \"/js/**\").permitAll() .anyRequest().authenticated()) .formLogin(form -&gt; form.loginPage(\"/login\").permitAll()) .logout(LogoutConfigurer::permitAll) .csrf( csrf -&gt; csrf.ignoringRequestMatchers(\"/css/**\", \"/img/**\", \"/js/**\") .disable() ) ; return http.build(); } @Bean public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); } } "
}, {
  "id" : 110,
  "url" : "/subject/java/outputprint22/index.html",
  "subject" : "Java",
  "title" : "Java Output / Print2",
  "content" : "Print TextYou learned from the previous chapter that you can use the&nbsp;println()&nbsp;method to output values or print text in Java:ExampleGet your own Java ServerSystem.out.println(\"Hello World!\");Try it Yourself »You can add as many&nbsp;println()&nbsp;methods as you want. Note that it will add a new line for each method:ExampleSystem.out.println(\"Hello World!\"); System.out.println(\"I am learning Java.\"); System.out.println(\"It is awesome!\");Try it Yourself »Double QuotesText must be wrapped inside double quotations marks&nbsp;\"\".If you forget the double quotes, an error occurs:ExampleSystem.out.println(\"This sentence will work!\");System.out.println(This sentence will produce an error);Try it Yourself »The Print() MethodThere is also a&nbsp;print()&nbsp;method, which is similar to&nbsp;println().The only difference is that it does not insert a new line at the end of the output:ExampleSystem.out.print(\"Hello World! \"); System.out.print(\"I will print on the same line.\");Try it Yourself »Note that we add an extra space (after \"Hello World!\" in the example above) for better readability.In this tutorial, we will only use&nbsp;println()&nbsp;as it makes the code output easier to read."
}, {
  "id" : 107,
  "url" : "/subject/springboot/springsecurity/index.html",
  "subject" : "SpringBoot",
  "title" : "Spring Security Kerberos",
  "content" : "Spring Security KerberosSpring Security KerberosSpring Security KerberosSpring Security KerberosSpring Security KerberosSpring Security KerberosSpring Security Kerberos"
}, {
  "id" : 102,
  "url" : "/subject/springboot/springdata/index.html",
  "subject" : "SpringBoot",
  "title" : "Spring Data",
  "content" : "The Spring Framework provides a comprehensive programming and configuration model for modern Java-based enterprise applications - on any kind of deployment platform.A key element of Spring is infrastructural support at the application level: Spring focuses on the \"plumbing\" of enterprise applications so that teams can focus on application-level business logic, without unnecessary ties to specific deployment environments.Support Policy and MigrationFor information about minimum requirements, guidance on upgrading from earlier versions and support policies, please check out&nbsp;the official Spring Framework wiki pageFeaturesCore technologies: dependency injection, events, resources, i18n, validation, data binding, type conversion, SpEL, AOP.Testing: mock objects, TestContext framework, Spring MVC Test,&nbsp;WebTestClient.Data Access: transactions, DAO support, JDBC, ORM, Marshalling XML.Spring MVC&nbsp;and&nbsp;Spring WebFlux&nbsp;web frameworks.Integration: remoting, JMS, JCA, JMX, email, tasks, scheduling, cache and observability.Languages: Kotlin, Groovy, dynamic languages."
}, {
  "id" : 101,
  "url" : "/subject/springboot/info/index.html",
  "subject" : "SpringBoot",
  "title" : "Spring Framework",
  "content" : "The Spring Framework provides a comprehensive programming and configuration model for modern Java-based enterprise applications - on any kind of deployment platform.A key element of Spring is infrastructural support at the application level: Spring focuses on the \"plumbing\" of enterprise applications so that teams can focus on application-level business logic, without unnecessary ties to specific deployment environments.Support Policy and MigrationFor information about minimum requirements, guidance on upgrading from earlier versions and support policies, please check out&nbsp;the official Spring Framework wiki pageFeaturesCore technologies: dependency injection, events, resources, i18n, validation, data binding, type conversion, SpEL, AOP.Testing: mock objects, TestContext framework, Spring MVC Test,&nbsp;WebTestClient.Data Access: transactions, DAO support, JDBC, ORM, Marshalling XML.Spring MVC&nbsp;and&nbsp;Spring WebFlux&nbsp;web frameworks.Integration: remoting, JMS, JCA, JMX, email, tasks, scheduling, cache and observability.Languages: Kotlin, Groovy, dynamic languages."
}, {
  "id" : 114,
  "url" : "/subject/html/htmltutorial/index.html",
  "subject" : "html",
  "title" : "HTML Tutorial",
  "content" : "HTML is the standard markup language for Web pages. With HTML you can create your own Website. HTML is easy to learn - You will enjoy it! Easy Learning with HTML \"Try it Yourself\" With our \"Try it Yourself\" editor, you can edit the HTML code and view the result: Example Page Title This is a Heading This is a paragraph. Click on the \"Try it Yourself\" button to see how it works. HTML Examples In this HTML tutorial, you will find more than 200 examples. With our online \"Try it Yourself\" editor, you can edit and test each example yourself! "
}, {
  "id" : 103,
  "url" : "/subject/springboot/springcloud/index.html",
  "subject" : "SpringBoot",
  "title" : "Spring Cloud",
  "content" : "Spring Cloud provides tools for developers to quickly build some of the common patterns in distributed systems (e.g. configuration management, service discovery, circuit breakers, intelligent routing, micro-proxy, control bus, short lived microservices and contract testing). Coordination of distributed systems leads to boiler plate patterns, and using Spring Cloud developers can quickly stand up services and applications that implement those patterns. They will work well in any distributed environment, including the developer’s own laptop, bare metal data centres, and managed platforms such as Cloud Foundry.FeaturesSpring Cloud focuses on providing good out of box experience for typical use cases and extensibility mechanism to cover others.Distributed/versioned configurationService registration and discoveryRoutingService-to-service callsLoad balancingCircuit BreakersDistributed messagingShort lived microservices (tasks)Consumer-driven and producer-driven contract testingTalks and videosDistributed Applications with Spring Cloud: Spring Office HoursBeginner’s Guide To Spring CloudGetting StartedGenerating A New Spring Cloud ProjectThe easiest way to get started is visit&nbsp;start.spring.io, select your Spring Boot version and the Spring Cloud projects you want to use. This will add the corresponding Spring Cloud BOM version to your Maven/Gradle file when you generate the project.Adding Spring Cloud To An Existing Spring Boot ApplicationIf you an existing Spring Boot app you want to add Spring Cloud to that app, the first step is to determine the version of Spring Cloud you should use. The version you use in your app will depend on the version of Spring Boot you are using.The table below outlines which version of Spring Cloud maps to which version of Spring Boot.Table 1. Release train Spring Boot compatibility (see&nbsp;here&nbsp;for more detailed information).Release TrainSpring Boot Generation2023.0.x&nbsp;aka Leyton3.3.x, 3.2.x2022.0.x&nbsp;aka Kilburn3.0.x, 3.1.x (Starting with 2022.0.3)2021.0.x&nbsp;aka Jubilee2.6.x, 2.7.x (Starting with 2021.0.3)2020.0.x&nbsp;aka Ilford2.4.x, 2.5.x (Starting with 2020.0.3)Hoxton2.2.x, 2.3.x (Starting with SR5)Greenwich2.1.xFinchley2.0.xEdgware1.5.xDalston1.5.xSpring Cloud Dalston, Edgware, Finchley, Greenwich, 2020.0 (aka Ilford), 2021.0 (aka Jubilee), and 2022.0 (aka Kilburn) have all reached end of life status and are no longer supported.Bug fixes and backwards compatible features are added to each release train via a service release (SR). Once you determine which version of Spring Cloud to use, you should use the latest service release for that release train. You can find the latest service release information on our&nbsp;release notes page.Now that you know which release train to use and the latest service release for that release train you are ready to add the Spring Cloud BOM to your application."
}, {
  "id" : 121,
  "url" : "/articles/qiwen1/index.html",
  "subject" : "点滴文章",
  "title" : "厦门文化执法支队正科级干部钟劭臻婚内出轨11年",
  "content" : "2024年10月24报道，近日，厦门文化执法支队正科级干部钟劭臻婚内出轨11年，与小三结婚后被小四税务女网红的叶曼娜曝光！吃瓜内容比价多，我就不一一捋了，大家自己看吧！ 一共五楼，最后有惊喜"
}, {
  "id" : 126,
  "url" : "/subject/springboot/aspectj/index.html",
  "subject" : "SpringBoot",
  "title" : "aspectj如何操作",
  "content" : "/*弹出对话框通用函数*/ function showPublicConfirm(e, callback, message) { //1 通用确认对话框位置确定 //在当前单击位置的右边显示 let left = e.clientX + 10; let top = e.clientY + 10; //判断不要越界(因为确定按钮和关闭按钮都在右侧,所以判断右边不要越界); let windowWidth = getScrollDocumentWidth(); if (left + this.offsetWidth &gt; windowWidth) { left = windowWidth - this.offsetWidth; } publicConfirmTipContent.style.left = left + 'px'; publicConfirmTipContent.style.top = top + 'px'; publicConfirmTipContent.style.position = 'absolute'; //2 通用对话框的内容确定 publicConfirmTooltip.innerHTML = message; //3 显示对话框 publicConfirmTip.style.display = \"block\"; publicConfirmTipButtonConfirm.onclick = function () { callback(e); publicConfirmTip.style.display = 'none'; } publicConfirmTipButtonCancel.onclick = function () { publicConfirmTip.style.display = 'none'; } }"
}, {
  "id" : 106,
  "url" : "/subject/springboot/security/index.html",
  "subject" : "SpringBoot",
  "title" : "Spring Security",
  "content" : "package com.example.demo.config; @Bean public AuthenticationManager authenticationManager() { DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(); authenticationProvider.setPasswordEncoder(passwordEncoder()); authenticationProvider.setUserDetailsService(userDetailsService()); ProviderManager providerManager = new ProviderManager(authenticationProvider); providerManager.setEraseCredentialsAfterAuthentication(false);//认证后不擦除凭证 return providerManager; } @Bean public UserDetailsService userDetailsService() { UserDetails userDetails = User.builder() .passwordEncoder(x -&gt; passwordEncoder().encode(\"123\")) .username(\"user\") .roles(\"USER\") .build(); return new InMemoryUserDetailsManager(userDetails); }"
}, {
  "id" : 138,
  "url" : "/articles/spring-logbak10/index.html",
  "subject" : "点滴文章",
  "title" : "SpringBoot3 整合 Logback 日志框架100000",
  "content" : "safdadsfaadsfasspring: main: allow-circular-references: true application: name: jit datasource: url: jdbc:mariadb://localhost:4306/jit username: root password: 123456 driver-class-name: org.mariadb.jdbc.Driver jpa: properties: hibernate: format_sql: true show-sql: true data: jdbc: dialect: maria redis: host: localhost port: 6379 database: 0 password: redis123 lettuce: pool: max-active: 8 max-idle: 8 min-idle: 0 enabled: true web: resources: cache: cachecontrol: max-age: 3600 server: port: 8080 tomcat: basedir: /Users/apple/tmp/jit3 upload: save-path: /Users/apple/workplace/studyproject/springstudy/spring6/jit/frontend/ base-url: http://localhost:8081/ relative-path: img/upload/ relative-subject-path: img/upload/subject/ subject-pic: img/upload/subject/ article-pic: img/upload/ deploy: save-path: /Users/apple/workplace/studyproject/springstudy/spring6/jit/frontend/ subject: subject/ article: articles/ indexFile: js/ index-key-prefix: LocalIndex logging: file: name: ${server.tomcat.basedir}/log/jit.log logback: rollingpolicy: max-file-size: 10MB level: root: infoCopy "
}, {
  "id" : 113,
  "url" : "/subject/java/javadatatype/index.html",
  "subject" : "Java",
  "title" : "Java Data Types",
  "content" : "Print NumbersYou can also use the&nbsp;println()&nbsp;method to print numbers.However, unlike text, we don't put numbers inside double quotes:"
}, {
  "id" : 127,
  "url" : "/subject/search-engine/google/index.html",
  "subject" : "搜索引擎",
  "title" : "谷歌",
  "content" : "谷歌（Google）是美国一家专门从事互联网相关服务和产品的跨国公司。产品覆盖搜索、云计算、软件和在线广告技术。公司大部分利润来自于AdWords。它是由斯坦福大学的博士生拉里·佩奇和谢尔盖·布林创建的，他们共同拥有其股份的16％左右。起初在1998年9月4日，谷歌只是一家私人持有的公司。随后在2004年8月19日，它首次公开募股。从一开始，它就声明它的使命是“组织全世界的信息，使人人皆可访问并从中受益”，其非的口号是“不作恶”。在2006年，公司总部迁移到加州山景城。谷歌自注册成立以来就高速发展，除了核心的搜索引擎，它还开发一系列的产品，收购其他公司。它还提供在线软件，包括一个办公套件、电子邮件和社交网站。桌面产品包括网页浏览、组织和编辑照片和即时消息的应用程序等。谷歌主要产品还包括Android手机操作系统和为Chromebook上网本制定的谷歌Chrome OS浏览器。最近，它开始涉及移动通信硬件领域：与各大电子厂商的合作伙伴生产其高端的Nexus设备，并于2012年5月收购摩托罗拉移动。2012年，谷歌开始在堪萨斯城安装谷歌光纤基础设施，以促进光纤宽带服务。它在世界各地的数据中心拥有超过一百万台服务器，每天服务器要处理超过一亿个搜索请求和用户生成的数据，每天流量大约24千兆兆字节。谷歌纳斯达克股票代码为GOOG。2012年，公司收入为501.8亿美元，营业收入为127.6亿美元，利润为107.4亿美元，总资产为938亿美元，权益总额为717.2亿美元。截止2013年第1季，它拥有53891名员工。谷歌附属公司包括：AdMob、DoubleClick、摩托罗拉移动、On2 Technologies、Picnik、YouTube、Zagat。"
}, {
  "id" : 120,
  "url" : "/subject/css/combinerselector/index.html",
  "subject" : "CSS",
  "title" : "组合选择器",
  "content" : "The CSS class SelectorThe class selector selects HTML elements with a specific class attribute.To select elements with a specific class, write a period (.) character, followed by the class name.ExampleIn this example all HTML elements with class=\"center\" will be red and center-aligned:&nbsp;.center&nbsp;{&nbsp; text-align:&nbsp;center;&nbsp;&nbsp;color:&nbsp;red;}Try it Yourself »You can also specify that only specific HTML elements should be affected by a class.ExampleIn this example only  elements with class=\"center\" will be red and center-aligned:&nbsp;p.center&nbsp;{&nbsp; text-align:&nbsp;center;&nbsp;&nbsp;color:&nbsp;red;}Try it Yourself »HTML elements can also refer to more than one class."
}, {
  "id" : 104,
  "url" : "/subject/springboot/springjdbc/index.html",
  "subject" : "SpringBoot",
  "title" : "Spring Data JDBC",
  "content" : "Spring Data JDBC, part of the larger Spring Data family, makes it easy to implement JDBC based repositories. This module deals with enhanced support for JDBC based data access layers. It makes it easier to build Spring powered applications that use data access technologies.FeaturesBased on the aggregate abstractionAnnotation based entity mappingImmutable entitiesQuery derivationCustom queries via&nbsp;@Query&nbsp;annotationsMyBatis integrationLifecycle callbacksAuditingmodified_undeploy"
}, {
  "id" : 142,
  "url" : "/subject/springboot/route53/index.html",
  "subject" : "SpringBoot",
  "title" : "你你你",
  "content" : "밑그림과 같이 밑줄 친 부분을 찾습니다 원하는 도메인을 입력하고 선택을 누르니 '정확히 일치' 가 나오면 성공, 결제하러 가실 수 있습니다. 약 몇 분 후 도메인 섹션에 자신이 신청한 도메인을 보게 되는데, 상태가 '활성'이면 성공입니다."
}, {
  "id" : 117,
  "url" : "/subject/css/cssselectors/index.html",
  "subject" : "CSS",
  "title" : "css选择器",
  "content" : "CSS SelectorsCSS selectors are used to \"find\" (or select) the HTML elements you want to style.We can divide CSS selectors into five categories:Simple selectors (select elements based on name, id, class)Combinator selectors&nbsp;(select elements based on a specific relationship between them)Pseudo-class selectors&nbsp;(select elements based on a certain state)Pseudo-elements selectors&nbsp;(select and style a part of an element)Attribute selectors&nbsp;(select elements based on an attribute or attribute value)This page will explain the most basic CSS selectors."
}, {
  "id" : 136,
  "url" : "/subject/springboot/info2/index.html",
  "subject" : "SpringBoot",
  "title" : "SpringBoot3 整合 Logback 日志框架3",
  "content" : "async function moveToInServer(currentId, targetId) { let response = await fetch(`/article/moveTo/${currentId}/${targetId}`, { method: 'POST' }); return await response.json(); }a"
}, {
  "id" : 125,
  "url" : "/subject/html/updattitle/index.html",
  "subject" : "html",
  "title" : "js如何修改html的title标题",
  "content" : "        单体文章                               点滴文章         文章列表        朱海保   自由职业 专业开发 10年it魔域经验       showNavBar();    "
}, {
  "id" : 105,
  "url" : "/subject/springboot/springjpa/index.html",
  "subject" : "SpringBoot",
  "title" : "Spring Data JPA",
  "content" : "Spring Data JPA, part of the larger Spring Data family, makes it easy to easily implement JPA-based (Java Persistence API) repositories. It makes it easier to build Spring-powered applications that use data access technologies.Implementing a data access layer for an application can be quite cumbersome. Too much boilerplate code has to be written to execute the simplest queries. Add things like pagination, auditing, and other often-needed options, and you end up lost.Spring Data JPA aims to significantly improve the implementation of data access layers by reducing the effort to the amount that’s actually needed. As a developer you write your repository interfaces using any number of techniques, and Spring will wire it up for you automatically. You can even use custom finders or query by example and Spring will write the query for you!Support Policy and MigrationFor information about minimum requirements, guidance on upgrading from earlier versions and support policies, please check out&nbsp;the official Spring Data release train wiki page.Also check out the&nbsp;supported versions of Spring Data JPA in relation to Spring Boot.FeaturesfadfasdGetting Started: Sophisticated support to build repositories based on Spring and JPAVarious Query Methods: Pagination support, dynamic query execution, ability to integrate custom data access codeValidation of&nbsp;@Query&nbsp;annotated queries at bootstrap timeSupport for&nbsp;Querydsl&nbsp;predicates and type-safe JPA queriesAuditing: Transparent auditing of domain classConfiguration: Modern configuration using annotations as well as legacy support for XML-based systems."
}, {
  "id" : 137,
  "url" : "/articles/spring-logbak2/index.html",
  "subject" : "点滴文章",
  "title" : "SpringBoot3 整合 Logback 日志框架2",
  "content" : "   4.0.0  com.jumper spring6 0.0.1-SNAPSHOT ../pom.xml   com.jumper.jit jit jar 1.0.0 jit jit   org.springframework.boot spring-boot-starter-web   org.springframework.boot spring-boot-starter-validation   org.springframework.boot spring-boot-devtools   org.projectlombok lombok   org.springframework.boot spring-boot-starter-security   org.springframework.boot spring-boot-starter-thymeleaf   org.thymeleaf.extras thymeleaf-extras-springsecurity6   org.springframework.boot spring-boot-starter-data-jpa   org.mariadb.jdbc mariadb-java-client 3.4.1   org.aspectj aspectjweaver   com.alibaba fastjson 2.0.24    org.springframework.boot spring-boot-starter-data-redis   org.apache.commons commons-pool2    junit junit test   org.springframework.boot spring-boot-starter-test test   org.springframework.security spring-security-test test   org.springframework.boot spring-boot-starter-actuator       org.apache.maven.plugins maven-surefire-plugin  true -XX:+EnableDynamicAgentLoading     Copy "
}, {
  "id" : 118,
  "url" : "/subject/css/attributeselector/index.html",
  "subject" : "CSS",
  "title" : "属性选择器",
  "content" : "CSS SelectorsCSS selectors are used to \"find\" (or select) the HTML elements you want to style.We can divide CSS selectors into five categories:Simple selectors (select elements based on name, id, class)Combinator selectors&nbsp;(select elements based on a specific relationship between them)Pseudo-class selectors&nbsp;(select elements based on a certain state)Pseudo-elements selectors&nbsp;(select and style a part of an element)Attribute selectors&nbsp;(select elements based on an attribute or attribute value)This page will explain the most basic CSS selectors."
}, {
  "id" : 119,
  "url" : "/subject/css/pssuedo/index.html",
  "subject" : "CSS",
  "title" : "伪类选择器",
  "content" : "The CSS class SelectorThe class selector selects HTML elements with a specific class attribute.To select elements with a specific class, write a period (.) character, followed by the class name.ExampleIn this example all HTML elements with class=\"center\" will be red and center-aligned:&nbsp;.center&nbsp;{&nbsp; text-align:&nbsp;center;&nbsp;&nbsp;color:&nbsp;red;}Try it Yourself »You can also specify that only specific HTML elements should be affected by a class.ExampleIn this example only  elements with class=\"center\" will be red and center-aligned:&nbsp;p.center&nbsp;{&nbsp; text-align:&nbsp;center;&nbsp;&nbsp;color:&nbsp;red;}Try it Yourself »HTML elements can also refer to more than one class."
}, {
  "id" : 109,
  "url" : "/subject/java/javasyntax/index.html",
  "subject" : "Java",
  "title" : "Java Syntax",
  "content" : "Example explainedEvery line of code that runs in Java must be inside a&nbsp;class. And the class name should always start with an uppercase first letter. In our example, we named the class&nbsp;Main.Note:&nbsp;Java is case-sensitive: \"MyClass\" and \"myclass\" has different meaning.The name of the java file&nbsp;must match&nbsp;the class name. When saving the file, save it using the class name and add \".java\" to the end of the filename. To run the example above on your computer, make sure that Java is properly installed: Go to the&nbsp;Get Started Chapter&nbsp;for how to install Java. The output should be:Hello WorldThe main MethodThe&nbsp;main()&nbsp;method is required and you will see it in every Java program:public static void main(String[] args)Any code inside the&nbsp;main()&nbsp;method will be executed. Don't worry about the keywords before and after it. You will get to know them bit by bit while reading this tutorial.For now, just remember that every Java program has a&nbsp;class&nbsp;name which must match the filename, and that every program must contain the&nbsp;main()&nbsp;method.System.out.println()Inside the&nbsp;main()&nbsp;method, we can use the&nbsp;println()&nbsp;method to print a line of text to the screen:"
}, {
  "id" : 133,
  "url" : "/subject/rabbitmq/introduce12/index.html",
  "subject" : "rabbitmq",
  "title" : "简介-sstart",
  "content" : "Why RabbitMQ?RabbitMQ is a reliable and mature messaging and streaming broker, which is easy to deploy on cloud environments, on-premises, and on your local machine. It is currently used by millions worldwide.InteroperableRabbitMQ&nbsp;supports several open standard protocols, including AMQP 1.0 and MQTT 5.0. There are multiple client libraries available, which can be used with your programming language of choice, just pick one. No vendor lock-in!FlexibleRabbitMQ provides many options you can combine to define how your messages go from the publisher to one or many consumers.&nbsp;Routing,&nbsp;filtering,&nbsp;streaming,&nbsp;federation, and so on, you name it.ReliableWith the ability to&nbsp;acknowledge message delivery&nbsp;and to&nbsp;replicate messages across a cluster, you can ensure your messages are safe with RabbitMQ.Examples of common use casesHere are a few common use cases we hear about from the community or our customers. This should help you better understand what RabbitMQ is and how it can help.Decoupling servicesRPCStreamingIoTDecoupling interconnected servicesYou have a backend service that needs to send notifications to end users. There are two notification channels: emails and push notifications for the mobile application.The backend publishes the notification to two queues, one for each channel. Programs that manage emails and push notification subscribe to the queue they are interested in and handle notifications as soon as they arrive.➡ BenefitsRabbitMQ absorbs the load spike.You can do some maintenance on the notification managers without interrupting the whole service.BackendEmailPushEmail senderPush notification senderWhat about the license?Since its original release in 2007, RabbitMQ is Free and Open Source Software. In addition, Broadcom offer a range of commercial offerings.Free and Open SourceRabbitMQ is dual-licensed under the Apache License 2.0 and the Mozilla Public License 2. You have the freedom to use and modify RabbitMQ however you want.Of course, contributions are more than welcome! Whether it is through bug reports, patches, helping someone, documentation or any form of advocacy. In fact contributing is the best way to support the project! Take a look at our&nbsp;Contributors page.Commercial offeringsBroadcom offers&nbsp;enterprise-grade 24/7 support&nbsp;where you have access to the engineers making the product.In addition, a range of commercial offerings for RabbitMQ are available. These commercial offerings include all of the features of RabbitMQ, with some additional management and advanced features like&nbsp;warm standby replication&nbsp;and&nbsp;intra-cluster data compression. These features are a must for heavy workloads.For a list of the commercial offerings, take a look at the&nbsp;Ways to run Tanzu RabbitMQ and Free and Open Source RabbitMQ distributions table."
}, {
  "id" : 115,
  "url" : "/subject/html/introduction/index.html",
  "subject" : "html",
  "title" : "HTML Introduction",
  "content" : "HTML is the standard markup language for creating Web pages. What is HTML? HTML stands for Hyper Text Markup Language HTML is the standard markup language for creating Web pages HTML describes the structure of a Web page HTML consists of a series of elements HTML elements tell the browser how to display the content HTML elements label pieces of content such as \"this is a heading\", \"this is a paragraph\", \"this is a link\", etc. A Simple HTML Document Example Page Title My First Heading My first paragraph. Example Explained The declaration defines that this document is an HTML5 document The element is the root element of an HTML page The element contains meta information about the HTML page The element specifies a title for the HTML page (which is shown in the browser's title bar or in the page's tab) The  element defines the document's body, and is a container for all the visible contents, such as headings, paragraphs, images, hyperlinks, tables, lists, etc. The  element defines a large heading The  element defines a paragraph What is an HTML Element? An HTML element is defined by a start tag, some content, and an end tag:  Content goes here...  The HTML element is everything from the start tag to the end tag: My First Heading My first paragraph. Start tag Element content End tag  My First Heading   My first paragraph.   none none Note: Some HTML elements have no content (like the  element). These elements are called empty elements. Empty elements do not have an end tag! "
}, {
  "id" : 124,
  "url" : "/subject/springboot/gatewaay/index.html",
  "subject" : "SpringBoot",
  "title" : "Spring 网关",
  "content" : "1.网关介绍如果没有网关，难道不行吗？功能上是可以的，我们直接调用提供的接口就可以了。那为什么还需要网关？因为网关的作用不仅仅是转发请求而已。我们可以试想一下，如果需要做一个请求认证功能，我们可以接入到 API 服务中。但是倘若后续又有服务需要接入，我们又需要重复接入。这样我们不仅代码要重复编写，而且后期也不利于维护。由于接入网关后，网关将转发请求。所以在这一层做请求认证，天然合适。这样这需要编写一次代码，在这一层过滤完毕，再转发给下面的 API。所以 API 网关的通常作用是完成一些通用的功能，如请求认证，请求记录，请求限流，黑白名单判断等。API网关是一个服务器，是系统的唯一入口。API网关方式的核心要点是，所有的客户端和消费端都通过统一的网关接入微服务，在网关层处理所有的非业务功能。通常，网关提供REST/HTTP的访问API。2.Spring Cloud Gateway介绍Spring Cloud Gateway是Spring Cloud的新一代API网关，基于WebFlux框架实现，它旨在为微服务架构提供一种简单而有效的统一的API路由管理方式。Spring Cloud Gateway作为Spring Cloud生态系统中的网关，目标是替代Netflix ZUUL，具有更好的性能、更强的扩展性、以及更丰富的功能特性，其不仅提供统一的路由方式，并且基于Filter链的方式提供了网关基本的功能，例如:安全，监控/埋点，限流等。3.Spring Cloud Gateway的特性基于Spring Framework 5， Project Reactor和Spring Boot 2.0动态路由：能够匹配任何请求属性可以对路由指定 Predicate 和 Filter集成Hystrix断路器集成Spring Cloud DiscoveryClient 服务发现功能易于编写的Predicate和Filter请求限流支持路径重写4.Spring Cloud Gateway的三大核心概念路由(Route)：&nbsp;路由是网关最基础的部分，路由信息由一个ID，一个目标URI，一组断言和过滤器组成。路由断言Predicate用于匹配请求，过滤器Filter用于修改请求和响应。如果断言为true，则说明请求URI和配置匹配，则执行路由。spring: cloud: gateway: # 定义多个路由 routes: # 一个路由route的id - id: path_route # 该路由转发的目标URI uri: https://example.org # 路由条件集合 predicates: - Path=/test/** # 过滤器集合 filters: - AddRequestHeader=X-Request-Id, 1024 - AddRequestParameter=color, redCopy断言(Predicate)：&nbsp;参考Java8中的断言Predicate，用于实现请求匹配逻辑，例如匹配路径、请求头、请求参数等。请求与断言匹配则执行该路由。过滤器(Filter)：&nbsp;指的是Spring框架中GatewayFilter的实例，使用过滤器，可以在请求被路由前后对请求进行修改。5.Gateway工作流程客户端向Spring Cloud Gateway发出请求，然后在Gateway Handler Mapping中找到与请求相匹配的路由，将其发送到Gateway Web Handler。Handler再通过指定的过滤器链来对请求进行过滤处理，最后发送到我们实际的服务执行业务逻辑，然后返回。过滤器链被虚线分隔，是因为过滤器既可以在转发请求前拦截请求，也可以在请求处理之后对响应进行拦截处理。推荐一个开源免费的 Spring Boot 实战项目："
}, {
  "id" : 140,
  "url" : "/subject/java/tesst2/index.html",
  "subject" : "Java",
  "title" : "标题测试2",
  "content" : "test标题测试2标题测试2标题测试2标题测试2标题测试2"
}, {
  "id" : 131,
  "url" : "/subject/springboot/spring-redis/index.html",
  "subject" : "SpringBoot",
  "title" : "Spring Redis",
  "content" : "Redis&nbsp;是一款开源的，使用 C 开发的高性能内存 Key/Value 数据库，支持 String、Set、Hash、List、Stream 等等数据类型。它被广泛用于缓存、消息队列、实时分析、计数器和排行榜等场景。基本上是当代应用中必不可少的软件！Spring Boot 对 Redis 提供了开箱即用的组件：spring-boot-starter-data-redis。通过这个 starter，我们只需要几行简单的配置就可以快速地在 Spring Boot 中整合、使用 Redis。除了&nbsp;spring-boot-starter-data-redis&nbsp;外，还添加了&nbsp;commons-pool2&nbsp;依赖，是因为我们需要使用到连接池。配置属性只需要在&nbsp;application.yaml | properties&nbsp;中配置如下常用的基本属性即可注意，如果你使用的是 spring boot 2.x，上述配置的命名空间应该是&nbsp;spring.redis&nbsp;而不是&nbsp;spring.data.redis。更多完整的配置属性，请参阅&nbsp;官方文档。使用 Jedis 客户端Spring Data Redis 默认使用&nbsp;Lettuce&nbsp;作为 Redis 客户端。官方还对&nbsp;Jedis&nbsp;提供了支持，你可以根据你的喜好进行选择。要替换为 Jedis，首先需要从&nbsp;spring-boot-starter-data-redis&nbsp;排除&nbsp;lettuce&nbsp;，并且添加&nbsp;jedis&nbsp;依赖"
}, {
  "id" : 122,
  "url" : "/articles/qiwen2/index.html",
  "subject" : "点滴文章",
  "title" : "单位来了一位美国访问学者",
  "content" : "单位来了一位美国访问学者，闲谈中他说他们学院有两个华裔禽兽，过去十几年来，一个做了中国的千人学者（经中央党校培训一周后上岗，自动给中国绿卡），另一个做了中国的（李嘉诚）长江教授。发表论文中美两边地址都写，基金两边申请，工资两边都拿，内外通吃，如鱼得水，滋润得很。四年前被查（FBI到家里搜查，铐走），涉及往中国转移美国的知识产权，其中甚至有军工内容。一个开除，一个坐牢，都被税务部门罚款了。"
}, {
  "id" : 134,
  "url" : "/subject/rabbitmq/getstart/index.html",
  "subject" : "rabbitmq",
  "title" : "Getting Started",
  "content" : "RabbitMQ TutorialsThese tutorials cover the basics of creating messaging applications using RabbitMQ.You need to have the RabbitMQ server installed to go through the tutorials, please see the&nbsp;installation guide&nbsp;or use the&nbsp;community Docker image.Executable versions of these tutorials&nbsp;are open source, as is&nbsp;this website.There are two groups of tutorials:RabbitMQ queuesRabbitMQ streamsnoteYou can use these tutorials with any versions of RabbitMQ. That said, we recommend to familiarize yourself with the latest version! For the stream tutorials, you need to use RabbitMQ 3.9.0 or later.Queue tutorialsThis section covers the default RabbitMQ protocol, AMQP 0-9-1.1. \"Hello World!\"The simplest thing that does&nbsp;somethingPQueueCPythonJavaRubyPHPC#JavaScriptGoElixirObjective-CSwiftSpring AMQP2. Work QueuesDistributing tasks among workers (the&nbsp;competing consumers pattern)PQueueC₁C₂PythonJavaRubyPHPC#JavaScriptGoElixirObjective-CSwiftSpring AMQP3. Publish/SubscribeSending messages to many consumers at oncePXQ₁Q₂C₁C₂PythonJavaRubyPHPC#JavaScriptGoElixirObjective-CSwiftSpring AMQP4. RoutingReceiving messages selectivelyaabcPXQ₁Q₂C₁C₂PythonJavaRubyPHPC#JavaScriptGoElixirObjective-CSwiftSpring AMQP5. TopicsReceiving messages based on a pattern (topics)*.a.**.*.bc.#PXQ₁Q₂C₁C₂PythonJavaRubyPHPC#JavaScriptGoElixirObjective-CSwiftSpring AMQP6. RPCRequest/reply pattern&nbsp;examplerequestreplyClientServerRPCReplyPythonJavaRubyPHPC#JavaScriptGoElixirSpring AMQP7. Publisher ConfirmsReliable publishing with publisher confirmsJavaCPHP"
}, {
  "id" : 108,
  "url" : "/subject/java/javatutorial/index.html",
  "subject" : "Java",
  "title" : "Java Tutorial",
  "content" : "Java QuizTest your Java skills with a quiz.Start Java QuizLearn by ExamplesLearn by examples! This tutorial supplements all explanations with clarifying examples.See All Java ExamplesMy LearningTrack your progress with the free \"My Learning\" program here at W3Schools.Log in to your account, and start earning points!This is an optional feature. You can study at W3Schools without using My Learning."
}, {
  "id" : 123,
  "url" : "/subject/springboot/mongo/index.html",
  "subject" : "SpringBoot",
  "title" : "Spring mongo",
  "content" : "DescriptionThe&nbsp;parseInt&nbsp;function&nbsp;converts its first argument to a string, parses that string, then returns an integer or&nbsp;NaN.If not&nbsp;NaN, the return value will be the integer that is the first argument taken as a number in the specified&nbsp;radix. (For example, a&nbsp;radix&nbsp;of&nbsp;10&nbsp;converts from a decimal number,&nbsp;8&nbsp;converts from octal,&nbsp;16&nbsp;from hexadecimal, and so on.)The&nbsp;radix&nbsp;argument is&nbsp;converted to a number. If it's unprovided, or if the value becomes 0,&nbsp;NaN&nbsp;or&nbsp;Infinity&nbsp;(undefined&nbsp;is coerced to&nbsp;NaN), JavaScript assumes the following:If the input&nbsp;string, with leading whitespace and possible&nbsp;+/-&nbsp;signs removed, begins with&nbsp;0x&nbsp;or&nbsp;0X&nbsp;(a zero, followed by lowercase or uppercase X),&nbsp;radix&nbsp;is assumed to be&nbsp;16&nbsp;and the rest of the string is parsed as a hexadecimal number.If the input&nbsp;string&nbsp;begins with any other value, the radix is&nbsp;10&nbsp;(decimal).Note:&nbsp;Other prefixes like&nbsp;0b, which are valid in&nbsp;number literals, are treated as normal digits by&nbsp;parseInt().&nbsp;parseInt()&nbsp;does&nbsp;not&nbsp;treat strings beginning with a&nbsp;0&nbsp;character as octal values either. The only prefix that&nbsp;parseInt()&nbsp;recognizes is&nbsp;0x&nbsp;or&nbsp;0X&nbsp;for hexadecimal values — everything else is parsed as a decimal value if&nbsp;radix&nbsp;is missing.&nbsp;Number()&nbsp;or&nbsp;BigInt()&nbsp;can be used instead to parse these prefixes.If the radix is&nbsp;16,&nbsp;parseInt()&nbsp;allows the string to be optionally prefixed by&nbsp;0x&nbsp;or&nbsp;0X&nbsp;after the optional sign character (+/-).If the radix value (coerced if necessary) is not in range [2, 36] (inclusive)&nbsp;parseInt&nbsp;returns&nbsp;NaN.For radices above&nbsp;10, letters of the English alphabet indicate numerals greater than&nbsp;9. For example, for hexadecimal numbers (base&nbsp;16),&nbsp;A&nbsp;through&nbsp;F&nbsp;are used. The letters are case-insensitive.parseInt&nbsp;understands exactly two signs:&nbsp;+&nbsp;for positive, and&nbsp;-&nbsp;for negative. It is done as an initial step in the parsing after whitespace is removed. If no signs are found, the algorithm moves to the following step; otherwise, it removes the sign and runs the number-parsing on the rest of the string.If&nbsp;parseInt&nbsp;encounters a character that is not a numeral in the specified&nbsp;radix, it ignores it and all succeeding characters and returns the integer value parsed up to that point. For example, although&nbsp;1e3&nbsp;technically encodes an integer (and will be correctly parsed to the integer&nbsp;1000&nbsp;by&nbsp;parseFloat()),&nbsp;parseInt(\"1e3\", 10)&nbsp;returns&nbsp;1, because&nbsp;e&nbsp;is not a valid numeral in base 10. Because&nbsp;.&nbsp;is not a numeral either, the return value will always be an integer.If the first character cannot be converted to a number with the radix in use,&nbsp;parseInt&nbsp;returns&nbsp;NaN. Leading whitespace is allowed.For arithmetic purposes, the&nbsp;NaN&nbsp;value is not a number in any radix. You can call the&nbsp;Number.isNaN&nbsp;function to determine if the result of&nbsp;parseInt&nbsp;is&nbsp;NaN. If&nbsp;NaN&nbsp;is passed on to arithmetic operations, the operation result will also be&nbsp;NaN.Because large numbers use the&nbsp;e&nbsp;character in their string representation (e.g.&nbsp;6.022e23&nbsp;for 6.022 × 1023), using&nbsp;parseInt&nbsp;to truncate numbers will produce unexpected results when used on very large or very small numbers.&nbsp;parseInt&nbsp;should&nbsp;not&nbsp;be used as a substitute for&nbsp;Math.trunc().To convert a number to its string literal in a particular radix, use&nbsp;thatNumber.toString(radix).Because&nbsp;parseInt()&nbsp;returns a number, it may suffer from loss of precision if the integer represented by the string is&nbsp;outside the safe range. The&nbsp;BigInt()&nbsp;function supports parsing integers of arbitrary length accurately, by returning a&nbsp;BigInt."
}, {
  "id" : 116,
  "url" : "/subject/html/htmleditors/index.html",
  "subject" : "html",
  "title" : "HTML Editors",
  "content" : "A simple text editor is all you need to learn HTML.Learn HTML Using Notepad or TextEditWeb pages can be created and modified by using professional HTML editors.However, for learning HTML we recommend a simple text editor like Notepad (PC) or TextEdit (Mac).We believe that using a simple text editor is a good way to learn HTML.Follow the steps below to create your first web page with Notepad or TextEdit.Step 1: Open Notepad (PC)Windows 8 or later:Open the&nbsp;Start Screen&nbsp;(the window symbol at the bottom left on your screen). Type&nbsp;Notepad.Windows 7 or earlier:Open&nbsp;Start&nbsp;&gt;&nbsp;Programs &gt;&nbsp;Accessories &gt;&nbsp;NotepadStep 1: Open TextEdit (Mac)Open&nbsp;Finder &gt; Applications &gt; TextEditAlso change some preferences to get the application to save files correctly.&nbsp;In&nbsp;Preferences &gt; Format &gt;&nbsp;choose&nbsp;\"Plain Text\"Then under \"Open and Save\", check the box that says \"Display HTML files as HTML code instead of formatted text\".Then open a new document to place the code.Step 2: Write Some HTMLWrite or copy the following HTML code into Notepad:"
}, {
  "id" : 135,
  "url" : "/subject/kafka/introduce2/index.html",
  "subject" : "kafka 연습",
  "title" : "kafka 소개",
  "content" : "初识 Kafka什么是 KafkaKafka 是由 Linkedin 公司开发的，它是一个分布式的，支持多分区、多副本，基于 Zookeeper 的分布式消息流平台，它同时也是一款开源的基于发布订阅模式的消息引擎系统。Kafka 的基本术语消息：Kafka 中的数据单元被称为消息，也被称为记录，可以把它看作数据库表中某一行的记录。批次：为了提高效率， 消息会分批次写入 Kafka，批次就代指的是一组消息。主题：消息的种类称为 主题（Topic）,可以说一个主题代表了一类消息。相当于是对消息进行分类。主题就像是数据库中的表。分区：主题可以被分为若干个分区（partition），同一个主题中的分区可以不在一个机器上，有可能会部署在多个机器上，由此来实现 kafka 的伸缩性，单一主题中的分区有序，但是无法保证主题中所有的分区有序生产者：向主题发布消息的客户端应用程序称为生产者（Producer），生产者用于持续不断的向某个主题发送消息。消费者：订阅主题消息的客户端程序称为消费者（Consumer），消费者用于处理生产者产生的消息。消费者群组：生产者与消费者的关系就如同餐厅中的厨师和顾客之间的关系一样，一个厨师对应多个顾客，也就是一个生产者对应多个消费者，消费者群组（Consumer Group）指的就是由一个或多个消费者组成的群体。偏移量：偏移量（Consumer Offset）是一种元数据，它是一个不断递增的整数值，用来记录消费者发生重平衡时的位置，以便用来恢复数据。broker: 一个独立的 Kafka 服务器就被称为 broker，broker 接收来自生产者的消息，为消息设置偏移量，并提交消息到磁盘保存。broker 集群：broker 是集群 的组成部分，broker 集群由一个或多个 broker 组成，每个集群都有一个 broker 同时充当了集群控制器的角色（自动从集群的活跃成员中选举出来）。副本：Kafka 中消息的备份又叫做 副本（Replica），副本的数量是可以配置的，Kafka 定义了两类副本：领导者副本（Leader Replica） 和 追随者副本（Follower Replica），前者对外提供服务，后者只是被动跟随。重平衡：Rebalance。消费者组内某个消费者实例挂掉后，其他消费者实例自动重新分配订阅主题分区的过程。Rebalance 是 Kafka 消费者端实现高可用的重要手段。"
}]