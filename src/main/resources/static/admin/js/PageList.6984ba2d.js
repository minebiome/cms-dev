(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["PageList"],{"7bf5":function(t,e,a){"use strict";a.r(e);var n=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",[a("a-table",{attrs:{pagination:!1,columns:t.columns,dataSource:t.data,rowKey:function(t){return t.id}},scopedSlots:t._u([{key:"existNav",fn:function(e,n){return a("div",{},[a("a-switch",{attrs:{defaultChecked:""},on:{change:function(e){return t.onChangeNav(n.id)}},model:{value:n.existNav,callback:function(e){t.$set(n,"existNav",e)},expression:"record.existNav"}})],1)}},{key:"action",fn:function(e,n){return a("span",{},[a("a",{attrs:{href:"javascript:;"},on:{click:function(e){return t.edit(n.id)}}},[t._v("编辑")]),a("a-divider",{attrs:{type:"vertical"}}),a("a",{attrs:{href:"javascript:;"},on:{click:function(e){return t.generateHtml(n.id)}}},[t._v("生成HTML")]),a("a-divider",{attrs:{type:"vertical"}}),a("a",{attrs:{href:"javascript:;"},on:{click:function(e){return t.deleteById(n.id)}}},[t._v("删除")]),a("a-divider",{attrs:{type:"vertical"}}),a("a",{attrs:{href:"javascript:;"},on:{click:function(e){return t.preview(n.id)}}},[t._v("预览")]),a("a-divider",{attrs:{type:"vertical"}}),a("a",{attrs:{href:"javascript:;"},on:{click:function(e){return t.openHtml(n)}}},[t._v("查看HTML")])],1)}}])},[a("template",{slot:"footer"},[a("div",{staticClass:"page-wrapper",style:{textAlign:"right"}},[a("a-pagination",{staticClass:"pagination",attrs:{current:t.pagination.page,total:t.pagination.total,defaultPageSize:t.pagination.size,pageSizeOptions:["1","2","5","10","20","50","100"],showSizeChanger:""},on:{showSizeChange:t.handlePaginationChange,change:t.handlePaginationChange}})],1)])],2)],1)},i=[],o=a("ed66"),r=a("5c07"),c=[{title:"Title",dataIndex:"title",key:"title"},{title:"页面的名称",dataIndex:"viewName",key:"viewName"},{title:"模板",dataIndex:"templateName",key:"templateName"},{title:"创建时间",dataIndex:"createDate",key:"createDate"},{title:"是否添加到导航",dataIndex:"existNav",key:"existNav",scopedSlots:{customRender:"existNav"}},{title:"Action",key:"action",scopedSlots:{customRender:"action"}}],s={data:function(){return{pagination:{page:0,size:5,sort:null},queryParam:{page:0,size:10,sort:null,keyword:null,categoryId:null,status:null},data:[],columns:c}},created:function(){this.loadSheet()},methods:{loadSheet:function(){var t=this;this.queryParam.page=this.pagination.page-1,this.queryParam.size=this.pagination.size,this.queryParam.sort=this.pagination.sort,o["a"].list(this.queryParam).then((function(e){t.data=e.data.data.content,t.pagination.total=e.data.data.totalElements}))},handlePaginationChange:function(t,e){this.pagination.page=t,this.pagination.size=e,this.loadSheet()},edit:function(t){this.$router.push({name:"PageCreate",query:{sheetId:t}})},deleteById:function(t){var e=this;o["a"].deleteById(t).then((function(t){e.$notification["success"]({message:t.data.message}),e.loadSheet()}))},onChangeNav:function(t){var e=this;o["a"].addOrRemoveToMenu(t).then((function(t){t.data.data.existNav?e.$notification["success"]({message:"成功添加"+t.data.data.title+"到导航!!"}):e.$notification["success"]({message:"成功移除"+t.data.data.title+"到导航!!"})}))},preview:function(t){window.open(r["a"].Online("sheet",t),"_blank")},openHtml:function(t){var e=t.viewName;t.path&&(e=t.path+"/"+e),window.open(r["a"].Html(e),"_blank")},generateHtml:function(t){var e=this;o["a"].generateHtml(t).then((function(t){e.$notification["success"]({message:"页面生成成功!!"+t.data.message})}))}}},d=s,u=a("2877"),l=Object(u["a"])(d,n,i,!1,null,null,null);e["default"]=l.exports},ed66:function(t,e,a){"use strict";var n=a("9efd"),i="/api/sheet",o={list:function(t){return Object(n["a"])({url:i,params:t,method:"get"})},addOrRemoveToMenu:function(t){return Object(n["a"])({url:"".concat(i,"/addOrRemoveToMenu/").concat(t),method:"get"})},findListByChannelId:function(t){return Object(n["a"])({url:"".concat(i,"/findListByChannelId/").concat(t),method:"get"})},deleteById:function(t){return Object(n["a"])({url:"".concat(i,"/delete/").concat(t),method:"get"})},generateHtml:function(t){return Object(n["a"])({url:"".concat(i,"/generate/").concat(t),method:"get"})},query:function(t){return Object(n["a"])({url:i,params:t,method:"get"})},findById:function(t){return Object(n["a"])({url:"".concat(i,"/find/").concat(t),method:"get"})},create:function(t){return Object(n["a"])({url:i,data:t,method:"post"})},saveSheet:function(t){return Object(n["a"])({url:"".concat(i,"/save"),data:t,method:"post"})},modifySheet:function(t,e){return Object(n["a"])({url:"".concat(i,"/save/").concat(t),data:e,method:"post"})},update:function(t,e){return Object(n["a"])({url:"".concat(i,"/update/").concat(t),data:e,method:"post"})}};e["a"]=o}}]);
//# sourceMappingURL=PageList.6984ba2d.js.map