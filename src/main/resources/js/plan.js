
window.onload = function() {
	$.ajax({
		url : "/plan/list",
		type : "get",
		dataType : "json",
		success : function(response) {
			let planList = response.planList; 
			let count = 0;
			for(let i=0; i<planList.length; i++) {
				if(planList[i].complete === 'N') {
					count++;
				}
			}
			
			document.getElementById("not-completed").innerHTML = count;
			document.getElementById("completed").innerHTML = planList.length - count;
			
			let planTitle = null;
			let planEndDate = null;
			let childNodes = '';
			for(let i=0; i<planList.length; i++) {
				planTitle = planList[i].title;
				planEndDate = planList[i].endDate;
				
				childNodes +=
			`<li>
				<div class="plannerItem">
				<div style="display: flex;">
					<input type="checkbox" name="complete" value="complete"
						class="com_radio" onchange="completePlanner(1)"><strong id="listTitle">
							${planTitle}
						</strong>
				</div>
				<div class="plannerDate">
					<span id="listEndDate">${planEndDate}</span>
				</div>
				<span class="deleteButton" onclick="deletePlanner(1)"><b>X</b></span>
			</div>
		</li>`;
	}
	document.getElementById("plannersEle").innerHTML = childNodes;
	
	document.getElementById("userIdInfo").innerHTML = response.nickname;
	},
	error : function() {
		alert("연결실패!")
	},
	})
    initializeDateInput();
    planners = [];
    var now_utc = Date.now()
	var timeOff = new Date().getTimezoneOffset() * 60000;
	var today = new Date(now_utc - timeOff).toISOString().split("T")[0];

	document.getElementById("startDate").setAttribute("min", today);
	document.getElementById("endDate").setAttribute("min", today);
	document.getElementById("endAlarmDate").setAttribute("min", today);

	// html 인라인으로 선언한 함수 부분 수정하기
	document.getElementById("endAlarmDateBoolean").onchange = is_checked;
	document.getElementById("save").onclick = submitPlanner;

};

function is_checked() {
	if (document.getElementById("endAlarmDateBoolean").checked == true) {
		document.getElementById("endAlarmDate").readOnly = false;
	} else if (document.getElementById("endAlarmDateBoolean").checked == false) {
		document.getElementById("endAlarmDate").readOnly = true;
		document.getElementById("endAlarmDate").value = "";
	}
}


function initPlanners(list) {
	if (list !== null) {
		list.forEach((item) => {
			planners.push(item);
		})
	}
}


function submitPlanner() {
	const title = document.getElementById('title').value;
	let startDate = document.getElementById('startDate').value;
	let endDate = document.getElementById('endDate').value;
	let endAlarmDateBoolean = document
		.getElementById('endAlarmDateBoolean')
	let endAlarmDate = document.getElementById('endAlarmDate').value;


	if (!title) {
		alert('제목을 입력해주세요!');

		document.getElementById('title').focus();
		return false;
	}

	if (!startDate) {
		startDate = getCurrentDate();

	}

	if (!endDate) {

		endDate = getCurrentDate();
	}

	if (startDate > endDate) {

		alert('마감 날짜를 다시 설정해주세요!');
		return false;
	}
	if (endAlarmDateBoolean.checked) {
		if (startDate > endAlarmDate) {

			alert('마감 알람 날짜를 다시 설정해주세요!');
			return false;
		}
		
		
	}
	if(endAlarmDateBoolean.checked) {
		if (endDate < endAlarmDate) {
			alert('마감 알람 날짜를 다시 설정해주세요!');
			return false;
		}
	}
	


	if (endAlarmDateBoolean.checked && !endAlarmDate) {

		endAlarmDate = getCurrentDate();
	}
	let formData = { "title" : title, "startDate" : startDate, "endDate" : endDate, "remindAlarmDate" : endAlarmDate}
	console.log(formData);
	$.ajax({
		url : "/plan/create",
		type : "post",
		data : formData,
		success : function() {
			alert("추가 성공!")
			location.reload();
		},
		error : function() {
			alert("추가 실패!")
		}
		
	})
}


function formatDate(dateString) {
	const date = new Date(dateString);
	const month = (date.getMonth() + 1).toString().padStart(2, '0');
	const day = date.getDate().toString().padStart(2, '0');
	return `${month}월 ${day}일`;
}

function getCurrentDate() {
	const today = new Date();
	const month = (today.getMonth() + 1).toString().padStart(2, '0');
	const day = today.getDate().toString().padStart(2, '0');
	return `${today.getFullYear()}-${month}-${day}`;
}

function initializeDateInput() {
	// 날짜 입력 필드 찾기
	var dateInput = document.getElementById('startDate');
	var dateInput2 = document.getElementById('endDate');

	// 현재 날짜를 포함한 임의의 날짜로 설정 (형식을 강제로 바꾸기 위함)
	var currentDate = new Date();
	var year = currentDate.getFullYear();
	var month = ('0' + (currentDate.getMonth() + 1)).slice(-2);
	var day = ('0' + currentDate.getDate()).slice(-2);
	var today = year + '-' + month + '-' + day;

	// 날짜 입력 필드의 값을 현재 날짜로 설정
	dateInput.value = today;
	dateInput2.value = today;


}

