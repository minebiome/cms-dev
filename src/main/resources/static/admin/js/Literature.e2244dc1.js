(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["Literature"],{"04d7":function(t,e,a){"use strict";a.r(e);var i=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",[a("a-button",{on:{click:t.importData}},[t._v("导入")]),a("a-button",{on:{click:t.generateHtml}},[t._v("HTML")]),a("a-button",{on:{click:t.addMenu}},[t._v("添加菜单")]),a("a-button",{attrs:{type:"primary",icon:"cloud-upload"},on:{click:function(){return t.uploadVisible=!0}}},[t._v("上传")]),a("a-table",{attrs:{pagination:!1,columns:t.columns,dataSource:t.literatures,rowKey:function(t){return t.id}},scopedSlots:t._u([{key:"title_",fn:function(e){return a("div",{},[a("a",{attrs:{href:"javascript:;"}},[t._v(t._s(e))])])}},{key:"action",fn:function(e,i){return a("span",{},[a("a",{attrs:{href:"javascript:;"},on:{click:function(e){return t.edit(i)}}},[t._v("更新")]),a("a-divider",{attrs:{type:"vertical"}}),a("a",{attrs:{href:"javascript:;"},on:{click:function(e){return t.deleteLiteratures(i)}}},[t._v("删除")]),a("a-divider",{attrs:{type:"vertical"}})],1)}}])},[a("template",{slot:"footer"},[a("div",{staticClass:"page-wrapper",style:{textAlign:"right"}},[a("a-pagination",{staticClass:"pagination",attrs:{current:t.pagination.page,total:t.pagination.total,defaultPageSize:t.pagination.size,pageSizeOptions:["1","2","5","10","20","50","100"],showSizeChanger:""},on:{showSizeChange:t.handlePaginationChange,change:t.handlePaginationChange}})],1)])],2),a("a-modal",{attrs:{title:"上传文献",afterClose:t.onUploadClose,destroyOnClose:""},model:{value:t.uploadVisible,callback:function(e){t.uploadVisible=e},expression:"uploadVisible"}},[a("a-upload-dragger",{attrs:{name:"file",multiple:!0,action:t.upload,headers:t.headers,withCredentials:!0},on:{change:t.uploadPic}},[a("p",{staticClass:"ant-upload-drag-icon"},[a("img",{attrs:{src:t.literature.picPath,width:"100%",alt:"",srcset:""}})]),a("p",{staticClass:"ant-upload-text"},[t._v("Click or drag file to this area to upload")]),a("p",{staticClass:"ant-upload-hint"},[t._v(" Support for a single or bulk upload. Strictly prohibit from uploading company data or other band files ")])])],1),a("a-modal",{attrs:{title:"添加分类"},on:{ok:t.handleOk},model:{value:t.visible,callback:function(e){t.visible=e},expression:"visible"}},[a("a-form",[a("a-form-item",{attrs:{label:"文献标题"}},[a("a-input",{model:{value:t.literature.title,callback:function(e){t.$set(t.literature,"title",e)},expression:"literature.title"}})],1),a("a-form-item",{attrs:{label:"url"}},[a("a-input",{model:{value:t.literature.url,callback:function(e){t.$set(t.literature,"url",e)},expression:"literature.url"}})],1),a("a-form-item",{attrs:{label:"key"}},[a("a-input",{model:{value:t.literature.key,callback:function(e){t.$set(t.literature,"key",e)},expression:"literature.key"}})],1)],1)],1)],1)},n=[],r=(a("b0c0"),a("9efd")),o="/api/literature",l={import:function(){return Object(r["a"])({url:"".concat(o,"/import"),method:"get"})},generateHtml:function(){return Object(r["a"])({url:"".concat(o,"/generateHtml"),method:"get"})},list:function(t){return Object(r["a"])({url:o,method:"get",params:t})},delete:function(t){return Object(r["a"])({url:"".concat(o,"/delete/").concat(t),method:"get"})},add:function(t){return Object(r["a"])({url:o,method:"post",data:t})},update:function(t,e){return Object(r["a"])({url:"".concat(o,"/update/").concat(t),method:"post",data:e})},findById:function(t){return Object(r["a"])({url:"".concat(o,"/find/").concat(t),method:"get"})}},s=l,u=a("a796"),c=a("5c07"),d=[{title:"id",dataIndex:"id",key:"id"},{title:"Key",dataIndex:"key",key:"key"},{title:"Title",dataIndex:"title",key:"title",scopedSlots:{customRender:"title_"}},{title:"Action",key:"action",scopedSlots:{customRender:"action"}}],p={data:function(){return{literatures:[],visible:!1,isUpdate:!0,updateId:"",columns:d,pagination:{page:0,size:5,sort:null},literature:{},queryParam:{title:"",url:"",key:"",parentId:"",status:!0,target:"_blank"},uploadVisible:!1}},created:function(){this.loadLiterature()},computed:{upload:function(){return u["a"].uploadBib()},headers:function(){var t=localStorage.getItem("Authorization");return{Authorization:"Bearer "+t}}},methods:{loadLiterature:function(){var t=this;this.queryParam.page=this.pagination.page-1,this.queryParam.size=this.pagination.size,this.queryParam.sort=this.pagination.sort,s.list(this.queryParam).then((function(e){t.literatures=e.data.data.content,t.pagination.total=e.data.data.totalElements}))},handlePaginationChange:function(t,e){this.pagination.page=t,this.pagination.size=e,this.loadLiterature()},addMenu:function(){this.isUpdate=!1,this.visible=!0},uploadPic:function(t){var e=t.file.status;"done"===e?(this.loadLiterature(),this.uploadVisible=!1,this.$message.success("".concat(t.file.name," file uploaded successfully."))):"error"===e&&this.$message.error("".concat(t.file.name," file upload failed."))},onUploadClose:function(){},edit:function(t){var e=this;this.isUpdate=!0,this.visible=!0,this.updateId=t.id,s.findById(t.id).then((function(t){e.literature=t.data.data}))},deleteLiteratures:function(t){var e=this;s.delete(t.id).then((function(t){e.$notification["success"]({message:t.data.message}),e.loadLiterature()}))},importData:function(){var t=this;s.import().then((function(e){t.$message.success(e.data.message),t.loadLiterature()}))},generateHtml:function(){var t=this;s.generateHtml().then((function(e){t.$message.success(e.data.message),t.loadLiterature()}))},preview:function(t){window.open(c["a"].Online("literatureList",t),"_blank")},handleOk:function(){var t=this;this.isUpdate?s.update(this.updateId,this.literature).then((function(e){t.$notification["success"]({message:e.data.message}),t.loadLiterature(),t.visible=!1})):s.add(this.literature).then((function(e){t.$notification["success"]({message:e.data.message}),t.loadLiterature(),t.visible=!1}))}}},f=p,h=a("2877"),m=Object(h["a"])(f,i,n,!1,null,null,null);e["default"]=m.exports},a796:function(t,e,a){"use strict";a("99af");var i=a("9efd"),n=a("72ac"),r="/api/attachment",o={upload:function(){return"http://".concat(n["a"].baseUrl,":").concat(n["a"].port,"/api/attachment/upload")},uploadBib:function(){return"http://".concat(n["a"].baseUrl,":").concat(n["a"].port,"/api/attachment/uploadBib?update=true")},list:function(t){return Object(i["a"])({url:r,params:t,method:"get"})}};e["a"]=o},b0c0:function(t,e,a){var i=a("83ab"),n=a("9bf2").f,r=Function.prototype,o=r.toString,l=/^\s*function ([^ (]*)/,s="name";!i||s in r||n(r,s,{configurable:!0,get:function(){try{return o.call(this).match(l)[1]}catch(t){return""}}})}}]);
//# sourceMappingURL=Literature.e2244dc1.js.map