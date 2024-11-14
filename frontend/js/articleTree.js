//默认titleTree内部不显示a后面的div标签
document.getElementById("titleTree").querySelectorAll("div")
    .forEach(e => e.hidden = true);
//绑定a标签的单击事件
document.getElementById("titleTree").addEventListener("click", function (e) {
    //调整样式
    e.preventDefault();
    if (e.target.tagName !== 'A') return;
    let selected = removeSelected(e.target);
    e.target.classList.add("selected");
    if (e.target.nextElementSibling && e.target.nextElementSibling.tagName === 'DIV') {
        e.target.nextElementSibling.hidden = !e.target.nextElementSibling.hidden;
        e.target.nextElementSibling.classList.toggle("open");
    }
    if (e.target === selected) return;
    //加载data
    loadData(e.target.href);
    history.pushState({}, '', e.target.href);
});

//取消selected样式
function removeSelected(target) {
    let selected = document.getElementById("titleTree").querySelector(".selected");
    if (selected && selected !== target) {
        selected.classList.remove("selected");
    }
    return selected;
}

//着色selected样式
function selected() {
    let currentElement = document.getElementById("titleTree").querySelector(`a[href="${window.location.pathname}"]`);
    if (currentElement) {
        currentElement.classList.add("selected");
        //如果不是展开状态,则展开它
        if (currentElement.parentElement.tagName === 'DIV') {
            if (currentElement.parentElement.hidden) {
                currentElement.parentElement.hidden = false;
                currentElement.parentElement.classList.add("open");
            }
        }
    } else {
        let firstA = document.getElementById("titleTree").querySelector("a");
        if (firstA) {
            firstA.classList.add("selected");
        }
    }
}

//加载json
function loadData(href) {
    let pathName = new URL(href).pathname;
    let url = pathName.substring(0, pathName.length - 4) + "json";
    fetch(url).then(resp => resp.json()).then(result => {
        document.title = result.pageTitle;
        document.getElementsByTagName('meta')['keywords'].setAttribute('content', result.keyword);
        document.getElementById("articleTitle").innerHTML = result.title;
        document.getElementById("articleContent").innerHTML = result.content;
    })
}

//回退或者前进事件
window.addEventListener("popstate", ev => {
    let currentHref = window.location.href;
    //加载数据
    loadData(currentHref);
    //取消selected样式
    removeSelected(null);
    //查找当前元素并选中并着色selected样式
    selected();
});
window.addEventListener("load", ev => {
//查找当前元素并选中并着色selected样式
    selected();
});

