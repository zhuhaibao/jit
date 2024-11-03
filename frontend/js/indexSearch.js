import data from './index-data.js';
import * as page from './page.js';
import {renderClickPage} from './page.js';

const flexSearch = new FlexSearch.Document({
    document: {
        index: [
            {
                field: 'title',
                tokenize: 'full',
                optimize: true,
                resolution: 9,
                minlength: 1,
            },
            {
                field: 'content',
                tokenize: 'full',
                optimize: true,
                resolution: 5,
                minlength: 2,
            }]
    }
});
for (let i = 0; i < data.length; i++) {
    flexSearch.add(i, data[i]);
}


/************************************搜索有关的函数调用-start************************************************/
// let searchByKeyDecorator = debounce(queryFromServer, 500);//设定500ms的间隔

//给页面绑定函数
document.addEventListener("click", function (e) {
    if (e.target.closest("#searchResultDiv")) return;
    document.getElementById("searchResultDiv").style.display = 'none';
});
document.getElementById("searchInput").addEventListener("focus", searchByKey);
document.getElementById("searchInput").addEventListener("input", searchByKey);
document.getElementById("searchInput").addEventListener("blur", onFocusLose);
document.getElementById("showSearchAll").addEventListener("click", searchAll);
document.getElementById("searchButton").addEventListener("click", searchAll);
document.getElementById("searchInput").addEventListener("keyup", function (e) {
    if (e.key === 'Enter') {
        searchAll(e);
    }
});

//input框失去焦点
function onFocusLose(e) {
    let related = e.relatedTarget;
    if (related != null && (related.classList.contains('resultItem') || related.classList.contains('siteSearch'))) return;
    searchResultDiv.style.display = 'none';
}


//关键字搜索
function searchByKey(e) {
    let searchKey = searchInput.value;
    if (!searchKey || searchKey.trim().length === 0) return;
    queryFromServer(searchInput.value);
}

//关键字搜索,请求服务
function queryFromServer(searchKey) {
    if (!searchKey) return;
    //从服务器获取数据,默认最多显示10条
    let result = flexSearch.search(searchKey, 10);
    //往searchResult中填充数据
    if (result.length === 0) return;
    let ids = result[0].result;
    let resultHTML = ``;
    let regex = new RegExp(`${searchKey}`, "gi");
    ids.forEach(id => {
        let title = data[id].title.replaceAll(regex, `<span class='keyword'>${searchKey}</span>`);
        resultHTML += `<a class="resultItem" href='${data[id].url}'>${title}</a>`;
    })
    document.getElementById("searchResult").innerHTML = resultHTML;
    //显示searchResultDivs
    searchResultDiv.style.left = searchInput.getBoundingClientRect().left + 'px';
    searchResultDiv.style.top = searchInput.getBoundingClientRect().bottom + 'px';
    searchResultDiv.style.display = 'block';
}

//flex搜索
function searchAll() {
    let searchKey = document.getElementById("searchInput").value;
    if (!searchKey) return;
    //从服务器获取数据
    let startTime = Date.now();
    let result = flexSearch.search(searchKey);
    let totalTime = (Date.now() - startTime) / 1000;
    let ids = result[0].result;
    //显示
    document.getElementById("modalResult").style.display = 'block';
    //初始化
    initData(ids, totalTime, searchKey);
    //分页事件
    bindFunctionForPage(ids, searchKey);
}

function initData(ids, totalTime, searchKey) {
    //替换耗时
    document.getElementById("resultAnalysis").innerHTML = `About ${ids.length} results (${totalTime} ms)`;
    //开始渲染分页,从第一页开始加载数据
    let params = {
        currentPage: 1,
        startPage: 1,
        endPage: 10,
        toPage: 1,
        total: ids.length,
        pageSize: 5,
        pageBarNum: 10,
    };
    page.initPageParams(params);
    loadData(1, ids, searchKey);
}

function bindFunctionForPage(ids, searchKey) {
    document.getElementById("pagePrev").addEventListener("click", function (e) {
        page.renderClickPage(e);
        loadData(page.pageParams.currentPage, ids, searchKey);
    });
    document.getElementById("pageNext").addEventListener("click", function (e) {
        page.renderClickPage(e);
        loadData(page.pageParams.currentPage, ids, searchKey);
    });
    document.body.querySelector(".pageIndex").addEventListener("click", function (e) {
        if (!e.target.closest("a")) return;
        if (e.target.className !== "pageSelected") {
            renderClickPage(e);
            loadData(page.pageParams.currentPage, ids, searchKey);
        }
    });
}

function loadData(pageNo, ids, searchKey) {
    let regex = new RegExp(`${searchKey}`, "gi");
    let resultHTML = ``;
    let dataIndex = (pageNo - 1) * page.pageParams.pageSize;
    for (let i = dataIndex; i < pageNo * page.pageParams.pageSize && i < ids.length; i++) {
        let content = data[ids[i]].content;
        if (content.length > 300) content = content.substring(0, 300);
        content = content.replaceAll(regex, `<span class='keyword'>${searchKey}</span>`);
        resultHTML += `
                <div class="gItem">
                    <a class="gTitle" href='${data[ids[i]].url}'><b>${data[ids[i]].title}</b></a><br>
                    <span class="gPath">${data[ids[i]].subject} > ${data[ids[i]].title}</span><br>
                    <span class="gInfo">${content}</span>
                </div>`;
    }
    document.getElementById("searchResultAll").innerHTML = resultHTML;
}

/************************************搜索有关的函数调用-end************************************************/
