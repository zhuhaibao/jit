//分页总参数
export let pageParams = {};

//分页脚本
export function initPageParams(param) {
    //初始化分页参数
    pageParams.currentPage = param.currentPage;
    pageParams.startPage = param.startPage; //当前页导航第一页,默认页面加载时为1
    pageParams.endPage = param.endPage; //当前页导航最后一页,默认
    pageParams.toPage = param.toPage; //要跳转的页,默认
    pageParams.total = param.total;
    pageParams.pageSize = param.pageSize;//单页数据量
    pageParams.pageBarNum = param.pageBarNum;
    //开始渲染
    renderPage();
}

/*
    分页渲染逻辑
        1 假设开始页==1,分页为 1 2 3 4 5,在点击到中位数3之后才开始渲染
        2 假设结束页是13最后一页,分页位 9 10 11 12 13,在点击到中位数11之前才开始渲染
        其它情况:让点击的页数居中,依次展开页数
*/
export function renderPage() {
    //计算开始和结束页
    calcPage();
    //刷新页面
    renderPageBar();
    //改变按钮显示
    showButton();
    //改变导航显示
    // setPageBarNumber();
}

//计算开始页和结束页
function calcPage() {
    //总页数
    let totalPage = Math.ceil(pageParams.total / pageParams.pageSize);

    //开始页和结束页计算得当之后就可以渲染页面了.
    let calcStartPage, calcEndPage;
    //如果总页数小于pageBarNum,未撑满
    if (totalPage <= pageParams.pageBarNum) {
        calcStartPage = 1;
        calcEndPage = totalPage;
    } else {
        //计算开始页数的中位数,得出开始startPage页
        let middlePage = Math.ceil((1 + pageParams.pageBarNum) / 2);
        if (pageParams.startPage === 1 && pageParams.toPage < middlePage) {
            calcStartPage = 1;
        } else {
            middlePage = Math.ceil((pageParams.startPage + pageParams.endPage) / 2);
            //计算要去的页和现在的中位数的差距,调整开始页码
            calcStartPage = pageParams.startPage + (pageParams.toPage - middlePage);
        }
        //防止越界
        if (calcStartPage < 1) calcStartPage = 1;
        calcEndPage = calcStartPage + pageParams.pageBarNum - 1; //计算的结束页
        //如果由开始页计算的结束页超过最大页totalPage,则需要重新计算开始页
        if (calcEndPage > totalPage) {
            calcEndPage = totalPage;
            calcStartPage = calcEndPage - pageParams.pageBarNum + 1;
        }
    }
    pageParams.startPage = calcStartPage;
    pageParams.endPage = calcEndPage;
}

//刷新freshPageBar页面
export function renderPageBar() {
    //操作page的dom
    let pageIndex = document.body.querySelector(".pageIndex");
    pageIndex.innerHTML = "";
    for (let i = pageParams.startPage; i <= pageParams.endPage; i++) {
        if (i !== pageParams.toPage) {
            pageIndex.innerHTML += "<a>" + i + "</a>";
        } else {
            pageIndex.innerHTML += "<a class='pageSelected'>" + i + "</a>";
            pageParams.currentPage = i; //确定当前页
        }
    }
}

//控制显示分页button
export function showButton() {
    let totalPage = Math.ceil(pageParams.total / pageParams.pageSize);
    let pagePrev = document.getElementById("pagePrev");
    let pageNext = document.getElementById("pageNext");
    if ((pageParams.currentPage === 1 && totalPage > 0) || totalPage === 0) {
        pagePrev.style.display = 'none'
    } else {
        pagePrev.style.display = 'block';
    }
    pageParams.currentPage < totalPage ? pageNext.style.display = 'block' : pageNext.style.display = 'none';
}

//分页单击事件
export function renderClickPage(e) {
    let clickElem = e.target;
    if (clickElem.id === "pagePrev") {
        pageParams.toPage = pageParams.currentPage - 1;
    } else if (clickElem.id === "pageNext") {
        pageParams.toPage = pageParams.currentPage + 1;
    } else if (clickElem.className !== "pageSelected") {
        //先清除pageSelected
        document.body.querySelector(".pageSelected").removeAttribute("class");
        clickElem.className = "pageSelected";
        pageParams.toPage = parseInt(clickElem.textContent);
    }
    renderPage();
}

//分页大小调整函数
export function setPageSize(event) {
    if (event.key === 'Enter') {
        document.getElementById("pageSize").value = event.target.value;
        renderPageParams();
    }
}

//导航现实的页码数调整
export function setPageBarNum(event) {
    if (event.key === 'Enter') {
        let totalPage = Math.ceil(pageParams.total / pageParams.pageSize);
        let pageBarNum = parseInt(event.target.value);
        if (pageBarNum < 0 || pageBarNum > totalPage) {
            alert(`页码数应当在1和${totalPage}之间`);
            pageParams.pageBarNum = totalPage;
        } else {
            pageParams.pageBarNum = pageBarNum;
        }
        renderPage();
    }
}

//跳转页
export function toPage(e) {
    if (e.key === 'Enter') {
        let totalPage = Math.ceil(pageParams.total / pageParams.pageSize);
        let toPage = parseInt(e.target.value);
        if (toPage > totalPage) {
            alert(`页码数在1和${totalPage}之间`);
            return;
        }
        pageParams.toPage = toPage;
        renderPage();
    }
}

//页导航下面数据显示
export function setPageBarNumber() {
    totalPage.value = Math.ceil(total.value / pageSize.value);
    currentPage.value = pageParams.currentPage;
    pageBarNum.value = pageParams.pageBarNum;
}

