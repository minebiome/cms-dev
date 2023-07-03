### 待办事项
### 存在的问题
1. PDF文件生成问题
2. 创建文章没有及时生成日期
3. 创建文章没有指定分类不能生成
### 需要添加的功能
ALTER TABLE Institution RENAME TO "institution"

```
$:git merge --abort
$:git reset --merge
$:git pull
```
### 二、技术概况
#### 2.1 页面公共部分的处理
```java
    private static  String pattern = "<!--#include file=\"(.*?)\"-->";
    private static String varPattern = "<!--\\{\\{(.*?)}}-->";
```
采用正则表达式匹配`    <!--#include file="/components/header.html"-->`，找到文件之后，读取替换。

这个插入语句是nginx的，所有将静态目录设置在nginx可以不通过服务器，直接访问静态页面

#### 2.2 分页静态化的实现
```java
    @GetMapping("/{categoryPath}/{categoryViewName}/page-{page}.html")
    public String articleListBy(HttpServletRequest request, @PathVariable("categoryPath") String categoryPath, @PathVariable("categoryViewName") String categoryViewName, @PathVariable("page") Integer page){
        File file = new File(CmsConst.WORK_DIR+"/html/"+categoryPath+"/"+categoryViewName+"/"+page+".html");
        String result = null;
        if(file.exists()){
            result = FileUtils.convert(file,request);
        }else {
            Category category = categoryService.findByViewName(categoryViewName);
            if (category!=null){
                String resultHtml = htmlService.convertArticleListBy(category,page);
               result =  FileUtils.convertByString(resultHtml,request);
            }
        }
        if (request!=null){
            return result;
        }
        System.out.println(categoryViewName);
        System.out.println(page);
        return  "Page is not found!";
    }
```
当第一次访问分页页面时，会生成其html页面。当第二次访问时，判断文件存在就直接文件返回。

在对文章增删改时会删除生成的分类列表缓存。

#### 2.3 权限管理


[Springboot日志](https://blog.csdn.net/aa390481978/article/details/108096503)

http://www.diyiziti.com/Download/153


//
//            String appid = WxMpConfigStorageHolder.get();
//            List<WxMpTemplateData> data  = new ArrayList<>();
//            data.add(new WxMpTemplateData("first","模板消息测试"));
//            data.add(new WxMpTemplateData("keywords1","xxxxx"));
//            data.add(new WxMpTemplateData("keywords2","xxxxx"));
//            data.add(new WxMpTemplateData("remark","点击查看消息详情"));
//            if(templateId!=null && !"".equals(templateId)){
//                WxMpTemplateMessage wxMpTemplateMessage = WxMpTemplateMessage.builder()
//                        .templateId(templateId)
//                        .url("https://www.yuque.com/nifury/wx/cyku5l")
//                        .toUser(wxUser.getOpenId())
//                        .data(data)
//                        .build();
//                templateMsgService.sendTemplateMsg(wxMpTemplateMessage,appid);
//
//            }
//            if(subscribeTemplateId!=null && !"".equals(subscribeTemplateId)){
//
//            }


//            context.setVariable();
//            modelAndView.addObject("state",state);