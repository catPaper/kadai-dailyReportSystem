<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="actRep" value="${ForwardConst.ACT_REP.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}" />

<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:import url="_index.jsp" />
        <br><br>

        <p><a href="<c:url value='?action=${actRep}&command=${commShow}&r_id=${sessionScope.comment_report.id}' />">日報の詳細ページへ</a></p>
        <p><a href="<c:url value='?action=${actRep}&command=${commIdx}' />">一覧に戻る</a></p>

    </c:param>
</c:import>