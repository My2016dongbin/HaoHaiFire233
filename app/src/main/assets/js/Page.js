var CurrPageIndex;
var RecordCount;
var PagesCount;

//初始化分页控件
function InitPage(RecordCount,CurrPageIndex,PageSize)
{
    PagesCount = Math.ceil(RecordCount / PageSize);
    var pagearray = $(".page>a[name='pageindex']");
    var startpage;
    var endpage;
    var activepageindex;
    if (CurrPageIndex < 3) {
        startpage = 1;
        activepageindex = CurrPageIndex - 1;
    }
    else if (PagesCount - CurrPageIndex < 3)
    {
        startpage = PagesCount - 4;
        activepageindex = CurrPageIndex - startpage;
    }
    else
    {
        startpage = CurrPageIndex - 2;
        activepageindex = 2;
    }
    if(startpage<=0){
        startpage=1;
    }
    endpage = startpage + 4;

    for (var i = startpage,index = 0; i <= endpage; i++,index++)
    {
        if (activepageindex == index) {
            pagearray[index].outerHTML = "<a name='pageindex' class='active' href='javascript:;' onclick='GoPage(" + i + ");'>" + i + "</a>";
        }
        else {
            if(index>=PagesCount){
                //pagearray[index].outerHTML = "<a name='pageindex' href='javascript:return false;'>" + i + "</a>";
                pagearray[index].style.display="none";
            }else{
                pagearray[index].outerHTML = "<a name='pageindex' href='javascript:;' onclick='GoPage(" + i + ");'>" + i + "</a>";
            }
        }


    }
}

$("#pagetop").click(function () {
    GoPage(1);
});
$("#pagepre").click(function () {
    if (QueryParam.page == 1) {
        GoPage(1);
    }
    else {
        GoPage(QueryParam.page-1);
    }
});
$("#pagenext").click(function () {
    if (QueryParam.page == PagesCount) {
        GoPage(PagesCount);
    }
    else {
        GoPage(QueryParam.page + 1);
    }
});
$("#pagebottom").click(function () {
    GoPage(PagesCount);
});

function GoPage(PageIndex)
{
    QueryParam.page = PageIndex;
    QueryFireInfo(QueryParam);
}