/*弹出对话框通用函数*/
function showPublicConfirm(e,callback, message) {
    //1 通用确认对话框位置确定
    //在当前单击位置的右边显示
    let left = e.clientX + 10;
    let top = e.clientY + 10;
    //判断不要越界(因为确定按钮和关闭按钮都在右侧,所以判断右边不要越界);
    let windowWidth = getScrollDocumentWidth();
    if (left + this.offsetWidth > windowWidth) {
        left = windowWidth - this.offsetWidth;
    }
    publicConfirmTipContent.style.left = left + 'px';
    publicConfirmTipContent.style.top = top + 'px';
    publicConfirmTipContent.style.position = 'absolute';
    //2 通用对话框的内容确定
    publicConfirmTooltip.innerHTML = message;
    //3 显示对话框
    publicConfirmTip.style.display = "block";

    publicConfirmTipButtonConfirm.onclick = function () {
        callback(e);
        publicConfirmTip.style.display = 'none';
    }
    publicConfirmTipButtonCancel.onclick = function () {
        publicConfirmTip.style.display = 'none';
    }
}

function getScrollDocumentWidth() {
    return Math.max(
        document.documentElement.scrollWidth,
        document.documentElement.offsetWidth,
        document.documentElement.clientWidth,
        document.body.scrollWidth,
        document.body.offsetWidth,
        document.body.clientWidth,
    );
}

function getScrollDocumentHeight() {
    return Math.max(
        document.documentElement.scrollHeight,
        document.documentElement.offsetHeight,
        document.documentElement.clientHeight,
        document.body.scrollHeight,
        document.body.offsetHeight,
        document.body.clientHeight,
    );
}

//对于输入变动事件的函数进行包装,防抖动功能,避免请求太频繁
function debounce(f, timeout) {
    let timerId;
    return function () {
        clearTimeout(timerId); //立刻清除上次timer
        timerId = setTimeout(() => {
            f.apply(this, arguments);
        }, timeout);
    };
}

function randomInteger(min, max) { //产生随机整数
    return Math.floor(min + Math.random() * (max - min + 1));
}

function randomInteger(n) { //产生随机整数
    return Math.floor(Math.random() * n);
}

/*加载jodit editor编辑器插件- free version*/
function loadJoditEditor(selector) {
    return Jodit.make(selector, {
        uploader: {
            url: '/article/uploadFile',
            format:'json',
            getMessage: function (resp) {
                if(resp.statusText==='ok'){
                    resp.data.fileNames.forEach(fileName=>{
                        this.jodit.selection.insertImage(resp.data.baseurl+ resp.data.path + fileName);
                    });
                }
                return resp.msg;
            }
        }
    });
}
//分页脚本
    function renderPageParams() {
        //初始化分页参数
        pageParams.currentPage = document.getElementById("currentPage").value; //当前页,默认页面加载时为1
        pageParams.startPage = 1; //当前页导航第一页,默认页面加载时为1
        pageParams.endPage = 5; //当前页导航最后一页,默认
        pageParams.toPage = 1; //要跳转的页,默认
        pageParams.total = parseInt(document.getElementById("total").value); //数据量
        pageParams.pageSize = parseInt(document.getElementById("pageSize").value); //单页数据量
        pageParams.pageBarNum = parseInt(document.getElementById("pageBarNum").value);
        //开始渲染
        renderPage();
    }

    /*
        分页渲染逻辑
            1 假设开始页==1,分页为 1 2 3 4 5,在点击到中位数3之后才开始渲染
            2 假设结束页是13最后一页,分页位 9 10 11 12 13,在点击到中位数11之前才开始渲染
            其它情况:让点击的页数居中,依次展开页数
    */
    function renderPage() {
        //计算开始和结束页
        calcPage();
        //刷新页面
        renderPageBar();
        //改变按钮显示
        showButton();
        //改变导航显示
        setPageBarNumber();
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
            if(calcStartPage<1)calcStartPage=1;
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
    function renderPageBar() {
        //操作page的dom
        let pageIndex = document.body.querySelector(".pageIndex");
        pageIndex.innerHTML = "";
        for (let i = pageParams.startPage; i <= pageParams.endPage; i++) {
            if (i !== pageParams.toPage) {
                pageIndex.innerHTML += "<a onclick='renderClickPage(this)'>" + i + "</a>";
            } else {
                pageIndex.innerHTML += "<a class='pageSelected'>" + i + "</a>";
                pageParams.currentPage = i; //确定当前页
            }
        }
    }
    //控制显示分页button
    function showButton() {
        let totalPage = Math.ceil(pageParams.total / pageParams.pageSize);
        let pagePrev = document.getElementById("pagePrev");
        let pageNext = document.getElementById("pageNext");
        if((pageParams.currentPage === 1 && totalPage>0) || totalPage===0){
            pagePrev.style.display = 'none'
        }else{
            pagePrev.style.display = 'block';
        }
        pageParams.currentPage < totalPage ? pageNext.style.display = 'block' : pageNext.style.display = 'none';
    }
    //分页单击事件
    function renderClickPage(clickElem) {
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
    function setPageSize(event) {
        if(event.key==='Enter'){
            document.getElementById("pageSize").value=event.target.value;
            renderPageParams();
        }
    }
    //导航现实的页码数调整
    function setPageBarNum(event){
        if(event.key==='Enter'){
            let totalPage = Math.ceil(pageParams.total / pageParams.pageSize);
            let pageBarNum = parseInt(event.target.value);
            if(pageBarNum<0||pageBarNum>totalPage){
                alert(`页码数应当在1和${totalPage}之间`);
                pageParams.pageBarNum = totalPage;
            }else{
                pageParams.pageBarNum = pageBarNum;
            }
            renderPage();
        }
    }
    //跳转页
    function toPage(event){
        if(event.key==='Enter'){
            let totalPage = Math.ceil(pageParams.total / pageParams.pageSize);
            let toPage = parseInt(event.target.value);
            if(toPage>totalPage){
                alert(`页码数在1和${totalPage}之间`);
                return;
            }
            pageParams.toPage= toPage;
            renderPage();
        }
    }
    //页导航下面数据显示
    function setPageBarNumber(){
        totalPage.value=Math.ceil(total.value/pageSize.value);
        currentPage.value = pageParams.currentPage;
        pageBarNum.value = pageParams.pageBarNum;
    }

    //渲染错误form表单验证错误信息
    function renderFormFieldErrors(form, errors) {
        for(let key in errors){
            if(form.elements[key]){
                form.elements[key].insertAdjacentHTML('afterend',`<span class='feildErr'>${errors[key]}</span>`);
            }
        }
    }
    //清除form表单验证错误信息
    function clearFormFieldErrors(form) {
        Array.from(form.querySelectorAll(".feildErr")).forEach(e=>{
            e.remove();
        });
    }

    //判断页面给导航着色
    window.addEventListener("load",()=>{
        let url = window.location.href;
        let color = "#03AA6D";
        if(url.includes('/subject')){
            document.getElementById("subjectNav").style.color=color;
        }
        if(url.includes('/article')){
            document.getElementById("articleNav").style.color=color;
        }
        if(url.includes('/navigation')){
            document.getElementById("navigationNav").style.color=color;
        }
    });

