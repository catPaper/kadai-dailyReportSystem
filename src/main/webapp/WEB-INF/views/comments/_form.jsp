<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="actCmt" value="${ForwardConst.ACT_CMT.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />

<c:if test="${errors != null }">
    <div id="flush_error">
        入力内容にエラーがあります。<br>
        <c:forEach var="error" items="${errors}">
            ・<c:out value="${error}" /><br>
        </c:forEach>
    </div>
</c:if>

<label for="name">氏名</label><br>
<c:out value="${sessionScope.login_employee.name}" />
<br><br>
<label for="${AttributeConst.CMT_CONTENT.getValue()}">内容</label><br>
<textarea name="${AttributeConst.CMT_CONTENT.getValue()}" rows="5" cols="45">${comment.content}</textarea>
<br><br>

<input type="hidden" name="${AttributeConst.CMT_ID.getValue()}" value="${comment.id}" />
<input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />
<button type="submit">コメントする</button>