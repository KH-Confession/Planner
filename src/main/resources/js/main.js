$(window).ready(function () {
  requestPlanList()
})
 const today = new Date();
 const todayDate = today.getFullYear() + '-'
   + String(today.getMonth() + 1).padStart(2, '0') + '-'
   + String(today.getDate()).padStart(2, '0')
 $(window).ready(function () {
   requestPlanList()

 })


function requestPlanList() {
  $.ajax({
    url: "/plan/list",
    type: "GET",
    async: false,
    dataType: "json",
    success: renderMainPage,
    error: function (xhr) {
      if (xhr.status === 401) {
        alert("로그인이 필요한 페이지입니다.")
        window.location.href = "user/login.html";
      }
    }
  })
}

function renderMainPage(response) {
  renderMyInfo(response.nickname);
  renderCompletePlan(response.planList);
  renderDateInput();
  renderPlanList(response.planList);

  planList.on("click", ".detailPlan", getDetailList)
}

function renderMyInfo(nickname) {
  $("#nickname").text(nickname);
}

function renderCompletePlan(planList) {
  let total = planList.length;
  let completeCount = 0;
  for (let i = 0; i < planList.length; i++) {
    completeCount += planList[i].complete === 'Y' ? 1 : 0;
  }
  $("#completedPlan").text(completeCount);
  $("#notCompletedPlan").text(Number(total - completeCount));
}

function renderDateInput() {
  $("#startDate").attr("min", todayDate).val(todayDate);
  $("#endDate").attr("min", todayDate).val(todayDate);
  $("#remindAlarmDateCheck").on("change", function () {
    let checked = $(this).prop("checked");
    $("#remindAlarmDate").attr({
      "readOnly": !checked,
      "min": todayDate
    }).val(checked ? todayDate : "");
  });
}

function renderPlanList(planList) {
  let target = $("#plannersEle");
  target.empty();
  $.each(planList, function (index, plan) {
    target.append(
      $("<li>").append(
        $("<div>").addClass("plannerItem").append(
          $("<div style='display: flex'>").append(
            $("<input>").prop({
              "type": "checkbox",
              "name": "complete",
              "class": "comRadio",
              "onchange": `completePlanner(${plan.planId})`
            }).attr(`${plan.complete === 'Y' ? "checked" : "notChecked"}`, "true")
          ).append(
            $("<strong>").addClass("detailPlan")
              .attr({
                "data-bs-toggle": "offcanvas",
                "data-bs-target": "#detailPlan"
              })
              .text(plan.title)
              .data("plan", plan)
          )
        ).append(
          $("<div class='plannerDate'>").append($("<span>").text(plan.endDate))
        ).append(
          $("<span class='deleteButton'>").on("click", deletePlanner(plan.planId)).append("<b>X</b>")
        )
      )
    )
  })
}

function completePlanner(planId) {
  /*
  ajax로 update api 호출해서 complete 수정
      성공시 반영
          title에 취소줄 반영, class="plan-complete" 이용
          완료할 작업 수 / 완료한 작업수 반영, id=notCompletedPlan / id=completedPlan 이용
      실패시 오류메세지
  */

}

function deletePlanner(planId) {
  /*
  ㄹㅇ 삭제할건지 물어보고
  ajax로 delete api 호출해서 Plan 삭제
      성공시 반영
          단순히 main.html 페이지 reload
      실패시 오류메세지
  */
}

function moveToDetailPage() {
  window.location.href = 'https://www.google.com';
}
