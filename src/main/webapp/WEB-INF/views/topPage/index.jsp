<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.ForwardConst" %>
<%@ page import="constants.AttributeConst" %>

<c:set var="actTop" value="${ForwardConst.ACT_TOP.getValue()}" />
<c:set var="actEmp" value="${ForwardConst.ACT_EMP.getValue()}" />
<c:set var="actRep" value="${ForwardConst.ACT_REP.getValue()}" />
<c:set var="actCmt" value="${ForwardConst.ACT_CMT.getValue()}" />

<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commNew" value="${ForwardConst.CMD_NEW.getValue()}" />
<c:set var="commPnchIn" value="${ForwardConst.CMD_PUNCH_IN.getValue()}" />
<c:set var="commPnchInCancel" value="${ForwardConst.CMD_PUNCH_IN_CANCEL.getValue()}" />

<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}" />
            </div>
        </c:if>
        <h2>日報管理システムへようこそ</h2>

        <c:choose>
            <c:when test="${t_punch_in == null}">
                <form method="POST"
                    action="<c:url value='?action=${actTop}&command=${commPnchIn}' />">
                    <input type="hidden" name="${AttributeConst.TOKEN.getValue()}"
                        value="${_token}" />
                    &nbsp;<button type="submit">出勤する</button>
                </form>
            </c:when>
            <c:otherwise>
                <form method="POST"
                    action="<c:url value='?action=${actTop}&command=${commPnchInCancel}' />">
                    <input type="hidden" name="${AttributeConst.TOKEN.getValue()}"
                        value="${_token}" />
                    &nbsp;<button type="submit">出勤を取り消す</button>
                </form>
            </c:otherwise>
        </c:choose>
        <br>
        <h3>【自分の日報 一覧】</h3>
        <c:choose>
            <c:when test="${show_unread == 1}">
                <p><a href="<c:url value='?action=${actTop}&command=${commIdx}&show_unread=0' />">自分の日報一覧を表示する</a></p>
            </c:when>
            <c:otherwise>
                <c:if test="${exist_unread == 1}">
                    <p><span class="font_bold">未読のコメントがついた日報があります</span></p>
                    <p><a href="<c:url value='?action=${actTop}&command=${commIdx}&show_unread=1' />">未読の付いた日報一覧を表示する</a></p>
                </c:if>
            </c:otherwise>
        </c:choose>
        <table>
            <tbody>
                <tr>
                    <th class="report_name">氏名</th>
                    <th class="report_date">日付</th>
                    <th class="report_title">タイトル</th>
                    <th class = "report_punchIn">出勤時刻</th>
                    <th class = "report_puncOut">退勤時刻</th>
                    <th class="report_action">操作</th>
                    <th class="report_comment">コメント</th>
                    <th class="report_reaction">「いいね」</th>
                </tr>
                <c:forEach var="report" items="${reports}" varStatus="status">
                    <fmt:parseDate value="${report.reportDate}" pattern="yyyy-MM-dd"
                        var="reportDay" type="date" />
                    <tr class="row${status.count % 2}">
                        <td class="report_name"><c:out
                                value="${report.employee.name}" /></td>
                        <td class="report_date"><fmt:formatDate value="${reportDay}"
                                pattern="yyyy-MM-dd" /></td>
                        <td class="report_title"><c:out value="${report.title}" /></td>
                        <td class = "report_punchIn"><fmt:formatDate value="${report.punchIn}" pattern="HH:mm" type="time"  /></td>
                        <td class = "report_punchOut"><fmt:formatDate value="${report.punchOut}" pattern="HH:mm" type="time "/></td>
                        <td class="repot_action"><a
                            href="<c:url value='?action=${actRep}&command=${commShow}&r_id=${report.id}' />">詳細を見る</a></td>
                        <td class = "report_comment">
                            <a href="<c:url value='?action=${actCmt}&command=${commIdx}&r_id=${report.id}' />">
                            コメント(${report.commentCount})<br>
                            <c:if test="${sessionScope.login_employee.id == report.employee.id &&
                                        report.isReadComment == 0}">
                                未読メッセージ
                            </c:if>
                            </a>
                        </td>
                        <td class = "report_reaction">
                            <c:choose>
                                <c:when test="${reactions[status.index] == 0}">
                                    <c:out value=" " />
                                </c:when>
                                <c:otherwise>
                                    <c:out value="${reactions[status.index]}件" />
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="pagination">
            （全 ${reports_count}件）<br>
            <c:forEach var="i" begin="1" end="${((reports_count - 1) / maxRow) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='?action=${actTop}&command=${commIdx}&page=${i}' />"><c:out value="${i}" />&nbsp;</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p><a href="<c:url value='?action=${actRep}&command=${commNew}' />">新規日報の登録</a></p>
    </c:param>
</c:import>
