<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title th:text="${title}">${title}</title>
<!-スタイルシートを定義したCSSファイルを読み込む。 -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/app/css/styles.css" type="text/css">
</head>
<body>
    <h1>Todo List</h1>
    <div id="todoForm">
        <!-- (1) -->
        <t:messagesPanel />

        <!-- 新規作成処理用のformを表示する。action属性には新規作成処理を実行するためのURL(<contextPath>/todo/create)を指定 -->
        <!--modelAttribute属性には、ControllerでModelに追加したFormの名前を指定 -->
        
        <form:form
           action="${pageContext.request.contextPath}/todo/create"
            method="post" modelAttribute="todoForm">
         <table>
         <tr>
         　　　<th>グループ</th>
            <td><form:input path="todoGroup" /></td>
         </tr>
         <tr>
         　　　<th>タイトル</th>         
            <td><form:input path="todoTitle" /><form:errors path="todoTitle" /><!--入力エラーがあった場合に表示する --></td><!-- modelAttribute属性に指定したFormのプロパティ名と、path属性の値が一致 -->
         </tr>
         <tr>
         　　　<th>内容</th>                              
            <td><form:input path="contents" /></td>
         </tr>
         <tr>
         　　　<th>リミット</th>                  
            <td><form:input path="timelimit" /></td>           
         </tr>
          <tr>
          <td colspan="1">
            <form:button>作成</form:button>
          </td>  
         </tr>
         <table>
        </form:form>
    </div>
    <hr />
    <div id="todoList">
        <ul>
            <c:forEach items="${todos}" var="todo">
                <li><c:choose>
                        <c:when test="${todo.finished}">
                            <span class="strike">
                            ${f:h(todo.todoTitle)}
                            </span>
                        </c:when>
                        <c:otherwise>
                            ${f:h(todo.todoTitle)}
                            <!-- TODOが未完了の場合は、TODOを完了させるためのリクエストを送信するformを表示する -->
                            <form:form
                                action="${pageContext.request.contextPath}/todo/finish"
                                method="post"
                                modelAttribute="todoForm"
                                cssClass="inline">
                                <!-- リクエストパラメータとしてtodoIdを送信する.
                                value属性に値を設定する場合も、 必ずf:h()関数でHTMLエスケープする -->
                                <form:hidden path="todoId"
                                    value="${f:h(todo.todoId)}" />
                                <form:button>Finish</form:button>
                            </form:form>
                         </c:otherwise>
                    </c:choose>
                    <!--削除処理用のform -->
                    <form:form
                        action="${pageContext.request.contextPath}/todo/delete"
                        method="post" modelAttribute="todoForm"
                        cssClass="inline">
                        <!-- リクエストパラメータとしてtodoIdを送信 -->
                        <form:hidden path="todoId"
                            value="${f:h(todo.todoId)}" />
                        <form:button>Delete</form:button>
                    </form:form>                    
                  </li>
            </c:forEach>
        </ul>
    </div>
    <table border="1" width="30%">
         <thead>
    	<tr valign="middle">    	
        <th>全件</th>
        <th>未完了件数</th>
		</tr>
	</thead>
    <tbody>
      <tr valign="middle">
 		<td>${viewcount}</td>
		<td>${allcount}</td>
       </tr>
    </tbody>
	</table>
    <b>当サイトの内容、テキスト、画像等の無断転載・無断使用を固く禁じます。<br>
    また、まとめサイト等への引用を厳禁致します。<br>
    お問い合わせはメールでご連絡をお願い致します。</b><br>
    
</body>
</html>