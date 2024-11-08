let totalNumber = parseInt(document.getElementById("total").value);
let pageSizeNumber = parseInt(document.getElementById("pageSize").value);
const pageParams = {
    //初始化分页参数
    currentPage: 1, //当前页,默认页面加载时为0
    pageBarNum: 10, //当前页导航最多显示多少页码,默认
    startPage: 1, //当前页导航第一页,默认页面加载时为0
    endPage: 5, //当前页导航最后一页,默认
    toPage: 1, //要跳转的页,默认
    total: totalNumber, //数据量
    pageSize: pageSizeNumber, //单页数据量
};
initPage();

function initPage() {
    renderPage();
}

//搜索函数
function submitSearch() {
    let form = document.getElementById("searchForm");
    form.pageSize.value = document.getElementById("pageSize").value;
    if (document.getElementById("status")) {
        form.status.value = document.getElementById("status").value;
    }
    form.submit();
}

//注册监听--start---
document.getElementById("searchButton").addEventListener("click", submitSearch);
//单击上一页和下一页按钮时绑定分页变化
document.getElementById("pagePrev").addEventListener("click", function (e) {
    renderClickPage(e);
    loadData();
});
document.getElementById("pageNext").addEventListener("click", function (e) {
    renderClickPage(e);
    loadData();
});
document.getElementById("pageSize").addEventListener("keyup", (e) => {
    if (e.key === 'Enter') {
        submitSearch();
    }
});
document.getElementById("currentPage").addEventListener("keyup", (e) => {
    if (e.key === 'Enter') {
        let totalPage = parseInt(document.getElementById("totalPage").value);
        if (e.target.value > totalPage) return;
        if (confirm(`跳转到${e.target.value}页么`)) {
            loadData();
        }
    }
});

//单击页码时,页面会变化,给新的页码绑定事件
document.body.querySelector(".pageIndex").addEventListener("click", function (e) {
    if (!e.target.closest("a")) return;
    if (e.target.className !== "pageSelected") {
        renderClickPage(e);
        loadData();
    }
});

function bindLoadDataForPage() {
    Array.from(document.body.querySelector(".pageIndex").querySelectorAll("a")).forEach(a => {
        if (a.className !== "pageSelected") {
            a.addEventListener("click", loadData)
        }
    });
}


//搜索
function loadData() {
    let formData = new FormData();
    formData.set("keyword", document.getElementById("searchInput").value);
    formData.set("pageSize", document.getElementById("pageSize").value);
    if (document.getElementById("status")) {
        formData.set("status", document.getElementById("status").value);
    }
    formData.set("pageNo", parseInt(document.getElementById("currentPage").value) - 1 + "");


    fetch(loadUrl, {
        method: "POST",
        body: formData,
    }).then(async result => {
        let json = await result.json();
        if (json.statusText === 'ok') {
            //渲染table
            renderTable(json);
            //用返回的json数据改变页面导航的输入框数据
            document.getElementById("total").value = json.data.total;
            document.getElementById("totalPage").value = json.data.totalPage;
            document.getElementById("pageSize").value = json.data.pageSize;
            document.getElementById("currentPage").value = json.data.pageNo + 1;
        } else {
            alert("查询报错!" + json);
        }
    })
}
