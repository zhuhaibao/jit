/*弹出对话框通用函数*/
function showPublicConfirm(e, message) {
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
        //后台服务
        /*后代服务代码*/
        e.target.offsetParent.remove();
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

/*下面函数决定如何显示导航栏以及左右按钮,导航栏左右按钮轮播,需求如下
            1 页面刚开始加载时,根据选中的区域决定是否滚动,
                窗口滚动的距离计算:获取当前选中的元素li,x=(li.left+li.offsetWidth-document.documentElement.offsetWidth);
            2 单击左箭头,向右滚动,不越界
            3 单击右箭头,向右边滚动,不越界
            4 滚动到最左边,左边箭头消失;滚动到最右边右边箭头消失;当导航长度小于窗口长度时,左右按钮都消失.
        */
function showNavBar() {
    //当导航长度小于窗口长度时,左右按钮都消失.当导航长度大于窗口长度时,只显示右按钮(因为加载时默认从左边开始,所以左按钮默认不会显示)
    let nav = document.body.querySelector('.navBar ul');
    showArrow(nav);

    //页面刚开始加载时,根据选中的区域决定是否滚动
    let selectedLi = document.body.querySelector('.navBar ul li.selected');
    if (selectedLi) {
        let coords = selectedLi.getBoundingClientRect();
        //如果右边距大于窗口宽度,则需要一次性向右滚动;同时显示左按钮
        let needScrollWidth = coords.right - document.documentElement.offsetWidth;
        if (needScrollWidth > 0) {
            nav.scrollBy(needScrollWidth + 70, 0);//多出2个箭头的像素宽度
        }
        showArrow(nav);
    }
    let width = 100,//100像素
        start = 500,//半秒
        step = 100;//0.1秒

    function toRight() {
        nav.scrollBy(width, 0);
        showArrow(nav);
    }

    function toLeft() {
        nav.scrollBy(-width, 0);
        showArrow(nav);
    }

    document.getElementById("rightArrow").addEventListener("click", toRight);
    document.getElementById("leftArrow").addEventListener("click", toLeft);
    nav.onscroll = function (e) {
        showArrow(nav);
    }
}

//每个页面顶部要加载的导航
async function loadNav() {
    let response = await fetch("/nav.json");
    let result = await response.json();
    let ul = document.getElementById("topNav");
    let innerHtml = ``;
    innerHtml += `<li id="articleLabel"><a href="/articles/index.html">点滴文章</a></li>`;
    result.forEach(sub => {
        innerHtml += `<li><a href="/subject/${sub.dir}/index.html">${sub.subName}</a></li>`;
    });
    ul.innerHTML = innerHtml;

    selectedNavLi();//选中导航
    showNavBar(); //显示导航条
    //手动高亮设置
    window.Prism = window.Prism || {};
    window.Prism.manual = true;
}

function selectedNavLi() {
    //根据url自动选中导航
    let url = new URL(location.href);
    if (url.pathname.startsWith('/subject')) {
        let x = url.pathname.replace('/subject/', '');
        let liUrl = '/subject/' + x.substring(0, x.indexOf('/')) + '/index.html';
        let navA = document.getElementById('topNav').querySelector(`a[href="${liUrl}"]`);
        if (navA && navA.parentElement) navA.parentElement.classList.add('selected');
    } else if (url.pathname.startsWith('/articles')) {
        document.getElementById('articleLabel').classList.add('selected');
    }
}

/**
 * tapHold 事件包装器
 * @param elem 按住的元素
 * @param f 被包装的移动函数
 * @param start 开始执行时间,只要按住超过start的时间,那么就会执行步长为step的移动
 * @param step 时间间隔,
 * @returns {(function(): void)|*}
 */
function tapHold(elem, f, start, step) {
    let timerOut, timeInterval;
    return function () {
        timerOut = setTimeout(() => {
            timeInterval = setInterval(() => {
                f.apply(f, arguments);
                if (!elem || elem.hidden || (elem.style.display === 'none')) {
                    clearTimeout(timeInterval);
                    clearTimeout(timerOut);
                }
            }, step);
        }, start);
        elem.onmouseup = () => {
            clearTimeout(timeInterval);
            clearTimeout(timerOut);
        };
    }
}

//是否显示左右按钮
function showArrow(nav) {
    let treeBarWidth = 0;
    if (document.getElementById('treeBar')) treeBarWidth = document.getElementById('treeBar').offsetWidth;
    //如果导航左边距<0,显示左按钮
    if (nav.firstElementChild.nextElementSibling.getBoundingClientRect().left + 2 - document.getElementById('articleLabel').getBoundingClientRect().right < 0) {//需要去掉ul边界的padding-left
        leftArrow.hidden = false;
        //如果要显示左按钮,就要调整nav的左边距
        leftArrow.style.left = treeBarWidth + 'px';
        document.getElementById('articleLabel').style.left = document.getElementById('leftArrow').offsetWidth + treeBarWidth + 'px';
        nav.style.paddingLeft = document.getElementById('articleLabel').getBoundingClientRect().right + 'px';
    } else {
        leftArrow.hidden = true;
        document.getElementById('articleLabel').style.left = treeBarWidth + 'px';
        nav.style.paddingLeft = document.getElementById('articleLabel').getBoundingClientRect().right + 'px';
    }
    if (nav.lastElementChild.getBoundingClientRect().right > document.documentElement.offsetWidth) {
        rightArrow.hidden = false;
    } else {
        rightArrow.hidden = true;
    }
}

//treeBar点击处理
function treeBarFun() {
    document.getElementById("titleTreeDiv").classList.toggle('titleTreeDiv700');
}

//页面委托处理treeBar的显示与否
document.addEventListener('click', e => {
    let div = document.getElementById("titleTreeDiv");
    if (!div) return;
    if (e.target.closest('#treeBar')) return;
    if (e.target.closest('.titleTreeDiv')) return;
    if (!div.classList.contains('titleTreeDiv700')) {
        div.classList.toggle('titleTreeDiv700');
    }
});




