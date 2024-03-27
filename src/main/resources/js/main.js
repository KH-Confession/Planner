const today = new Date();
const todayDate = today.getFullYear() + '-'
  + String(today.getMonth() + 1).padStart(2, '0') + '-'
  + String(today.getDate()).padStart(2, '0')

$(window).ready(function () {
  requestPlanList()

  // 이벤트
  $("#save").on("click", requestCreatePlan);
  $("#endDateASC").on("click", () => sortBy("endDate", "ASC"));
  $("#endDateDESC").on("click", () => sortBy("endDate", "DESC"));
  $("#startDateASC").on("click", () => sortBy("startDate", "ASC"));
  $("#startDateDESC").on("click", () => sortBy("startDate", "DESC"));

  // Offcavans 이벤트
  $("#plannersEle").on("click", ".detailPlan", getDetailList);
})

function requestPlanList() {
  $.ajax({
    url: "/plans",
    type: "GET",
    async: false,
    dataType: "json",
    success: renderMainPage,
    error: function (xhr) {
      if (xhr.status === 401) {
        alert("로그인이 필요한 페이지입니다.")
        window.location.href = "/user/signin.html";
      }
    }
  })
}

function renderMainPage(response) {
  renderMyInfo(response.nickname);
  renderCompletePlan(response.planList);
  renderDateInput();
  renderPlanList(response.planList);
}

function renderMyInfo(nickname) {
  $("#nickname").text(nickname);
}

function renderCompletePlan(planList) {
  let completeCount = 0;
  for (let i = 0; i < planList.length; i++) {
    completeCount += planList[i].complete === 'Y' ? 1 : 0;
  }
  $("#completedPlan").text(completeCount);
  $("#notCompletedPlan").text(Number(planList.length - completeCount));
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
      $("<li>").attr("id", `plan-${plan.planId}`).append(
        $("<div>").addClass("plannerItem").append(
          $("<div style='display: flex'>").append(
            $("<input>").prop({
              "type": "checkbox",
              "name": "complete",
              "class": "comRadio",
            }).attr(`${plan.complete === 'Y' ? "checked" : "notChecked"}`, "true")
              .on("change", () => requestComplete(plan.planId))
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
          $("<span class='deleteButton'>").on("click", () => requestDelete(plan.planId)).append("<b>X</b>")
        )
      )
    )
  })
}

function requestCreatePlan() {
  let title = $("#title").val();
  let startDate = $("#startDate").val() ?? todayDate;
  let endDate = $("#endDate").val() ?? todayDate;
  let remindAlarmDate = $("#remindAlarmDate").val();

  let requestBody = {
    "title": title,
    "startDate": startDate,
    "endDate": endDate,
    "remindAlarmDate": remindAlarmDate
  }

  $.ajax({
    url: "/plans",
    type: "POST",
    data: requestBody,
    dataType: "json",
    success: function () {
      location.reload();
    },
    error: function (xhr) {
      console.log(xhr)
      if (xhr.status === 401) {
        alert("로그인이 필요한 페이지 입니다.");
        window.location = "/user/signin.html";
      } else if (xhr.status === 400) {
        alert(xhr.responseJSON.message);
      }
    }
  })
}

function sortBy(targetData, order) {
  let planListDiv = $("#plannersEle");
  let li = planListDiv.children();
  let planList = [];
  $(li).each(function () {
    planList.push($(this).find("strong").data("plan"));
  })
  console.log(planList);

  planList.sort(function (a, b) {
    let targetDataA = a[targetData];
    let targetDataB = b[targetData];
    let timeA = new Date(targetDataA).getTime()
    let timeB = new Date(targetDataB).getTime()

    return timeA === timeB ? a["planId"] - b["planId"]
      : order === "ASC" ? timeA - timeB : timeB - timeA;
  })
  renderPlanList(planList);
}

function requestComplete(planId) {
  let formCheckInput = $(`#plan-${planId} .comRadio`);
  let complete = $(formCheckInput).prop("checked");
  $.ajax({
    url: `/plan/${planId}`,
    type: "PATCH",
    data: JSON.stringify({ "complete": (complete ? "Y" : "N") }),
    contentType: "application/json",
    success: function () {
      location.reload()
    },
    error: function (xhr) {
      if (xhr.status === 401) {
        alert("로그인이 필요한 페이지 입니다.");
        window.location.href = "/user/signin.html";
      } else {
        alert("invalid error");
        location.reload();
      }
    }
  })

}

function requestDelete(planId) {
  if (confirm("정말 삭제하시겠습니까?")) {
    $.ajax({
      url: `/plan/${planId}`,
      type: "DELETE",
      success: function () {
        location.reload();
      },
      error: function (xhr) {
        if (xhr.status === 401) {
          alert("로그인이 필요한 페이지 입니다.");
          window.location.href = "/user/signin.html";
        } else {
          alert("invalid error");
          location.reload();
        }
      }
    })
  }
}

function moveToDetailPage() {
  window.location.href = 'https://www.google.com';
}
