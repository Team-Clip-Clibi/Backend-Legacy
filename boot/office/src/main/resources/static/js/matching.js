document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("matchingForm");
    const buttons = document.querySelectorAll("#tabButtons .btn");
    const tbody = document.getElementById("matchingTableBody");

    const dummyData = {
        "원띵모임": [
            {nickname: "승범", phone: "010-1111-1111",region: "강남", job: "개발자", status: "싱글", diet: "상관없음", lang: "영어",  match: "BEFORE"},
            {nickname: "혜민", phone: "010-2222-2222", region: "홍대", job: "기획자", status: "연애중", diet: "채식", lang: "일본어",  match: "DONE"}
        ],
        "랜덤모임": [
            {nickname: "영희", phone: "010-4444-4444", purpose: "랜덤", job: "디자이너", status: "기혼", diet: "채식", lang: "중국어", region: "성수", match: "DONE"}
        ]
    };

    function renderTable(type) {
        tbody.innerHTML = "";
        const data = dummyData[type] || [];
        data.forEach((item, idx) => {
            const row = `
                <tr class="align-middle">
                    <td>${idx + 1}</td>
                    <td>${item.nickname}</td>
                    <td>${item.phone}</td>
                    <td>${item.region}</td>
                    <td>${item.job}</td>
                    <td>${item.status}</td>
                    <td>${item.diet}</td>
                    <td>${item.lang}</td>
                    
                    <td><button class="btn btn-sm ${item.match === 'BEFORE' ? 'btn-outline-success' : 'btn-secondary'}">${item.match === 'BEFORE' ? '매칭전' : '완료'}</button></td>
                </tr>
            `;
            tbody.insertAdjacentHTML("beforeend", row);
        });
    }

    function updateModal(type) {
        const modalTitle = document.getElementById("createMatchingModalLabel");
        const formBody = document.querySelector("#matchingForm .modal-body");
        const createBtn = document.querySelector("[data-bs-target='#createMatchingModal']");

        // 버튼 텍스트 변경
        if (type === "랜덤모임") {
            createBtn.textContent = "랜덤 모임 생성";
            modalTitle.textContent = "랜덤 모임 생성";

            formBody.innerHTML = `
                <div class="mb-3">
                    <label for="randomDistrict" class="form-label">선호지역</label>
                    <select class="form-select" id="randomDistrict" name="randomDistrict" required>
                        <option value="GANGNAM">강남</option>
                        <option value="YOUNGSAN_ITAEWON">용산/이태원</option>
                        <option value="GUNDAE_SEONGSU">건대/성수</option>
                        <option value="YEOUIDO_YEONGDEUNGPO">여의도/영등포</option>
                        <option value="HONGDAE_HAPJEONG">홍대/합정</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="location" class="form-label">장소</label>
                    <input type="text" class="form-control" id="location" name="location" required>
                </div>
                <div class="mb-3">
                    <label for="restaurantName" class="form-label">식당명</label>
                    <input type="text" class="form-control" id="restaurantName" name="restaurantName" required>
                </div>
                <div class="mb-3">
                    <label for="meetingTime" class="form-label">시간</label>
                    <input type="datetime-local" class="form-control" id="meetingTime" name="meetingTime" required>
                </div>
            `;
        } else {
            createBtn.textContent = "모임 생성";
            modalTitle.textContent = "모임 생성";

            formBody.innerHTML = `
                <div class="mb-3">
                    <label for="meetingTitle" class="form-label">모임 이름</label>
                    <input type="text" class="form-control" id="meetingTitle" name="meetingTitle" required>
                </div>
                <div class="mb-3">
                    <label for="meetingTime" class="form-label">모임 시간</label>
                    <input type="datetime-local" class="form-control" id="meetingTime" name="meetingTime" required>
                </div>
                <div class="mb-3">
                    <label for="location" class="form-label">장소</label>
                    <input type="text" class="form-control" id="location" name="location" required>
                </div>
            `;
        }
    }

    // 초기 상태
    const defaultType = "원띵모임";
    renderTable(defaultType);
    updateModal(defaultType);

    // 탭 클릭 이벤트
    buttons.forEach(btn => {
        btn.addEventListener("click", function () {
            buttons.forEach(b => b.classList.remove("active"));
            this.classList.add("active");
            const type = this.textContent.trim();
            renderTable(type);
            updateModal(type);
        });
    });

    // 모임 생성 폼 전송
    if (form) {
        form.addEventListener("submit", function (e) {
            e.preventDefault();

            // 폼 데이터 수집 (랜덤모임/일반모임 구분 가능)
            const formData = new FormData(form);
            const jsonData = {};
            formData.forEach((value, key) => {
                jsonData[key] = value;
            });

            console.log("보내는 데이터:", jsonData);

            fetch("/office/matching/create", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                credentials: "include",
                body: JSON.stringify(jsonData)
            })
                .then(response => {
                    if (!response.ok) throw new Error("요청 실패");
                    return response.json();
                })
                .then(result => {
                    alert("모임 생성 완료!");
                    const modal = bootstrap.Modal.getInstance(document.getElementById("createMatchingModal"));
                    modal.hide();
                    location.reload();
                })
                .catch(error => {
                    alert("에러 발생: " + error.message);
                });
        });
    }
});
