<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Part</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css" type="text/css"/>
    <script type="text/javascript" src="resources/js/script.js"></script>

    <link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/themes/smoothness/jquery-ui.css">
    <script src="http://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="http://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <script>
        $(document).ready(function () {
            $(".myDateInput").datepicker({
                dateFormat: 'M dd, yy',
                monthNamesShort: ["Jan", "Feb", "Mar", "Apr", "Maj", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"],
                beforeShow: function (input, inst) {
                    setTimeout(function () {
                        inst.dpDiv.css({
                            top: $(".myDateInput").offset().top + 50
                        });
                    }, 0);
                }
            });
        });
    </script>
</head>
<body>
<div class="content-box">
    <form id="form" name="form" action="?action=filter" method="post" class="submission-form">
        <dl id="title">Filter</dl>
        <dl>
            <dt>PN</dt>
            <dd><input type="text" name="number" value="${param.number}"></dd>
        </dl>
        <dl>
            <dt>Part Name</dt>
            <dd><input type="text" name="name" value="${param.name}"></dd>
        </dl>
        <dl>
            <dt>Vendor</dt>
            <dd><input type="text" name="vendor" value="${param.vendor}"></dd>
        </dl>
        <dl>
            <dt>Qty</dt>
            <dd><input type="text" name="qty" value="${param.qty}"></dd>
        </dl>

        <dl>
            <dt>Shipped</dt>
            <span>after</span>
            <dd><input class="myDateInput" placeholder="MMM dd, yyyy" type="text" name="shippedAfter"
                       value="${param.shippedAfter}"></dd>
            <span>before</span>
            <dd><input class="myDateInput" placeholder="MMM dd, yyyy" type="text" name="shippedBefore"
                       value="${param.shippedBefore}"></dd>
        </dl>

        <dl>
            <dt>Received</dt>
            <span>after</span>
            <dd><input class="myDateInput" placeholder="MMM dd, yyyy" type="text" name="receivedAfter"
                       value="${param.receivedAfter}"></dd>
            <span>before</span>
            <dd><input class="myDateInput" placeholder="MMM dd, yyyy" type="text" name="receivedBefore"
                       value="${param.receivedBefore}"></dd>
        </dl>

        <div id="filter">
            <button type="submit">Filter</button>
        </div>

    </form>
</div>

<div>
    <table class="sortable" id="myTable" border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th width="10%">PN</th>
            <th width="25%">Part Name</th>
            <th width="25%">Vendor</th>
            <th width="10%">Qty</th>
            <th width="15%">Shipped</th>
            <th width="15%">Received</th>
        </tr>
        </thead>
        <tbody id="body">
        <c:forEach var="part" items="${parts}">
            <tr>
                <td>${part.number}</td>
                <td>${part.name}</td>
                <td>${part.vendor}</td>
                <td>${part.qty}</td>
                <td sorttable_customkey="${part.shipped}"><fmt:formatDate value="${part.shipped}"
                                                                          pattern="MMM dd, yyyy"/></td>
                <td sorttable_customkey="${part.receive}"><fmt:formatDate value="${part.receive}"
                                                                          pattern="MMM dd, yyyy"/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

</body>
</html>