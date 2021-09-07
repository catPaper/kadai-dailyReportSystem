<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="actCmt" value="${ForwardConst.ACT_CMT.getValue()}" />
<c:set var="commUpd" value="${ForwardConst.CMD_UPDATE.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />

<c:import url = "../layout/app.jsp">
    <c:param name="content">
        <h2>コメント 編集ページ</h2>
        <form method="POST" action="<c:url value='?action=${actCmt}&command=${commUpd}' />">
            <c:import url="_form.jsp" />
        </form>

        <p><a href="<c:url value='?action=${actCmt}&command=${commIdx}' />">一覧に戻る</a></p>
    </c:param>
</c:import>